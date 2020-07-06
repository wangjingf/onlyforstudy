package antlr.graphql.ast;
import antlr.graphql.Node;

import java.util.ArrayList;
import java.util.List;

public class InterfaceTypeDefinition extends TypeDefinition {
    String name;
    List<Directive> directives = new ArrayList<>();
    List<FieldDefinition> definitions = new ArrayList<>();

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

    public List<FieldDefinition> getDefinitions() {
        return definitions;
    }

    public void setDefinitions(List<FieldDefinition> definitions) {
        this.definitions = definitions;
    }
}
