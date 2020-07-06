package antlr.graphql.ast;
import antlr.graphql.Node;

public class TypeExtensionDefinition extends TypeSystemDefinition {
    ObjectTypeDefinition objectTypeDefinition = null;

    public ObjectTypeDefinition getObjectTypeDefinition() {
        return objectTypeDefinition;
    }

    public void setObjectTypeDefinition(ObjectTypeDefinition objectTypeDefinition) {
        this.objectTypeDefinition = objectTypeDefinition;
    }
}
