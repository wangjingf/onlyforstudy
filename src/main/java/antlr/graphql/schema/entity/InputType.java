package antlr.graphql.schema.entity;

import antlr.graphql.ast.Directive;
import antlr.graphql.ast.Type;
import antlr.graphql.schema.exception.CoercingParseValueException;
import io.study.exception.StdException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class InputType extends Type {
    @Override
    public String getPrimitiveTypeName() {
        return name;
    }
    List<Directive> directives = new ArrayList<>();
    Map<String,FieldInfo> fields = new LinkedHashMap<>();
    public void registerField(FieldInfo field){
        fields.put(field.getName(),field);
    }

    public List<Directive> getDirectives() {
        return directives;
    }

    public void setDirectives(List<Directive> directives) {
        this.directives = directives;
    }

    public Map<String, FieldInfo> getFields() {
        return fields;
    }

    public void setFields(Map<String, FieldInfo> fields) {
        this.fields = fields;
    }


}
