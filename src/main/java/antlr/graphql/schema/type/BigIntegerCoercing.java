package antlr.graphql.schema.type;

import antlr.graphql.ast.Value;

import java.math.BigInteger;

public class BigIntegerCoercing implements Coercing {
    @Override
    public Object serialize(Object value) {
        BigInteger bigInteger = new BigInteger(value.toString());
        return bigInteger.toString();
    }

    @Override
    public Object parseValue(Object value) {
        BigInteger bigInteger = new BigInteger(value.toString());
        return bigInteger.toString();
    }

    @Override
    public Object parseLiteral(Object astNode) {
        if(astNode instanceof Value){
            String val = ((Value)astNode).getValue().toString();
            return new BigInteger(val);
        }
        return null;
    }
}
