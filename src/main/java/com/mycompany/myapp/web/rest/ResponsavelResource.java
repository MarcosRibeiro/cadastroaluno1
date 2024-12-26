package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Responsavel;
import com.mycompany.myapp.repository.ResponsavelRepository;
import com.mycompany.myapp.repository.search.ResponsavelSearchRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.web.rest.errors.ElasticsearchExceptionMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Responsavel}.
 */
@RestController
@RequestMapping("/api/responsavels")
@Transactional
public class ResponsavelResource {

    private static final Logger LOG = LoggerFactory.getLogger(ResponsavelResource.class);

    private static final String ENTITY_NAME = "responsavel";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ResponsavelRepository responsavelRepository;

    private final ResponsavelSearchRepository responsavelSearchRepository;

    public ResponsavelResource(ResponsavelRepository responsavelRepository, ResponsavelSearchRepository responsavelSearchRepository) {
        this.responsavelRepository = responsavelRepository;
        this.responsavelSearchRepository = responsavelSearchRepository;
    }

    /**
     * {@code POST  /responsavels} : Create a new responsavel.
     *
     * @param responsavel the responsavel to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new responsavel, or with status {@code 400 (Bad Request)} if the responsavel has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Responsavel> createResponsavel(@Valid @RequestBody Responsavel responsavel) throws URISyntaxException {
        LOG.debug("REST request to save Responsavel : {}", responsavel);
        if (responsavel.getId() != null) {
            throw new BadRequestAlertException("A new responsavel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        responsavel = responsavelRepository.save(responsavel);
        responsavelSearchRepository.index(responsavel);
        return ResponseEntity.created(new URI("/api/responsavels/" + responsavel.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, responsavel.getId().toString()))
            .body(responsavel);
    }

    /**
     * {@code PUT  /responsavels/:id} : Updates an existing responsavel.
     *
     * @param id the id of the responsavel to save.
     * @param responsavel the responsavel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated responsavel,
     * or with status {@code 400 (Bad Request)} if the responsavel is not valid,
     * or with status {@code 500 (Internal Server Error)} if the responsavel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Responsavel> updateResponsavel(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Responsavel responsavel
    ) throws URISyntaxException {
        LOG.debug("REST request to update Responsavel : {}, {}", id, responsavel);
        if (responsavel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, responsavel.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!responsavelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        responsavel = responsavelRepository.save(responsavel);
        responsavelSearchRepository.index(responsavel);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, responsavel.getId().toString()))
            .body(responsavel);
    }

    /**
     * {@code PATCH  /responsavels/:id} : Partial updates given fields of an existing responsavel, field will ignore if it is null
     *
     * @param id the id of the responsavel to save.
     * @param responsavel the responsavel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated responsavel,
     * or with status {@code 400 (Bad Request)} if the responsavel is not valid,
     * or with status {@code 404 (Not Found)} if the responsavel is not found,
     * or with status {@code 500 (Internal Server Error)} if the responsavel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Responsavel> partialUpdateResponsavel(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Responsavel responsavel
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Responsavel partially : {}, {}", id, responsavel);
        if (responsavel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, responsavel.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!responsavelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Responsavel> result = responsavelRepository
            .findById(responsavel.getId())
            .map(existingResponsavel -> {
                if (responsavel.getNome() != null) {
                    existingResponsavel.setNome(responsavel.getNome());
                }
                if (responsavel.getParentesco() != null) {
                    existingResponsavel.setParentesco(responsavel.getParentesco());
                }

                return existingResponsavel;
            })
            .map(responsavelRepository::save)
            .map(savedResponsavel -> {
                responsavelSearchRepository.index(savedResponsavel);
                return savedResponsavel;
            });

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, responsavel.getId().toString())
        );
    }

    /**
     * {@code GET  /responsavels} : get all the responsavels.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of responsavels in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Responsavel>> getAllResponsavels(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Responsavels");
        Page<Responsavel> page = responsavelRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /responsavels/:id} : get the "id" responsavel.
     *
     * @param id the id of the responsavel to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the responsavel, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Responsavel> getResponsavel(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Responsavel : {}", id);
        Optional<Responsavel> responsavel = responsavelRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(responsavel);
    }

    /**
     * {@code DELETE  /responsavels/:id} : delete the "id" responsavel.
     *
     * @param id the id of the responsavel to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResponsavel(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Responsavel : {}", id);
        responsavelRepository.deleteById(id);
        responsavelSearchRepository.deleteFromIndexById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /responsavels/_search?query=:query} : search for the responsavel corresponding
     * to the query.
     *
     * @param query the query of the responsavel search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<Responsavel>> searchResponsavels(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of Responsavels for query {}", query);
        try {
            Page<Responsavel> page = responsavelSearchRepository.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
