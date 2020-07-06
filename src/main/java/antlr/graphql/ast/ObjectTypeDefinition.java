package antlr.graphql.ast;
import antlr.graphql.Node;

import java.util.ArrayList;
import java.util.List;

public class ObjectTypeDefinition extends TypeDefinition {
    String name;
    List<Directive> directives = new ArrayList<>();
    List<String> implementsInterfaces = new ArrayList<>();
   List<FieldDefinition>  fieldDefinition = new ArrayList<>();

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
}
