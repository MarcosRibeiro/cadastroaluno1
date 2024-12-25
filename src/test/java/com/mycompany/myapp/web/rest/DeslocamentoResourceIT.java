package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.DeslocamentoAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Deslocamento;
import com.mycompany.myapp.repository.DeslocamentoRepository;
import com.mycompany.myapp.repository.EntityManager;
import com.mycompany.myapp.repository.search.DeslocamentoSearchRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.assertj.core.util.IterableUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.data.util.Streamable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link DeslocamentoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class DeslocamentoResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_GRAU = "AAAAAAAAAA";
    private static final String UPDATED_GRAU = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/deslocamentos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/deslocamentos/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DeslocamentoRepository deslocamentoRepository;

    @Autowired
    private DeslocamentoSearchRepository deslocamentoSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Deslocamento deslocamento;

    private Deslocamento insertedDeslocamento;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Deslocamento createEntity() {
        return new Deslocamento().nome(DEFAULT_NOME).grau(DEFAULT_GRAU);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Deslocamento createUpdatedEntity() {
        return new Deslocamento().nome(UPDATED_NOME).grau(UPDATED_GRAU);
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Deslocamento.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    public void initTest() {
        deslocamento = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedDeslocamento != null) {
            deslocamentoRepository.delete(insertedDeslocamento).block();
            deslocamentoSearchRepository.delete(insertedDeslocamento).block();
            insertedDeslocamento = null;
        }
        deleteEntities(em);
    }

    @Test
    void createDeslocamento() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(deslocamentoSearchRepository.findAll().collectList().block());
        // Create the Deslocamento
        var returnedDeslocamento = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(deslocamento))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(Deslocamento.class)
            .returnResult()
            .getResponseBody();

        // Validate the Deslocamento in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertDeslocamentoUpdatableFieldsEquals(returnedDeslocamento, getPersistedDeslocamento(returnedDeslocamento));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(deslocamentoSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedDeslocamento = returnedDeslocamento;
    }

    @Test
    void createDeslocamentoWithExistingId() throws Exception {
        // Create the Deslocamento with an existing ID
        deslocamento.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(deslocamentoSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(deslocamento))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Deslocamento in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(deslocamentoSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkNomeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(deslocamentoSearchRepository.findAll().collectList().block());
        // set the field null
        deslocamento.setNome(null);

        // Create the Deslocamento, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(deslocamento))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(deslocamentoSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkGrauIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(deslocamentoSearchRepository.findAll().collectList().block());
        // set the field null
        deslocamento.setGrau(null);

        // Create the Deslocamento, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(deslocamento))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(deslocamentoSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllDeslocamentos() {
        // Initialize the database
        insertedDeslocamento = deslocamentoRepository.save(deslocamento).block();

        // Get all the deslocamentoList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(deslocamento.getId().intValue()))
            .jsonPath("$.[*].nome")
            .value(hasItem(DEFAULT_NOME))
            .jsonPath("$.[*].grau")
            .value(hasItem(DEFAULT_GRAU));
    }

    @Test
    void getDeslocamento() {
        // Initialize the database
        insertedDeslocamento = deslocamentoRepository.save(deslocamento).block();

        // Get the deslocamento
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, deslocamento.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(deslocamento.getId().intValue()))
            .jsonPath("$.nome")
            .value(is(DEFAULT_NOME))
            .jsonPath("$.grau")
            .value(is(DEFAULT_GRAU));
    }

    @Test
    void getNonExistingDeslocamento() {
        // Get the deslocamento
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingDeslocamento() throws Exception {
        // Initialize the database
        insertedDeslocamento = deslocamentoRepository.save(deslocamento).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();
        deslocamentoSearchRepository.save(deslocamento).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(deslocamentoSearchRepository.findAll().collectList().block());

        // Update the deslocamento
        Deslocamento updatedDeslocamento = deslocamentoRepository.findById(deslocamento.getId()).block();
        updatedDeslocamento.nome(UPDATED_NOME).grau(UPDATED_GRAU);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedDeslocamento.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(updatedDeslocamento))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Deslocamento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDeslocamentoToMatchAllProperties(updatedDeslocamento);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(deslocamentoSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Deslocamento> deslocamentoSearchList = Streamable.of(
                    deslocamentoSearchRepository.findAll().collectList().block()
                ).toList();
                Deslocamento testDeslocamentoSearch = deslocamentoSearchList.get(searchDatabaseSizeAfter - 1);

                // Test fails because reactive api returns an empty object instead of null
                // assertDeslocamentoAllPropertiesEquals(testDeslocamentoSearch, updatedDeslocamento);
                assertDeslocamentoUpdatableFieldsEquals(testDeslocamentoSearch, updatedDeslocamento);
            });
    }

    @Test
    void putNonExistingDeslocamento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(deslocamentoSearchRepository.findAll().collectList().block());
        deslocamento.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, deslocamento.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(deslocamento))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Deslocamento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(deslocamentoSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchDeslocamento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(deslocamentoSearchRepository.findAll().collectList().block());
        deslocamento.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(deslocamento))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Deslocamento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(deslocamentoSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamDeslocamento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(deslocamentoSearchRepository.findAll().collectList().block());
        deslocamento.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(deslocamento))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Deslocamento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(deslocamentoSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateDeslocamentoWithPatch() throws Exception {
        // Initialize the database
        insertedDeslocamento = deslocamentoRepository.save(deslocamento).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the deslocamento using partial update
        Deslocamento partialUpdatedDeslocamento = new Deslocamento();
        partialUpdatedDeslocamento.setId(deslocamento.getId());

        partialUpdatedDeslocamento.nome(UPDATED_NOME).grau(UPDATED_GRAU);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedDeslocamento.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedDeslocamento))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Deslocamento in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDeslocamentoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDeslocamento, deslocamento),
            getPersistedDeslocamento(deslocamento)
        );
    }

    @Test
    void fullUpdateDeslocamentoWithPatch() throws Exception {
        // Initialize the database
        insertedDeslocamento = deslocamentoRepository.save(deslocamento).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the deslocamento using partial update
        Deslocamento partialUpdatedDeslocamento = new Deslocamento();
        partialUpdatedDeslocamento.setId(deslocamento.getId());

        partialUpdatedDeslocamento.nome(UPDATED_NOME).grau(UPDATED_GRAU);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedDeslocamento.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedDeslocamento))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Deslocamento in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDeslocamentoUpdatableFieldsEquals(partialUpdatedDeslocamento, getPersistedDeslocamento(partialUpdatedDeslocamento));
    }

    @Test
    void patchNonExistingDeslocamento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(deslocamentoSearchRepository.findAll().collectList().block());
        deslocamento.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, deslocamento.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(deslocamento))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Deslocamento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(deslocamentoSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchDeslocamento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(deslocamentoSearchRepository.findAll().collectList().block());
        deslocamento.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(deslocamento))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Deslocamento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(deslocamentoSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamDeslocamento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(deslocamentoSearchRepository.findAll().collectList().block());
        deslocamento.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(deslocamento))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Deslocamento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(deslocamentoSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteDeslocamento() {
        // Initialize the database
        insertedDeslocamento = deslocamentoRepository.save(deslocamento).block();
        deslocamentoRepository.save(deslocamento).block();
        deslocamentoSearchRepository.save(deslocamento).block();

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(deslocamentoSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the deslocamento
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, deslocamento.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(deslocamentoSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchDeslocamento() {
        // Initialize the database
        insertedDeslocamento = deslocamentoRepository.save(deslocamento).block();
        deslocamentoSearchRepository.save(deslocamento).block();

        // Search the deslocamento
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + deslocamento.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(deslocamento.getId().intValue()))
            .jsonPath("$.[*].nome")
            .value(hasItem(DEFAULT_NOME))
            .jsonPath("$.[*].grau")
            .value(hasItem(DEFAULT_GRAU));
    }

    protected long getRepositoryCount() {
        return deslocamentoRepository.count().block();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Deslocamento getPersistedDeslocamento(Deslocamento deslocamento) {
        return deslocamentoRepository.findById(deslocamento.getId()).block();
    }

    protected void assertPersistedDeslocamentoToMatchAllProperties(Deslocamento expectedDeslocamento) {
        // Test fails because reactive api returns an empty object instead of null
        // assertDeslocamentoAllPropertiesEquals(expectedDeslocamento, getPersistedDeslocamento(expectedDeslocamento));
        assertDeslocamentoUpdatableFieldsEquals(expectedDeslocamento, getPersistedDeslocamento(expectedDeslocamento));
    }

    protected void assertPersistedDeslocamentoToMatchUpdatableProperties(Deslocamento expectedDeslocamento) {
        // Test fails because reactive api returns an empty object instead of null
        // assertDeslocamentoAllUpdatablePropertiesEquals(expectedDeslocamento, getPersistedDeslocamento(expectedDeslocamento));
        assertDeslocamentoUpdatableFieldsEquals(expectedDeslocamento, getPersistedDeslocamento(expectedDeslocamento));
    }
}
