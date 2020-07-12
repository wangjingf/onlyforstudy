package antlr.graphql.ast;
import antlr.g4.GraphQLAstVisitor;

import java.util.ArrayList;
import java.util.List;

public class ObjectTypeDefinition extends TypeDefinition {
    List<Directive> directives = new ArrayList<>();
    List<String> implementsInterfaces = new ArrayList<>();
   List<FieldDefinition>  fieldDefinition = new ArrayList<>();



    public List<Directive> getDirectives() {
        return directives;
    }

    public void setDirectives(List<Directive> directives) {
        this.directives = directives;
    }

    public List<String> getImplementsInterfaces() {
        return implementsInterfaces;
    }

    public void setImplementsInterfaces(List<String> implementsInterfaces) {
        this.implementsInterfaces = implementsInterfaces;
    }

    public List<FieldDefinition> getFieldDefinition() {
        return fieldDefinition;
    }

    public void setFieldDefinition(List<FieldDefinition> fieldDefinition) {
        this.fieldDefinition = fieldDefinition;
    }

    @Override
    protected void accept0(GraphQLAstVisitor visitor) {
        if(visitor.visit(this)){
            acceptChild(visitor,directives);
            acceptChild(visitor,fieldDefinition);
        }
        visitor.endVisit(this);
    }
}
