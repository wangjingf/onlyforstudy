package antlr.graphql.ast;

import antlr.g4.GraphQLAstVisitor;

public class NullValue extends Value<Object> {
    @Override
    public Object getValue() {
        return null;
    }

    @Override
    public void setValue(Object value) {

    }

    @Override
    public void accept0(GraphQLAstVisitor visitor) {
        if(visitor.visit(this)){
         }
        visitor.endVisit(this);
    }
}
