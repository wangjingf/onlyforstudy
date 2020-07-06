package antlr.graphql.ast;
import antlr.g4.GraphqlAstVisitor;
import antlr.graphql.Node;

public class NonNullType extends Type {
    Type type = null;

    public NonNullType(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    protected void accept0(GraphqlAstVisitor visitor) {
        if(visitor.visit(this)){
            acceptChild(visitor,type);
        }
        visitor.endVisit(this);
    }
}
