package io.study.gateway.stat.service;

import io.study.gateway.stat.RequestStat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Service;

@Service
public class RequestStatService {
    @Autowired
    ElasticsearchRestTemplate esRestTemplate;

    public ElasticsearchRestTemplate getEsRestTemplate() {
        return esRestTemplate;
    }

    public void setEsRestTemplate(ElasticsearchRestTemplate esRestTemplate) {
        this.esRestTemplate = esRestTemplate;
    }

    public void addRequestStat(RequestStat stat){
        RequestStat save = esRestTemplate.save(stat);
    }
}
