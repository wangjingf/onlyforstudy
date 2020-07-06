package antlr.graphql;

import antlr.g4.GraphqlAstVisitor;
import io.study.lang.IVisitable;
import io.study.lang.IVisitor;

public abstract class Node implements IVisitable<GraphqlAstVisitor> {
    public abstract void accept0(GraphqlAstVisitor visitor);

    @Override
    public void accept(GraphqlAstVisitor visitor) {
        if(visitor.preVisit(this)){
            accept0(visitor);
        }
        visitor.postVisit(this);
    }
    //SourceLocation sourceLocation;
}
