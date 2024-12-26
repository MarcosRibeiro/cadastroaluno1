package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.DeslocamentoAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Deslocamento;
import com.mycompany.myapp.repository.DeslocamentoRepository;
import com.mycompany.myapp.repository.search.DeslocamentoSearchRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.assertj.core.util.IterableUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.util.Streamable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link DeslocamentoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
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
    private MockMvc restDeslocamentoMockMvc;

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

    @BeforeEach
    public void initTest() {
        deslocamento = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedDeslocamento != null) {
            deslocamentoRepository.delete(insertedDeslocamento);
            deslocamentoSearchRepository.delete(insertedDeslocamento);
            insertedDeslocamento = null;
        }
    }

    @Test
    @Transactional
    void createDeslocamento() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(deslocamentoSearchRepository.findAll());
        // Create the Deslocamento
        var returnedDeslocamento = om.readValue(
            restDeslocamentoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(deslocamento)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Deslocamento.class
        );

        // Validate the Deslocamento in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertDeslocamentoUpdatableFieldsEquals(returnedDeslocamento, getPersistedDeslocamento(returnedDeslocamento));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(deslocamentoSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedDeslocamento = returnedDeslocamento;
    }

    @Test
    @Transactional
    void createDeslocamentoWithExistingId() throws Exception {
        // Create the Deslocamento with an existing ID
        deslocamento.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(deslocamentoSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restDeslocamentoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(deslocamento)))
            .andExpect(status().isBadRequest());

        // Validate the Deslocamento in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(deslocamentoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(deslocamentoSearchRepository.findAll());
        // set the field null
        deslocamento.setNome(null);

        // Create the Deslocamento, which fails.

        restDeslocamentoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(deslocamento)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(deslocamentoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkGrauIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(deslocamentoSearchRepository.findAll());
        // set the field null
        deslocamento.setGrau(null);

        // Create the Deslocamento, which fails.

        restDeslocamentoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(deslocamento)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(deslocamentoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllDeslocamentos() throws Exception {
        // Initialize the database
        insertedDeslocamento = deslocamentoRepository.saveAndFlush(deslocamento);

        // Get all the deslocamentoList
        restDeslocamentoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deslocamento.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].grau").value(hasItem(DEFAULT_GRAU)));
    }

    @Test
    @Transactional
    void getDeslocamento() throws Exception {
        // Initialize the database
        insertedDeslocamento = deslocamentoRepository.saveAndFlush(deslocamento);

        // Get the deslocamento
        restDeslocamentoMockMvc
            .perform(get(ENTITY_API_URL_ID, deslocamento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(deslocamento.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.grau").value(DEFAULT_GRAU));
    }

    @Test
    @Transactional
    void getNonExistingDeslocamento() throws Exception {
        // Get the deslocamento
        restDeslocamentoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDeslocamento() throws Exception {
        // Initialize the database
        insertedDeslocamento = deslocamentoRepository.saveAndFlush(deslocamento);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        deslocamentoSearchRepository.save(deslocamento);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(deslocamentoSearchRepository.findAll());

        // Update the deslocamento
        Deslocamento updatedDeslocamento = deslocamentoRepository.findById(deslocamento.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDeslocamento are not directly saved in db
        em.detach(updatedDeslocamento);
        updatedDeslocamento.nome(UPDATED_NOME).grau(UPDATED_GRAU);

        restDeslocamentoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDeslocamento.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedDeslocamento))
            )
            .andExpect(status().isOk());

        // Validate the Deslocamento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDeslocamentoToMatchAllProperties(updatedDeslocamento);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(deslocamentoSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Deslocamento> deslocamentoSearchList = Streamable.of(deslocamentoSearchRepository.findAll()).toList();
                Deslocamento testDeslocamentoSearch = deslocamentoSearchList.get(searchDatabaseSizeAfter - 1);

                assertDeslocamentoAllPropertiesEquals(testDeslocamentoSearch, updatedDeslocamento);
            });
    }

    @Test
    @Transactional
    void putNonExistingDeslocamento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(deslocamentoSearchRepository.findAll());
        deslocamento.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeslocamentoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, deslocamento.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(deslocamento))
            )
            .andExpect(status().isBadRequest());

        // Validate the Deslocamento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(deslocamentoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchDeslocamento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(deslocamentoSearchRepository.findAll());
        deslocamento.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeslocamentoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(deslocamento))
            )
            .andExpect(status().isBadRequest());

        // Validate the Deslocamento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(deslocamentoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDeslocamento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(deslocamentoSearchRepository.findAll());
        deslocamento.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeslocamentoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(deslocamento)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Deslocamento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(deslocamentoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateDeslocamentoWithPatch() throws Exception {
        // Initialize the database
        insertedDeslocamento = deslocamentoRepository.saveAndFlush(deslocamento);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the deslocamento using partial update
        Deslocamento partialUpdatedDeslocamento = new Deslocamento();
        partialUpdatedDeslocamento.setId(deslocamento.getId());

        partialUpdatedDeslocamento.nome(UPDATED_NOME);

        restDeslocamentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDeslocamento.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDeslocamento))
            )
            .andExpect(status().isOk());

        // Validate the Deslocamento in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDeslocamentoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDeslocamento, deslocamento),
            getPersistedDeslocamento(deslocamento)
        );
    }

    @Test
    @Transactional
    void fullUpdateDeslocamentoWithPatch() throws Exception {
        // Initialize the database
        insertedDeslocamento = deslocamentoRepository.saveAndFlush(deslocamento);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the deslocamento using partial update
        Deslocamento partialUpdatedDeslocamento = new Deslocamento();
        partialUpdatedDeslocamento.setId(deslocamento.getId());

        partialUpdatedDeslocamento.nome(UPDATED_NOME).grau(UPDATED_GRAU);

        restDeslocamentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDeslocamento.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDeslocamento))
            )
            .andExpect(status().isOk());

        // Validate the Deslocamento in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDeslocamentoUpdatableFieldsEquals(partialUpdatedDeslocamento, getPersistedDeslocamento(partialUpdatedDeslocamento));
    }

    @Test
    @Transactional
    void patchNonExistingDeslocamento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(deslocamentoSearchRepository.findAll());
        deslocamento.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeslocamentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, deslocamento.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(deslocamento))
            )
            .andExpect(status().isBadRequest());

        // Validate the Deslocamento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(deslocamentoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDeslocamento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(deslocamentoSearchRepository.findAll());
        deslocamento.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeslocamentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(deslocamento))
            )
            .andExpect(status().isBadRequest());

        // Validate the Deslocamento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(deslocamentoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDeslocamento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(deslocamentoSearchRepository.findAll());
        deslocamento.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeslocamentoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(deslocamento)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Deslocamento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(deslocamentoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteDeslocamento() throws Exception {
        // Initialize the database
        insertedDeslocamento = deslocamentoRepository.saveAndFlush(deslocamento);
        deslocamentoRepository.save(deslocamento);
        deslocamentoSearchRepository.save(deslocamento);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(deslocamentoSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the deslocamento
        restDeslocamentoMockMvc
            .perform(delete(ENTITY_API_URL_ID, deslocamento.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(deslocamentoSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchDeslocamento() throws Exception {
        // Initialize the database
        insertedDeslocamento = deslocamentoRepository.saveAndFlush(deslocamento);
        deslocamentoSearchRepository.save(deslocamento);

        // Search the deslocamento
        restDeslocamentoMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + deslocamento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deslocamento.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].grau").value(hasItem(DEFAULT_GRAU)));
    }

    protected long getRepositoryCount() {
        return deslocamentoRepository.count();
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
        return deslocamentoRepository.findById(deslocamento.getId()).orElseThrow();
    }

    protected void assertPersistedDeslocamentoToMatchAllProperties(Deslocamento expectedDeslocamento) {
        assertDeslocamentoAllPropertiesEquals(expectedDeslocamento, getPersistedDeslocamento(expectedDeslocamento));
    }

    protected void assertPersistedDeslocamentoToMatchUpdatableProperties(Deslocamento expectedDeslocamento) {
        assertDeslocamentoAllUpdatablePropertiesEquals(expectedDeslocamento, getPersistedDeslocamento(expectedDeslocamento));
    }
}
