package antlr.graphql.ast;
import antlr.g4.GraphqlAstVisitor;
import antlr.graphql.Node;
import io.study.lang.IVisitable;
import io.study.lang.IVisitor;

import java.util.ArrayList;
import java.util.List;

public class Document extends Node implements IVisitable<GraphqlAstVisitor> {
    List<Definition> definitions = new ArrayList<>();

    public List<Definition> getDefinitions() {
        return definitions;
    }

    public void setDefinitions(List<Definition> definitions) {
        this.definitions = definitions;
    }


    @Override
    public void accept0(GraphqlAstVisitor visitor) {

    }
}
