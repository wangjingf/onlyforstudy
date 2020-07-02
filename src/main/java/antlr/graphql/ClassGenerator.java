package antlr.graphql;

import antlr.g4.graphql.GraphqlLexer;
import antlr.g4.graphql.GraphqlParser;
import io.study.helper.IoHelper;
import io.study.util.StringHelper;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.File;
import java.time.temporal.Temporal;

public class ClassGenerator {
   static final  String TEMPLATE = "package antlr.graphql.ast;\n" +
           "import antlr.graphql.Node;\n" +
           "\n" +
           "public class %s extends Node {\n" +
           "    \n" +
           "}\n";
    public static void main(String[] args){
        File file = new File("D:\\wjf\\workspace\\onlyforstudy\\src\\main\\java\\antlr\\graphql\\ast");
        AstBuildVisitor visitor = new AstBuildVisitor();
        GraphqlLexer lexer = new GraphqlLexer(CharStreams.fromString("query {}"));
        CommonTokenStream tokens = new CommonTokenStream(lexer);


        GraphqlParser parser = new GraphqlParser(tokens);

        for (String ruleName : parser.getRuleNames()) {

            String fileName = StringHelper.capitalize(ruleName);
            File classFile = new File(file,fileName+".java");
            String content = String.format(TEMPLATE,fileName);
            if(!classFile.exists()){
                IoHelper.writeText(classFile,content);
            }
        }
    }
}
