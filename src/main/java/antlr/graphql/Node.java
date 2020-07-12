package antlr.graphql;

import antlr.g4.GraphQLAstVisitor;
import io.study.lang.IVisitable;

import java.util.Collection;

public abstract class Node implements IVisitable<GraphQLAstVisitor> {
    protected abstract void accept0(GraphQLAstVisitor visitor);
    protected  void acceptChild(GraphQLAstVisitor visitor, Collection<? extends Node> child){
        if (child != null) {
            for (Node node : child) {
                this.acceptChild(visitor,node);
            }
        }
    }
    protected final void acceptChild(GraphQLAstVisitor var1, Node var2) {
        if (var2 != null) {
            var2.accept(var1);
        }
    }
    @Override
    public void accept(GraphQLAstVisitor visitor) {
        if(visitor.preVisit(this)){
            accept0(visitor);
        }
        visitor.postVisit(this);
    }

    //SourceLocation sourceLocation;
}
