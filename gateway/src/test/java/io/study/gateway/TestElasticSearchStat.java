package io.study.gateway;

import com.jd.vd.common.util.MathHelper;
import io.study.gateway.base.BaseTestCase;
import io.study.gateway.proxy.ProxyProtocol;
import io.study.gateway.stat.ElasticSearchCollector;
import io.study.gateway.stat.RequestStat;
import io.study.gateway.stat.service.RequestStatService;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.client.RestClientFactoryBean;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;


public class TestElasticSearchStat extends BaseTestCase {
    ElasticSearchCollector collector = null;

    ElasticsearchRestTemplate newEsRestTemplate() throws Exception {
        RestClientFactoryBean bean = new RestClientFactoryBean();
        bean.afterPropertiesSet();
        RestHighLevelClient client = bean.getObject();
        ElasticsearchRestTemplate restTemplate = new ElasticsearchRestTemplate(client);
        return restTemplate;
    }
    @Override
    protected void setUp() throws Exception {
        RequestStatService statService = new RequestStatService();
        ElasticsearchRestTemplate esRestTemplate = newEsRestTemplate();
        statService.setEsRestTemplate(esRestTemplate);
        collector = new ElasticSearchCollector(statService);
        super.setUp();
    }

    public void testCollect(){
        for (int i = 0; i < 100000; i++) {
            RequestStat stat = new RequestStat();
            int startTimeDelta = MathHelper.random().nextInt(365*24*60*60*1000);
            stat.setStartTime(System.currentTimeMillis()-startTimeDelta);
            stat.setSuccess(true);
            int cost = MathHelper.random().nextInt(5000);
            stat.setCost(cost);
            stat.setEndTime(System.currentTimeMillis()+cost);
            if(i%3 == 0){
                stat.setSuccess(false);
            }
            if(i%2 == 0){
                stat.setProxyProtocol(ProxyProtocol.Http1_1);
            }else{
                stat.setProxyProtocol(ProxyProtocol.Rpc);
            }
            stat.setUri("/com.wjf.proxy/test");
            stat.setTarget("/test");
            collector.onStat(stat);
        }

    }
}
