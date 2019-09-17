package study.elasticsearch.spring.dao;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import study.elasticsearch.spring.entity.Article;

@Repository
public interface ArticleRepository extends ElasticsearchRepository<Article,String> {
}
