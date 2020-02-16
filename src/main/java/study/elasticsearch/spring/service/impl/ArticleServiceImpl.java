package study.elasticsearch.spring.service.impl;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.lucene.search.function.FunctionScoreQuery;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import study.elasticsearch.spring.dao.ArticleRepository;
import study.elasticsearch.spring.entity.Article;
import study.elasticsearch.spring.service.ArticleService;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {
    static final Integer PAGE_SIZE = 10;
    static final Integer DEFAULT_PAGE_NUMBER = 0;
    /* 搜索模式 */
    static final String SCORE_MODE_SUM = "sum"; // 权重分求和模式
    static final Float  MIN_SCORE = 10.0F;
    static final Logger logger = LoggerFactory.getLogger(ArticleService.class);
    @Autowired
    ArticleRepository articleRepository;
    @Autowired
    ElasticsearchTemplate elasticsearchTemplate;
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
    public static String concat(Text[] texts) {
        StringBuffer sb = new StringBuffer();
        for (Text text : texts) {
            sb.append(text.toString());
        }
        return sb.toString();
    }
    @Override
    public List<Article> searchArticle(Integer pageNumber, Integer pageSize, String searchContent) {
        // 校验分页参数
        if (pageSize == null || pageSize <= 0) {
            pageSize = PAGE_SIZE;
        }
        if (pageNumber == null || pageNumber < DEFAULT_PAGE_NUMBER) {
            pageNumber = DEFAULT_PAGE_NUMBER;
        }
        logger.info("\n searchCity: searchContent [" + searchContent + "] \n ");
        // 构建搜索查询
        SearchQuery searchQuery = getArticleSearchQuery(pageNumber,pageSize,searchContent);
        logger.info("\n searchCity: searchContent [" + searchContent + "] \n DSL  = \n " + searchQuery.getQuery().toString());
        //Page<Article> cityPage = articleRepository.search(searchQuery);
        Page<Article> cityPage = elasticsearchTemplate.queryForPage(searchQuery,Article.class,new SearchResultMapper() {

            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {
                List<Article> chunk = new ArrayList<>();
                for (SearchHit searchHit : response.getHits()) {
                    if (response.getHits().getHits().length <= 0) {
                        return null;
                    }
                    Article article = new Article();
                    //name or memoe
                    HighlightField name = searchHit.getHighlightFields().get("name");
                    if (name != null) {
                        article.setName(concat(name.fragments()));
                    }
                    HighlightField content = searchHit.getHighlightFields().get("content");
                    if (content != null) {
                        article.setContent(concat(content.fragments()));
                    }

                    chunk.add(article);
                }
                if (chunk.size() > 0) {
                    return new AggregatedPageImpl<>((List<T>) chunk);
                }
                return null;
            }
        });
        return cityPage.getContent();
    }
    /**
     * 根据搜索词构造搜索查询语句
     *
     * 代码流程：
     *      - 权重分查询
     *      - 短语匹配
     *      - 设置权重分最小值
     *      - 设置分页参数
     *
     * @param pageNumber 当前页码
     * @param pageSize 每页大小
     * @param searchContent 搜索内容
     * @return
     */
    private SearchQuery getArticleSearchQuery(Integer pageNumber, Integer pageSize,String searchContent) {
        MultiMatchQueryBuilder builder =  QueryBuilders.multiMatchQuery(searchContent,"name","content");
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        HighlightBuilder.Field highlightTitle =
                new HighlightBuilder.Field("name").preTags("<span style=color:red>")
                        .postTags("</span>").fragmentSize(1000);

        highlightBuilder.field(highlightTitle);
        HighlightBuilder.Field highlightContent = new HighlightBuilder.Field("content").preTags("<span style=color:red>")
                .postTags("</span>").fragmentSize(1000);
        highlightBuilder.field(highlightContent);

        highlightBuilder
                .preTags("<span style=color:red>")
                .postTags("</span>");
        // 分页参数
        Pageable pageable = new PageRequest(pageNumber, pageSize);
        return new NativeSearchQueryBuilder()
                .withPageable(pageable)
                //.withHighlightBuilder(highlightBuilder)
                .withHighlightFields(highlightTitle,highlightContent)
                .withQuery(builder).build();
    }

}
