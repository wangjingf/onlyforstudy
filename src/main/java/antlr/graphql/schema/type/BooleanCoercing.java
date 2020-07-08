package antlr.graphql.schema.type;

import antlr.graphql.ast.Value;
import antlr.graphql.schema.exception.CoercingParseValueException;
import io.study.helper.Variant;

public class BooleanCoercing implements Coercing {
    @Override
    public Object serialize(Object value) {
        Boolean val = Variant.valueOf(value).toBool(null);
        if(val == null){
            throw new CoercingParseValueException();
        }
        return val;
    }

    @Override
    public Object parseValue(Object value) {
        Boolean val = Variant.valueOf(value).toBool(null);
        if(val == null){
            throw new CoercingParseValueException();
        }
        return val;
    }

    @Override
    public Object parseLiteral(Object astNode) {
        if(astNode instanceof Value){
            return Variant.valueOf(((Value) astNode).getValue()).toBool(null);
        }
        return null;
    }
}
