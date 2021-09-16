package study.elasticsearch;

import org.elasticsearch.client.transport.TransportClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import study.elasticsearch.spring.entity.Article;
import study.elasticsearch.spring.service.ArticleService;

import java.sql.Timestamp;
import java.util.stream.Stream;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:spring-data-elasticsearch.xml")
public class HelloWorld  {
    @Autowired
   ArticleService articleService;
    @Autowired
   TransportClient client;

    @Autowired
    ElasticsearchRestTemplate restTemplate;
   @Test
    public void createIndex(){
       restTemplate.indexOps(Article.class).create();
       restTemplate.indexOps(Article.class).putMapping();

   }
   @Test
   public void saveArticle(){
       Article article = new Article();
       article.setId("1005");
       article.setCreateTime(new Timestamp(System.currentTimeMillis()));
       article.setName("测试SpringDat ElasticSearch");
       article.setContent("<p>钟山风雨起苍黄，百万雄师过大江。<p>虎踞龙盘今胜昔，天翻地覆慨而慷。</p>宜将剩勇追穷寇，不可沽名学霸王。天若有情天亦老，人间正道是沧桑。</p>");
       articleService.save(article);
   }
   @Test
    public void findArticle(){
       Criteria fuzzy = Criteria.where("content").fuzzy("百");
       Query query = new CriteriaQuery(fuzzy);
       SearchHits<Article> result = restTemplate.search(query, Article.class);

       Stream<SearchHit<Article>> searchHitStream = result.get();
       System.out.println(searchHitStream);
   }
}
