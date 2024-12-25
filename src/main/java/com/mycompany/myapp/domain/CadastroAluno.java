package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.Comportamento;
import com.mycompany.myapp.domain.enumeration.SimNao;
import com.mycompany.myapp.domain.enumeration.SituacaoResidencia;
import com.mycompany.myapp.domain.enumeration.TipoResidencia;
import com.mycompany.myapp.domain.enumeration.Turno;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A CadastroAluno.
 */
@Table("cadastro_aluno")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "cadastroaluno")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CadastroAluno implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("data_cadastro")
    private LocalDate dataCadastro;

    @Size(max = 20)
    @Column("matricula")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String matricula;

    @Size(max = 50)
    @Column("grupo")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String grupo;

    @NotNull(message = "must not be null")
    @Size(max = 255)
    @Column("nome")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String nome;

    @NotNull(message = "must not be null")
    @Column("dn")
    private LocalDate dn;

    @NotNull(message = "must not be null")
    @Size(max = 10)
    @Column("cep")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String cep;

    @NotNull(message = "must not be null")
    @Size(max = 5000)
    @Column("endereco")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String endereco;

    @Size(max = 10)
    @Column("qd")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String qd;

    @Size(max = 10)
    @Column("lote")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String lote;

    @Size(max = 10)
    @Column("endnumero")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String endnumero;

    @Size(max = 100)
    @Column("bairro")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String bairro;

    @Size(max = 100)
    @Column("municipio")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String municipio;

    @Size(max = 2)
    @Column("uf")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String uf;

    @NotNull(message = "must not be null")
    @Size(max = 15)
    @Column("fone")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String fone;

    @Size(max = 50)
    @Column("certidao")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String certidao;

    @Size(max = 50)
    @Column("termo")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String termo;

    @Size(max = 100)
    @Column("cartorio")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String cartorio;

    @Size(max = 100)
    @Column("naturalidade")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String naturalidade;

    @Size(max = 20)
    @Column("rg")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String rg;

    @NotNull(message = "must not be null")
    @Size(max = 14)
    @Column("cpf")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String cpf;

    @Size(max = 15)
    @Column("nis")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String nis;

    @Size(max = 100)
    @Column("cras")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String cras;

    @Size(max = 255)
    @Column("filiacao_pai")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String filiacaoPai;

    @Size(max = 15)
    @Column("pai_telefone")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String paiTelefone;

    @Size(max = 100)
    @Column("pai_naturalidade")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String paiNaturalidade;

    @Size(max = 2)
    @Column("pai_uf")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String paiUf;

    @Size(max = 20)
    @Column("pai_rg")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String paiRg;

    @Column("pai_data_nascimento")
    private LocalDate paiDataNascimento;

    @Size(max = 14)
    @Column("pai_cpf")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String paiCpf;

    @Size(max = 15)
    @Column("pai_nis")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String paiNis;

    @Size(max = 20)
    @Column("pai_titulo_eleitor")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String paiTituloEleitor;

    @Size(max = 10)
    @Column("pai_zona")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String paiZona;

    @Size(max = 10)
    @Column("pai_secao")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String paiSecao;

    @Size(max = 100)
    @Column("pai_municipio")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String paiMunicipio;

    @NotNull(message = "must not be null")
    @Size(max = 255)
    @Column("filiacao_mae")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String filiacaoMae;

    @Size(max = 15)
    @Column("mae_telefone")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String maeTelefone;

    @Size(max = 100)
    @Column("mae_naturalidade")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String maeNaturalidade;

    @Size(max = 2)
    @Column("mae_uf")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String maeUf;

    @Size(max = 20)
    @Column("mae_rg")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String maeRg;

    @Column("mae_data_nascimento")
    private LocalDate maeDataNascimento;

    @Size(max = 14)
    @Column("mae_cpf")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String maeCpf;

    @Size(max = 15)
    @Column("mae_nis")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String maeNis;

    @Size(max = 20)
    @Column("mae_titulo_eleitor")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String maeTituloEleitor;

    @Size(max = 10)
    @Column("mae_zona")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String maeZona;

    @Size(max = 10)
    @Column("mae_secao")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String maeSecao;

    @Size(max = 100)
    @Column("mae_municipio")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String maeMunicipio;

    @Size(max = 255)
    @Column("nome_escola")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String nomeEscola;

    @Size(max = 50)
    @Column("ano_cursando")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String anoCursando;

    @Column("turno")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private Turno turno;

    @Column("media_escolar")
    private Double mediaEscolar;

    @Column("prioritario")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private SimNao prioritario;

    @Size(max = 5000)
    @Column("obs")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String obs;

    @Column("comportamento_casa")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private Comportamento comportamentoCasa;

    @Column("comportamento_escola")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private Comportamento comportamentoEscola;

    @Column("deficiencia")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private SimNao deficiencia;

    @Column("adaptacoes")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private SimNao adaptacoes;

    @Column("medicacao")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private SimNao medicacao;

    @Size(max = 5000)
    @Column("medicacao_desc")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String medicacaoDesc;

    @Column("alergia")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private SimNao alergia;

    @Size(max = 5000)
    @Column("alergia_desc")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String alergiaDesc;

    @Size(max = 5000)
    @Column("historico_medico")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String historicoMedico;

    @Size(max = 20)
    @Column("renda_familiar")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String rendaFamiliar;

    @Column("pessoas_trabalham")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer pessoasTrabalham;

    @Column("num_pessoas_lar")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer numPessoasLar;

    @Column("beneficio_social")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private SimNao beneficioSocial;

    @Size(max = 5000)
    @Column("beneficios")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String beneficios;

    @Column("tipo_residencia")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private TipoResidencia tipoResidencia;

    @Size(max = 100)
    @Column("tipo_residencia_desc")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String tipoResidenciaDesc;

    @Column("situacao_residencia")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private SituacaoResidencia situacaoResidencia;

    @Size(max = 100)
    @Column("situacao_residencia_desc")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String situacaoResidenciaDesc;

    @NotNull(message = "must not be null")
    @Size(max = 255)
    @Column("contato_emergencia")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String contatoEmergencia;

    @NotNull(message = "must not be null")
    @Size(max = 15)
    @Column("fone_emergencia")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String foneEmergencia;

    @NotNull(message = "must not be null")
    @Size(max = 50)
    @Column("relacao_emergencia")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String relacaoEmergencia;

    @NotNull(message = "must not be null")
    @Column("autorizacao")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean autorizacao;

    @Column("foto_aluno")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String fotoAluno;

    @Column("foto_mae")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String fotoMae;

    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "cadastroAluno" }, allowSetters = true)
    private Set<Responsavel> responsaveis = new HashSet<>();

    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "cadastroAluno" }, allowSetters = true)
    private Set<Deslocamento> deslocamentos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CadastroAluno id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDataCadastro() {
        return this.dataCadastro;
    }

    public CadastroAluno dataCadastro(LocalDate dataCadastro) {
        this.setDataCadastro(dataCadastro);
        return this;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public String getMatricula() {
        return this.matricula;
    }

    public CadastroAluno matricula(String matricula) {
        this.setMatricula(matricula);
        return this;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getGrupo() {
        return this.grupo;
    }

    public CadastroAluno grupo(String grupo) {
        this.setGrupo(grupo);
        return this;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public String getNome() {
        return this.nome;
    }

    public CadastroAluno nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getDn() {
        return this.dn;
    }

    public CadastroAluno dn(LocalDate dn) {
        this.setDn(dn);
        return this;
    }

    public void setDn(LocalDate dn) {
        this.dn = dn;
    }

    public String getCep() {
        return this.cep;
    }

    public CadastroAluno cep(String cep) {
        this.setCep(cep);
        return this;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getEndereco() {
        return this.endereco;
    }

    public CadastroAluno endereco(String endereco) {
        this.setEndereco(endereco);
        return this;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getQd() {
        return this.qd;
    }

    public CadastroAluno qd(String qd) {
        this.setQd(qd);
        return this;
    }

    public void setQd(String qd) {
        this.qd = qd;
    }

    public String getLote() {
        return this.lote;
    }

    public CadastroAluno lote(String lote) {
        this.setLote(lote);
        return this;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public String getEndnumero() {
        return this.endnumero;
    }

    public CadastroAluno endnumero(String endnumero) {
        this.setEndnumero(endnumero);
        return this;
    }

    public void setEndnumero(String endnumero) {
        this.endnumero = endnumero;
    }

    public String getBairro() {
        return this.bairro;
    }

    public CadastroAluno bairro(String bairro) {
        this.setBairro(bairro);
        return this;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getMunicipio() {
        return this.municipio;
    }

    public CadastroAluno municipio(String municipio) {
        this.setMunicipio(municipio);
        return this;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getUf() {
        return this.uf;
    }

    public CadastroAluno uf(String uf) {
        this.setUf(uf);
        return this;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getFone() {
        return this.fone;
    }

    public CadastroAluno fone(String fone) {
        this.setFone(fone);
        return this;
    }

    public void setFone(String fone) {
        this.fone = fone;
    }

    public String getCertidao() {
        return this.certidao;
    }

    public CadastroAluno certidao(String certidao) {
        this.setCertidao(certidao);
        return this;
    }

    public void setCertidao(String certidao) {
        this.certidao = certidao;
    }

    public String getTermo() {
        return this.termo;
    }

    public CadastroAluno termo(String termo) {
        this.setTermo(termo);
        return this;
    }

    public void setTermo(String termo) {
        this.termo = termo;
    }

    public String getCartorio() {
        return this.cartorio;
    }

    public CadastroAluno cartorio(String cartorio) {
        this.setCartorio(cartorio);
        return this;
    }

    public void setCartorio(String cartorio) {
        this.cartorio = cartorio;
    }

    public String getNaturalidade() {
        return this.naturalidade;
    }

    public CadastroAluno naturalidade(String naturalidade) {
        this.setNaturalidade(naturalidade);
        return this;
    }

    public void setNaturalidade(String naturalidade) {
        this.naturalidade = naturalidade;
    }

    public String getRg() {
        return this.rg;
    }

    public CadastroAluno rg(String rg) {
        this.setRg(rg);
        return this;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getCpf() {
        return this.cpf;
    }

    public CadastroAluno cpf(String cpf) {
        this.setCpf(cpf);
        return this;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNis() {
        return this.nis;
    }

    public CadastroAluno nis(String nis) {
        this.setNis(nis);
        return this;
    }

    public void setNis(String nis) {
        this.nis = nis;
    }

    public String getCras() {
        return this.cras;
    }

    public CadastroAluno cras(String cras) {
        this.setCras(cras);
        return this;
    }

    public void setCras(String cras) {
        this.cras = cras;
    }

    public String getFiliacaoPai() {
        return this.filiacaoPai;
    }

    public CadastroAluno filiacaoPai(String filiacaoPai) {
        this.setFiliacaoPai(filiacaoPai);
        return this;
    }

    public void setFiliacaoPai(String filiacaoPai) {
        this.filiacaoPai = filiacaoPai;
    }

    public String getPaiTelefone() {
        return this.paiTelefone;
    }

    public CadastroAluno paiTelefone(String paiTelefone) {
        this.setPaiTelefone(paiTelefone);
        return this;
    }

    public void setPaiTelefone(String paiTelefone) {
        this.paiTelefone = paiTelefone;
    }

    public String getPaiNaturalidade() {
        return this.paiNaturalidade;
    }

    public CadastroAluno paiNaturalidade(String paiNaturalidade) {
        this.setPaiNaturalidade(paiNaturalidade);
        return this;
    }

    public void setPaiNaturalidade(String paiNaturalidade) {
        this.paiNaturalidade = paiNaturalidade;
    }

    public String getPaiUf() {
        return this.paiUf;
    }

    public CadastroAluno paiUf(String paiUf) {
        this.setPaiUf(paiUf);
        return this;
    }

    public void setPaiUf(String paiUf) {
        this.paiUf = paiUf;
    }

    public String getPaiRg() {
        return this.paiRg;
    }

    public CadastroAluno paiRg(String paiRg) {
        this.setPaiRg(paiRg);
        return this;
    }

    public void setPaiRg(String paiRg) {
        this.paiRg = paiRg;
    }

    public LocalDate getPaiDataNascimento() {
        return this.paiDataNascimento;
    }

    public CadastroAluno paiDataNascimento(LocalDate paiDataNascimento) {
        this.setPaiDataNascimento(paiDataNascimento);
        return this;
    }

    public void setPaiDataNascimento(LocalDate paiDataNascimento) {
        this.paiDataNascimento = paiDataNascimento;
    }

    public String getPaiCpf() {
        return this.paiCpf;
    }

    public CadastroAluno paiCpf(String paiCpf) {
        this.setPaiCpf(paiCpf);
        return this;
    }

    public void setPaiCpf(String paiCpf) {
        this.paiCpf = paiCpf;
    }

    public String getPaiNis() {
        return this.paiNis;
    }

    public CadastroAluno paiNis(String paiNis) {
        this.setPaiNis(paiNis);
        return this;
    }

    public void setPaiNis(String paiNis) {
        this.paiNis = paiNis;
    }

    public String getPaiTituloEleitor() {
        return this.paiTituloEleitor;
    }

    public CadastroAluno paiTituloEleitor(String paiTituloEleitor) {
        this.setPaiTituloEleitor(paiTituloEleitor);
        return this;
    }

    public void setPaiTituloEleitor(String paiTituloEleitor) {
        this.paiTituloEleitor = paiTituloEleitor;
    }

    public String getPaiZona() {
        return this.paiZona;
    }

    public CadastroAluno paiZona(String paiZona) {
        this.setPaiZona(paiZona);
        return this;
    }

    public void setPaiZona(String paiZona) {
        this.paiZona = paiZona;
    }

    public String getPaiSecao() {
        return this.paiSecao;
    }

    public CadastroAluno paiSecao(String paiSecao) {
        this.setPaiSecao(paiSecao);
        return this;
    }

    public void setPaiSecao(String paiSecao) {
        this.paiSecao = paiSecao;
    }

    public String getPaiMunicipio() {
        return this.paiMunicipio;
    }

    public CadastroAluno paiMunicipio(String paiMunicipio) {
        this.setPaiMunicipio(paiMunicipio);
        return this;
    }

    public void setPaiMunicipio(String paiMunicipio) {
        this.paiMunicipio = paiMunicipio;
    }

    public String getFiliacaoMae() {
        return this.filiacaoMae;
    }

    public CadastroAluno filiacaoMae(String filiacaoMae) {
        this.setFiliacaoMae(filiacaoMae);
        return this;
    }

    public void setFiliacaoMae(String filiacaoMae) {
        this.filiacaoMae = filiacaoMae;
    }

    public String getMaeTelefone() {
        return this.maeTelefone;
    }

    public CadastroAluno maeTelefone(String maeTelefone) {
        this.setMaeTelefone(maeTelefone);
        return this;
    }

    public void setMaeTelefone(String maeTelefone) {
        this.maeTelefone = maeTelefone;
    }

    public String getMaeNaturalidade() {
        return this.maeNaturalidade;
    }

    public CadastroAluno maeNaturalidade(String maeNaturalidade) {
        this.setMaeNaturalidade(maeNaturalidade);
        return this;
    }

    public void setMaeNaturalidade(String maeNaturalidade) {
        this.maeNaturalidade = maeNaturalidade;
    }

    public String getMaeUf() {
        return this.maeUf;
    }

    public CadastroAluno maeUf(String maeUf) {
        this.setMaeUf(maeUf);
        return this;
    }

    public void setMaeUf(String maeUf) {
        this.maeUf = maeUf;
    }

    public String getMaeRg() {
        return this.maeRg;
    }

    public CadastroAluno maeRg(String maeRg) {
        this.setMaeRg(maeRg);
        return this;
    }

    public void setMaeRg(String maeRg) {
        this.maeRg = maeRg;
    }

    public LocalDate getMaeDataNascimento() {
        return this.maeDataNascimento;
    }

    public CadastroAluno maeDataNascimento(LocalDate maeDataNascimento) {
        this.setMaeDataNascimento(maeDataNascimento);
        return this;
    }

    public void setMaeDataNascimento(LocalDate maeDataNascimento) {
        this.maeDataNascimento = maeDataNascimento;
    }

    public String getMaeCpf() {
        return this.maeCpf;
    }

    public CadastroAluno maeCpf(String maeCpf) {
        this.setMaeCpf(maeCpf);
        return this;
    }

    public void setMaeCpf(String maeCpf) {
        this.maeCpf = maeCpf;
    }

    public String getMaeNis() {
        return this.maeNis;
    }

    public CadastroAluno maeNis(String maeNis) {
        this.setMaeNis(maeNis);
        return this;
    }

    public void setMaeNis(String maeNis) {
        this.maeNis = maeNis;
    }

    public String getMaeTituloEleitor() {
        return this.maeTituloEleitor;
    }

    public CadastroAluno maeTituloEleitor(String maeTituloEleitor) {
        this.setMaeTituloEleitor(maeTituloEleitor);
        return this;
    }

    public void setMaeTituloEleitor(String maeTituloEleitor) {
        this.maeTituloEleitor = maeTituloEleitor;
    }

    public String getMaeZona() {
        return this.maeZona;
    }

    public CadastroAluno maeZona(String maeZona) {
        this.setMaeZona(maeZona);
        return this;
    }

    public void setMaeZona(String maeZona) {
        this.maeZona = maeZona;
    }

    public String getMaeSecao() {
        return this.maeSecao;
    }

    public CadastroAluno maeSecao(String maeSecao) {
        this.setMaeSecao(maeSecao);
        return this;
    }

    public void setMaeSecao(String maeSecao) {
        this.maeSecao = maeSecao;
    }

    public String getMaeMunicipio() {
        return this.maeMunicipio;
    }

    public CadastroAluno maeMunicipio(String maeMunicipio) {
        this.setMaeMunicipio(maeMunicipio);
        return this;
    }

    public void setMaeMunicipio(String maeMunicipio) {
        this.maeMunicipio = maeMunicipio;
    }

    public String getNomeEscola() {
        return this.nomeEscola;
    }

    public CadastroAluno nomeEscola(String nomeEscola) {
        this.setNomeEscola(nomeEscola);
        return this;
    }

    public void setNomeEscola(String nomeEscola) {
        this.nomeEscola = nomeEscola;
    }

    public String getAnoCursando() {
        return this.anoCursando;
    }

    public CadastroAluno anoCursando(String anoCursando) {
        this.setAnoCursando(anoCursando);
        return this;
    }

    public void setAnoCursando(String anoCursando) {
        this.anoCursando = anoCursando;
    }

    public Turno getTurno() {
        return this.turno;
    }

    public CadastroAluno turno(Turno turno) {
        this.setTurno(turno);
        return this;
    }

    public void setTurno(Turno turno) {
        this.turno = turno;
    }

    public Double getMediaEscolar() {
        return this.mediaEscolar;
    }

    public CadastroAluno mediaEscolar(Double mediaEscolar) {
        this.setMediaEscolar(mediaEscolar);
        return this;
    }

    public void setMediaEscolar(Double mediaEscolar) {
        this.mediaEscolar = mediaEscolar;
    }

    public SimNao getPrioritario() {
        return this.prioritario;
    }

    public CadastroAluno prioritario(SimNao prioritario) {
        this.setPrioritario(prioritario);
        return this;
    }

    public void setPrioritario(SimNao prioritario) {
        this.prioritario = prioritario;
    }

    public String getObs() {
        return this.obs;
    }

    public CadastroAluno obs(String obs) {
        this.setObs(obs);
        return this;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public Comportamento getComportamentoCasa() {
        return this.comportamentoCasa;
    }

    public CadastroAluno comportamentoCasa(Comportamento comportamentoCasa) {
        this.setComportamentoCasa(comportamentoCasa);
        return this;
    }

    public void setComportamentoCasa(Comportamento comportamentoCasa) {
        this.comportamentoCasa = comportamentoCasa;
    }

    public Comportamento getComportamentoEscola() {
        return this.comportamentoEscola;
    }

    public CadastroAluno comportamentoEscola(Comportamento comportamentoEscola) {
        this.setComportamentoEscola(comportamentoEscola);
        return this;
    }

    public void setComportamentoEscola(Comportamento comportamentoEscola) {
        this.comportamentoEscola = comportamentoEscola;
    }

    public SimNao getDeficiencia() {
        return this.deficiencia;
    }

    public CadastroAluno deficiencia(SimNao deficiencia) {
        this.setDeficiencia(deficiencia);
        return this;
    }

    public void setDeficiencia(SimNao deficiencia) {
        this.deficiencia = deficiencia;
    }

    public SimNao getAdaptacoes() {
        return this.adaptacoes;
    }

    public CadastroAluno adaptacoes(SimNao adaptacoes) {
        this.setAdaptacoes(adaptacoes);
        return this;
    }

    public void setAdaptacoes(SimNao adaptacoes) {
        this.adaptacoes = adaptacoes;
    }

    public SimNao getMedicacao() {
        return this.medicacao;
    }

    public CadastroAluno medicacao(SimNao medicacao) {
        this.setMedicacao(medicacao);
        return this;
    }

    public void setMedicacao(SimNao medicacao) {
        this.medicacao = medicacao;
    }

    public String getMedicacaoDesc() {
        return this.medicacaoDesc;
    }

    public CadastroAluno medicacaoDesc(String medicacaoDesc) {
        this.setMedicacaoDesc(medicacaoDesc);
        return this;
    }

    public void setMedicacaoDesc(String medicacaoDesc) {
        this.medicacaoDesc = medicacaoDesc;
    }

    public SimNao getAlergia() {
        return this.alergia;
    }

    public CadastroAluno alergia(SimNao alergia) {
        this.setAlergia(alergia);
        return this;
    }

    public void setAlergia(SimNao alergia) {
        this.alergia = alergia;
    }

    public String getAlergiaDesc() {
        return this.alergiaDesc;
    }

    public CadastroAluno alergiaDesc(String alergiaDesc) {
        this.setAlergiaDesc(alergiaDesc);
        return this;
    }

    public void setAlergiaDesc(String alergiaDesc) {
        this.alergiaDesc = alergiaDesc;
    }

    public String getHistoricoMedico() {
        return this.historicoMedico;
    }

    public CadastroAluno historicoMedico(String historicoMedico) {
        this.setHistoricoMedico(historicoMedico);
        return this;
    }

    public void setHistoricoMedico(String historicoMedico) {
        this.historicoMedico = historicoMedico;
    }

    public String getRendaFamiliar() {
        return this.rendaFamiliar;
    }

    public CadastroAluno rendaFamiliar(String rendaFamiliar) {
        this.setRendaFamiliar(rendaFamiliar);
        return this;
    }

    public void setRendaFamiliar(String rendaFamiliar) {
        this.rendaFamiliar = rendaFamiliar;
    }

    public Integer getPessoasTrabalham() {
        return this.pessoasTrabalham;
    }

    public CadastroAluno pessoasTrabalham(Integer pessoasTrabalham) {
        this.setPessoasTrabalham(pessoasTrabalham);
        return this;
    }

    public void setPessoasTrabalham(Integer pessoasTrabalham) {
        this.pessoasTrabalham = pessoasTrabalham;
    }

    public Integer getNumPessoasLar() {
        return this.numPessoasLar;
    }

    public CadastroAluno numPessoasLar(Integer numPessoasLar) {
        this.setNumPessoasLar(numPessoasLar);
        return this;
    }

    public void setNumPessoasLar(Integer numPessoasLar) {
        this.numPessoasLar = numPessoasLar;
    }

    public SimNao getBeneficioSocial() {
        return this.beneficioSocial;
    }

    public CadastroAluno beneficioSocial(SimNao beneficioSocial) {
        this.setBeneficioSocial(beneficioSocial);
        return this;
    }

    public void setBeneficioSocial(SimNao beneficioSocial) {
        this.beneficioSocial = beneficioSocial;
    }

    public String getBeneficios() {
        return this.beneficios;
    }

    public CadastroAluno beneficios(String beneficios) {
        this.setBeneficios(beneficios);
        return this;
    }

    public void setBeneficios(String beneficios) {
        this.beneficios = beneficios;
    }

    public TipoResidencia getTipoResidencia() {
        return this.tipoResidencia;
    }

    public CadastroAluno tipoResidencia(TipoResidencia tipoResidencia) {
        this.setTipoResidencia(tipoResidencia);
        return this;
    }

    public void setTipoResidencia(TipoResidencia tipoResidencia) {
        this.tipoResidencia = tipoResidencia;
    }

    public String getTipoResidenciaDesc() {
        return this.tipoResidenciaDesc;
    }

    public CadastroAluno tipoResidenciaDesc(String tipoResidenciaDesc) {
        this.setTipoResidenciaDesc(tipoResidenciaDesc);
        return this;
    }

    public void setTipoResidenciaDesc(String tipoResidenciaDesc) {
        this.tipoResidenciaDesc = tipoResidenciaDesc;
    }

    public SituacaoResidencia getSituacaoResidencia() {
        return this.situacaoResidencia;
    }

    public CadastroAluno situacaoResidencia(SituacaoResidencia situacaoResidencia) {
        this.setSituacaoResidencia(situacaoResidencia);
        return this;
    }

    public void setSituacaoResidencia(SituacaoResidencia situacaoResidencia) {
        this.situacaoResidencia = situacaoResidencia;
    }

    public String getSituacaoResidenciaDesc() {
        return this.situacaoResidenciaDesc;
    }

    public CadastroAluno situacaoResidenciaDesc(String situacaoResidenciaDesc) {
        this.setSituacaoResidenciaDesc(situacaoResidenciaDesc);
        return this;
    }

    public void setSituacaoResidenciaDesc(String situacaoResidenciaDesc) {
        this.situacaoResidenciaDesc = situacaoResidenciaDesc;
    }

    public String getContatoEmergencia() {
        return this.contatoEmergencia;
    }

    public CadastroAluno contatoEmergencia(String contatoEmergencia) {
        this.setContatoEmergencia(contatoEmergencia);
        return this;
    }

    public void setContatoEmergencia(String contatoEmergencia) {
        this.contatoEmergencia = contatoEmergencia;
    }

    public String getFoneEmergencia() {
        return this.foneEmergencia;
    }

    public CadastroAluno foneEmergencia(String foneEmergencia) {
        this.setFoneEmergencia(foneEmergencia);
        return this;
    }

    public void setFoneEmergencia(String foneEmergencia) {
        this.foneEmergencia = foneEmergencia;
    }

    public String getRelacaoEmergencia() {
        return this.relacaoEmergencia;
    }

    public CadastroAluno relacaoEmergencia(String relacaoEmergencia) {
        this.setRelacaoEmergencia(relacaoEmergencia);
        return this;
    }

    public void setRelacaoEmergencia(String relacaoEmergencia) {
        this.relacaoEmergencia = relacaoEmergencia;
    }

    public Boolean getAutorizacao() {
        return this.autorizacao;
    }

    public CadastroAluno autorizacao(Boolean autorizacao) {
        this.setAutorizacao(autorizacao);
        return this;
    }

    public void setAutorizacao(Boolean autorizacao) {
        this.autorizacao = autorizacao;
    }

    public String getFotoAluno() {
        return this.fotoAluno;
    }

    public CadastroAluno fotoAluno(String fotoAluno) {
        this.setFotoAluno(fotoAluno);
        return this;
    }

    public void setFotoAluno(String fotoAluno) {
        this.fotoAluno = fotoAluno;
    }

    public String getFotoMae() {
        return this.fotoMae;
    }

    public CadastroAluno fotoMae(String fotoMae) {
        this.setFotoMae(fotoMae);
        return this;
    }

    public void setFotoMae(String fotoMae) {
        this.fotoMae = fotoMae;
    }

    public Set<Responsavel> getResponsaveis() {
        return this.responsaveis;
    }

    public void setResponsaveis(Set<Responsavel> responsavels) {
        if (this.responsaveis != null) {
            this.responsaveis.forEach(i -> i.setCadastroAluno(null));
        }
        if (responsavels != null) {
            responsavels.forEach(i -> i.setCadastroAluno(this));
        }
        this.responsaveis = responsavels;
    }

    public CadastroAluno responsaveis(Set<Responsavel> responsavels) {
        this.setResponsaveis(responsavels);
        return this;
    }

    public CadastroAluno addResponsaveis(Responsavel responsavel) {
        this.responsaveis.add(responsavel);
        responsavel.setCadastroAluno(this);
        return this;
    }

    public CadastroAluno removeResponsaveis(Responsavel responsavel) {
        this.responsaveis.remove(responsavel);
        responsavel.setCadastroAluno(null);
        return this;
    }

    public Set<Deslocamento> getDeslocamentos() {
        return this.deslocamentos;
    }

    public void setDeslocamentos(Set<Deslocamento> deslocamentos) {
        if (this.deslocamentos != null) {
            this.deslocamentos.forEach(i -> i.setCadastroAluno(null));
        }
        if (deslocamentos != null) {
            deslocamentos.forEach(i -> i.setCadastroAluno(this));
        }
        this.deslocamentos = deslocamentos;
    }

    public CadastroAluno deslocamentos(Set<Deslocamento> deslocamentos) {
        this.setDeslocamentos(deslocamentos);
        return this;
    }

    public CadastroAluno addDeslocamentos(Deslocamento deslocamento) {
        this.deslocamentos.add(deslocamento);
        deslocamento.setCadastroAluno(this);
        return this;
    }

    public CadastroAluno removeDeslocamentos(Deslocamento deslocamento) {
        this.deslocamentos.remove(deslocamento);
        deslocamento.setCadastroAluno(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CadastroAluno)) {
            return false;
        }
        return getId() != null && getId().equals(((CadastroAluno) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CadastroAluno{" +
            "id=" + getId() +
            ", dataCadastro='" + getDataCadastro() + "'" +
            ", matricula='" + getMatricula() + "'" +
            ", grupo='" + getGrupo() + "'" +
            ", nome='" + getNome() + "'" +
            ", dn='" + getDn() + "'" +
            ", cep='" + getCep() + "'" +
            ", endereco='" + getEndereco() + "'" +
            ", qd='" + getQd() + "'" +
            ", lote='" + getLote() + "'" +
            ", endnumero='" + getEndnumero() + "'" +
            ", bairro='" + getBairro() + "'" +
            ", municipio='" + getMunicipio() + "'" +
            ", uf='" + getUf() + "'" +
            ", fone='" + getFone() + "'" +
            ", certidao='" + getCertidao() + "'" +
            ", termo='" + getTermo() + "'" +
            ", cartorio='" + getCartorio() + "'" +
            ", naturalidade='" + getNaturalidade() + "'" +
            ", rg='" + getRg() + "'" +
            ", cpf='" + getCpf() + "'" +
            ", nis='" + getNis() + "'" +
            ", cras='" + getCras() + "'" +
            ", filiacaoPai='" + getFiliacaoPai() + "'" +
            ", paiTelefone='" + getPaiTelefone() + "'" +
            ", paiNaturalidade='" + getPaiNaturalidade() + "'" +
            ", paiUf='" + getPaiUf() + "'" +
            ", paiRg='" + getPaiRg() + "'" +
            ", paiDataNascimento='" + getPaiDataNascimento() + "'" +
            ", paiCpf='" + getPaiCpf() + "'" +
            ", paiNis='" + getPaiNis() + "'" +
            ", paiTituloEleitor='" + getPaiTituloEleitor() + "'" +
            ", paiZona='" + getPaiZona() + "'" +
            ", paiSecao='" + getPaiSecao() + "'" +
            ", paiMunicipio='" + getPaiMunicipio() + "'" +
            ", filiacaoMae='" + getFiliacaoMae() + "'" +
            ", maeTelefone='" + getMaeTelefone() + "'" +
            ", maeNaturalidade='" + getMaeNaturalidade() + "'" +
            ", maeUf='" + getMaeUf() + "'" +
            ", maeRg='" + getMaeRg() + "'" +
            ", maeDataNascimento='" + getMaeDataNascimento() + "'" +
            ", maeCpf='" + getMaeCpf() + "'" +
            ", maeNis='" + getMaeNis() + "'" +
            ", maeTituloEleitor='" + getMaeTituloEleitor() + "'" +
            ", maeZona='" + getMaeZona() + "'" +
            ", maeSecao='" + getMaeSecao() + "'" +
            ", maeMunicipio='" + getMaeMunicipio() + "'" +
            ", nomeEscola='" + getNomeEscola() + "'" +
            ", anoCursando='" + getAnoCursando() + "'" +
            ", turno='" + getTurno() + "'" +
            ", mediaEscolar=" + getMediaEscolar() +
            ", prioritario='" + getPrioritario() + "'" +
            ", obs='" + getObs() + "'" +
            ", comportamentoCasa='" + getComportamentoCasa() + "'" +
            ", comportamentoEscola='" + getComportamentoEscola() + "'" +
            ", deficiencia='" + getDeficiencia() + "'" +
            ", adaptacoes='" + getAdaptacoes() + "'" +
            ", medicacao='" + getMedicacao() + "'" +
            ", medicacaoDesc='" + getMedicacaoDesc() + "'" +
            ", alergia='" + getAlergia() + "'" +
            ", alergiaDesc='" + getAlergiaDesc() + "'" +
            ", historicoMedico='" + getHistoricoMedico() + "'" +
            ", rendaFamiliar='" + getRendaFamiliar() + "'" +
            ", pessoasTrabalham=" + getPessoasTrabalham() +
            ", numPessoasLar=" + getNumPessoasLar() +
            ", beneficioSocial='" + getBeneficioSocial() + "'" +
            ", beneficios='" + getBeneficios() + "'" +
            ", tipoResidencia='" + getTipoResidencia() + "'" +
            ", tipoResidenciaDesc='" + getTipoResidenciaDesc() + "'" +
            ", situacaoResidencia='" + getSituacaoResidencia() + "'" +
            ", situacaoResidenciaDesc='" + getSituacaoResidenciaDesc() + "'" +
            ", contatoEmergencia='" + getContatoEmergencia() + "'" +
            ", foneEmergencia='" + getFoneEmergencia() + "'" +
            ", relacaoEmergencia='" + getRelacaoEmergencia() + "'" +
            ", autorizacao='" + getAutorizacao() + "'" +
            ", fotoAluno='" + getFotoAluno() + "'" +
            ", fotoMae='" + getFotoMae() + "'" +
            "}";
    }
}
