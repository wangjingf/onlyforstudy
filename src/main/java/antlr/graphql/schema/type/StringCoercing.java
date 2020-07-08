package antlr.graphql.schema.type;

import antlr.graphql.ast.Value;

public class StringCoercing implements Coercing {
    @Override
    public Object serialize(Object value) {
        return value.toString();
    }

    @Override
    public Object parseValue(Object value) {
        return value.toString();
    }

    @Override
    public Object parseLiteral(Object astNode) {
        return ((Value)astNode).getValue().toString();
    }
}
