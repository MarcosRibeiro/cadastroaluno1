package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.CadastroAlunoAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.CadastroAluno;
import com.mycompany.myapp.domain.enumeration.Comportamento;
import com.mycompany.myapp.domain.enumeration.Comportamento;
import com.mycompany.myapp.domain.enumeration.SimNao;
import com.mycompany.myapp.domain.enumeration.SimNao;
import com.mycompany.myapp.domain.enumeration.SimNao;
import com.mycompany.myapp.domain.enumeration.SimNao;
import com.mycompany.myapp.domain.enumeration.SimNao;
import com.mycompany.myapp.domain.enumeration.SimNao;
import com.mycompany.myapp.domain.enumeration.SituacaoResidencia;
import com.mycompany.myapp.domain.enumeration.TipoResidencia;
import com.mycompany.myapp.domain.enumeration.Turno;
import com.mycompany.myapp.repository.CadastroAlunoRepository;
import com.mycompany.myapp.repository.EntityManager;
import com.mycompany.myapp.repository.search.CadastroAlunoSearchRepository;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link CadastroAlunoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class CadastroAlunoResourceIT {

    private static final LocalDate DEFAULT_DATA_CADASTRO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_CADASTRO = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_MATRICULA = "AAAAAAAAAA";
    private static final String UPDATED_MATRICULA = "BBBBBBBBBB";

    private static final String DEFAULT_GRUPO = "AAAAAAAAAA";
    private static final String UPDATED_GRUPO = "BBBBBBBBBB";

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DN = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_CEP = "AAAAAAAAAA";
    private static final String UPDATED_CEP = "BBBBBBBBBB";

    private static final String DEFAULT_ENDERECO = "AAAAAAAAAA";
    private static final String UPDATED_ENDERECO = "BBBBBBBBBB";

    private static final String DEFAULT_QD = "AAAAAAAAAA";
    private static final String UPDATED_QD = "BBBBBBBBBB";

    private static final String DEFAULT_LOTE = "AAAAAAAAAA";
    private static final String UPDATED_LOTE = "BBBBBBBBBB";

    private static final String DEFAULT_ENDNUMERO = "AAAAAAAAAA";
    private static final String UPDATED_ENDNUMERO = "BBBBBBBBBB";

    private static final String DEFAULT_BAIRRO = "AAAAAAAAAA";
    private static final String UPDATED_BAIRRO = "BBBBBBBBBB";

    private static final String DEFAULT_MUNICIPIO = "AAAAAAAAAA";
    private static final String UPDATED_MUNICIPIO = "BBBBBBBBBB";

    private static final String DEFAULT_UF = "AA";
    private static final String UPDATED_UF = "BB";

    private static final String DEFAULT_FONE = "AAAAAAAAAA";
    private static final String UPDATED_FONE = "BBBBBBBBBB";

    private static final String DEFAULT_CERTIDAO = "AAAAAAAAAA";
    private static final String UPDATED_CERTIDAO = "BBBBBBBBBB";

    private static final String DEFAULT_TERMO = "AAAAAAAAAA";
    private static final String UPDATED_TERMO = "BBBBBBBBBB";

    private static final String DEFAULT_CARTORIO = "AAAAAAAAAA";
    private static final String UPDATED_CARTORIO = "BBBBBBBBBB";

    private static final String DEFAULT_NATURALIDADE = "AAAAAAAAAA";
    private static final String UPDATED_NATURALIDADE = "BBBBBBBBBB";

    private static final String DEFAULT_RG = "AAAAAAAAAA";
    private static final String UPDATED_RG = "BBBBBBBBBB";

    private static final String DEFAULT_CPF = "AAAAAAAAAA";
    private static final String UPDATED_CPF = "BBBBBBBBBB";

    private static final String DEFAULT_NIS = "AAAAAAAAAA";
    private static final String UPDATED_NIS = "BBBBBBBBBB";

    private static final String DEFAULT_CRAS = "AAAAAAAAAA";
    private static final String UPDATED_CRAS = "BBBBBBBBBB";

    private static final String DEFAULT_FILIACAO_PAI = "AAAAAAAAAA";
    private static final String UPDATED_FILIACAO_PAI = "BBBBBBBBBB";

    private static final String DEFAULT_PAI_TELEFONE = "AAAAAAAAAA";
    private static final String UPDATED_PAI_TELEFONE = "BBBBBBBBBB";

    private static final String DEFAULT_PAI_NATURALIDADE = "AAAAAAAAAA";
    private static final String UPDATED_PAI_NATURALIDADE = "BBBBBBBBBB";

    private static final String DEFAULT_PAI_UF = "AA";
    private static final String UPDATED_PAI_UF = "BB";

    private static final String DEFAULT_PAI_RG = "AAAAAAAAAA";
    private static final String UPDATED_PAI_RG = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_PAI_DATA_NASCIMENTO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PAI_DATA_NASCIMENTO = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_PAI_CPF = "AAAAAAAAAA";
    private static final String UPDATED_PAI_CPF = "BBBBBBBBBB";

    private static final String DEFAULT_PAI_NIS = "AAAAAAAAAA";
    private static final String UPDATED_PAI_NIS = "BBBBBBBBBB";

    private static final String DEFAULT_PAI_TITULO_ELEITOR = "AAAAAAAAAA";
    private static final String UPDATED_PAI_TITULO_ELEITOR = "BBBBBBBBBB";

    private static final String DEFAULT_PAI_ZONA = "AAAAAAAAAA";
    private static final String UPDATED_PAI_ZONA = "BBBBBBBBBB";

    private static final String DEFAULT_PAI_SECAO = "AAAAAAAAAA";
    private static final String UPDATED_PAI_SECAO = "BBBBBBBBBB";

    private static final String DEFAULT_PAI_MUNICIPIO = "AAAAAAAAAA";
    private static final String UPDATED_PAI_MUNICIPIO = "BBBBBBBBBB";

    private static final String DEFAULT_FILIACAO_MAE = "AAAAAAAAAA";
    private static final String UPDATED_FILIACAO_MAE = "BBBBBBBBBB";

    private static final String DEFAULT_MAE_TELEFONE = "AAAAAAAAAA";
    private static final String UPDATED_MAE_TELEFONE = "BBBBBBBBBB";

    private static final String DEFAULT_MAE_NATURALIDADE = "AAAAAAAAAA";
    private static final String UPDATED_MAE_NATURALIDADE = "BBBBBBBBBB";

    private static final String DEFAULT_MAE_UF = "AA";
    private static final String UPDATED_MAE_UF = "BB";

    private static final String DEFAULT_MAE_RG = "AAAAAAAAAA";
    private static final String UPDATED_MAE_RG = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_MAE_DATA_NASCIMENTO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_MAE_DATA_NASCIMENTO = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_MAE_CPF = "AAAAAAAAAA";
    private static final String UPDATED_MAE_CPF = "BBBBBBBBBB";

    private static final String DEFAULT_MAE_NIS = "AAAAAAAAAA";
    private static final String UPDATED_MAE_NIS = "BBBBBBBBBB";

    private static final String DEFAULT_MAE_TITULO_ELEITOR = "AAAAAAAAAA";
    private static final String UPDATED_MAE_TITULO_ELEITOR = "BBBBBBBBBB";

    private static final String DEFAULT_MAE_ZONA = "AAAAAAAAAA";
    private static final String UPDATED_MAE_ZONA = "BBBBBBBBBB";

    private static final String DEFAULT_MAE_SECAO = "AAAAAAAAAA";
    private static final String UPDATED_MAE_SECAO = "BBBBBBBBBB";

    private static final String DEFAULT_MAE_MUNICIPIO = "AAAAAAAAAA";
    private static final String UPDATED_MAE_MUNICIPIO = "BBBBBBBBBB";

    private static final String DEFAULT_NOME_ESCOLA = "AAAAAAAAAA";
    private static final String UPDATED_NOME_ESCOLA = "BBBBBBBBBB";

    private static final String DEFAULT_ANO_CURSANDO = "AAAAAAAAAA";
    private static final String UPDATED_ANO_CURSANDO = "BBBBBBBBBB";

    private static final Turno DEFAULT_TURNO = Turno.MATUTINO;
    private static final Turno UPDATED_TURNO = Turno.VESPERTINO;

    private static final Double DEFAULT_MEDIA_ESCOLAR = 1D;
    private static final Double UPDATED_MEDIA_ESCOLAR = 2D;

    private static final SimNao DEFAULT_PRIORITARIO = SimNao.SIM;
    private static final SimNao UPDATED_PRIORITARIO = SimNao.NAO;

    private static final String DEFAULT_OBS = "AAAAAAAAAA";
    private static final String UPDATED_OBS = "BBBBBBBBBB";

    private static final Comportamento DEFAULT_COMPORTAMENTO_CASA = Comportamento.TRANQUILO;
    private static final Comportamento UPDATED_COMPORTAMENTO_CASA = Comportamento.AGITADO;

    private static final Comportamento DEFAULT_COMPORTAMENTO_ESCOLA = Comportamento.TRANQUILO;
    private static final Comportamento UPDATED_COMPORTAMENTO_ESCOLA = Comportamento.AGITADO;

    private static final SimNao DEFAULT_DEFICIENCIA = SimNao.SIM;
    private static final SimNao UPDATED_DEFICIENCIA = SimNao.NAO;

    private static final SimNao DEFAULT_ADAPTACOES = SimNao.SIM;
    private static final SimNao UPDATED_ADAPTACOES = SimNao.NAO;

    private static final SimNao DEFAULT_MEDICACAO = SimNao.SIM;
    private static final SimNao UPDATED_MEDICACAO = SimNao.NAO;

    private static final String DEFAULT_MEDICACAO_DESC = "AAAAAAAAAA";
    private static final String UPDATED_MEDICACAO_DESC = "BBBBBBBBBB";

    private static final SimNao DEFAULT_ALERGIA = SimNao.SIM;
    private static final SimNao UPDATED_ALERGIA = SimNao.NAO;

    private static final String DEFAULT_ALERGIA_DESC = "AAAAAAAAAA";
    private static final String UPDATED_ALERGIA_DESC = "BBBBBBBBBB";

    private static final String DEFAULT_HISTORICO_MEDICO = "AAAAAAAAAA";
    private static final String UPDATED_HISTORICO_MEDICO = "BBBBBBBBBB";

    private static final String DEFAULT_RENDA_FAMILIAR = "AAAAAAAAAA";
    private static final String UPDATED_RENDA_FAMILIAR = "BBBBBBBBBB";

    private static final Integer DEFAULT_PESSOAS_TRABALHAM = 1;
    private static final Integer UPDATED_PESSOAS_TRABALHAM = 2;

    private static final Integer DEFAULT_NUM_PESSOAS_LAR = 1;
    private static final Integer UPDATED_NUM_PESSOAS_LAR = 2;

    private static final SimNao DEFAULT_BENEFICIO_SOCIAL = SimNao.SIM;
    private static final SimNao UPDATED_BENEFICIO_SOCIAL = SimNao.NAO;

    private static final String DEFAULT_BENEFICIOS = "AAAAAAAAAA";
    private static final String UPDATED_BENEFICIOS = "BBBBBBBBBB";

    private static final TipoResidencia DEFAULT_TIPO_RESIDENCIA = TipoResidencia.CASA;
    private static final TipoResidencia UPDATED_TIPO_RESIDENCIA = TipoResidencia.APARTAMENTO;

    private static final String DEFAULT_TIPO_RESIDENCIA_DESC = "AAAAAAAAAA";
    private static final String UPDATED_TIPO_RESIDENCIA_DESC = "BBBBBBBBBB";

    private static final SituacaoResidencia DEFAULT_SITUACAO_RESIDENCIA = SituacaoResidencia.PROPRIA;
    private static final SituacaoResidencia UPDATED_SITUACAO_RESIDENCIA = SituacaoResidencia.ALUGADA;

    private static final String DEFAULT_SITUACAO_RESIDENCIA_DESC = "AAAAAAAAAA";
    private static final String UPDATED_SITUACAO_RESIDENCIA_DESC = "BBBBBBBBBB";

    private static final String DEFAULT_CONTATO_EMERGENCIA = "AAAAAAAAAA";
    private static final String UPDATED_CONTATO_EMERGENCIA = "BBBBBBBBBB";

    private static final String DEFAULT_FONE_EMERGENCIA = "AAAAAAAAAA";
    private static final String UPDATED_FONE_EMERGENCIA = "BBBBBBBBBB";

    private static final String DEFAULT_RELACAO_EMERGENCIA = "AAAAAAAAAA";
    private static final String UPDATED_RELACAO_EMERGENCIA = "BBBBBBBBBB";

    private static final Boolean DEFAULT_AUTORIZACAO = false;
    private static final Boolean UPDATED_AUTORIZACAO = true;

    private static final String DEFAULT_FOTO_ALUNO = "AAAAAAAAAA";
    private static final String UPDATED_FOTO_ALUNO = "BBBBBBBBBB";

    private static final String DEFAULT_FOTO_MAE = "AAAAAAAAAA";
    private static final String UPDATED_FOTO_MAE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/cadastro-alunos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/cadastro-alunos/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CadastroAlunoRepository cadastroAlunoRepository;

    @Autowired
    private CadastroAlunoSearchRepository cadastroAlunoSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private CadastroAluno cadastroAluno;

    private CadastroAluno insertedCadastroAluno;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CadastroAluno createEntity() {
        return new CadastroAluno()
            .dataCadastro(DEFAULT_DATA_CADASTRO)
            .matricula(DEFAULT_MATRICULA)
            .grupo(DEFAULT_GRUPO)
            .nome(DEFAULT_NOME)
            .dn(DEFAULT_DN)
            .cep(DEFAULT_CEP)
            .endereco(DEFAULT_ENDERECO)
            .qd(DEFAULT_QD)
            .lote(DEFAULT_LOTE)
            .endnumero(DEFAULT_ENDNUMERO)
            .bairro(DEFAULT_BAIRRO)
            .municipio(DEFAULT_MUNICIPIO)
            .uf(DEFAULT_UF)
            .fone(DEFAULT_FONE)
            .certidao(DEFAULT_CERTIDAO)
            .termo(DEFAULT_TERMO)
            .cartorio(DEFAULT_CARTORIO)
            .naturalidade(DEFAULT_NATURALIDADE)
            .rg(DEFAULT_RG)
            .cpf(DEFAULT_CPF)
            .nis(DEFAULT_NIS)
            .cras(DEFAULT_CRAS)
            .filiacaoPai(DEFAULT_FILIACAO_PAI)
            .paiTelefone(DEFAULT_PAI_TELEFONE)
            .paiNaturalidade(DEFAULT_PAI_NATURALIDADE)
            .paiUf(DEFAULT_PAI_UF)
            .paiRg(DEFAULT_PAI_RG)
            .paiDataNascimento(DEFAULT_PAI_DATA_NASCIMENTO)
            .paiCpf(DEFAULT_PAI_CPF)
            .paiNis(DEFAULT_PAI_NIS)
            .paiTituloEleitor(DEFAULT_PAI_TITULO_ELEITOR)
            .paiZona(DEFAULT_PAI_ZONA)
            .paiSecao(DEFAULT_PAI_SECAO)
            .paiMunicipio(DEFAULT_PAI_MUNICIPIO)
            .filiacaoMae(DEFAULT_FILIACAO_MAE)
            .maeTelefone(DEFAULT_MAE_TELEFONE)
            .maeNaturalidade(DEFAULT_MAE_NATURALIDADE)
            .maeUf(DEFAULT_MAE_UF)
            .maeRg(DEFAULT_MAE_RG)
            .maeDataNascimento(DEFAULT_MAE_DATA_NASCIMENTO)
            .maeCpf(DEFAULT_MAE_CPF)
            .maeNis(DEFAULT_MAE_NIS)
            .maeTituloEleitor(DEFAULT_MAE_TITULO_ELEITOR)
            .maeZona(DEFAULT_MAE_ZONA)
            .maeSecao(DEFAULT_MAE_SECAO)
            .maeMunicipio(DEFAULT_MAE_MUNICIPIO)
            .nomeEscola(DEFAULT_NOME_ESCOLA)
            .anoCursando(DEFAULT_ANO_CURSANDO)
            .turno(DEFAULT_TURNO)
            .mediaEscolar(DEFAULT_MEDIA_ESCOLAR)
            .prioritario(DEFAULT_PRIORITARIO)
            .obs(DEFAULT_OBS)
            .comportamentoCasa(DEFAULT_COMPORTAMENTO_CASA)
            .comportamentoEscola(DEFAULT_COMPORTAMENTO_ESCOLA)
            .deficiencia(DEFAULT_DEFICIENCIA)
            .adaptacoes(DEFAULT_ADAPTACOES)
            .medicacao(DEFAULT_MEDICACAO)
            .medicacaoDesc(DEFAULT_MEDICACAO_DESC)
            .alergia(DEFAULT_ALERGIA)
            .alergiaDesc(DEFAULT_ALERGIA_DESC)
            .historicoMedico(DEFAULT_HISTORICO_MEDICO)
            .rendaFamiliar(DEFAULT_RENDA_FAMILIAR)
            .pessoasTrabalham(DEFAULT_PESSOAS_TRABALHAM)
            .numPessoasLar(DEFAULT_NUM_PESSOAS_LAR)
            .beneficioSocial(DEFAULT_BENEFICIO_SOCIAL)
            .beneficios(DEFAULT_BENEFICIOS)
            .tipoResidencia(DEFAULT_TIPO_RESIDENCIA)
            .tipoResidenciaDesc(DEFAULT_TIPO_RESIDENCIA_DESC)
            .situacaoResidencia(DEFAULT_SITUACAO_RESIDENCIA)
            .situacaoResidenciaDesc(DEFAULT_SITUACAO_RESIDENCIA_DESC)
            .contatoEmergencia(DEFAULT_CONTATO_EMERGENCIA)
            .foneEmergencia(DEFAULT_FONE_EMERGENCIA)
            .relacaoEmergencia(DEFAULT_RELACAO_EMERGENCIA)
            .autorizacao(DEFAULT_AUTORIZACAO)
            .fotoAluno(DEFAULT_FOTO_ALUNO)
            .fotoMae(DEFAULT_FOTO_MAE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CadastroAluno createUpdatedEntity() {
        return new CadastroAluno()
            .dataCadastro(UPDATED_DATA_CADASTRO)
            .matricula(UPDATED_MATRICULA)
            .grupo(UPDATED_GRUPO)
            .nome(UPDATED_NOME)
            .dn(UPDATED_DN)
            .cep(UPDATED_CEP)
            .endereco(UPDATED_ENDERECO)
            .qd(UPDATED_QD)
            .lote(UPDATED_LOTE)
            .endnumero(UPDATED_ENDNUMERO)
            .bairro(UPDATED_BAIRRO)
            .municipio(UPDATED_MUNICIPIO)
            .uf(UPDATED_UF)
            .fone(UPDATED_FONE)
            .certidao(UPDATED_CERTIDAO)
            .termo(UPDATED_TERMO)
            .cartorio(UPDATED_CARTORIO)
            .naturalidade(UPDATED_NATURALIDADE)
            .rg(UPDATED_RG)
            .cpf(UPDATED_CPF)
            .nis(UPDATED_NIS)
            .cras(UPDATED_CRAS)
            .filiacaoPai(UPDATED_FILIACAO_PAI)
            .paiTelefone(UPDATED_PAI_TELEFONE)
            .paiNaturalidade(UPDATED_PAI_NATURALIDADE)
            .paiUf(UPDATED_PAI_UF)
            .paiRg(UPDATED_PAI_RG)
            .paiDataNascimento(UPDATED_PAI_DATA_NASCIMENTO)
            .paiCpf(UPDATED_PAI_CPF)
            .paiNis(UPDATED_PAI_NIS)
            .paiTituloEleitor(UPDATED_PAI_TITULO_ELEITOR)
            .paiZona(UPDATED_PAI_ZONA)
            .paiSecao(UPDATED_PAI_SECAO)
            .paiMunicipio(UPDATED_PAI_MUNICIPIO)
            .filiacaoMae(UPDATED_FILIACAO_MAE)
            .maeTelefone(UPDATED_MAE_TELEFONE)
            .maeNaturalidade(UPDATED_MAE_NATURALIDADE)
            .maeUf(UPDATED_MAE_UF)
            .maeRg(UPDATED_MAE_RG)
            .maeDataNascimento(UPDATED_MAE_DATA_NASCIMENTO)
            .maeCpf(UPDATED_MAE_CPF)
            .maeNis(UPDATED_MAE_NIS)
            .maeTituloEleitor(UPDATED_MAE_TITULO_ELEITOR)
            .maeZona(UPDATED_MAE_ZONA)
            .maeSecao(UPDATED_MAE_SECAO)
            .maeMunicipio(UPDATED_MAE_MUNICIPIO)
            .nomeEscola(UPDATED_NOME_ESCOLA)
            .anoCursando(UPDATED_ANO_CURSANDO)
            .turno(UPDATED_TURNO)
            .mediaEscolar(UPDATED_MEDIA_ESCOLAR)
            .prioritario(UPDATED_PRIORITARIO)
            .obs(UPDATED_OBS)
            .comportamentoCasa(UPDATED_COMPORTAMENTO_CASA)
            .comportamentoEscola(UPDATED_COMPORTAMENTO_ESCOLA)
            .deficiencia(UPDATED_DEFICIENCIA)
            .adaptacoes(UPDATED_ADAPTACOES)
            .medicacao(UPDATED_MEDICACAO)
            .medicacaoDesc(UPDATED_MEDICACAO_DESC)
            .alergia(UPDATED_ALERGIA)
            .alergiaDesc(UPDATED_ALERGIA_DESC)
            .historicoMedico(UPDATED_HISTORICO_MEDICO)
            .rendaFamiliar(UPDATED_RENDA_FAMILIAR)
            .pessoasTrabalham(UPDATED_PESSOAS_TRABALHAM)
            .numPessoasLar(UPDATED_NUM_PESSOAS_LAR)
            .beneficioSocial(UPDATED_BENEFICIO_SOCIAL)
            .beneficios(UPDATED_BENEFICIOS)
            .tipoResidencia(UPDATED_TIPO_RESIDENCIA)
            .tipoResidenciaDesc(UPDATED_TIPO_RESIDENCIA_DESC)
            .situacaoResidencia(UPDATED_SITUACAO_RESIDENCIA)
            .situacaoResidenciaDesc(UPDATED_SITUACAO_RESIDENCIA_DESC)
            .contatoEmergencia(UPDATED_CONTATO_EMERGENCIA)
            .foneEmergencia(UPDATED_FONE_EMERGENCIA)
            .relacaoEmergencia(UPDATED_RELACAO_EMERGENCIA)
            .autorizacao(UPDATED_AUTORIZACAO)
            .fotoAluno(UPDATED_FOTO_ALUNO)
            .fotoMae(UPDATED_FOTO_MAE);
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(CadastroAluno.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @BeforeEach
    public void initTest() {
        cadastroAluno = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedCadastroAluno != null) {
            cadastroAlunoRepository.delete(insertedCadastroAluno).block();
            cadastroAlunoSearchRepository.delete(insertedCadastroAluno).block();
            insertedCadastroAluno = null;
        }
        deleteEntities(em);
    }

    @Test
    void createCadastroAluno() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(cadastroAlunoSearchRepository.findAll().collectList().block());
        // Create the CadastroAluno
        var returnedCadastroAluno = webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(cadastroAluno))
            .exchange()
            .expectStatus()
            .isCreated()
            .expectBody(CadastroAluno.class)
            .returnResult()
            .getResponseBody();

        // Validate the CadastroAluno in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCadastroAlunoUpdatableFieldsEquals(returnedCadastroAluno, getPersistedCadastroAluno(returnedCadastroAluno));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(cadastroAlunoSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedCadastroAluno = returnedCadastroAluno;
    }

    @Test
    void createCadastroAlunoWithExistingId() throws Exception {
        // Create the CadastroAluno with an existing ID
        cadastroAluno.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(cadastroAlunoSearchRepository.findAll().collectList().block());

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(cadastroAluno))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CadastroAluno in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(cadastroAlunoSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkDataCadastroIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(cadastroAlunoSearchRepository.findAll().collectList().block());
        // set the field null
        cadastroAluno.setDataCadastro(null);

        // Create the CadastroAluno, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(cadastroAluno))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(cadastroAlunoSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkNomeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(cadastroAlunoSearchRepository.findAll().collectList().block());
        // set the field null
        cadastroAluno.setNome(null);

        // Create the CadastroAluno, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(cadastroAluno))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(cadastroAlunoSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkDnIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(cadastroAlunoSearchRepository.findAll().collectList().block());
        // set the field null
        cadastroAluno.setDn(null);

        // Create the CadastroAluno, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(cadastroAluno))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(cadastroAlunoSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkCepIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(cadastroAlunoSearchRepository.findAll().collectList().block());
        // set the field null
        cadastroAluno.setCep(null);

        // Create the CadastroAluno, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(cadastroAluno))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(cadastroAlunoSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkEnderecoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(cadastroAlunoSearchRepository.findAll().collectList().block());
        // set the field null
        cadastroAluno.setEndereco(null);

        // Create the CadastroAluno, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(cadastroAluno))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(cadastroAlunoSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkFoneIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(cadastroAlunoSearchRepository.findAll().collectList().block());
        // set the field null
        cadastroAluno.setFone(null);

        // Create the CadastroAluno, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(cadastroAluno))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(cadastroAlunoSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkCpfIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(cadastroAlunoSearchRepository.findAll().collectList().block());
        // set the field null
        cadastroAluno.setCpf(null);

        // Create the CadastroAluno, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(cadastroAluno))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(cadastroAlunoSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkFiliacaoMaeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(cadastroAlunoSearchRepository.findAll().collectList().block());
        // set the field null
        cadastroAluno.setFiliacaoMae(null);

        // Create the CadastroAluno, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(cadastroAluno))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(cadastroAlunoSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkContatoEmergenciaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(cadastroAlunoSearchRepository.findAll().collectList().block());
        // set the field null
        cadastroAluno.setContatoEmergencia(null);

        // Create the CadastroAluno, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(cadastroAluno))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(cadastroAlunoSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkFoneEmergenciaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(cadastroAlunoSearchRepository.findAll().collectList().block());
        // set the field null
        cadastroAluno.setFoneEmergencia(null);

        // Create the CadastroAluno, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(cadastroAluno))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(cadastroAlunoSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkRelacaoEmergenciaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(cadastroAlunoSearchRepository.findAll().collectList().block());
        // set the field null
        cadastroAluno.setRelacaoEmergencia(null);

        // Create the CadastroAluno, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(cadastroAluno))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(cadastroAlunoSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void checkAutorizacaoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(cadastroAlunoSearchRepository.findAll().collectList().block());
        // set the field null
        cadastroAluno.setAutorizacao(null);

        // Create the CadastroAluno, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(cadastroAluno))
            .exchange()
            .expectStatus()
            .isBadRequest();

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(cadastroAlunoSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void getAllCadastroAlunos() {
        // Initialize the database
        insertedCadastroAluno = cadastroAlunoRepository.save(cadastroAluno).block();

        // Get all the cadastroAlunoList
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
            .value(hasItem(cadastroAluno.getId().intValue()))
            .jsonPath("$.[*].dataCadastro")
            .value(hasItem(DEFAULT_DATA_CADASTRO.toString()))
            .jsonPath("$.[*].matricula")
            .value(hasItem(DEFAULT_MATRICULA))
            .jsonPath("$.[*].grupo")
            .value(hasItem(DEFAULT_GRUPO))
            .jsonPath("$.[*].nome")
            .value(hasItem(DEFAULT_NOME))
            .jsonPath("$.[*].dn")
            .value(hasItem(DEFAULT_DN.toString()))
            .jsonPath("$.[*].cep")
            .value(hasItem(DEFAULT_CEP))
            .jsonPath("$.[*].endereco")
            .value(hasItem(DEFAULT_ENDERECO))
            .jsonPath("$.[*].qd")
            .value(hasItem(DEFAULT_QD))
            .jsonPath("$.[*].lote")
            .value(hasItem(DEFAULT_LOTE))
            .jsonPath("$.[*].endnumero")
            .value(hasItem(DEFAULT_ENDNUMERO))
            .jsonPath("$.[*].bairro")
            .value(hasItem(DEFAULT_BAIRRO))
            .jsonPath("$.[*].municipio")
            .value(hasItem(DEFAULT_MUNICIPIO))
            .jsonPath("$.[*].uf")
            .value(hasItem(DEFAULT_UF))
            .jsonPath("$.[*].fone")
            .value(hasItem(DEFAULT_FONE))
            .jsonPath("$.[*].certidao")
            .value(hasItem(DEFAULT_CERTIDAO))
            .jsonPath("$.[*].termo")
            .value(hasItem(DEFAULT_TERMO))
            .jsonPath("$.[*].cartorio")
            .value(hasItem(DEFAULT_CARTORIO))
            .jsonPath("$.[*].naturalidade")
            .value(hasItem(DEFAULT_NATURALIDADE))
            .jsonPath("$.[*].rg")
            .value(hasItem(DEFAULT_RG))
            .jsonPath("$.[*].cpf")
            .value(hasItem(DEFAULT_CPF))
            .jsonPath("$.[*].nis")
            .value(hasItem(DEFAULT_NIS))
            .jsonPath("$.[*].cras")
            .value(hasItem(DEFAULT_CRAS))
            .jsonPath("$.[*].filiacaoPai")
            .value(hasItem(DEFAULT_FILIACAO_PAI))
            .jsonPath("$.[*].paiTelefone")
            .value(hasItem(DEFAULT_PAI_TELEFONE))
            .jsonPath("$.[*].paiNaturalidade")
            .value(hasItem(DEFAULT_PAI_NATURALIDADE))
            .jsonPath("$.[*].paiUf")
            .value(hasItem(DEFAULT_PAI_UF))
            .jsonPath("$.[*].paiRg")
            .value(hasItem(DEFAULT_PAI_RG))
            .jsonPath("$.[*].paiDataNascimento")
            .value(hasItem(DEFAULT_PAI_DATA_NASCIMENTO.toString()))
            .jsonPath("$.[*].paiCpf")
            .value(hasItem(DEFAULT_PAI_CPF))
            .jsonPath("$.[*].paiNis")
            .value(hasItem(DEFAULT_PAI_NIS))
            .jsonPath("$.[*].paiTituloEleitor")
            .value(hasItem(DEFAULT_PAI_TITULO_ELEITOR))
            .jsonPath("$.[*].paiZona")
            .value(hasItem(DEFAULT_PAI_ZONA))
            .jsonPath("$.[*].paiSecao")
            .value(hasItem(DEFAULT_PAI_SECAO))
            .jsonPath("$.[*].paiMunicipio")
            .value(hasItem(DEFAULT_PAI_MUNICIPIO))
            .jsonPath("$.[*].filiacaoMae")
            .value(hasItem(DEFAULT_FILIACAO_MAE))
            .jsonPath("$.[*].maeTelefone")
            .value(hasItem(DEFAULT_MAE_TELEFONE))
            .jsonPath("$.[*].maeNaturalidade")
            .value(hasItem(DEFAULT_MAE_NATURALIDADE))
            .jsonPath("$.[*].maeUf")
            .value(hasItem(DEFAULT_MAE_UF))
            .jsonPath("$.[*].maeRg")
            .value(hasItem(DEFAULT_MAE_RG))
            .jsonPath("$.[*].maeDataNascimento")
            .value(hasItem(DEFAULT_MAE_DATA_NASCIMENTO.toString()))
            .jsonPath("$.[*].maeCpf")
            .value(hasItem(DEFAULT_MAE_CPF))
            .jsonPath("$.[*].maeNis")
            .value(hasItem(DEFAULT_MAE_NIS))
            .jsonPath("$.[*].maeTituloEleitor")
            .value(hasItem(DEFAULT_MAE_TITULO_ELEITOR))
            .jsonPath("$.[*].maeZona")
            .value(hasItem(DEFAULT_MAE_ZONA))
            .jsonPath("$.[*].maeSecao")
            .value(hasItem(DEFAULT_MAE_SECAO))
            .jsonPath("$.[*].maeMunicipio")
            .value(hasItem(DEFAULT_MAE_MUNICIPIO))
            .jsonPath("$.[*].nomeEscola")
            .value(hasItem(DEFAULT_NOME_ESCOLA))
            .jsonPath("$.[*].anoCursando")
            .value(hasItem(DEFAULT_ANO_CURSANDO))
            .jsonPath("$.[*].turno")
            .value(hasItem(DEFAULT_TURNO.toString()))
            .jsonPath("$.[*].mediaEscolar")
            .value(hasItem(DEFAULT_MEDIA_ESCOLAR))
            .jsonPath("$.[*].prioritario")
            .value(hasItem(DEFAULT_PRIORITARIO.toString()))
            .jsonPath("$.[*].obs")
            .value(hasItem(DEFAULT_OBS))
            .jsonPath("$.[*].comportamentoCasa")
            .value(hasItem(DEFAULT_COMPORTAMENTO_CASA.toString()))
            .jsonPath("$.[*].comportamentoEscola")
            .value(hasItem(DEFAULT_COMPORTAMENTO_ESCOLA.toString()))
            .jsonPath("$.[*].deficiencia")
            .value(hasItem(DEFAULT_DEFICIENCIA.toString()))
            .jsonPath("$.[*].adaptacoes")
            .value(hasItem(DEFAULT_ADAPTACOES.toString()))
            .jsonPath("$.[*].medicacao")
            .value(hasItem(DEFAULT_MEDICACAO.toString()))
            .jsonPath("$.[*].medicacaoDesc")
            .value(hasItem(DEFAULT_MEDICACAO_DESC))
            .jsonPath("$.[*].alergia")
            .value(hasItem(DEFAULT_ALERGIA.toString()))
            .jsonPath("$.[*].alergiaDesc")
            .value(hasItem(DEFAULT_ALERGIA_DESC))
            .jsonPath("$.[*].historicoMedico")
            .value(hasItem(DEFAULT_HISTORICO_MEDICO))
            .jsonPath("$.[*].rendaFamiliar")
            .value(hasItem(DEFAULT_RENDA_FAMILIAR))
            .jsonPath("$.[*].pessoasTrabalham")
            .value(hasItem(DEFAULT_PESSOAS_TRABALHAM))
            .jsonPath("$.[*].numPessoasLar")
            .value(hasItem(DEFAULT_NUM_PESSOAS_LAR))
            .jsonPath("$.[*].beneficioSocial")
            .value(hasItem(DEFAULT_BENEFICIO_SOCIAL.toString()))
            .jsonPath("$.[*].beneficios")
            .value(hasItem(DEFAULT_BENEFICIOS))
            .jsonPath("$.[*].tipoResidencia")
            .value(hasItem(DEFAULT_TIPO_RESIDENCIA.toString()))
            .jsonPath("$.[*].tipoResidenciaDesc")
            .value(hasItem(DEFAULT_TIPO_RESIDENCIA_DESC))
            .jsonPath("$.[*].situacaoResidencia")
            .value(hasItem(DEFAULT_SITUACAO_RESIDENCIA.toString()))
            .jsonPath("$.[*].situacaoResidenciaDesc")
            .value(hasItem(DEFAULT_SITUACAO_RESIDENCIA_DESC))
            .jsonPath("$.[*].contatoEmergencia")
            .value(hasItem(DEFAULT_CONTATO_EMERGENCIA))
            .jsonPath("$.[*].foneEmergencia")
            .value(hasItem(DEFAULT_FONE_EMERGENCIA))
            .jsonPath("$.[*].relacaoEmergencia")
            .value(hasItem(DEFAULT_RELACAO_EMERGENCIA))
            .jsonPath("$.[*].autorizacao")
            .value(hasItem(DEFAULT_AUTORIZACAO))
            .jsonPath("$.[*].fotoAluno")
            .value(hasItem(DEFAULT_FOTO_ALUNO))
            .jsonPath("$.[*].fotoMae")
            .value(hasItem(DEFAULT_FOTO_MAE));
    }

    @Test
    void getCadastroAluno() {
        // Initialize the database
        insertedCadastroAluno = cadastroAlunoRepository.save(cadastroAluno).block();

        // Get the cadastroAluno
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, cadastroAluno.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(cadastroAluno.getId().intValue()))
            .jsonPath("$.dataCadastro")
            .value(is(DEFAULT_DATA_CADASTRO.toString()))
            .jsonPath("$.matricula")
            .value(is(DEFAULT_MATRICULA))
            .jsonPath("$.grupo")
            .value(is(DEFAULT_GRUPO))
            .jsonPath("$.nome")
            .value(is(DEFAULT_NOME))
            .jsonPath("$.dn")
            .value(is(DEFAULT_DN.toString()))
            .jsonPath("$.cep")
            .value(is(DEFAULT_CEP))
            .jsonPath("$.endereco")
            .value(is(DEFAULT_ENDERECO))
            .jsonPath("$.qd")
            .value(is(DEFAULT_QD))
            .jsonPath("$.lote")
            .value(is(DEFAULT_LOTE))
            .jsonPath("$.endnumero")
            .value(is(DEFAULT_ENDNUMERO))
            .jsonPath("$.bairro")
            .value(is(DEFAULT_BAIRRO))
            .jsonPath("$.municipio")
            .value(is(DEFAULT_MUNICIPIO))
            .jsonPath("$.uf")
            .value(is(DEFAULT_UF))
            .jsonPath("$.fone")
            .value(is(DEFAULT_FONE))
            .jsonPath("$.certidao")
            .value(is(DEFAULT_CERTIDAO))
            .jsonPath("$.termo")
            .value(is(DEFAULT_TERMO))
            .jsonPath("$.cartorio")
            .value(is(DEFAULT_CARTORIO))
            .jsonPath("$.naturalidade")
            .value(is(DEFAULT_NATURALIDADE))
            .jsonPath("$.rg")
            .value(is(DEFAULT_RG))
            .jsonPath("$.cpf")
            .value(is(DEFAULT_CPF))
            .jsonPath("$.nis")
            .value(is(DEFAULT_NIS))
            .jsonPath("$.cras")
            .value(is(DEFAULT_CRAS))
            .jsonPath("$.filiacaoPai")
            .value(is(DEFAULT_FILIACAO_PAI))
            .jsonPath("$.paiTelefone")
            .value(is(DEFAULT_PAI_TELEFONE))
            .jsonPath("$.paiNaturalidade")
            .value(is(DEFAULT_PAI_NATURALIDADE))
            .jsonPath("$.paiUf")
            .value(is(DEFAULT_PAI_UF))
            .jsonPath("$.paiRg")
            .value(is(DEFAULT_PAI_RG))
            .jsonPath("$.paiDataNascimento")
            .value(is(DEFAULT_PAI_DATA_NASCIMENTO.toString()))
            .jsonPath("$.paiCpf")
            .value(is(DEFAULT_PAI_CPF))
            .jsonPath("$.paiNis")
            .value(is(DEFAULT_PAI_NIS))
            .jsonPath("$.paiTituloEleitor")
            .value(is(DEFAULT_PAI_TITULO_ELEITOR))
            .jsonPath("$.paiZona")
            .value(is(DEFAULT_PAI_ZONA))
            .jsonPath("$.paiSecao")
            .value(is(DEFAULT_PAI_SECAO))
            .jsonPath("$.paiMunicipio")
            .value(is(DEFAULT_PAI_MUNICIPIO))
            .jsonPath("$.filiacaoMae")
            .value(is(DEFAULT_FILIACAO_MAE))
            .jsonPath("$.maeTelefone")
            .value(is(DEFAULT_MAE_TELEFONE))
            .jsonPath("$.maeNaturalidade")
            .value(is(DEFAULT_MAE_NATURALIDADE))
            .jsonPath("$.maeUf")
            .value(is(DEFAULT_MAE_UF))
            .jsonPath("$.maeRg")
            .value(is(DEFAULT_MAE_RG))
            .jsonPath("$.maeDataNascimento")
            .value(is(DEFAULT_MAE_DATA_NASCIMENTO.toString()))
            .jsonPath("$.maeCpf")
            .value(is(DEFAULT_MAE_CPF))
            .jsonPath("$.maeNis")
            .value(is(DEFAULT_MAE_NIS))
            .jsonPath("$.maeTituloEleitor")
            .value(is(DEFAULT_MAE_TITULO_ELEITOR))
            .jsonPath("$.maeZona")
            .value(is(DEFAULT_MAE_ZONA))
            .jsonPath("$.maeSecao")
            .value(is(DEFAULT_MAE_SECAO))
            .jsonPath("$.maeMunicipio")
            .value(is(DEFAULT_MAE_MUNICIPIO))
            .jsonPath("$.nomeEscola")
            .value(is(DEFAULT_NOME_ESCOLA))
            .jsonPath("$.anoCursando")
            .value(is(DEFAULT_ANO_CURSANDO))
            .jsonPath("$.turno")
            .value(is(DEFAULT_TURNO.toString()))
            .jsonPath("$.mediaEscolar")
            .value(is(DEFAULT_MEDIA_ESCOLAR))
            .jsonPath("$.prioritario")
            .value(is(DEFAULT_PRIORITARIO.toString()))
            .jsonPath("$.obs")
            .value(is(DEFAULT_OBS))
            .jsonPath("$.comportamentoCasa")
            .value(is(DEFAULT_COMPORTAMENTO_CASA.toString()))
            .jsonPath("$.comportamentoEscola")
            .value(is(DEFAULT_COMPORTAMENTO_ESCOLA.toString()))
            .jsonPath("$.deficiencia")
            .value(is(DEFAULT_DEFICIENCIA.toString()))
            .jsonPath("$.adaptacoes")
            .value(is(DEFAULT_ADAPTACOES.toString()))
            .jsonPath("$.medicacao")
            .value(is(DEFAULT_MEDICACAO.toString()))
            .jsonPath("$.medicacaoDesc")
            .value(is(DEFAULT_MEDICACAO_DESC))
            .jsonPath("$.alergia")
            .value(is(DEFAULT_ALERGIA.toString()))
            .jsonPath("$.alergiaDesc")
            .value(is(DEFAULT_ALERGIA_DESC))
            .jsonPath("$.historicoMedico")
            .value(is(DEFAULT_HISTORICO_MEDICO))
            .jsonPath("$.rendaFamiliar")
            .value(is(DEFAULT_RENDA_FAMILIAR))
            .jsonPath("$.pessoasTrabalham")
            .value(is(DEFAULT_PESSOAS_TRABALHAM))
            .jsonPath("$.numPessoasLar")
            .value(is(DEFAULT_NUM_PESSOAS_LAR))
            .jsonPath("$.beneficioSocial")
            .value(is(DEFAULT_BENEFICIO_SOCIAL.toString()))
            .jsonPath("$.beneficios")
            .value(is(DEFAULT_BENEFICIOS))
            .jsonPath("$.tipoResidencia")
            .value(is(DEFAULT_TIPO_RESIDENCIA.toString()))
            .jsonPath("$.tipoResidenciaDesc")
            .value(is(DEFAULT_TIPO_RESIDENCIA_DESC))
            .jsonPath("$.situacaoResidencia")
            .value(is(DEFAULT_SITUACAO_RESIDENCIA.toString()))
            .jsonPath("$.situacaoResidenciaDesc")
            .value(is(DEFAULT_SITUACAO_RESIDENCIA_DESC))
            .jsonPath("$.contatoEmergencia")
            .value(is(DEFAULT_CONTATO_EMERGENCIA))
            .jsonPath("$.foneEmergencia")
            .value(is(DEFAULT_FONE_EMERGENCIA))
            .jsonPath("$.relacaoEmergencia")
            .value(is(DEFAULT_RELACAO_EMERGENCIA))
            .jsonPath("$.autorizacao")
            .value(is(DEFAULT_AUTORIZACAO))
            .jsonPath("$.fotoAluno")
            .value(is(DEFAULT_FOTO_ALUNO))
            .jsonPath("$.fotoMae")
            .value(is(DEFAULT_FOTO_MAE));
    }

    @Test
    void getNonExistingCadastroAluno() {
        // Get the cadastroAluno
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_PROBLEM_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingCadastroAluno() throws Exception {
        // Initialize the database
        insertedCadastroAluno = cadastroAlunoRepository.save(cadastroAluno).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();
        cadastroAlunoSearchRepository.save(cadastroAluno).block();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(cadastroAlunoSearchRepository.findAll().collectList().block());

        // Update the cadastroAluno
        CadastroAluno updatedCadastroAluno = cadastroAlunoRepository.findById(cadastroAluno.getId()).block();
        updatedCadastroAluno
            .dataCadastro(UPDATED_DATA_CADASTRO)
            .matricula(UPDATED_MATRICULA)
            .grupo(UPDATED_GRUPO)
            .nome(UPDATED_NOME)
            .dn(UPDATED_DN)
            .cep(UPDATED_CEP)
            .endereco(UPDATED_ENDERECO)
            .qd(UPDATED_QD)
            .lote(UPDATED_LOTE)
            .endnumero(UPDATED_ENDNUMERO)
            .bairro(UPDATED_BAIRRO)
            .municipio(UPDATED_MUNICIPIO)
            .uf(UPDATED_UF)
            .fone(UPDATED_FONE)
            .certidao(UPDATED_CERTIDAO)
            .termo(UPDATED_TERMO)
            .cartorio(UPDATED_CARTORIO)
            .naturalidade(UPDATED_NATURALIDADE)
            .rg(UPDATED_RG)
            .cpf(UPDATED_CPF)
            .nis(UPDATED_NIS)
            .cras(UPDATED_CRAS)
            .filiacaoPai(UPDATED_FILIACAO_PAI)
            .paiTelefone(UPDATED_PAI_TELEFONE)
            .paiNaturalidade(UPDATED_PAI_NATURALIDADE)
            .paiUf(UPDATED_PAI_UF)
            .paiRg(UPDATED_PAI_RG)
            .paiDataNascimento(UPDATED_PAI_DATA_NASCIMENTO)
            .paiCpf(UPDATED_PAI_CPF)
            .paiNis(UPDATED_PAI_NIS)
            .paiTituloEleitor(UPDATED_PAI_TITULO_ELEITOR)
            .paiZona(UPDATED_PAI_ZONA)
            .paiSecao(UPDATED_PAI_SECAO)
            .paiMunicipio(UPDATED_PAI_MUNICIPIO)
            .filiacaoMae(UPDATED_FILIACAO_MAE)
            .maeTelefone(UPDATED_MAE_TELEFONE)
            .maeNaturalidade(UPDATED_MAE_NATURALIDADE)
            .maeUf(UPDATED_MAE_UF)
            .maeRg(UPDATED_MAE_RG)
            .maeDataNascimento(UPDATED_MAE_DATA_NASCIMENTO)
            .maeCpf(UPDATED_MAE_CPF)
            .maeNis(UPDATED_MAE_NIS)
            .maeTituloEleitor(UPDATED_MAE_TITULO_ELEITOR)
            .maeZona(UPDATED_MAE_ZONA)
            .maeSecao(UPDATED_MAE_SECAO)
            .maeMunicipio(UPDATED_MAE_MUNICIPIO)
            .nomeEscola(UPDATED_NOME_ESCOLA)
            .anoCursando(UPDATED_ANO_CURSANDO)
            .turno(UPDATED_TURNO)
            .mediaEscolar(UPDATED_MEDIA_ESCOLAR)
            .prioritario(UPDATED_PRIORITARIO)
            .obs(UPDATED_OBS)
            .comportamentoCasa(UPDATED_COMPORTAMENTO_CASA)
            .comportamentoEscola(UPDATED_COMPORTAMENTO_ESCOLA)
            .deficiencia(UPDATED_DEFICIENCIA)
            .adaptacoes(UPDATED_ADAPTACOES)
            .medicacao(UPDATED_MEDICACAO)
            .medicacaoDesc(UPDATED_MEDICACAO_DESC)
            .alergia(UPDATED_ALERGIA)
            .alergiaDesc(UPDATED_ALERGIA_DESC)
            .historicoMedico(UPDATED_HISTORICO_MEDICO)
            .rendaFamiliar(UPDATED_RENDA_FAMILIAR)
            .pessoasTrabalham(UPDATED_PESSOAS_TRABALHAM)
            .numPessoasLar(UPDATED_NUM_PESSOAS_LAR)
            .beneficioSocial(UPDATED_BENEFICIO_SOCIAL)
            .beneficios(UPDATED_BENEFICIOS)
            .tipoResidencia(UPDATED_TIPO_RESIDENCIA)
            .tipoResidenciaDesc(UPDATED_TIPO_RESIDENCIA_DESC)
            .situacaoResidencia(UPDATED_SITUACAO_RESIDENCIA)
            .situacaoResidenciaDesc(UPDATED_SITUACAO_RESIDENCIA_DESC)
            .contatoEmergencia(UPDATED_CONTATO_EMERGENCIA)
            .foneEmergencia(UPDATED_FONE_EMERGENCIA)
            .relacaoEmergencia(UPDATED_RELACAO_EMERGENCIA)
            .autorizacao(UPDATED_AUTORIZACAO)
            .fotoAluno(UPDATED_FOTO_ALUNO)
            .fotoMae(UPDATED_FOTO_MAE);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedCadastroAluno.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(updatedCadastroAluno))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CadastroAluno in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCadastroAlunoToMatchAllProperties(updatedCadastroAluno);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(cadastroAlunoSearchRepository.findAll().collectList().block());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<CadastroAluno> cadastroAlunoSearchList = Streamable.of(
                    cadastroAlunoSearchRepository.findAll().collectList().block()
                ).toList();
                CadastroAluno testCadastroAlunoSearch = cadastroAlunoSearchList.get(searchDatabaseSizeAfter - 1);

                // Test fails because reactive api returns an empty object instead of null
                // assertCadastroAlunoAllPropertiesEquals(testCadastroAlunoSearch, updatedCadastroAluno);
                assertCadastroAlunoUpdatableFieldsEquals(testCadastroAlunoSearch, updatedCadastroAluno);
            });
    }

    @Test
    void putNonExistingCadastroAluno() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(cadastroAlunoSearchRepository.findAll().collectList().block());
        cadastroAluno.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, cadastroAluno.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(cadastroAluno))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CadastroAluno in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(cadastroAlunoSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithIdMismatchCadastroAluno() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(cadastroAlunoSearchRepository.findAll().collectList().block());
        cadastroAluno.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(cadastroAluno))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CadastroAluno in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(cadastroAlunoSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void putWithMissingIdPathParamCadastroAluno() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(cadastroAlunoSearchRepository.findAll().collectList().block());
        cadastroAluno.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(om.writeValueAsBytes(cadastroAluno))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the CadastroAluno in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(cadastroAlunoSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void partialUpdateCadastroAlunoWithPatch() throws Exception {
        // Initialize the database
        insertedCadastroAluno = cadastroAlunoRepository.save(cadastroAluno).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cadastroAluno using partial update
        CadastroAluno partialUpdatedCadastroAluno = new CadastroAluno();
        partialUpdatedCadastroAluno.setId(cadastroAluno.getId());

        partialUpdatedCadastroAluno
            .dataCadastro(UPDATED_DATA_CADASTRO)
            .nome(UPDATED_NOME)
            .cep(UPDATED_CEP)
            .endereco(UPDATED_ENDERECO)
            .qd(UPDATED_QD)
            .endnumero(UPDATED_ENDNUMERO)
            .uf(UPDATED_UF)
            .certidao(UPDATED_CERTIDAO)
            .termo(UPDATED_TERMO)
            .cartorio(UPDATED_CARTORIO)
            .rg(UPDATED_RG)
            .nis(UPDATED_NIS)
            .filiacaoPai(UPDATED_FILIACAO_PAI)
            .paiTelefone(UPDATED_PAI_TELEFONE)
            .paiUf(UPDATED_PAI_UF)
            .paiRg(UPDATED_PAI_RG)
            .paiDataNascimento(UPDATED_PAI_DATA_NASCIMENTO)
            .paiCpf(UPDATED_PAI_CPF)
            .paiNis(UPDATED_PAI_NIS)
            .paiTituloEleitor(UPDATED_PAI_TITULO_ELEITOR)
            .paiMunicipio(UPDATED_PAI_MUNICIPIO)
            .filiacaoMae(UPDATED_FILIACAO_MAE)
            .maeNaturalidade(UPDATED_MAE_NATURALIDADE)
            .maeZona(UPDATED_MAE_ZONA)
            .maeSecao(UPDATED_MAE_SECAO)
            .maeMunicipio(UPDATED_MAE_MUNICIPIO)
            .nomeEscola(UPDATED_NOME_ESCOLA)
            .anoCursando(UPDATED_ANO_CURSANDO)
            .obs(UPDATED_OBS)
            .comportamentoCasa(UPDATED_COMPORTAMENTO_CASA)
            .deficiencia(UPDATED_DEFICIENCIA)
            .medicacao(UPDATED_MEDICACAO)
            .pessoasTrabalham(UPDATED_PESSOAS_TRABALHAM)
            .beneficios(UPDATED_BENEFICIOS)
            .tipoResidencia(UPDATED_TIPO_RESIDENCIA)
            .tipoResidenciaDesc(UPDATED_TIPO_RESIDENCIA_DESC)
            .contatoEmergencia(UPDATED_CONTATO_EMERGENCIA)
            .fotoAluno(UPDATED_FOTO_ALUNO)
            .fotoMae(UPDATED_FOTO_MAE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCadastroAluno.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedCadastroAluno))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CadastroAluno in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCadastroAlunoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCadastroAluno, cadastroAluno),
            getPersistedCadastroAluno(cadastroAluno)
        );
    }

    @Test
    void fullUpdateCadastroAlunoWithPatch() throws Exception {
        // Initialize the database
        insertedCadastroAluno = cadastroAlunoRepository.save(cadastroAluno).block();

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cadastroAluno using partial update
        CadastroAluno partialUpdatedCadastroAluno = new CadastroAluno();
        partialUpdatedCadastroAluno.setId(cadastroAluno.getId());

        partialUpdatedCadastroAluno
            .dataCadastro(UPDATED_DATA_CADASTRO)
            .matricula(UPDATED_MATRICULA)
            .grupo(UPDATED_GRUPO)
            .nome(UPDATED_NOME)
            .dn(UPDATED_DN)
            .cep(UPDATED_CEP)
            .endereco(UPDATED_ENDERECO)
            .qd(UPDATED_QD)
            .lote(UPDATED_LOTE)
            .endnumero(UPDATED_ENDNUMERO)
            .bairro(UPDATED_BAIRRO)
            .municipio(UPDATED_MUNICIPIO)
            .uf(UPDATED_UF)
            .fone(UPDATED_FONE)
            .certidao(UPDATED_CERTIDAO)
            .termo(UPDATED_TERMO)
            .cartorio(UPDATED_CARTORIO)
            .naturalidade(UPDATED_NATURALIDADE)
            .rg(UPDATED_RG)
            .cpf(UPDATED_CPF)
            .nis(UPDATED_NIS)
            .cras(UPDATED_CRAS)
            .filiacaoPai(UPDATED_FILIACAO_PAI)
            .paiTelefone(UPDATED_PAI_TELEFONE)
            .paiNaturalidade(UPDATED_PAI_NATURALIDADE)
            .paiUf(UPDATED_PAI_UF)
            .paiRg(UPDATED_PAI_RG)
            .paiDataNascimento(UPDATED_PAI_DATA_NASCIMENTO)
            .paiCpf(UPDATED_PAI_CPF)
            .paiNis(UPDATED_PAI_NIS)
            .paiTituloEleitor(UPDATED_PAI_TITULO_ELEITOR)
            .paiZona(UPDATED_PAI_ZONA)
            .paiSecao(UPDATED_PAI_SECAO)
            .paiMunicipio(UPDATED_PAI_MUNICIPIO)
            .filiacaoMae(UPDATED_FILIACAO_MAE)
            .maeTelefone(UPDATED_MAE_TELEFONE)
            .maeNaturalidade(UPDATED_MAE_NATURALIDADE)
            .maeUf(UPDATED_MAE_UF)
            .maeRg(UPDATED_MAE_RG)
            .maeDataNascimento(UPDATED_MAE_DATA_NASCIMENTO)
            .maeCpf(UPDATED_MAE_CPF)
            .maeNis(UPDATED_MAE_NIS)
            .maeTituloEleitor(UPDATED_MAE_TITULO_ELEITOR)
            .maeZona(UPDATED_MAE_ZONA)
            .maeSecao(UPDATED_MAE_SECAO)
            .maeMunicipio(UPDATED_MAE_MUNICIPIO)
            .nomeEscola(UPDATED_NOME_ESCOLA)
            .anoCursando(UPDATED_ANO_CURSANDO)
            .turno(UPDATED_TURNO)
            .mediaEscolar(UPDATED_MEDIA_ESCOLAR)
            .prioritario(UPDATED_PRIORITARIO)
            .obs(UPDATED_OBS)
            .comportamentoCasa(UPDATED_COMPORTAMENTO_CASA)
            .comportamentoEscola(UPDATED_COMPORTAMENTO_ESCOLA)
            .deficiencia(UPDATED_DEFICIENCIA)
            .adaptacoes(UPDATED_ADAPTACOES)
            .medicacao(UPDATED_MEDICACAO)
            .medicacaoDesc(UPDATED_MEDICACAO_DESC)
            .alergia(UPDATED_ALERGIA)
            .alergiaDesc(UPDATED_ALERGIA_DESC)
            .historicoMedico(UPDATED_HISTORICO_MEDICO)
            .rendaFamiliar(UPDATED_RENDA_FAMILIAR)
            .pessoasTrabalham(UPDATED_PESSOAS_TRABALHAM)
            .numPessoasLar(UPDATED_NUM_PESSOAS_LAR)
            .beneficioSocial(UPDATED_BENEFICIO_SOCIAL)
            .beneficios(UPDATED_BENEFICIOS)
            .tipoResidencia(UPDATED_TIPO_RESIDENCIA)
            .tipoResidenciaDesc(UPDATED_TIPO_RESIDENCIA_DESC)
            .situacaoResidencia(UPDATED_SITUACAO_RESIDENCIA)
            .situacaoResidenciaDesc(UPDATED_SITUACAO_RESIDENCIA_DESC)
            .contatoEmergencia(UPDATED_CONTATO_EMERGENCIA)
            .foneEmergencia(UPDATED_FONE_EMERGENCIA)
            .relacaoEmergencia(UPDATED_RELACAO_EMERGENCIA)
            .autorizacao(UPDATED_AUTORIZACAO)
            .fotoAluno(UPDATED_FOTO_ALUNO)
            .fotoMae(UPDATED_FOTO_MAE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedCadastroAluno.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(partialUpdatedCadastroAluno))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the CadastroAluno in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCadastroAlunoUpdatableFieldsEquals(partialUpdatedCadastroAluno, getPersistedCadastroAluno(partialUpdatedCadastroAluno));
    }

    @Test
    void patchNonExistingCadastroAluno() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(cadastroAlunoSearchRepository.findAll().collectList().block());
        cadastroAluno.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, cadastroAluno.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(cadastroAluno))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CadastroAluno in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(cadastroAlunoSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithIdMismatchCadastroAluno() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(cadastroAlunoSearchRepository.findAll().collectList().block());
        cadastroAluno.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, longCount.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(cadastroAluno))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the CadastroAluno in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(cadastroAlunoSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void patchWithMissingIdPathParamCadastroAluno() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(cadastroAlunoSearchRepository.findAll().collectList().block());
        cadastroAluno.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(om.writeValueAsBytes(cadastroAluno))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the CadastroAluno in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(cadastroAlunoSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    void deleteCadastroAluno() {
        // Initialize the database
        insertedCadastroAluno = cadastroAlunoRepository.save(cadastroAluno).block();
        cadastroAlunoRepository.save(cadastroAluno).block();
        cadastroAlunoSearchRepository.save(cadastroAluno).block();

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(cadastroAlunoSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the cadastroAluno
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, cadastroAluno.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(cadastroAlunoSearchRepository.findAll().collectList().block());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    void searchCadastroAluno() {
        // Initialize the database
        insertedCadastroAluno = cadastroAlunoRepository.save(cadastroAluno).block();
        cadastroAlunoSearchRepository.save(cadastroAluno).block();

        // Search the cadastroAluno
        webTestClient
            .get()
            .uri(ENTITY_SEARCH_API_URL + "?query=id:" + cadastroAluno.getId())
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(cadastroAluno.getId().intValue()))
            .jsonPath("$.[*].dataCadastro")
            .value(hasItem(DEFAULT_DATA_CADASTRO.toString()))
            .jsonPath("$.[*].matricula")
            .value(hasItem(DEFAULT_MATRICULA))
            .jsonPath("$.[*].grupo")
            .value(hasItem(DEFAULT_GRUPO))
            .jsonPath("$.[*].nome")
            .value(hasItem(DEFAULT_NOME))
            .jsonPath("$.[*].dn")
            .value(hasItem(DEFAULT_DN.toString()))
            .jsonPath("$.[*].cep")
            .value(hasItem(DEFAULT_CEP))
            .jsonPath("$.[*].endereco")
            .value(hasItem(DEFAULT_ENDERECO))
            .jsonPath("$.[*].qd")
            .value(hasItem(DEFAULT_QD))
            .jsonPath("$.[*].lote")
            .value(hasItem(DEFAULT_LOTE))
            .jsonPath("$.[*].endnumero")
            .value(hasItem(DEFAULT_ENDNUMERO))
            .jsonPath("$.[*].bairro")
            .value(hasItem(DEFAULT_BAIRRO))
            .jsonPath("$.[*].municipio")
            .value(hasItem(DEFAULT_MUNICIPIO))
            .jsonPath("$.[*].uf")
            .value(hasItem(DEFAULT_UF))
            .jsonPath("$.[*].fone")
            .value(hasItem(DEFAULT_FONE))
            .jsonPath("$.[*].certidao")
            .value(hasItem(DEFAULT_CERTIDAO))
            .jsonPath("$.[*].termo")
            .value(hasItem(DEFAULT_TERMO))
            .jsonPath("$.[*].cartorio")
            .value(hasItem(DEFAULT_CARTORIO))
            .jsonPath("$.[*].naturalidade")
            .value(hasItem(DEFAULT_NATURALIDADE))
            .jsonPath("$.[*].rg")
            .value(hasItem(DEFAULT_RG))
            .jsonPath("$.[*].cpf")
            .value(hasItem(DEFAULT_CPF))
            .jsonPath("$.[*].nis")
            .value(hasItem(DEFAULT_NIS))
            .jsonPath("$.[*].cras")
            .value(hasItem(DEFAULT_CRAS))
            .jsonPath("$.[*].filiacaoPai")
            .value(hasItem(DEFAULT_FILIACAO_PAI))
            .jsonPath("$.[*].paiTelefone")
            .value(hasItem(DEFAULT_PAI_TELEFONE))
            .jsonPath("$.[*].paiNaturalidade")
            .value(hasItem(DEFAULT_PAI_NATURALIDADE))
            .jsonPath("$.[*].paiUf")
            .value(hasItem(DEFAULT_PAI_UF))
            .jsonPath("$.[*].paiRg")
            .value(hasItem(DEFAULT_PAI_RG))
            .jsonPath("$.[*].paiDataNascimento")
            .value(hasItem(DEFAULT_PAI_DATA_NASCIMENTO.toString()))
            .jsonPath("$.[*].paiCpf")
            .value(hasItem(DEFAULT_PAI_CPF))
            .jsonPath("$.[*].paiNis")
            .value(hasItem(DEFAULT_PAI_NIS))
            .jsonPath("$.[*].paiTituloEleitor")
            .value(hasItem(DEFAULT_PAI_TITULO_ELEITOR))
            .jsonPath("$.[*].paiZona")
            .value(hasItem(DEFAULT_PAI_ZONA))
            .jsonPath("$.[*].paiSecao")
            .value(hasItem(DEFAULT_PAI_SECAO))
            .jsonPath("$.[*].paiMunicipio")
            .value(hasItem(DEFAULT_PAI_MUNICIPIO))
            .jsonPath("$.[*].filiacaoMae")
            .value(hasItem(DEFAULT_FILIACAO_MAE))
            .jsonPath("$.[*].maeTelefone")
            .value(hasItem(DEFAULT_MAE_TELEFONE))
            .jsonPath("$.[*].maeNaturalidade")
            .value(hasItem(DEFAULT_MAE_NATURALIDADE))
            .jsonPath("$.[*].maeUf")
            .value(hasItem(DEFAULT_MAE_UF))
            .jsonPath("$.[*].maeRg")
            .value(hasItem(DEFAULT_MAE_RG))
            .jsonPath("$.[*].maeDataNascimento")
            .value(hasItem(DEFAULT_MAE_DATA_NASCIMENTO.toString()))
            .jsonPath("$.[*].maeCpf")
            .value(hasItem(DEFAULT_MAE_CPF))
            .jsonPath("$.[*].maeNis")
            .value(hasItem(DEFAULT_MAE_NIS))
            .jsonPath("$.[*].maeTituloEleitor")
            .value(hasItem(DEFAULT_MAE_TITULO_ELEITOR))
            .jsonPath("$.[*].maeZona")
            .value(hasItem(DEFAULT_MAE_ZONA))
            .jsonPath("$.[*].maeSecao")
            .value(hasItem(DEFAULT_MAE_SECAO))
            .jsonPath("$.[*].maeMunicipio")
            .value(hasItem(DEFAULT_MAE_MUNICIPIO))
            .jsonPath("$.[*].nomeEscola")
            .value(hasItem(DEFAULT_NOME_ESCOLA))
            .jsonPath("$.[*].anoCursando")
            .value(hasItem(DEFAULT_ANO_CURSANDO))
            .jsonPath("$.[*].turno")
            .value(hasItem(DEFAULT_TURNO.toString()))
            .jsonPath("$.[*].mediaEscolar")
            .value(hasItem(DEFAULT_MEDIA_ESCOLAR))
            .jsonPath("$.[*].prioritario")
            .value(hasItem(DEFAULT_PRIORITARIO.toString()))
            .jsonPath("$.[*].obs")
            .value(hasItem(DEFAULT_OBS))
            .jsonPath("$.[*].comportamentoCasa")
            .value(hasItem(DEFAULT_COMPORTAMENTO_CASA.toString()))
            .jsonPath("$.[*].comportamentoEscola")
            .value(hasItem(DEFAULT_COMPORTAMENTO_ESCOLA.toString()))
            .jsonPath("$.[*].deficiencia")
            .value(hasItem(DEFAULT_DEFICIENCIA.toString()))
            .jsonPath("$.[*].adaptacoes")
            .value(hasItem(DEFAULT_ADAPTACOES.toString()))
            .jsonPath("$.[*].medicacao")
            .value(hasItem(DEFAULT_MEDICACAO.toString()))
            .jsonPath("$.[*].medicacaoDesc")
            .value(hasItem(DEFAULT_MEDICACAO_DESC))
            .jsonPath("$.[*].alergia")
            .value(hasItem(DEFAULT_ALERGIA.toString()))
            .jsonPath("$.[*].alergiaDesc")
            .value(hasItem(DEFAULT_ALERGIA_DESC))
            .jsonPath("$.[*].historicoMedico")
            .value(hasItem(DEFAULT_HISTORICO_MEDICO))
            .jsonPath("$.[*].rendaFamiliar")
            .value(hasItem(DEFAULT_RENDA_FAMILIAR))
            .jsonPath("$.[*].pessoasTrabalham")
            .value(hasItem(DEFAULT_PESSOAS_TRABALHAM))
            .jsonPath("$.[*].numPessoasLar")
            .value(hasItem(DEFAULT_NUM_PESSOAS_LAR))
            .jsonPath("$.[*].beneficioSocial")
            .value(hasItem(DEFAULT_BENEFICIO_SOCIAL.toString()))
            .jsonPath("$.[*].beneficios")
            .value(hasItem(DEFAULT_BENEFICIOS))
            .jsonPath("$.[*].tipoResidencia")
            .value(hasItem(DEFAULT_TIPO_RESIDENCIA.toString()))
            .jsonPath("$.[*].tipoResidenciaDesc")
            .value(hasItem(DEFAULT_TIPO_RESIDENCIA_DESC))
            .jsonPath("$.[*].situacaoResidencia")
            .value(hasItem(DEFAULT_SITUACAO_RESIDENCIA.toString()))
            .jsonPath("$.[*].situacaoResidenciaDesc")
            .value(hasItem(DEFAULT_SITUACAO_RESIDENCIA_DESC))
            .jsonPath("$.[*].contatoEmergencia")
            .value(hasItem(DEFAULT_CONTATO_EMERGENCIA))
            .jsonPath("$.[*].foneEmergencia")
            .value(hasItem(DEFAULT_FONE_EMERGENCIA))
            .jsonPath("$.[*].relacaoEmergencia")
            .value(hasItem(DEFAULT_RELACAO_EMERGENCIA))
            .jsonPath("$.[*].autorizacao")
            .value(hasItem(DEFAULT_AUTORIZACAO))
            .jsonPath("$.[*].fotoAluno")
            .value(hasItem(DEFAULT_FOTO_ALUNO))
            .jsonPath("$.[*].fotoMae")
            .value(hasItem(DEFAULT_FOTO_MAE));
    }

    protected long getRepositoryCount() {
        return cadastroAlunoRepository.count().block();
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

    protected CadastroAluno getPersistedCadastroAluno(CadastroAluno cadastroAluno) {
        return cadastroAlunoRepository.findById(cadastroAluno.getId()).block();
    }

    protected void assertPersistedCadastroAlunoToMatchAllProperties(CadastroAluno expectedCadastroAluno) {
        // Test fails because reactive api returns an empty object instead of null
        // assertCadastroAlunoAllPropertiesEquals(expectedCadastroAluno, getPersistedCadastroAluno(expectedCadastroAluno));
        assertCadastroAlunoUpdatableFieldsEquals(expectedCadastroAluno, getPersistedCadastroAluno(expectedCadastroAluno));
    }

    protected void assertPersistedCadastroAlunoToMatchUpdatableProperties(CadastroAluno expectedCadastroAluno) {
        // Test fails because reactive api returns an empty object instead of null
        // assertCadastroAlunoAllUpdatablePropertiesEquals(expectedCadastroAluno, getPersistedCadastroAluno(expectedCadastroAluno));
        assertCadastroAlunoUpdatableFieldsEquals(expectedCadastroAluno, getPersistedCadastroAluno(expectedCadastroAluno));
    }
}
