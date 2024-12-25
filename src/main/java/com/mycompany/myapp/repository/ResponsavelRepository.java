package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Responsavel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the Responsavel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ResponsavelRepository extends ReactiveCrudRepository<Responsavel, Long>, ResponsavelRepositoryInternal {
    Flux<Responsavel> findAllBy(Pageable pageable);

    @Query("SELECT * FROM responsavel entity WHERE entity.cadastro_aluno_id = :id")
    Flux<Responsavel> findByCadastroAluno(Long id);

    @Query("SELECT * FROM responsavel entity WHERE entity.cadastro_aluno_id IS NULL")
    Flux<Responsavel> findAllWhereCadastroAlunoIsNull();

    @Override
    <S extends Responsavel> Mono<S> save(S entity);

    @Override
    Flux<Responsavel> findAll();

    @Override
    Mono<Responsavel> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface ResponsavelRepositoryInternal {
    <S extends Responsavel> Mono<S> save(S entity);

    Flux<Responsavel> findAllBy(Pageable pageable);

    Flux<Responsavel> findAll();

    Mono<Responsavel> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<Responsavel> findAllBy(Pageable pageable, Criteria criteria);
}
