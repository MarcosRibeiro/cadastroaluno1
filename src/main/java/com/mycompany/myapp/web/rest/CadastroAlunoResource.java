package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.CadastroAluno;
import com.mycompany.myapp.repository.CadastroAlunoRepository;
import com.mycompany.myapp.repository.search.CadastroAlunoSearchRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.CadastroAluno}.
 */
@RestController
@RequestMapping("/api/cadastro-alunos")
@Transactional
public class CadastroAlunoResource {

    private static final Logger LOG = LoggerFactory.getLogger(CadastroAlunoResource.class);

    private static final String ENTITY_NAME = "cadastroAluno";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CadastroAlunoRepository cadastroAlunoRepository;

    private final CadastroAlunoSearchRepository cadastroAlunoSearchRepository;

    public CadastroAlunoResource(
        CadastroAlunoRepository cadastroAlunoRepository,
        CadastroAlunoSearchRepository cadastroAlunoSearchRepository
    ) {
        this.cadastroAlunoRepository = cadastroAlunoRepository;
        this.cadastroAlunoSearchRepository = cadastroAlunoSearchRepository;
    }

    /**
     * {@code POST  /cadastro-alunos} : Create a new cadastroAluno.
     *
     * @param cadastroAluno the cadastroAluno to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cadastroAluno, or with status {@code 400 (Bad Request)} if the cadastroAluno has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public Mono<ResponseEntity<CadastroAluno>> createCadastroAluno(@Valid @RequestBody CadastroAluno cadastroAluno)
        throws URISyntaxException {
        LOG.debug("REST request to save CadastroAluno : {}", cadastroAluno);
        if (cadastroAluno.getId() != null) {
            throw new BadRequestAlertException("A new cadastroAluno cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return cadastroAlunoRepository
            .save(cadastroAluno)
            .flatMap(cadastroAlunoSearchRepository::save)
            .map(result -> {
                try {
                    return ResponseEntity.created(new URI("/api/cadastro-alunos/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /cadastro-alunos/:id} : Updates an existing cadastroAluno.
     *
     * @param id the id of the cadastroAluno to save.
     * @param cadastroAluno the cadastroAluno to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cadastroAluno,
     * or with status {@code 400 (Bad Request)} if the cadastroAluno is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cadastroAluno couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public Mono<ResponseEntity<CadastroAluno>> updateCadastroAluno(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CadastroAluno cadastroAluno
    ) throws URISyntaxException {
        LOG.debug("REST request to update CadastroAluno : {}, {}", id, cadastroAluno);
        if (cadastroAluno.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cadastroAluno.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return cadastroAlunoRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return cadastroAlunoRepository
                    .save(cadastroAluno)
                    .flatMap(cadastroAlunoSearchRepository::save)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity.ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /cadastro-alunos/:id} : Partial updates given fields of an existing cadastroAluno, field will ignore if it is null
     *
     * @param id the id of the cadastroAluno to save.
     * @param cadastroAluno the cadastroAluno to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cadastroAluno,
     * or with status {@code 400 (Bad Request)} if the cadastroAluno is not valid,
     * or with status {@code 404 (Not Found)} if the cadastroAluno is not found,
     * or with status {@code 500 (Internal Server Error)} if the cadastroAluno couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<CadastroAluno>> partialUpdateCadastroAluno(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CadastroAluno cadastroAluno
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CadastroAluno partially : {}, {}", id, cadastroAluno);
        if (cadastroAluno.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cadastroAluno.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return cadastroAlunoRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<CadastroAluno> result = cadastroAlunoRepository
                    .findById(cadastroAluno.getId())
                    .map(existingCadastroAluno -> {
                        if (cadastroAluno.getDataCadastro() != null) {
                            existingCadastroAluno.setDataCadastro(cadastroAluno.getDataCadastro());
                        }
                        if (cadastroAluno.getMatricula() != null) {
                            existingCadastroAluno.setMatricula(cadastroAluno.getMatricula());
                        }
                        if (cadastroAluno.getGrupo() != null) {
                            existingCadastroAluno.setGrupo(cadastroAluno.getGrupo());
                        }
                        if (cadastroAluno.getNome() != null) {
                            existingCadastroAluno.setNome(cadastroAluno.getNome());
                        }
                        if (cadastroAluno.getDn() != null) {
                            existingCadastroAluno.setDn(cadastroAluno.getDn());
                        }
                        if (cadastroAluno.getCep() != null) {
                            existingCadastroAluno.setCep(cadastroAluno.getCep());
                        }
                        if (cadastroAluno.getEndereco() != null) {
                            existingCadastroAluno.setEndereco(cadastroAluno.getEndereco());
                        }
                        if (cadastroAluno.getQd() != null) {
                            existingCadastroAluno.setQd(cadastroAluno.getQd());
                        }
                        if (cadastroAluno.getLote() != null) {
                            existingCadastroAluno.setLote(cadastroAluno.getLote());
                        }
                        if (cadastroAluno.getEndnumero() != null) {
                            existingCadastroAluno.setEndnumero(cadastroAluno.getEndnumero());
                        }
                        if (cadastroAluno.getBairro() != null) {
                            existingCadastroAluno.setBairro(cadastroAluno.getBairro());
                        }
                        if (cadastroAluno.getMunicipio() != null) {
                            existingCadastroAluno.setMunicipio(cadastroAluno.getMunicipio());
                        }
                        if (cadastroAluno.getUf() != null) {
                            existingCadastroAluno.setUf(cadastroAluno.getUf());
                        }
                        if (cadastroAluno.getFone() != null) {
                            existingCadastroAluno.setFone(cadastroAluno.getFone());
                        }
                        if (cadastroAluno.getCertidao() != null) {
                            existingCadastroAluno.setCertidao(cadastroAluno.getCertidao());
                        }
                        if (cadastroAluno.getTermo() != null) {
                            existingCadastroAluno.setTermo(cadastroAluno.getTermo());
                        }
                        if (cadastroAluno.getCartorio() != null) {
                            existingCadastroAluno.setCartorio(cadastroAluno.getCartorio());
                        }
                        if (cadastroAluno.getNaturalidade() != null) {
                            existingCadastroAluno.setNaturalidade(cadastroAluno.getNaturalidade());
                        }
                        if (cadastroAluno.getRg() != null) {
                            existingCadastroAluno.setRg(cadastroAluno.getRg());
                        }
                        if (cadastroAluno.getCpf() != null) {
                            existingCadastroAluno.setCpf(cadastroAluno.getCpf());
                        }
                        if (cadastroAluno.getNis() != null) {
                            existingCadastroAluno.setNis(cadastroAluno.getNis());
                        }
                        if (cadastroAluno.getCras() != null) {
                            existingCadastroAluno.setCras(cadastroAluno.getCras());
                        }
                        if (cadastroAluno.getFiliacaoPai() != null) {
                            existingCadastroAluno.setFiliacaoPai(cadastroAluno.getFiliacaoPai());
                        }
                        if (cadastroAluno.getPaiTelefone() != null) {
                            existingCadastroAluno.setPaiTelefone(cadastroAluno.getPaiTelefone());
                        }
                        if (cadastroAluno.getPaiNaturalidade() != null) {
                            existingCadastroAluno.setPaiNaturalidade(cadastroAluno.getPaiNaturalidade());
                        }
                        if (cadastroAluno.getPaiUf() != null) {
                            existingCadastroAluno.setPaiUf(cadastroAluno.getPaiUf());
                        }
                        if (cadastroAluno.getPaiRg() != null) {
                            existingCadastroAluno.setPaiRg(cadastroAluno.getPaiRg());
                        }
                        if (cadastroAluno.getPaiDataNascimento() != null) {
                            existingCadastroAluno.setPaiDataNascimento(cadastroAluno.getPaiDataNascimento());
                        }
                        if (cadastroAluno.getPaiCpf() != null) {
                            existingCadastroAluno.setPaiCpf(cadastroAluno.getPaiCpf());
                        }
                        if (cadastroAluno.getPaiNis() != null) {
                            existingCadastroAluno.setPaiNis(cadastroAluno.getPaiNis());
                        }
                        if (cadastroAluno.getPaiTituloEleitor() != null) {
                            existingCadastroAluno.setPaiTituloEleitor(cadastroAluno.getPaiTituloEleitor());
                        }
                        if (cadastroAluno.getPaiZona() != null) {
                            existingCadastroAluno.setPaiZona(cadastroAluno.getPaiZona());
                        }
                        if (cadastroAluno.getPaiSecao() != null) {
                            existingCadastroAluno.setPaiSecao(cadastroAluno.getPaiSecao());
                        }
                        if (cadastroAluno.getPaiMunicipio() != null) {
                            existingCadastroAluno.setPaiMunicipio(cadastroAluno.getPaiMunicipio());
                        }
                        if (cadastroAluno.getFiliacaoMae() != null) {
                            existingCadastroAluno.setFiliacaoMae(cadastroAluno.getFiliacaoMae());
                        }
                        if (cadastroAluno.getMaeTelefone() != null) {
                            existingCadastroAluno.setMaeTelefone(cadastroAluno.getMaeTelefone());
                        }
                        if (cadastroAluno.getMaeNaturalidade() != null) {
                            existingCadastroAluno.setMaeNaturalidade(cadastroAluno.getMaeNaturalidade());
                        }
                        if (cadastroAluno.getMaeUf() != null) {
                            existingCadastroAluno.setMaeUf(cadastroAluno.getMaeUf());
                        }
                        if (cadastroAluno.getMaeRg() != null) {
                            existingCadastroAluno.setMaeRg(cadastroAluno.getMaeRg());
                        }
                        if (cadastroAluno.getMaeDataNascimento() != null) {
                            existingCadastroAluno.setMaeDataNascimento(cadastroAluno.getMaeDataNascimento());
                        }
                        if (cadastroAluno.getMaeCpf() != null) {
                            existingCadastroAluno.setMaeCpf(cadastroAluno.getMaeCpf());
                        }
                        if (cadastroAluno.getMaeNis() != null) {
                            existingCadastroAluno.setMaeNis(cadastroAluno.getMaeNis());
                        }
                        if (cadastroAluno.getMaeTituloEleitor() != null) {
                            existingCadastroAluno.setMaeTituloEleitor(cadastroAluno.getMaeTituloEleitor());
                        }
                        if (cadastroAluno.getMaeZona() != null) {
                            existingCadastroAluno.setMaeZona(cadastroAluno.getMaeZona());
                        }
                        if (cadastroAluno.getMaeSecao() != null) {
                            existingCadastroAluno.setMaeSecao(cadastroAluno.getMaeSecao());
                        }
                        if (cadastroAluno.getMaeMunicipio() != null) {
                            existingCadastroAluno.setMaeMunicipio(cadastroAluno.getMaeMunicipio());
                        }
                        if (cadastroAluno.getNomeEscola() != null) {
                            existingCadastroAluno.setNomeEscola(cadastroAluno.getNomeEscola());
                        }
                        if (cadastroAluno.getAnoCursando() != null) {
                            existingCadastroAluno.setAnoCursando(cadastroAluno.getAnoCursando());
                        }
                        if (cadastroAluno.getTurno() != null) {
                            existingCadastroAluno.setTurno(cadastroAluno.getTurno());
                        }
                        if (cadastroAluno.getMediaEscolar() != null) {
                            existingCadastroAluno.setMediaEscolar(cadastroAluno.getMediaEscolar());
                        }
                        if (cadastroAluno.getPrioritario() != null) {
                            existingCadastroAluno.setPrioritario(cadastroAluno.getPrioritario());
                        }
                        if (cadastroAluno.getObs() != null) {
                            existingCadastroAluno.setObs(cadastroAluno.getObs());
                        }
                        if (cadastroAluno.getComportamentoCasa() != null) {
                            existingCadastroAluno.setComportamentoCasa(cadastroAluno.getComportamentoCasa());
                        }
                        if (cadastroAluno.getComportamentoEscola() != null) {
                            existingCadastroAluno.setComportamentoEscola(cadastroAluno.getComportamentoEscola());
                        }
                        if (cadastroAluno.getDeficiencia() != null) {
                            existingCadastroAluno.setDeficiencia(cadastroAluno.getDeficiencia());
                        }
                        if (cadastroAluno.getAdaptacoes() != null) {
                            existingCadastroAluno.setAdaptacoes(cadastroAluno.getAdaptacoes());
                        }
                        if (cadastroAluno.getMedicacao() != null) {
                            existingCadastroAluno.setMedicacao(cadastroAluno.getMedicacao());
                        }
                        if (cadastroAluno.getMedicacaoDesc() != null) {
                            existingCadastroAluno.setMedicacaoDesc(cadastroAluno.getMedicacaoDesc());
                        }
                        if (cadastroAluno.getAlergia() != null) {
                            existingCadastroAluno.setAlergia(cadastroAluno.getAlergia());
                        }
                        if (cadastroAluno.getAlergiaDesc() != null) {
                            existingCadastroAluno.setAlergiaDesc(cadastroAluno.getAlergiaDesc());
                        }
                        if (cadastroAluno.getHistoricoMedico() != null) {
                            existingCadastroAluno.setHistoricoMedico(cadastroAluno.getHistoricoMedico());
                        }
                        if (cadastroAluno.getRendaFamiliar() != null) {
                            existingCadastroAluno.setRendaFamiliar(cadastroAluno.getRendaFamiliar());
                        }
                        if (cadastroAluno.getPessoasTrabalham() != null) {
                            existingCadastroAluno.setPessoasTrabalham(cadastroAluno.getPessoasTrabalham());
                        }
                        if (cadastroAluno.getNumPessoasLar() != null) {
                            existingCadastroAluno.setNumPessoasLar(cadastroAluno.getNumPessoasLar());
                        }
                        if (cadastroAluno.getBeneficioSocial() != null) {
                            existingCadastroAluno.setBeneficioSocial(cadastroAluno.getBeneficioSocial());
                        }
                        if (cadastroAluno.getBeneficios() != null) {
                            existingCadastroAluno.setBeneficios(cadastroAluno.getBeneficios());
                        }
                        if (cadastroAluno.getTipoResidencia() != null) {
                            existingCadastroAluno.setTipoResidencia(cadastroAluno.getTipoResidencia());
                        }
                        if (cadastroAluno.getTipoResidenciaDesc() != null) {
                            existingCadastroAluno.setTipoResidenciaDesc(cadastroAluno.getTipoResidenciaDesc());
                        }
                        if (cadastroAluno.getSituacaoResidencia() != null) {
                            existingCadastroAluno.setSituacaoResidencia(cadastroAluno.getSituacaoResidencia());
                        }
                        if (cadastroAluno.getSituacaoResidenciaDesc() != null) {
                            existingCadastroAluno.setSituacaoResidenciaDesc(cadastroAluno.getSituacaoResidenciaDesc());
                        }
                        if (cadastroAluno.getContatoEmergencia() != null) {
                            existingCadastroAluno.setContatoEmergencia(cadastroAluno.getContatoEmergencia());
                        }
                        if (cadastroAluno.getFoneEmergencia() != null) {
                            existingCadastroAluno.setFoneEmergencia(cadastroAluno.getFoneEmergencia());
                        }
                        if (cadastroAluno.getRelacaoEmergencia() != null) {
                            existingCadastroAluno.setRelacaoEmergencia(cadastroAluno.getRelacaoEmergencia());
                        }
                        if (cadastroAluno.getAutorizacao() != null) {
                            existingCadastroAluno.setAutorizacao(cadastroAluno.getAutorizacao());
                        }
                        if (cadastroAluno.getFotoAluno() != null) {
                            existingCadastroAluno.setFotoAluno(cadastroAluno.getFotoAluno());
                        }
                        if (cadastroAluno.getFotoMae() != null) {
                            existingCadastroAluno.setFotoMae(cadastroAluno.getFotoMae());
                        }

                        return existingCadastroAluno;
                    })
                    .flatMap(cadastroAlunoRepository::save)
                    .flatMap(savedCadastroAluno -> {
                        cadastroAlunoSearchRepository.save(savedCadastroAluno);
                        return Mono.just(savedCadastroAluno);
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
     * {@code GET  /cadastro-alunos} : get all the cadastroAlunos.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cadastroAlunos in body.
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<List<CadastroAluno>>> getAllCadastroAlunos(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        LOG.debug("REST request to get a page of CadastroAlunos");
        return cadastroAlunoRepository
            .count()
            .zipWith(cadastroAlunoRepository.findAllBy(pageable).collectList())
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
     * {@code GET  /cadastro-alunos/:id} : get the "id" cadastroAluno.
     *
     * @param id the id of the cadastroAluno to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cadastroAluno, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public Mono<ResponseEntity<CadastroAluno>> getCadastroAluno(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CadastroAluno : {}", id);
        Mono<CadastroAluno> cadastroAluno = cadastroAlunoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(cadastroAluno);
    }

    /**
     * {@code DELETE  /cadastro-alunos/:id} : delete the "id" cadastroAluno.
     *
     * @param id the id of the cadastroAluno to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteCadastroAluno(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CadastroAluno : {}", id);
        return cadastroAlunoRepository
            .deleteById(id)
            .then(cadastroAlunoSearchRepository.deleteById(id))
            .then(
                Mono.just(
                    ResponseEntity.noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                        .build()
                )
            );
    }

    /**
     * {@code SEARCH  /cadastro-alunos/_search?query=:query} : search for the cadastroAluno corresponding
     * to the query.
     *
     * @param query the query of the cadastroAluno search.
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public Mono<ResponseEntity<Flux<CadastroAluno>>> searchCadastroAlunos(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request
    ) {
        LOG.debug("REST request to search for a page of CadastroAlunos for query {}", query);
        return cadastroAlunoSearchRepository
            .count()
            .map(total -> new PageImpl<>(new ArrayList<>(), pageable, total))
            .map(page ->
                PaginationUtil.generatePaginationHttpHeaders(
                    ForwardedHeaderUtils.adaptFromForwardedHeaders(request.getURI(), request.getHeaders()),
                    page
                )
            )
            .map(headers -> ResponseEntity.ok().headers(headers).body(cadastroAlunoSearchRepository.search(query, pageable)));
    }
}
