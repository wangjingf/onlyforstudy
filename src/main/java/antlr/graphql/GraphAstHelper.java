package antlr.graphql;

import io.study.util.StringHelper;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

public class GraphAstHelper {
    static final String template = "public boolean visit({{name}} {{lowName}}){\n" +
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
            String name = StringHelper.firstPart(file.getName(),'.');
            String lowName = StringHelper.decapitalize(name);
            Map<String,Object> vars = new LinkedHashMap<>();
            vars.put("name",name);
            vars.put("lowName",lowName);
           System.out.println(StringHelper.format(template,vars));
        }
    }
}
