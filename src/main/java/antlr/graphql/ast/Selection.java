package antlr.graphql.ast;
import antlr.g4.GraphqlAstVisitor;
import antlr.graphql.Node;

public class Selection extends Node {
    public Field getMergedResult(Selection selection){
        return null;
    }

    @Override
    public void accept0(GraphqlAstVisitor visitor) {

    }
}
