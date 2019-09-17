package study.elasticsearch.spring.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import study.elasticsearch.spring.dao.ArticleRepository;
import study.elasticsearch.spring.entity.Article;
import study.elasticsearch.spring.service.ArticleService;
@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    ArticleRepository articleRepository;
    @Override
    public void save(Article article) {
        articleRepository.save(article);
    }
    public void delete(Article article){
        articleRepository.delete(article);
    }
    public Iterable<Article> findAll() {
        Iterable<Article> iter = articleRepository.findAll();
        return iter;
    }

    public Page<Article> findAll(Pageable pageable) {
        return articleRepository.findAll(pageable);
    }

}
