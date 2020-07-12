package antlr.graphql.ast;
import antlr.g4.GraphQLAstVisitor;

public class ListType extends Type {
    public ListType(Type type){
        this.type = type;
    }
    public ListType(){}
    Type type = null;

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
    public boolean isListType() {
        return true;
    }

    @Override
    public String getPrimitiveTypeName() {
        return type.getName();
    }

}
