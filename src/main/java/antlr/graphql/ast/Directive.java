package antlr.graphql.ast;
import antlr.g4.GraphQLAstVisitor;
import antlr.graphql.Node;

import java.util.List;
import java.util.Map;

public abstract class Directive extends Node {
    String name;
    List<Argument> arguments;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Argument> getArguments() {
        return arguments;
    }

    public void setArguments(List<Argument> arguments) {
        this.arguments = arguments;
    }

    @Override
    public void accept0(GraphQLAstVisitor visitor) {
        if(visitor.visit(this)){
            acceptChild(visitor,arguments);
        }
        visitor.endVisit(this);
    }
   public boolean isValid(Map<String,Object> args){
        return true;
   }
    public abstract void validate();
}
