package antlr.graphql.ast;
import antlr.g4.GraphqlAstVisitor;
import antlr.graphql.Node;

import java.util.ArrayList;
import java.util.List;

public class SchemaDefinition extends TypeSystemDefinition {
    List<Directive> directives = new ArrayList<>();
    List<OperationTypeDefinition> operationTypeDefinitions = new ArrayList<>();

    public List<Directive> getDirectives() {
        return directives;
    }

    public void setDirectives(List<Directive> directives) {
        this.directives = directives;
    }

    public List<OperationTypeDefinition> getOperationTypeDefinitions() {
        return operationTypeDefinitions;
    }

    public void setOperationTypeDefinitions(List<OperationTypeDefinition> operationTypeDefinitions) {
        this.operationTypeDefinitions = operationTypeDefinitions;
    }

    @Override
    protected void accept0(GraphqlAstVisitor visitor) {
        if(visitor.visit(this)){
            acceptChild(visitor,directives);
            acceptChild(visitor,operationTypeDefinitions);
        }
        visitor.endVisit(this);
    }
}
