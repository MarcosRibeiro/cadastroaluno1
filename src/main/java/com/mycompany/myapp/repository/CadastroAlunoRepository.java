package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.CadastroAluno;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the CadastroAluno entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CadastroAlunoRepository extends ReactiveCrudRepository<CadastroAluno, Long>, CadastroAlunoRepositoryInternal {
    Flux<CadastroAluno> findAllBy(Pageable pageable);

    @Override
    <S extends CadastroAluno> Mono<S> save(S entity);

    @Override
    Flux<CadastroAluno> findAll();

    @Override
    Mono<CadastroAluno> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface CadastroAlunoRepositoryInternal {
    <S extends CadastroAluno> Mono<S> save(S entity);

    Flux<CadastroAluno> findAllBy(Pageable pageable);

    Flux<CadastroAluno> findAll();

    Mono<CadastroAluno> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<CadastroAluno> findAllBy(Pageable pageable, Criteria criteria);
}
