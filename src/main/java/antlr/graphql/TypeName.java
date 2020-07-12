package antlr.graphql;

import antlr.g4.GraphQLAstVisitor;
import antlr.graphql.ast.Type;

public class TypeName extends Type {
    public TypeName(String text) {
        this.name = text;
    }


    @Override
    protected void accept0(GraphQLAstVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }

    @Override
    public boolean isListType() {
        return false;
    }

    @Override
    public String getPrimitiveTypeName() {
        return name;
    }
}
