package com.mycompany.myapp.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.mycompany.myapp.domain.Deslocamento;
import com.mycompany.myapp.repository.DeslocamentoRepository;
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
 * Spring Data Elasticsearch repository for the {@link Deslocamento} entity.
 */
public interface DeslocamentoSearchRepository extends ElasticsearchRepository<Deslocamento, Long>, DeslocamentoSearchRepositoryInternal {}

interface DeslocamentoSearchRepositoryInternal {
    Page<Deslocamento> search(String query, Pageable pageable);

    Page<Deslocamento> search(Query query);

    @Async
    void index(Deslocamento entity);

    @Async
    void deleteFromIndexById(Long id);
}

class DeslocamentoSearchRepositoryInternalImpl implements DeslocamentoSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final DeslocamentoRepository repository;

    DeslocamentoSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, DeslocamentoRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Deslocamento> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<Deslocamento> search(Query query) {
        SearchHits<Deslocamento> searchHits = elasticsearchTemplate.search(query, Deslocamento.class);
        List<Deslocamento> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Deslocamento entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), Deslocamento.class);
    }
}
