package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Deslocamento;
import com.mycompany.myapp.repository.DeslocamentoRepository;
import com.mycompany.myapp.repository.search.DeslocamentoSearchRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.ForwardedHeaderUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

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
    public Mono<ResponseEntity<Deslocamento>> createDeslocamento(@Valid @RequestBody Deslocamento deslocamento) throws URISyntaxException {
        LOG.debug("REST request to save Deslocamento : {}", deslocamento);
        if (deslocamento.getId() != null) {
            throw new BadRequestAlertException("A new deslocamento cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return deslocamentoRepository
            .save(deslocamento)
            .flatMap(deslocamentoSearchRepository::save)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/deslocamentos/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
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
    public Mono<ResponseEntity<Deslocamento>> updateDeslocamento(
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

        return deslocamentoRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return deslocamentoRepository
                    .save(deslocamento)
                    .flatMap(deslocamentoSearchRepository::save)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
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
    public Mono<ResponseEntity<Deslocamento>> partialUpdateDeslocamento(
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

        return deslocamentoRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<Deslocamento> result = deslocamentoRepository
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
                    .flatMap(deslocamentoRepository::save)
                    .flatMap(savedDeslocamento -> {
                        deslocamentoSearchRepository.save(savedDeslocamento);
                        return Mono.just(savedDeslocamento);
                    });

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                            .body(res)
                    );
            });
    }

    /**
     * {@code GET  /deslocamentos} : get all the deslocamentos.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of deslocamentos in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<Deslocamento>>> getAllDeslocamentos(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        LOG.debug("REST request to get a page of Deslocamentos");
        return deslocamentoRepository
            .count()
            .zipWith(deslocamentoRepository.findAllBy(pageable).collectList())
            .map(countWithEntities ->
                ResponseEntity.ok()
                    .headers(
                        PaginationUtil.generatePaginationHttpHeaders(
                            ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                            new PageImpl<>(countWithEntities.getT2(), pageable, countWithEntities.getT1())
                        )
                    )
                    .body(countWithEntities.getT2())
            );
    }

    /**
     * {@code GET  /deslocamentos/:id} : get the "id" deslocamento.
     *
     * @param id the id of the deslocamento to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the deslocamento, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<Deslocamento>> getDeslocamento(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Deslocamento : {}", id);
        Mono<Deslocamento> deslocamento = deslocamentoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(deslocamento);
    }

    /**
     * {@code DELETE  /deslocamentos/:id} : delete the "id" deslocamento.
     *
     * @param id the id of the deslocamento to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteDeslocamento(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Deslocamento : {}", id);
        return deslocamentoRepository
            .deleteById(id)
            .then(deslocamentoSearchRepository.deleteById(id))
            .then(
                Mono.just(
                    ResponseEntity.noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                        .build()
                )
            );
    }

    /**
     * {@code SEARCH  /deslocamentos/_search?query=:query} : search for the deslocamento corresponding
     * to the query.
     *
     * @param query the query of the deslocamento search.
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public Mono<ResponseEntity<Flux<Deslocamento>>> searchDeslocamentos(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        LOG.debug("REST request to search for a page of Deslocamentos for query {}", query);
        return deslocamentoSearchRepository
            .count()
            .map(total -> new PageImpl<>(new ArrayList<>(), pageable, total))
            .map(page ->
                PaginationUtil.generatePaginationHttpHeaders(
                    ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                    page
                )
            )
            .map(headers -> ResponseEntity.ok().headers(headers).body(deslocamentoSearchRepository.search(query, pageable)));
    }
}
