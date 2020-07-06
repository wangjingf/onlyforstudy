package antlr.graphql.ast;
import antlr.graphql.Node;

import java.util.ArrayList;
import java.util.List;

public class FieldDefinition extends Node {
   String name;
   Type type;
   List<InputValueDefinition> argumentsDefinition = new ArrayList<>();
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

    public List<InputValueDefinition> getArgumentsDefinition() {
        return argumentsDefinition;
    }

    public void setArgumentsDefinition(List<InputValueDefinition> argumentsDefinition) {
        this.argumentsDefinition = argumentsDefinition;
    }

    public List<Directive> getDirectives() {
        return directives;
    }

    public void setDirectives(List<Directive> directives) {
        this.directives = directives;
    }
}
