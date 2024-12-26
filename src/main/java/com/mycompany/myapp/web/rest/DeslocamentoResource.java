package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Deslocamento;
import com.mycompany.myapp.repository.DeslocamentoRepository;
import com.mycompany.myapp.repository.search.DeslocamentoSearchRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Deslocamento}.
 */
@RestController
@RequestMapping("/api/deslocamentos")
@Transactional
public class DeslocamentoResource {

    private static final Logger LOG = LoggerFactory.getLogger(DeslocamentoResource.class);

    private static final String ENTITY_NAME = "deslocamento";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DeslocamentoRepository deslocamentoRepository;

    private final DeslocamentoSearchRepository deslocamentoSearchRepository;

    public DeslocamentoResource(DeslocamentoRepository deslocamentoRepository, DeslocamentoSearchRepository deslocamentoSearchRepository) {
        this.deslocamentoRepository = deslocamentoRepository;
        this.deslocamentoSearchRepository = deslocamentoSearchRepository;
    }

    /**
     * {@code POST  /deslocamentos} : Create a new deslocamento.
     *
     * @param deslocamento the deslocamento to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new deslocamento, or with status {@code 400 (Bad Request)} if the deslocamento has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Deslocamento> createDeslocamento(@Valid @RequestBody Deslocamento deslocamento) throws URISyntaxException {
        LOG.debug("REST request to save Deslocamento : {}", deslocamento);
        if (deslocamento.getId() != null) {
            throw new BadRequestAlertException("A new deslocamento cannot already have an ID", ENTITY_NAME, "idexists");
        }
        deslocamento = deslocamentoRepository.save(deslocamento);
        deslocamentoSearchRepository.index(deslocamento);
        return ResponseEntity.created(new URI("/api/deslocamentos/" + deslocamento.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, deslocamento.getId().toString()))
            .body(deslocamento);
    }

    /**
     * {@code PUT  /deslocamentos/:id} : Updates an existing deslocamento.
     *
     * @param id the id of the deslocamento to save.
     * @param deslocamento the deslocamento to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deslocamento,
     * or with status {@code 400 (Bad Request)} if the deslocamento is not valid,
     * or with status {@code 500 (Internal Server Error)} if the deslocamento couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Deslocamento> updateDeslocamento(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Deslocamento deslocamento
    ) throws URISyntaxException {
        LOG.debug("REST request to update Deslocamento : {}, {}", id, deslocamento);
        if (deslocamento.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, deslocamento.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!deslocamentoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        deslocamento = deslocamentoRepository.save(deslocamento);
        deslocamentoSearchRepository.index(deslocamento);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, deslocamento.getId().toString()))
            .body(deslocamento);
    }

    /**
     * {@code PATCH  /deslocamentos/:id} : Partial updates given fields of an existing deslocamento, field will ignore if it is null
     *
     * @param id the id of the deslocamento to save.
     * @param deslocamento the deslocamento to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deslocamento,
     * or with status {@code 400 (Bad Request)} if the deslocamento is not valid,
     * or with status {@code 404 (Not Found)} if the deslocamento is not found,
     * or with status {@code 500 (Internal Server Error)} if the deslocamento couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Deslocamento> partialUpdateDeslocamento(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Deslocamento deslocamento
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Deslocamento partially : {}, {}", id, deslocamento);
        if (deslocamento.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, deslocamento.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!deslocamentoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Deslocamento> result = deslocamentoRepository
            .findById(deslocamento.getId())
            .map(existingDeslocamento -> {
                if (deslocamento.getNome() != null) {
                    existingDeslocamento.setNome(deslocamento.getNome());
                }
                if (deslocamento.getGrau() != null) {
                    existingDeslocamento.setGrau(deslocamento.getGrau());
                }

                return existingDeslocamento;
            })
            .map(deslocamentoRepository::save)
            .map(savedDeslocamento -> {
                deslocamentoSearchRepository.index(savedDeslocamento);
                return savedDeslocamento;
            });

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, deslocamento.getId().toString())
        );
    }

    /**
     * {@code GET  /deslocamentos} : get all the deslocamentos.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of deslocamentos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Deslocamento>> getAllDeslocamentos(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Deslocamentos");
        Page<Deslocamento> page = deslocamentoRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /deslocamentos/:id} : get the "id" deslocamento.
     *
     * @param id the id of the deslocamento to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the deslocamento, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Deslocamento> getDeslocamento(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Deslocamento : {}", id);
        Optional<Deslocamento> deslocamento = deslocamentoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(deslocamento);
    }

    /**
     * {@code DELETE  /deslocamentos/:id} : delete the "id" deslocamento.
     *
     * @param id the id of the deslocamento to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDeslocamento(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Deslocamento : {}", id);
        deslocamentoRepository.deleteById(id);
        deslocamentoSearchRepository.deleteFromIndexById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /deslocamentos/_search?query=:query} : search for the deslocamento corresponding
     * to the query.
     *
     * @param query the query of the deslocamento search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<Deslocamento>> searchDeslocamentos(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of Deslocamentos for query {}", query);
        try {
            Page<Deslocamento> page = deslocamentoSearchRepository.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
