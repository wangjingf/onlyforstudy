package antlr.graphql.ast;
import antlr.g4.GraphQLAstVisitor;

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
    protected void accept0(GraphQLAstVisitor visitor) {
        if(visitor.visit(this)){
            acceptChild(visitor,type);
        }
        visitor.endVisit(this);
    }

    @Override
    public String getName() {
        return type.getName();
    }

    @Override
    public boolean isListType() {
        return type.isListType();
    }

    @Override
    public String getPrimitiveTypeName() {
        return type.getName();
    }
}
