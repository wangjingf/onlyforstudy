package io.study.gateway.stat;

import io.study.gateway.stat.service.RequestStatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ElasticSearchCollector implements IStatCollector {
    RequestStatService statService = null;
    static final Logger logger = LoggerFactory.getLogger(ElasticSearchCollector.class);
    public ElasticSearchCollector(RequestStatService statService){
        this.statService = statService;
    }
    @Override
    public void onStat(RequestStat stat) {
        logger.info("elastic.collect_request_stat");
        statService.addRequestStat(stat);
    }
}
