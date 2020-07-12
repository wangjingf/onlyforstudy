package antlr.graphql.schema.entity;

import antlr.graphql.ast.Directive;
import antlr.graphql.ast.Type;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class UnionType extends Type {
    Map<String,ObjectType> types = new LinkedHashMap<>();
    List<Directive> directives = new ArrayList<>();
    @Override
    public boolean isListType() {
        return false;
    }

    @Override
    public String getPrimitiveTypeName() {
        return null;
    }

    public Map<String, ObjectType> getTypes() {
        return types;
    }

    public void setTypes(Map<String, ObjectType> types) {
        this.types = types;
    }

    public List<Directive> getDirectives() {
        return directives;
    }

    public void setDirectives(List<Directive> directives) {
        this.directives = directives;
    }
}
