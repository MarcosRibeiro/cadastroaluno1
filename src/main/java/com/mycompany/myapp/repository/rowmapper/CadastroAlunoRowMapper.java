package com.mycompany.myapp.repository.rowmapper;

import com.mycompany.myapp.domain.CadastroAluno;
import com.mycompany.myapp.domain.enumeration.Comportamento;
import com.mycompany.myapp.domain.enumeration.SimNao;
import com.mycompany.myapp.domain.enumeration.SituacaoResidencia;
import com.mycompany.myapp.domain.enumeration.TipoResidencia;
import com.mycompany.myapp.domain.enumeration.Turno;
import io.r2dbc.spi.Row;
import java.time.LocalDate;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link CadastroAluno}, with proper type conversions.
 */
@Service
public class CadastroAlunoRowMapper implements BiFunction<Row, String, CadastroAluno> {

    private final ColumnConverter converter;

    public CadastroAlunoRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link CadastroAluno} stored in the database.
     */
    @Override
    public CadastroAluno apply(Row row, String prefix) {
        CadastroAluno entity = new CadastroAluno();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setDataCadastro(converter.fromRow(row, prefix + "_data_cadastro", LocalDate.class));
        entity.setMatricula(converter.fromRow(row, prefix + "_matricula", String.class));
        entity.setGrupo(converter.fromRow(row, prefix + "_grupo", String.class));
        entity.setNome(converter.fromRow(row, prefix + "_nome", String.class));
        entity.setDn(converter.fromRow(row, prefix + "_dn", LocalDate.class));
        entity.setCep(converter.fromRow(row, prefix + "_cep", String.class));
        entity.setEndereco(converter.fromRow(row, prefix + "_endereco", String.class));
        entity.setQd(converter.fromRow(row, prefix + "_qd", String.class));
        entity.setLote(converter.fromRow(row, prefix + "_lote", String.class));
        entity.setEndnumero(converter.fromRow(row, prefix + "_endnumero", String.class));
        entity.setBairro(converter.fromRow(row, prefix + "_bairro", String.class));
        entity.setMunicipio(converter.fromRow(row, prefix + "_municipio", String.class));
        entity.setUf(converter.fromRow(row, prefix + "_uf", String.class));
        entity.setFone(converter.fromRow(row, prefix + "_fone", String.class));
        entity.setCertidao(converter.fromRow(row, prefix + "_certidao", String.class));
        entity.setTermo(converter.fromRow(row, prefix + "_termo", String.class));
        entity.setCartorio(converter.fromRow(row, prefix + "_cartorio", String.class));
        entity.setNaturalidade(converter.fromRow(row, prefix + "_naturalidade", String.class));
        entity.setRg(converter.fromRow(row, prefix + "_rg", String.class));
        entity.setCpf(converter.fromRow(row, prefix + "_cpf", String.class));
        entity.setNis(converter.fromRow(row, prefix + "_nis", String.class));
        entity.setCras(converter.fromRow(row, prefix + "_cras", String.class));
        entity.setFiliacaoPai(converter.fromRow(row, prefix + "_filiacao_pai", String.class));
        entity.setPaiTelefone(converter.fromRow(row, prefix + "_pai_telefone", String.class));
        entity.setPaiNaturalidade(converter.fromRow(row, prefix + "_pai_naturalidade", String.class));
        entity.setPaiUf(converter.fromRow(row, prefix + "_pai_uf", String.class));
        entity.setPaiRg(converter.fromRow(row, prefix + "_pai_rg", String.class));
        entity.setPaiDataNascimento(converter.fromRow(row, prefix + "_pai_data_nascimento", LocalDate.class));
        entity.setPaiCpf(converter.fromRow(row, prefix + "_pai_cpf", String.class));
        entity.setPaiNis(converter.fromRow(row, prefix + "_pai_nis", String.class));
        entity.setPaiTituloEleitor(converter.fromRow(row, prefix + "_pai_titulo_eleitor", String.class));
        entity.setPaiZona(converter.fromRow(row, prefix + "_pai_zona", String.class));
        entity.setPaiSecao(converter.fromRow(row, prefix + "_pai_secao", String.class));
        entity.setPaiMunicipio(converter.fromRow(row, prefix + "_pai_municipio", String.class));
        entity.setFiliacaoMae(converter.fromRow(row, prefix + "_filiacao_mae", String.class));
        entity.setMaeTelefone(converter.fromRow(row, prefix + "_mae_telefone", String.class));
        entity.setMaeNaturalidade(converter.fromRow(row, prefix + "_mae_naturalidade", String.class));
        entity.setMaeUf(converter.fromRow(row, prefix + "_mae_uf", String.class));
        entity.setMaeRg(converter.fromRow(row, prefix + "_mae_rg", String.class));
        entity.setMaeDataNascimento(converter.fromRow(row, prefix + "_mae_data_nascimento", LocalDate.class));
        entity.setMaeCpf(converter.fromRow(row, prefix + "_mae_cpf", String.class));
        entity.setMaeNis(converter.fromRow(row, prefix + "_mae_nis", String.class));
        entity.setMaeTituloEleitor(converter.fromRow(row, prefix + "_mae_titulo_eleitor", String.class));
        entity.setMaeZona(converter.fromRow(row, prefix + "_mae_zona", String.class));
        entity.setMaeSecao(converter.fromRow(row, prefix + "_mae_secao", String.class));
        entity.setMaeMunicipio(converter.fromRow(row, prefix + "_mae_municipio", String.class));
        entity.setNomeEscola(converter.fromRow(row, prefix + "_nome_escola", String.class));
        entity.setAnoCursando(converter.fromRow(row, prefix + "_ano_cursando", String.class));
        entity.setTurno(converter.fromRow(row, prefix + "_turno", Turno.class));
        entity.setMediaEscolar(converter.fromRow(row, prefix + "_media_escolar", Double.class));
        entity.setPrioritario(converter.fromRow(row, prefix + "_prioritario", SimNao.class));
        entity.setObs(converter.fromRow(row, prefix + "_obs", String.class));
        entity.setComportamentoCasa(converter.fromRow(row, prefix + "_comportamento_casa", Comportamento.class));
        entity.setComportamentoEscola(converter.fromRow(row, prefix + "_comportamento_escola", Comportamento.class));
        entity.setDeficiencia(converter.fromRow(row, prefix + "_deficiencia", SimNao.class));
        entity.setAdaptacoes(converter.fromRow(row, prefix + "_adaptacoes", SimNao.class));
        entity.setMedicacao(converter.fromRow(row, prefix + "_medicacao", SimNao.class));
        entity.setMedicacaoDesc(converter.fromRow(row, prefix + "_medicacao_desc", String.class));
        entity.setAlergia(converter.fromRow(row, prefix + "_alergia", SimNao.class));
        entity.setAlergiaDesc(converter.fromRow(row, prefix + "_alergia_desc", String.class));
        entity.setHistoricoMedico(converter.fromRow(row, prefix + "_historico_medico", String.class));
        entity.setRendaFamiliar(converter.fromRow(row, prefix + "_renda_familiar", String.class));
        entity.setPessoasTrabalham(converter.fromRow(row, prefix + "_pessoas_trabalham", Integer.class));
        entity.setNumPessoasLar(converter.fromRow(row, prefix + "_num_pessoas_lar", Integer.class));
        entity.setBeneficioSocial(converter.fromRow(row, prefix + "_beneficio_social", SimNao.class));
        entity.setBeneficios(converter.fromRow(row, prefix + "_beneficios", String.class));
        entity.setTipoResidencia(converter.fromRow(row, prefix + "_tipo_residencia", TipoResidencia.class));
        entity.setTipoResidenciaDesc(converter.fromRow(row, prefix + "_tipo_residencia_desc", String.class));
        entity.setSituacaoResidencia(converter.fromRow(row, prefix + "_situacao_residencia", SituacaoResidencia.class));
        entity.setSituacaoResidenciaDesc(converter.fromRow(row, prefix + "_situacao_residencia_desc", String.class));
        entity.setContatoEmergencia(converter.fromRow(row, prefix + "_contato_emergencia", String.class));
        entity.setFoneEmergencia(converter.fromRow(row, prefix + "_fone_emergencia", String.class));
        entity.setRelacaoEmergencia(converter.fromRow(row, prefix + "_relacao_emergencia", String.class));
        entity.setAutorizacao(converter.fromRow(row, prefix + "_autorizacao", Boolean.class));
        entity.setFotoAluno(converter.fromRow(row, prefix + "_foto_aluno", String.class));
        entity.setFotoMae(converter.fromRow(row, prefix + "_foto_mae", String.class));
        return entity;
    }
}
