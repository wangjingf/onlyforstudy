package antlr.graphql.ast;
import antlr.graphql.Node;

import java.util.ArrayList;
import java.util.List;

public class Document extends Node {
    List<Definition> definitions = new ArrayList<>();

    public List<Definition> getDefinitions() {
        return definitions;
    }

    public void setDefinitions(List<Definition> definitions) {
        this.definitions = definitions;
    }
}
