package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.ResponsavelAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Responsavel;
import com.mycompany.myapp.repository.ResponsavelRepository;
import com.mycompany.myapp.repository.search.ResponsavelSearchRepository;
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
 * Integration tests for the {@link ResponsavelResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
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
    private MockMvc restResponsavelMockMvc;

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

    @BeforeEach
    public void initTest() {
        responsavel = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedResponsavel != null) {
            responsavelRepository.delete(insertedResponsavel);
            responsavelSearchRepository.delete(insertedResponsavel);
            insertedResponsavel = null;
        }
    }

    @Test
    @Transactional
    void createResponsavel() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(responsavelSearchRepository.findAll());
        // Create the Responsavel
        var returnedResponsavel = om.readValue(
            restResponsavelMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(responsavel)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Responsavel.class
        );

        // Validate the Responsavel in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertResponsavelUpdatableFieldsEquals(returnedResponsavel, getPersistedResponsavel(returnedResponsavel));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(responsavelSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedResponsavel = returnedResponsavel;
    }

    @Test
    @Transactional
    void createResponsavelWithExistingId() throws Exception {
        // Create the Responsavel with an existing ID
        responsavel.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(responsavelSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restResponsavelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(responsavel)))
            .andExpect(status().isBadRequest());

        // Validate the Responsavel in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(responsavelSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(responsavelSearchRepository.findAll());
        // set the field null
        responsavel.setNome(null);

        // Create the Responsavel, which fails.

        restResponsavelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(responsavel)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(responsavelSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkParentescoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(responsavelSearchRepository.findAll());
        // set the field null
        responsavel.setParentesco(null);

        // Create the Responsavel, which fails.

        restResponsavelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(responsavel)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(responsavelSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllResponsavels() throws Exception {
        // Initialize the database
        insertedResponsavel = responsavelRepository.saveAndFlush(responsavel);

        // Get all the responsavelList
        restResponsavelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(responsavel.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].parentesco").value(hasItem(DEFAULT_PARENTESCO)));
    }

    @Test
    @Transactional
    void getResponsavel() throws Exception {
        // Initialize the database
        insertedResponsavel = responsavelRepository.saveAndFlush(responsavel);

        // Get the responsavel
        restResponsavelMockMvc
            .perform(get(ENTITY_API_URL_ID, responsavel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(responsavel.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.parentesco").value(DEFAULT_PARENTESCO));
    }

    @Test
    @Transactional
    void getNonExistingResponsavel() throws Exception {
        // Get the responsavel
        restResponsavelMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingResponsavel() throws Exception {
        // Initialize the database
        insertedResponsavel = responsavelRepository.saveAndFlush(responsavel);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        responsavelSearchRepository.save(responsavel);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(responsavelSearchRepository.findAll());

        // Update the responsavel
        Responsavel updatedResponsavel = responsavelRepository.findById(responsavel.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedResponsavel are not directly saved in db
        em.detach(updatedResponsavel);
        updatedResponsavel.nome(UPDATED_NOME).parentesco(UPDATED_PARENTESCO);

        restResponsavelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedResponsavel.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedResponsavel))
            )
            .andExpect(status().isOk());

        // Validate the Responsavel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedResponsavelToMatchAllProperties(updatedResponsavel);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(responsavelSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Responsavel> responsavelSearchList = Streamable.of(responsavelSearchRepository.findAll()).toList();
                Responsavel testResponsavelSearch = responsavelSearchList.get(searchDatabaseSizeAfter - 1);

                assertResponsavelAllPropertiesEquals(testResponsavelSearch, updatedResponsavel);
            });
    }

    @Test
    @Transactional
    void putNonExistingResponsavel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(responsavelSearchRepository.findAll());
        responsavel.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restResponsavelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, responsavel.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(responsavel))
            )
            .andExpect(status().isBadRequest());

        // Validate the Responsavel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(responsavelSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchResponsavel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(responsavelSearchRepository.findAll());
        responsavel.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResponsavelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(responsavel))
            )
            .andExpect(status().isBadRequest());

        // Validate the Responsavel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(responsavelSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamResponsavel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(responsavelSearchRepository.findAll());
        responsavel.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResponsavelMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(responsavel)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Responsavel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(responsavelSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateResponsavelWithPatch() throws Exception {
        // Initialize the database
        insertedResponsavel = responsavelRepository.saveAndFlush(responsavel);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the responsavel using partial update
        Responsavel partialUpdatedResponsavel = new Responsavel();
        partialUpdatedResponsavel.setId(responsavel.getId());

        partialUpdatedResponsavel.nome(UPDATED_NOME);

        restResponsavelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedResponsavel.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedResponsavel))
            )
            .andExpect(status().isOk());

        // Validate the Responsavel in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertResponsavelUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedResponsavel, responsavel),
            getPersistedResponsavel(responsavel)
        );
    }

    @Test
    @Transactional
    void fullUpdateResponsavelWithPatch() throws Exception {
        // Initialize the database
        insertedResponsavel = responsavelRepository.saveAndFlush(responsavel);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the responsavel using partial update
        Responsavel partialUpdatedResponsavel = new Responsavel();
        partialUpdatedResponsavel.setId(responsavel.getId());

        partialUpdatedResponsavel.nome(UPDATED_NOME).parentesco(UPDATED_PARENTESCO);

        restResponsavelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedResponsavel.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedResponsavel))
            )
            .andExpect(status().isOk());

        // Validate the Responsavel in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertResponsavelUpdatableFieldsEquals(partialUpdatedResponsavel, getPersistedResponsavel(partialUpdatedResponsavel));
    }

    @Test
    @Transactional
    void patchNonExistingResponsavel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(responsavelSearchRepository.findAll());
        responsavel.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restResponsavelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, responsavel.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(responsavel))
            )
            .andExpect(status().isBadRequest());

        // Validate the Responsavel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(responsavelSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchResponsavel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(responsavelSearchRepository.findAll());
        responsavel.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResponsavelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(responsavel))
            )
            .andExpect(status().isBadRequest());

        // Validate the Responsavel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(responsavelSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamResponsavel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(responsavelSearchRepository.findAll());
        responsavel.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResponsavelMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(responsavel)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Responsavel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(responsavelSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteResponsavel() throws Exception {
        // Initialize the database
        insertedResponsavel = responsavelRepository.saveAndFlush(responsavel);
        responsavelRepository.save(responsavel);
        responsavelSearchRepository.save(responsavel);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(responsavelSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the responsavel
        restResponsavelMockMvc
            .perform(delete(ENTITY_API_URL_ID, responsavel.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(responsavelSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchResponsavel() throws Exception {
        // Initialize the database
        insertedResponsavel = responsavelRepository.saveAndFlush(responsavel);
        responsavelSearchRepository.save(responsavel);

        // Search the responsavel
        restResponsavelMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + responsavel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(responsavel.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].parentesco").value(hasItem(DEFAULT_PARENTESCO)));
    }

    protected long getRepositoryCount() {
        return responsavelRepository.count();
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
        return responsavelRepository.findById(responsavel.getId()).orElseThrow();
    }

    protected void assertPersistedResponsavelToMatchAllProperties(Responsavel expectedResponsavel) {
        assertResponsavelAllPropertiesEquals(expectedResponsavel, getPersistedResponsavel(expectedResponsavel));
    }

    protected void assertPersistedResponsavelToMatchUpdatableProperties(Responsavel expectedResponsavel) {
        assertResponsavelAllUpdatablePropertiesEquals(expectedResponsavel, getPersistedResponsavel(expectedResponsavel));
    }
}
