package com.mycompany.myapp.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.mycompany.myapp.domain.Responsavel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.ReactiveElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import reactor.core.publisher.Flux;

/**
 * Spring Data Elasticsearch repository for the {@link Responsavel} entity.
 */
public interface ResponsavelSearchRepository
    extends ReactiveElasticsearchRepository<Responsavel, Long>, ResponsavelSearchRepositoryInternal {}

interface ResponsavelSearchRepositoryInternal {
    Flux<Responsavel> search(String query, Pageable pageable);

    Flux<Responsavel> search(Query query);
}

class ResponsavelSearchRepositoryInternalImpl implements ResponsavelSearchRepositoryInternal {

    private final ReactiveElasticsearchTemplate reactiveElasticsearchTemplate;

    ResponsavelSearchRepositoryInternalImpl(ReactiveElasticsearchTemplate reactiveElasticsearchTemplate) {
        this.reactiveElasticsearchTemplate = reactiveElasticsearchTemplate;
    }

    @Override
    public Flux<Responsavel> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        nativeQuery.setPageable(pageable);
        return search(nativeQuery);
    }

    @Override
    public Flux<Responsavel> search(Query query) {
        return reactiveElasticsearchTemplate.search(query, Responsavel.class).map(SearchHit::getContent);
    }
}
