package com.mycompany.myapp.repository.rowmapper;

import com.mycompany.myapp.domain.Responsavel;
import io.r2dbc.spi.Row;
import java.util.function.BiFunction;
import org.springframework.stereotype.Service;

/**
 * Converter between {@link Row} to {@link Responsavel}, with proper type conversions.
 */
@Service
public class ResponsavelRowMapper implements BiFunction<Row, String, Responsavel> {

    private final ColumnConverter converter;

    public ResponsavelRowMapper(ColumnConverter converter) {
        this.converter = converter;
    }

    /**
     * Take a {@link Row} and a column prefix, and extract all the fields.
     * @return the {@link Responsavel} stored in the database.
     */
    @Override
    public Responsavel apply(Row row, String prefix) {
        Responsavel entity = new Responsavel();
        entity.setId(converter.fromRow(row, prefix + "_id", Long.class));
        entity.setNome(converter.fromRow(row, prefix + "_nome", String.class));
        entity.setParentesco(converter.fromRow(row, prefix + "_parentesco", String.class));
        entity.setCadastroAlunoId(converter.fromRow(row, prefix + "_cadastro_aluno_id", Long.class));
        return entity;
    }
}
