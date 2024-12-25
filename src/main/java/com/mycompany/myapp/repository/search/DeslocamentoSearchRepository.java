package com.mycompany.myapp.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.mycompany.myapp.domain.Deslocamento;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.ReactiveElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import reactor.core.publisher.Flux;

/**
 * Spring Data Elasticsearch repository for the {@link Deslocamento} entity.
 */
public interface DeslocamentoSearchRepository
    extends ReactiveElasticsearchRepository<Deslocamento, Long>, DeslocamentoSearchRepositoryInternal {}

interface DeslocamentoSearchRepositoryInternal {
    Flux<Deslocamento> search(String query, Pageable pageable);

    Flux<Deslocamento> search(Query query);
}

class DeslocamentoSearchRepositoryInternalImpl implements DeslocamentoSearchRepositoryInternal {

    private final ReactiveElasticsearchTemplate reactiveElasticsearchTemplate;

    DeslocamentoSearchRepositoryInternalImpl(ReactiveElasticsearchTemplate reactiveElasticsearchTemplate) {
        this.reactiveElasticsearchTemplate = reactiveElasticsearchTemplate;
    }

    @Override
    public Flux<Deslocamento> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        nativeQuery.setPageable(pageable);
        return search(nativeQuery);
    }

    @Override
    public Flux<Deslocamento> search(Query query) {
        return reactiveElasticsearchTemplate.search(query, Deslocamento.class).map(SearchHit::getContent);
    }
}
