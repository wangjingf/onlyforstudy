package antlr.graphql.schema.entity;

import antlr.graphql.ast.Directive;
import antlr.graphql.ast.Type;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class InterfaceType extends Type {
    Map<String,FieldInfo> fields = new LinkedHashMap<>();
    List<Directive> directives = new ArrayList<>();
    public InterfaceType(String name){
        this.name = name;
    }
    @Override
    public String getPrimitiveTypeName() {
        return getName();
    }

    public Map<String, FieldInfo> getFields() {
        return fields;
    }

    public void setFields(Map<String, FieldInfo> fields) {
        this.fields = fields;
    }

    public List<Directive> getDirectives() {
        return directives;
    }

    public void setDirectives(List<Directive> directives) {
        this.directives = directives;
    }
    public void registerField(FieldInfo fieldInfo){
        fields.put(fieldInfo.getName(),fieldInfo);
    }
}
