package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Deslocamento;
import com.mycompany.myapp.repository.rowmapper.CadastroAlunoRowMapper;
import com.mycompany.myapp.repository.rowmapper.DeslocamentoRowMapper;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.convert.R2dbcConverter;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.support.SimpleR2dbcRepository;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Comparison;
import org.springframework.data.relational.core.sql.Condition;
import org.springframework.data.relational.core.sql.Conditions;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Select;
import org.springframework.data.relational.core.sql.SelectBuilder.SelectFromAndJoinCondition;
import org.springframework.data.relational.core.sql.Table;
import org.springframework.data.relational.repository.support.MappingRelationalEntityInformation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC custom repository implementation for the Deslocamento entity.
 */
@SuppressWarnings("unused")
class DeslocamentoRepositoryInternalImpl extends SimpleR2dbcRepository<Deslocamento, Long> implements DeslocamentoRepositoryInternal {

    private final DatabaseClient db;
    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final EntityManager entityManager;

    private final CadastroAlunoRowMapper cadastroalunoMapper;
    private final DeslocamentoRowMapper deslocamentoMapper;

    private static final Table entityTable = Table.aliased("deslocamento", EntityManager.ENTITY_ALIAS);
    private static final Table cadastroAlunoTable = Table.aliased("cadastro_aluno", "cadastroAluno");

    public DeslocamentoRepositoryInternalImpl(
        R2dbcEntityTemplate template,
        EntityManager entityManager,
        CadastroAlunoRowMapper cadastroalunoMapper,
        DeslocamentoRowMapper deslocamentoMapper,
        R2dbcEntityOperations entityOperations,
        R2dbcConverter converter
    ) {
        super(
            new MappingRelationalEntityInformation(converter.getMappingContext().getRequiredPersistentEntity(Deslocamento.class)),
            entityOperations,
            converter
        );
        this.db = template.getDatabaseClient();
        this.r2dbcEntityTemplate = template;
        this.entityManager = entityManager;
        this.cadastroalunoMapper = cadastroalunoMapper;
        this.deslocamentoMapper = deslocamentoMapper;
    }

    @Override
    public Flux<Deslocamento> findAllBy(Pageable pageable) {
        return createQuery(pageable, null).all();
    }

    RowsFetchSpec<Deslocamento> createQuery(Pageable pageable, Condition whereClause) {
        List<Expression> columns = DeslocamentoSqlHelper.getColumns(entityTable, EntityManager.ENTITY_ALIAS);
        columns.addAll(CadastroAlunoSqlHelper.getColumns(cadastroAlunoTable, "cadastroAluno"));
        SelectFromAndJoinCondition selectFrom = Select.builder()
            .select(columns)
            .from(entityTable)
            .leftOuterJoin(cadastroAlunoTable)
            .on(Column.create("cadastro_aluno_id", entityTable))
            .equals(Column.create("id", cadastroAlunoTable));
        // we do not support Criteria here for now as of https://github.com/jhipster/generator-jhipster/issues/18269
        String select = entityManager.createSelect(selectFrom, Deslocamento.class, pageable, whereClause);
        return db.sql(select).map(this::process);
    }

    @Override
    public Flux<Deslocamento> findAll() {
        return findAllBy(null);
    }

    @Override
    public Mono<Deslocamento> findById(Long id) {
        Comparison whereClause = Conditions.isEqual(entityTable.column("id"), Conditions.just(id.toString()));
        return createQuery(null, whereClause).one();
    }

    private Deslocamento process(Row row, RowMetadata metadata) {
        Deslocamento entity = deslocamentoMapper.apply(row, "e");
        entity.setCadastroAluno(cadastroalunoMapper.apply(row, "cadastroAluno"));
        return entity;
    }

    @Override
    public <S extends Deslocamento> Mono<S> save(S entity) {
        return super.save(entity);
    }
}
