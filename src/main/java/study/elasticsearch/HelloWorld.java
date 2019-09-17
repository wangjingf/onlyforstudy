package study.elasticsearch;

import org.elasticsearch.client.transport.TransportClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import study.elasticsearch.spring.entity.Article;
import study.elasticsearch.spring.service.ArticleService;

import java.sql.Timestamp;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:spring-data-elasticsearch.xml")
public class HelloWorld  {
    @Autowired
   ArticleService articleService;
    @Autowired
   TransportClient client;
    @Autowired
    ElasticsearchTemplate elasticsearchTemplate;
   @Test
    public void createIndex(){
       elasticsearchTemplate.createIndex(Article.class);
       elasticsearchTemplate.putMapping(Article.class);
   }
   @Test
   public void saveArticle(){
       Article article = new Article();
       article.setId("1000");
       article.setCreateTime(new Timestamp(System.currentTimeMillis()));
       article.setName("测试SpringDat ElasticSearch");
       article.setContent("<p>钟山风雨起苍黄，百万雄师过大江。<p>虎踞龙盘今胜昔，天翻地覆慨而慷。</p>宜将剩勇追穷寇，不可沽名学霸王。天若有情天亦老，人间正道是沧桑。</p>");
       articleService.save(article);
   }
   @Test
    public void findArticle(){

   }
}
