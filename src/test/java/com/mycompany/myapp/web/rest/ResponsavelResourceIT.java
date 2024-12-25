package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.ResponsavelAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Responsavel;
import com.mycompany.myapp.repository.EntityManager;
import com.mycompany.myapp.repository.ResponsavelRepository;
import com.mycompany.myapp.repository.search.ResponsavelSearchRepository;
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
 * Integration tests for the {@link ResponsavelResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ResponsavelResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_PARENTESCO = "AAAAAAAAAA";
    private static final String UPDATED_PARENTESCO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/responsavels";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/responsavels/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ResponsavelRepository responsavelRepository;

    @Autowired
    private ResponsavelSearchRepository responsavelSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private Responsavel responsavel;

    private Responsavel insertedResponsavel;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Responsavel createEntity() {
        return new Responsavel().nome(DEFAULT_NOME).parentesco(DEFAULT_PARENTESCO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Responsavel createUpdatedEntity() {
        return new Responsavel().nome(UPDATED_NOME).parentesco(UPDATED_PARENTESCO);
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(Responsavel.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    public void initTest() {
        responsavel = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedResponsavel != null) {
            responsavelRepository.delete(insertedResponsavel).block();
            responsavelSearchRepository.delete(insertedResponsavel).block();
            insertedResponsavel = null;
        }
        deleteEntities(em);
    }

    @Test
    void createResponsavel() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(responsavelSearchRepository.findAll().collectList().block());
        // Create the Responsavel
        var returnedResponsavel = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(responsavel))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(Responsavel.class)
            .returnResult()
            .getResponseBody();

        // Validate the Responsavel in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertResponsavelUpdatableFieldsEquals(returnedResponsavel, getPersistedResponsavel(returnedResponsavel));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(responsavelSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedResponsavel = returnedResponsavel;
    }

    @Test
    void createResponsavelWithExistingId() throws Exception {
        // Create the Responsavel with an existing ID
        responsavel.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(responsavelSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(responsavel))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Responsavel in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(responsavelSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkNomeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(responsavelSearchRepository.findAll().collectList().block());
        // set the field null
        responsavel.setNome(null);

        // Create the Responsavel, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(responsavel))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(responsavelSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkParentescoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(responsavelSearchRepository.findAll().collectList().block());
        // set the field null
        responsavel.setParentesco(null);

        // Create the Responsavel, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(responsavel))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(responsavelSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllResponsavels() {
        // Initialize the database
        insertedResponsavel = responsavelRepository.save(responsavel).block();

        // Get all the responsavelList
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
            .value(hasItem(responsavel.getId().intValue()))
            .jsonPath("$.[*].nome")
            .value(hasItem(DEFAULT_NOME))
            .jsonPath("$.[*].parentesco")
            .value(hasItem(DEFAULT_PARENTESCO));
    }

    @Test
    void getResponsavel() {
        // Initialize the database
        insertedResponsavel = responsavelRepository.save(responsavel).block();

        // Get the responsavel
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, responsavel.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(responsavel.getId().intValue()))
            .jsonPath("$.nome")
            .value(is(DEFAULT_NOME))
            .jsonPath("$.parentesco")
            .value(is(DEFAULT_PARENTESCO));
    }

    @Test
    void getNonExistingResponsavel() {
        // Get the responsavel
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingResponsavel() throws Exception {
        // Initialize the database
        insertedResponsavel = responsavelRepository.save(responsavel).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();
        responsavelSearchRepository.save(responsavel).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(responsavelSearchRepository.findAll().collectList().block());

        // Update the responsavel
        Responsavel updatedResponsavel = responsavelRepository.findById(responsavel.getId()).block();
        updatedResponsavel.nome(UPDATED_NOME).parentesco(UPDATED_PARENTESCO);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedResponsavel.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(updatedResponsavel))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Responsavel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedResponsavelToMatchAllProperties(updatedResponsavel);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(responsavelSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Responsavel> responsavelSearchList = Streamable.of(
                    responsavelSearchRepository.findAll().collectList().block()
                ).toList();
                Responsavel testResponsavelSearch = responsavelSearchList.get(searchDatabaseSizeAfter - 1);

                // Test fails because reactive api returns an empty object instead of null
                // assertResponsavelAllPropertiesEquals(testResponsavelSearch, updatedResponsavel);
                assertResponsavelUpdatableFieldsEquals(testResponsavelSearch, updatedResponsavel);
            });
    }

    @Test
    void putNonExistingResponsavel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(responsavelSearchRepository.findAll().collectList().block());
        responsavel.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, responsavel.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(responsavel))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Responsavel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(responsavelSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchResponsavel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(responsavelSearchRepository.findAll().collectList().block());
        responsavel.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(responsavel))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Responsavel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(responsavelSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamResponsavel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(responsavelSearchRepository.findAll().collectList().block());
        responsavel.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(responsavel))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Responsavel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(responsavelSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateResponsavelWithPatch() throws Exception {
        // Initialize the database
        insertedResponsavel = responsavelRepository.save(responsavel).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the responsavel using partial update
        Responsavel partialUpdatedResponsavel = new Responsavel();
        partialUpdatedResponsavel.setId(responsavel.getId());

        partialUpdatedResponsavel.nome(UPDATED_NOME).parentesco(UPDATED_PARENTESCO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedResponsavel.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedResponsavel))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Responsavel in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertResponsavelUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedResponsavel, responsavel),
            getPersistedResponsavel(responsavel)
        );
    }

    @Test
    void fullUpdateResponsavelWithPatch() throws Exception {
        // Initialize the database
        insertedResponsavel = responsavelRepository.save(responsavel).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the responsavel using partial update
        Responsavel partialUpdatedResponsavel = new Responsavel();
        partialUpdatedResponsavel.setId(responsavel.getId());

        partialUpdatedResponsavel.nome(UPDATED_NOME).parentesco(UPDATED_PARENTESCO);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedResponsavel.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedResponsavel))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Responsavel in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertResponsavelUpdatableFieldsEquals(partialUpdatedResponsavel, getPersistedResponsavel(partialUpdatedResponsavel));
    }

    @Test
    void patchNonExistingResponsavel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(responsavelSearchRepository.findAll().collectList().block());
        responsavel.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, responsavel.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(responsavel))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Responsavel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(responsavelSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchResponsavel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(responsavelSearchRepository.findAll().collectList().block());
        responsavel.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(responsavel))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Responsavel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(responsavelSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamResponsavel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(responsavelSearchRepository.findAll().collectList().block());
        responsavel.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(responsavel))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Responsavel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(responsavelSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteResponsavel() {
        // Initialize the database
        insertedResponsavel = responsavelRepository.save(responsavel).block();
        responsavelRepository.save(responsavel).block();
        responsavelSearchRepository.save(responsavel).block();

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(responsavelSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the responsavel
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, responsavel.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(responsavelSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchResponsavel() {
        // Initialize the database
        insertedResponsavel = responsavelRepository.save(responsavel).block();
        responsavelSearchRepository.save(responsavel).block();

        // Search the responsavel
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + responsavel.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(responsavel.getId().intValue()))
            .jsonPath("$.[*].nome")
            .value(hasItem(DEFAULT_NOME))
            .jsonPath("$.[*].parentesco")
            .value(hasItem(DEFAULT_PARENTESCO));
    }

    protected long getRepositoryCount() {
        return responsavelRepository.count().block();
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

    protected Responsavel getPersistedResponsavel(Responsavel responsavel) {
        return responsavelRepository.findById(responsavel.getId()).block();
    }

    protected void assertPersistedResponsavelToMatchAllProperties(Responsavel expectedResponsavel) {
        // Test fails because reactive api returns an empty object instead of null
        // assertResponsavelAllPropertiesEquals(expectedResponsavel, getPersistedResponsavel(expectedResponsavel));
        assertResponsavelUpdatableFieldsEquals(expectedResponsavel, getPersistedResponsavel(expectedResponsavel));
    }

    protected void assertPersistedResponsavelToMatchUpdatableProperties(Responsavel expectedResponsavel) {
        // Test fails because reactive api returns an empty object instead of null
        // assertResponsavelAllUpdatablePropertiesEquals(expectedResponsavel, getPersistedResponsavel(expectedResponsavel));
        assertResponsavelUpdatableFieldsEquals(expectedResponsavel, getPersistedResponsavel(expectedResponsavel));
    }
}
