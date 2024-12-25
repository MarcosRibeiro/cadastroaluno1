package com.mycompany.myapp.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.mycompany.myapp.domain.CadastroAluno;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.ReactiveElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import reactor.core.publisher.Flux;

/**
 * Spring Data Elasticsearch repository for the {@link CadastroAluno} entity.
 */
public interface CadastroAlunoSearchRepository
    extends ReactiveElasticsearchRepository<CadastroAluno, Long>, CadastroAlunoSearchRepositoryInternal {}

interface CadastroAlunoSearchRepositoryInternal {
    Flux<CadastroAluno> search(String query, Pageable pageable);

    Flux<CadastroAluno> search(Query query);
}

class CadastroAlunoSearchRepositoryInternalImpl implements CadastroAlunoSearchRepositoryInternal {

    private final ReactiveElasticsearchTemplate reactiveElasticsearchTemplate;

    CadastroAlunoSearchRepositoryInternalImpl(ReactiveElasticsearchTemplate reactiveElasticsearchTemplate) {
        this.reactiveElasticsearchTemplate = reactiveElasticsearchTemplate;
    }

    @Override
    public Flux<CadastroAluno> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        nativeQuery.setPageable(pageable);
        return search(nativeQuery);
    }

    @Override
    public Flux<CadastroAluno> search(Query query) {
        return reactiveElasticsearchTemplate.search(query, CadastroAluno.class).map(SearchHit::getContent);
    }
}
