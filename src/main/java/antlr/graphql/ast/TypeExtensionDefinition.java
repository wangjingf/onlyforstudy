package antlr.graphql.ast;
import antlr.g4.GraphQLAstVisitor;

public class TypeExtensionDefinition extends TypeSystemDefinition {
    ObjectTypeDefinition objectTypeDefinition = null;

    public ObjectTypeDefinition getObjectTypeDefinition() {
        return objectTypeDefinition;
    }

    public void setObjectTypeDefinition(ObjectTypeDefinition objectTypeDefinition) {
        this.objectTypeDefinition = objectTypeDefinition;
    }

    @Override
    protected void accept0(GraphQLAstVisitor visitor) {
        if(visitor.visit(this)){
            acceptChild(visitor,objectTypeDefinition);
        }
        visitor.endVisit(this);
    }
}
