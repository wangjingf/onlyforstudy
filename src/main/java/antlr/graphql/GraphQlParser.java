package antlr.graphql;

import antlr.g4.graphql.GraphqlLexer;
import antlr.g4.graphql.GraphqlParser;
import antlr.graphql.ast.Document;
import compiler.expr.SyntaxException;
import io.study.exception.StdException;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.ParseCancellationException;

import java.util.LinkedHashMap;
import java.util.Map;

public class GraphQlParser {
    String schema;
    public GraphQlParser(String schema){
        this.schema = schema;
    }
    public void dumpToken(){

        AstBuildVisitor visitor = new AstBuildVisitor();
        GraphqlLexer lexer = new GraphqlLexer(CharStreams.fromString(schema));
        Map<String,Integer> map =  lexer.getTokenTypeMap();
        Map<Integer,String> tokenMap = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            tokenMap.put(entry.getValue(),entry.getKey());
        }
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        Token token1 = tokens.LT(1);

        while (token1.getType() != Token.EOF){
            System.out.println(tokenMap.get(token1.getType())+"::"+token1.getText());
            tokens.consume();
            token1 = tokens.LT(1);
        }
    }
    public Document parseDocument(){
        AstBuildVisitor visitor = new AstBuildVisitor();
        GraphqlLexer lexer = new GraphqlLexer(CharStreams.fromString(schema));
        CommonTokenStream tokens = new CommonTokenStream(lexer);


        GraphqlParser parser = new GraphqlParser(tokens);
        parser.setErrorHandler(new BailErrorStrategy());
        // ParseTree tree =  parser.document();
        try{
            GraphqlParser.DocumentContext document = parser.document();
            return (Document) visitor.visitDocument(document);
        }catch (ParseCancellationException exception){
            Throwable e = exception.getCause();
            if(e instanceof RecognitionException){
                RecognitionException recognitionException = (RecognitionException) e;
                Token offendingToken = recognitionException.getOffendingToken();
                int line = offendingToken.getLine();
                int column = offendingToken.getCharPositionInLine();
                offendingToken.getType();

                throw new SyntaxException("at line"+line+" column "+column+" expected is "+recognitionException.getExpectedTokens().toString(VocabularyImpl.fromTokenNames(parser.getTokenNames())));
            }
            throw StdException.adapt(exception.getCause());
        }


    }
}
