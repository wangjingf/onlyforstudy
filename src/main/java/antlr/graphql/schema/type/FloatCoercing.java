package antlr.graphql.schema.type;

import antlr.graphql.ast.Value;
import antlr.graphql.schema.exception.CoercingParseValueException;
import antlr.graphql.schema.exception.CoercingSerializeException;
import io.study.helper.Variant;

public class FloatCoercing implements Coercing {
    @Override
    public Object serialize(Object value) {
        Double ret = Variant.valueOf(value).doubleValue(null);
        if(ret == null){
            throw new CoercingSerializeException();
        }
        return ret;
    }

    @Override
    public Object parseValue(Object value) {
        Double ret = Variant.valueOf(value).doubleValue(null);
        if(ret == null){
            throw new CoercingParseValueException();
        }
        return ret;
    }

    @Override
    public Object parseLiteral(Object astNode) {

        if(!(astNode instanceof Value)){
            return null;
        }
        return Variant.valueOf(((Value)astNode).getValue()).doubleValue(null);
    }
}
