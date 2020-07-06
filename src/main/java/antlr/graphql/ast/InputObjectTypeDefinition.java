package antlr.graphql.ast;
import antlr.g4.GraphqlAstVisitor;
import antlr.graphql.Node;

import java.util.ArrayList;
import java.util.List;

public class InputObjectTypeDefinition extends TypeDefinition {
    String name;
    List<Directive> directives = new ArrayList<>();
    List<InputValueDefinition> inputValueDefinition = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Directive> getDirectives() {
        return directives;
    }

    public void setDirectives(List<Directive> directives) {
        this.directives = directives;
    }

    public List<InputValueDefinition> getInputValueDefinition() {
        return inputValueDefinition;
    }

    public void setInputValueDefinition(List<InputValueDefinition> inputValueDefinition) {
        this.inputValueDefinition = inputValueDefinition;
    }

    @Override
    protected void accept0(GraphqlAstVisitor visitor) {
        if(visitor.visit(this)){
            acceptChild(visitor,directives);
            acceptChild(visitor,inputValueDefinition);
        }
        visitor.endVisit(this);
    }
}
