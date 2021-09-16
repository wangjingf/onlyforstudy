package study.elasticsearch;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import study.elasticsearch.spring.entity.Article;

public class TestEs {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring-data-elasticsearch.xml");
        context.refresh();
        ElasticsearchRestTemplate restTemplate = context.getBean(ElasticsearchRestTemplate.class);
        restTemplate.indexOps(Article.class).create();
        restTemplate.indexOps(Article.class).putMapping();
    }
}
