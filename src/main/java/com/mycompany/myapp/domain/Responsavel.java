package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Responsavel.
 */
@Table("responsavel")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "responsavel")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Responsavel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Size(max = 255)
    @Column("nome")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String nome;

    @NotNull(message = "must not be null")
    @Size(max = 50)
    @Column("parentesco")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String parentesco;

    @org.springframework.data.annotation.Transient
    @JsonIgnoreProperties(value = { "responsaveis", "deslocamentos" }, allowSetters = true)
    private CadastroAluno cadastroAluno;

    @Column("cadastro_aluno_id")
    private Long cadastroAlunoId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Responsavel id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public Responsavel nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getParentesco() {
        return this.parentesco;
    }

    public Responsavel parentesco(String parentesco) {
        this.setParentesco(parentesco);
        return this;
    }

    public void setParentesco(String parentesco) {
        this.parentesco = parentesco;
    }

    public CadastroAluno getCadastroAluno() {
        return this.cadastroAluno;
    }

    public void setCadastroAluno(CadastroAluno cadastroAluno) {
        this.cadastroAluno = cadastroAluno;
        this.cadastroAlunoId = cadastroAluno != null ? cadastroAluno.getId() : null;
    }

    public Responsavel cadastroAluno(CadastroAluno cadastroAluno) {
        this.setCadastroAluno(cadastroAluno);
        return this;
    }

    public Long getCadastroAlunoId() {
        return this.cadastroAlunoId;
    }

    public void setCadastroAlunoId(Long cadastroAluno) {
        this.cadastroAlunoId = cadastroAluno;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Responsavel)) {
            return false;
        }
        return getId() != null && getId().equals(((Responsavel) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Responsavel{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", parentesco='" + getParentesco() + "'" +
            "}";
    }
}
