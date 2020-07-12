package antlr.graphql.schema.entity;

import antlr.graphql.ast.Directive;
import antlr.graphql.ast.EnumTypeDefinition;
import antlr.graphql.ast.EnumValueDefinition;
import antlr.graphql.ast.Type;

import java.util.ArrayList;
import java.util.List;

public class EnumType extends Type {
    public List<EnumValueDefinition> values = new ArrayList<>();
    List<Directive> directives = new ArrayList<>();
    public EnumType(String name,List<EnumValueDefinition> values) {
        this.name = name;
        this.values = values;
    }

    @Override
    public String getPrimitiveTypeName() {
        return null;
    }

    public List<EnumValueDefinition> getValues() {
        return values;
    }

    public void setValues(List<EnumValueDefinition> values) {
        this.values = values;
    }

    public List<Directive> getDirectives() {
        return directives;
    }

    public void setDirectives(List<Directive> directives) {
        this.directives = directives;
    }
}
