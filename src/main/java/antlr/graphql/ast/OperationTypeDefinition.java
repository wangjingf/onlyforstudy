package antlr.graphql.ast;
import antlr.graphql.Node;

public class OperationTypeDefinition extends Node {
    Operation operation = null;
    String typeName;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }
}
