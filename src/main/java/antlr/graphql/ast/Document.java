package antlr.graphql.ast;
import antlr.g4.GraphQLAstVisitor;
import antlr.graphql.Node;
import io.study.lang.IVisitable;

import java.util.ArrayList;
import java.util.List;

public class Document extends Node implements IVisitable<GraphQLAstVisitor> {
    List<Definition> definitions = new ArrayList<>();

    public List<Definition> getDefinitions() {
        return definitions;
    }

    public void setDefinitions(List<Definition> definitions) {
        this.definitions = definitions;
    }


    @Override
    public void accept0(GraphQLAstVisitor visitor) {
        if(visitor.visit(this)){
            acceptChild(visitor,definitions);
        }
        visitor.endVisit(this);
    }
}
