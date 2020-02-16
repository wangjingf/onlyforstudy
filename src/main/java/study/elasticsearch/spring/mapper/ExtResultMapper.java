package study.elasticsearch.spring.mapper;


import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.AbstractResultMapper;
import org.springframework.data.elasticsearch.core.EntityMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.stereotype.Component;

import java.util.LinkedList;


public class ExtResultMapper extends AbstractResultMapper {


    public ExtResultMapper(EntityMapper entityMapper) {
        super(entityMapper);
    }

    @Override
    public <T> T mapResult(GetResponse response, Class<T> clazz) {
        return null;
    }

    @Override
    public <T> LinkedList<T> mapResults(MultiGetResponse responses, Class<T> clazz) {
        return null;
    }

    @Override
    public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {
        return null;
    }
}