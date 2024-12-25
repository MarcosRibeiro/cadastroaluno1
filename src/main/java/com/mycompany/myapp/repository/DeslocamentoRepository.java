package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Deslocamento;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Deslocamento entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DeslocamentoRepository extends ReactiveCrudRepository<Deslocamento, Long>, DeslocamentoRepositoryInternal {
    Flux<Deslocamento> findAllBy(Pageable pageable);

    @Query("SELECT * FROM deslocamento entity WHERE entity.cadastro_aluno_id = :id")
    Flux<Deslocamento> findByCadastroAluno(Long id);

    @Query("SELECT * FROM deslocamento entity WHERE entity.cadastro_aluno_id IS NULL")
    Flux<Deslocamento> findAllWhereCadastroAlunoIsNull();

    @Override
    <S extends Deslocamento> Mono<S> save(S entity);

    @Override
    Flux<Deslocamento> findAll();

    @Override
    Mono<Deslocamento> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface DeslocamentoRepositoryInternal {
    <S extends Deslocamento> Mono<S> save(S entity);

    Flux<Deslocamento> findAllBy(Pageable pageable);

    Flux<Deslocamento> findAll();

    Mono<Deslocamento> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Deslocamento> findAllBy(Pageable pageable, Criteria criteria);
}
