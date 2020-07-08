package antlr.graphql.schema.type;

import antlr.graphql.ast.Value;

import java.math.BigDecimal;

public class BigDecimalCoercing implements Coercing {
    @Override
    public Object serialize(Object value) {
        BigDecimal decimal = new BigDecimal(value.toString());
        return decimal.toString();
    }

    @Override
    public Object parseValue(Object value) {
        BigDecimal decimal = new BigDecimal(value.toString());
        return decimal.toString();
    }

    @Override
    public Object parseLiteral(Object astNode) {
        if(astNode instanceof Value){
            String val = ((Value)astNode).getValue().toString();
            return new BigDecimal(val);
        }
        return null;
    }
}
