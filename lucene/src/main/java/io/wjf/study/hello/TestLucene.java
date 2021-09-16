package io.wjf.study.hello;

import junit.framework.TestCase;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.document.*;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Before;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Paths;
import java.util.Collection;

/**
 * https://blog.csdn.net/fulibaocs/article/details/79355997
 */

/**
 * 	/**https://blog.csdn.net/weixin_34346099/article/details/93810029
 * 		* 新版本中使用了Int/Long/DoublePoint来表示数值型字段,但是默认不存储,不排序,也不支持加权
 * 		* 创建索引加权值在6.6版本后就已经废除了,并给了搜索时设置的新query,这个后面查询时再说
 * 		* 如果存储需要用StoredField写相同的字段,排序还要再使用NumericDocValuesField写相同的排序,
 * 		* 如下的fileSize,添加long值索引,存储并添加排序支持
 * 		/
 * 		//文件名
 * 		doc.add(new TextField("fileName", file.getName(), Store.YES););
 * 		//大小,数字类型使用point添加到索引中,同时如果需要存储,由于没有Stroe,所以需要再创建一个StoredField进行存储
 * 		// 即 IntPoint,DoublePoint等
 * 		doc.add(new LongPoint("fileSize", file.length()));
 * 		//大小
 * 		doc.add(new StoredField("fileSize", file.length()));
 * 		//同时添加排序支持
 * 		doc.add(new NumericDocValuesField("fileSize",file.length()));
 * 		//路径
 * 		doc.add(new StoredField("filePath",file.getPath()));
 * 		//内容
 * 		doc.add(new TextField("fileContent",FileUtils.readFileToString(file),Store.NO));
 * 		//doc.add(new TextField("fileContent",new FileReader(file)));
 * 		// 4 使用indexWriter将doc对象写入索引库,此过程创建索引,并将索引和文档对象写入索引库
 * 		writer.addDocument(doc);
 */
public class TestLucene extends TestCase {
    @Before
    public void setUp() {
        // 索引文件将要存放的位置

    }
    static File outputDir = new File("D:\\temp\\index_dir");
    static final String SCAN_PATH = "D:\\docs";

    public static IndexWriter getIndexWriter() throws Exception{
        Directory directory = null;
        Analyzer analyzer = new IKAnalyzer6x(true);
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
        //创建索引写入对象
        IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);
        return indexWriter;
    }
    public void testCreateIndex(){
        createIndex();
    }
    public void testSearch(){
        search("总结");
    }

    public void testSearchNumber(){
        Query query = LongPoint.newRangeQuery("updateTime",10000000L,Long.MAX_VALUE);
        search(query);
    }
     public void  createIndex1(){

     }

    /**
     * 创建索引
     */
    public void createIndex()
    {
        IndexWriter indexWriter = null;
        try
        {
            Directory directory = FSDirectory.open(Paths.get(outputDir.toURI()));
            //Analyzer analyzer = new StandardAnalyzer();
            Analyzer analyzer = new IKAnalyzer6x(true);
            IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
            indexWriter = new IndexWriter(directory, indexWriterConfig);
            indexWriter.deleteAll();// 清除以前的index
            // 获取被扫描目录下的所有文件，包括子目录
            Collection<File> files =   FileUtils.listFiles(new File(SCAN_PATH),null,true);
            for (File file : files) {
                Document document = new Document();
                String content = null;
                if(file.getName().endsWith(".txt")){
                    content = IOUtils.toString(new FileReader(file));
                }else{
                    content = "empty";
                }
                document.add(new Field("content", content, TextField.TYPE_STORED));
                document.add(new Field("fileName", file.getName(), TextField.TYPE_STORED));
                document.add(new Field("filePath", file.getAbsolutePath(), TextField.TYPE_STORED));
                document.add(new LongPoint("updateTime", file.lastModified()));
                //大小
                document.add(new StoredField("updateTime", file.length()));
                //同时添加排序支持
                document.add(new NumericDocValuesField("updateTime",file.length()));
                indexWriter.addDocument(document);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if(indexWriter != null) indexWriter.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    public void testFuzzySearch(){
        FuzzyQuery query=new FuzzyQuery(new Term("content","总"),2);
        search(query);
    }
    public void testRegexpSearch(){
        String regex = "总.*结";
        Term t = new Term("content", regex);
        RegexpQuery regexpQuery = new RegexpQuery(t);
        search(regexpQuery);
    }

    public void testNumberSearch(){
        //Query q = NumericRangeQuery.newLongRange("updateTime", 1L, 10L, true, true);

    }

    /**
     * 搜索
     */
    public void search(String keyWord)  { // 4、创建搜索的Query
        // Analyzer analyzer = new StandardAnalyzer();
        Analyzer analyzer = new IKAnalyzer6x(true); // 使用IK分词

        // 简单的查询，创建Query表示搜索域为content包含keyWord的文档
        //Query query = new QueryParser("content", analyzer).parse(keyWord);

        String[] fields = {"fileName"}; // , "content" 要搜索的字段，一般搜索时都不会只搜索一个字段
        // 字段之间的与或非关系，MUST表示and，MUST_NOT表示not，SHOULD表示or，有几个fields就必须有几个clauses
        BooleanClause.Occur[] clauses = {BooleanClause.Occur.MUST};//, BooleanClause.Occur.MUST
        // MultiFieldQueryParser表示多个域解析， 同时可以解析含空格的字符串，如果我们搜索"上海 中国"
        Query multiFieldQuery = null;
        try {
            multiFieldQuery = MultiFieldQueryParser.parse(keyWord, fields, clauses, analyzer);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        search(multiFieldQuery);
    }

    /**
     * 搜索
     */
    public void search(Query query)
    {
        DirectoryReader directoryReader = null;
        try
        {
            // 1、创建Directory
            Directory directory = FSDirectory.open(Paths.get(outputDir.toURI()));
            // 2、创建IndexReader
            directoryReader = DirectoryReader.open(directory);
            // 3、根据IndexReader创建IndexSearch
            IndexSearcher indexSearcher = new IndexSearcher(directoryReader);
            // 4、创建搜索的Query
            // Analyzer analyzer = new StandardAnalyzer();
            Analyzer analyzer = new IKAnalyzer6x(true); // 使用IK分词

            // 简单的查询，创建Query表示搜索域为content包含keyWord的文档
            //Query query = new QueryParser("content", analyzer).parse(keyWord);

            String[] fields = {"fileName", "content"}; // 要搜索的字段，一般搜索时都不会只搜索一个字段
            // 字段之间的与或非关系，MUST表示and，MUST_NOT表示not，SHOULD表示or，有几个fields就必须有几个clauses
            BooleanClause.Occur[] clauses = {BooleanClause.Occur.MUST, BooleanClause.Occur.MUST};
            // MultiFieldQueryParser表示多个域解析， 同时可以解析含空格的字符串，如果我们搜索"上海 中国"
            Query multiFieldQuery = query;

            // 5、根据searcher搜索并且返回TopDocs
            TopDocs topDocs = indexSearcher.search(multiFieldQuery, 100); // 搜索前100条结果
            System.out.println("共找到匹配处：" + topDocs.totalHits); // totalHits和scoreDocs.length的区别还没搞明白
            // 6、根据TopDocs获取ScoreDoc对象
            ScoreDoc[] scoreDocs = topDocs.scoreDocs;
            System.out.println("共找到匹配文档数：" + scoreDocs.length);

            QueryScorer scorer = new QueryScorer(multiFieldQuery, "content");
            // 自定义高亮代码
            SimpleHTMLFormatter htmlFormatter = new SimpleHTMLFormatter("<span style=\"backgroud:red\">", "</span>");
            Highlighter highlighter = new Highlighter(htmlFormatter, scorer);
            highlighter.setTextFragmenter(new SimpleSpanFragmenter(scorer));
            for (ScoreDoc scoreDoc : scoreDocs)
            {
                // 7、根据searcher和ScoreDoc对象获取具体的Document对象
                Document document = indexSearcher.doc(scoreDoc.doc);
                //TokenStream tokenStream = new SimpleAnalyzer().tokenStream("content", new StringReader(content));
                //TokenSources.getTokenStream("content", tvFields, content, analyzer, 100);
                //TokenStream tokenStream = TokenSources.getAnyTokenStream(indexSearcher.getIndexReader(), scoreDoc.doc, "content", document, analyzer);
                //System.out.println(highlighter.getBestFragment(tokenStream, content));
                System.out.println("-----------------------------------------");
                System.out.println("fileName::"+document.get("fileName") + ",filePath:" + document.get("filePath"));
                System.out.println("updateTime:"+document.get("updateTime"));
                System.out.println("content::"+highlighter.getBestFragment(analyzer, "content", document.get("content")));
                System.out.println("");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if(directoryReader != null) directoryReader.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    public static void displayToken(String str,Analyzer analyzer){
        try {
            //将一个字符串创建成Token流
            TokenStream stream  = analyzer.tokenStream("", new StringReader(str));
            stream.reset();
            //保存相应词汇
            CharTermAttribute cta = stream.addAttribute(CharTermAttribute.class);
            OffsetAttribute offsetAttribute = stream.addAttribute(OffsetAttribute.class);
            while(stream.incrementToken()){
                System.out.print("[" + cta + " start="+offsetAttribute.startOffset()+" end="+offsetAttribute.endOffset()+"]");

            }
            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void testTokenizer(){
        Analyzer aly1 = new StandardAnalyzer();


        String str = "hello kim,I am dennisit,我是 中国人,my email is dennisit@163.com, and my QQ is 1325103287";

        displayToken(str, aly1);

    }
}
