package antlr.graphql.ast;
import antlr.g4.GraphQLAstVisitor;
import antlr.graphql.Node;

import java.util.ArrayList;
import java.util.List;

public class InputValueDefinition extends Node {
    String name;
    Type type;
    Value defaultValue;
    List<Directive> directives = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Value getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Value defaultValue) {
        this.defaultValue = defaultValue;
    }

    public List<Directive> getDirectives() {
        return directives;
    }

    public void setDirectives(List<Directive> directives) {
        this.directives = directives;
    }

    @Override
    public void accept0(GraphQLAstVisitor visitor) {
        if(visitor.visit(this)){
            acceptChild(visitor,type);
            acceptChild(visitor,defaultValue);
            acceptChild(visitor,directives);
        }
        visitor.endVisit(this);
    }
}
