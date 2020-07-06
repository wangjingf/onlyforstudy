package antlr.graphql.ast;
import antlr.g4.GraphqlAstVisitor;
import antlr.graphql.Node;

import java.util.ArrayList;
import java.util.List;

public class DirectiveDefinition extends Node {
    String name;
    List<InputValueDefinition> argumentsDefinition = new ArrayList<>();
    List<String> directiveLocations = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<InputValueDefinition> getArgumentsDefinition() {
        return argumentsDefinition;
    }

    public void setArgumentsDefinition(List<InputValueDefinition> argumentsDefinition) {
        this.argumentsDefinition = argumentsDefinition;
    }

    public List<String> getDirectiveLocations() {
        return directiveLocations;
    }

    public void setDirectiveLocations(List<String> directiveLocations) {
        this.directiveLocations = directiveLocations;
    }

    @Override
    public void accept0(GraphqlAstVisitor visitor) {

    }
}
