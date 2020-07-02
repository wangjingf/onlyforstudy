package antlr.g4;

import antlr.g4.hello.HelloLexer;
import antlr.g4.hello.HelloParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.UnbufferedCharStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.StringBufferInputStream;

public class TestAntlr {
    /**
     *
     * @param args
     */
    public static void main(String[] args){
        //CharStreams.fromString("hello world");
        HelloLexer lexer = new HelloLexer(CharStreams.fromString("hello    world"));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        HelloParser parser = new HelloParser(tokens);
         HelloParser.RContext context = parser.r();
        System.out.println("---------start----------");
        for (ParseTree child : context.children) {
            System.out.println(child.getText());
        }
        System.out.println("---------end----------");
         Token token= context.ID().getSymbol();
        System.out.println("token.getText()::"+token.getText());
        //HelloParser.CompilationUnitContext tree = parser.compilationUnit(); // parse a compilationUnit

      /*  MyListener extractor = new MyListener(parser);
        ParseTreeWalker.DEFAULT.walk(extractor, tree); // ini*/
    }
}
