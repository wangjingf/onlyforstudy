package antlr.graphql.ast;

import antlr.g4.GraphQLAstVisitor;

import java.util.ArrayList;
import java.util.List;

public class OperationDefinition extends Definition {
    SelectionSet selectionSet;
    Operation operation;
    String name;
    List<VariableDefinition> variables = new ArrayList<>();
    List<Directive> directives = new ArrayList<>();

    public SelectionSet getSelectionSet() {
        return selectionSet;
    }

    public void setSelectionSet(SelectionSet selectionSet) {
        this.selectionSet = selectionSet;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<VariableDefinition> getVariables() {
        return variables;
    }

    public void setVariables(List<VariableDefinition> variables) {
        this.variables = variables;
    }

    public List<Directive> getDirectives() {
        return directives;
    }

    public void setDirectives(List<Directive> directives) {
        this.directives = directives;
    }

    @Override
    protected void accept0(GraphQLAstVisitor visitor) {
        if(visitor.visit(this)){
            acceptChild(visitor,selectionSet);
            acceptChild(visitor,variables);
            acceptChild(visitor,directives);
        }
        visitor.endVisit(this);
    }
}
