package com.mycompany.myapp.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.mycompany.myapp.domain.Responsavel;
import com.mycompany.myapp.repository.ResponsavelRepository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link Responsavel} entity.
 */
public interface ResponsavelSearchRepository extends ElasticsearchRepository<Responsavel, Long>, ResponsavelSearchRepositoryInternal {}

interface ResponsavelSearchRepositoryInternal {
    Page<Responsavel> search(String query, Pageable pageable);

    Page<Responsavel> search(Query query);

    @Async
    void index(Responsavel entity);

    @Async
    void deleteFromIndexById(Long id);
}

class ResponsavelSearchRepositoryInternalImpl implements ResponsavelSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final ResponsavelRepository repository;

    ResponsavelSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, ResponsavelRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Responsavel> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<Responsavel> search(Query query) {
        SearchHits<Responsavel> searchHits = elasticsearchTemplate.search(query, Responsavel.class);
        List<Responsavel> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Responsavel entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), Responsavel.class);
    }
}
