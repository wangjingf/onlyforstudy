package study.elasticsearch.spring.service;

import study.elasticsearch.spring.entity.Article;

import java.util.List;

public interface ArticleService {
    public void save(Article article);
    public List<Article> searchArticle(Integer pageNumber, Integer pageSize, String searchContent);
}
