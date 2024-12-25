package com.mycompany.myapp.repository.rowmapper;

import com.mycompany.myapp.domain.Deslocamento;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Deslocamento}, with proper type conversions.
 */
@Service
public class DeslocamentoRowMapper implements BiFunction<Row, String, Deslocamento> {

    private final ColumnConverter converter;

    public DeslocamentoRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Deslocamento} stored in the database.
     */
    @Override
    public Deslocamento apply(Row row, String prefix) {
        Deslocamento entity = new Deslocamento();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNome(converter.fromRow(row, prefix + "_nome", String.class));
        entity.setGrau(converter.fromRow(row, prefix + "_grau", String.class));
        entity.setCadastroAlunoId(converter.fromRow(row, prefix + "_cadastro_aluno_id", Long.class));
        return entity;
    }
}
