package antlr.graphql;

import io.study.util.StringHelper;

import java.io.File;

public class GraphAstHelper {
    String template = "public boolean visit{{name}}({{name}} {{lowName}}){\n" +
            "  return true;\n" +
            "}\n" +
            "public void endVisit({{name}} {{lowName}}){\n" +
            "\n" +
            "}";
    public void accept(){

    }
    public static void main(String[] args){
        String path = "D:\\wjf\\workspace\\onlyforstudy\\src\\main\\java\\antlr\\graphql\\ast";

        File f = new File(path);
        for (File file : f.listFiles()) {
            String name = StringHelper.lastPart(file.getName(),'.',false);
           // System.out.println(template);
        }
    }
}
