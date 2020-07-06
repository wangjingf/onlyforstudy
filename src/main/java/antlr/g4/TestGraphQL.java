package antlr.g4;

import antlr.g4.expr.ExprLexer;
import antlr.g4.expr.ExprParser;

import antlr.g4.graphql.GraphqlLexer;
import antlr.g4.graphql.GraphqlParser;
import antlr.graphql.AstBuildVisitor;
import antlr.graphql.DateFetcher;
import antlr.graphql.GraphQlParser;
import antlr.graphql.ast.Document;
import io.study.helper.IoHelper;
import junit.framework.TestCase;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

public class TestGraphQL extends TestCase {
    String graphQl = "query {\n" +
            "  hero {\n" +
            "    name\n" +
            "  }\n" +
            "  droid(id: \"2000\") {\n" +
            "    name\n" +
            "  }\n" +
            "}" ;
  /* String graphQl = "query { hero}" ;*/
    public void testLexerToken(){
        GraphqlLexer lexer = new GraphqlLexer(CharStreams.fromString(graphQl));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        Map<String,Integer> map =  lexer.getTokenTypeMap();
        Map<Integer,String> tokenMap = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            tokenMap.put(entry.getValue(),entry.getKey());
        }
        Token token1 = tokens.LT(1);

        while (token1.getType() != Token.EOF){
            System.out.println(tokenMap.get(token1.getType())+"::"+token1.getText());
            tokens.consume();
            token1 = tokens.LT(1);
        }

    }
    public void testQuery(){
        GraphqlLexer lexer = new GraphqlLexer(CharStreams.fromString(graphQl));
        CommonTokenStream tokens = new CommonTokenStream(lexer);


        GraphqlParser parser = new GraphqlParser(tokens);
        ParseTree tree =  parser.document();
        for (ParseTree child :  ((GraphqlParser.DocumentContext) tree).children) {
            System.out.println(child.getText());
        }
    }
    public void testAst(){
        AstBuildVisitor visitor = new AstBuildVisitor();
        GraphqlLexer lexer = new GraphqlLexer(CharStreams.fromString(graphQl));
        CommonTokenStream tokens = new CommonTokenStream(lexer);


        GraphqlParser parser = new GraphqlParser(tokens);
       // ParseTree tree =  parser.document();
       Document document = (Document) visitor.visitDocument(parser.document());
        DateFetcher fetcher = new DateFetcher();
        fetcher.fetchData(document);
    }
    public String readSchema(String fileName){
        String path = "D:\\wjf\\workspace\\onlyforstudy\\src\\main\\java\\antlr\\graphql\\file";
        File file = new File(path,fileName);
        return IoHelper.toString(file);
    }

        public void testTokens(){
            String type = readSchema("schema.txt");
            GraphQlParser parser = new GraphQlParser(type);
            parser.dumpToken();
        }
    public void testSchema(){
        String type = readSchema("schema.txt");
        GraphQlParser parser = new GraphQlParser(type);
        Document doc = parser.parseDocument();

    }


}
