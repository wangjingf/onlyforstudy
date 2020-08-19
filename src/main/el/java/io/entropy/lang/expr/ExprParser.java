package io.entropy.lang.expr;

import antlr.g4.graphql.GraphqlLexer;
import antlr.g4.graphql.GraphqlParser;
import antlr.graphql.ast.Document;
import compiler.expr.SyntaxException;
import io.entropy.lang.ast.Program;
import io.study.exception.StdException;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.ParseCancellationException;

public class ExprParser {
    String program;
    public ExprParser(String program){
        this.program = program;
    }

    public Program parseExpr(){
        GraphqlLexer lexer = new GraphqlLexer(CharStreams.fromString(program));
        CommonTokenStream tokens = new CommonTokenStream(lexer);


        GraphqlParser parser = new GraphqlParser(tokens);
        parser.setErrorHandler(new BailErrorStrategy());
        try{
            GraphqlParser.DocumentContext document = parser.document();
            return null;//(Document) visitor.visitDocument(document);
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
