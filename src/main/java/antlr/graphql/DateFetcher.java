package antlr.graphql;

import antlr.graphql.ast.*;
import io.study.helper.IoHelper;

import java.io.File;
import java.io.FileInputStream;

public class DateFetcher {
    public String readSchema(String fileName){
        String path = "D:\\wjf\\workspace\\onlyforstudy\\src\\main\\java\\antlr\\graphql\\file";
        File file = new File(path,fileName);
        return IoHelper.toString(file);
    }
    public Object fetchData(Document document){
        for (Definition definition : document.getDefinitions()) {
            if(definition instanceof OperationDefinition){
                return queryData((OperationDefinition) definition);
            }
        }
        return null;
    }
    public Object queryData(OperationDefinition definition){
        Operation operation = definition.getOperation();
        for (Selection selection : definition.getSelectionSet().getSelections()) {
            return querySelection(selection);
        }
        return null;
    }
    public Object querySelection(Selection selection){
        Field field = (Field) selection;
        String tableName = field.getName();
        for (Selection child : field.getSelectionSet().getSelections()) {
            Field subField = (Field) child;
            String fieldName = subField.getName();
        }
        return null;
    }

    public static void main(String[] args){
        DateFetcher dataFetcher = new DateFetcher();
     }
}
