package antlr.graphql.schema.type;

import antlr.graphql.ast.Value;
import antlr.graphql.schema.exception.CoercingParseValueException;
import antlr.graphql.schema.exception.CoercingSerializeException;
import io.study.helper.Variant;

public class IntCoercing implements Coercing {
    @Override
    public Object serialize(Object o) {
        Integer value = Variant.valueOf(o).toInt(null);
        if(value == null){
            throw new CoercingSerializeException();
        }
        return value;
    }

    @Override
    public Object parseValue(Object o) {
        Integer value = Variant.valueOf(o).toInt(null);
        if(value == null){
            throw new CoercingParseValueException();
        }
        return value;
    }

    @Override
    public Object parseLiteral(Object astNode) {
        if(!(astNode instanceof Value)){
            return null;
        }
        return Variant.valueOf(((Value)astNode).getValue()).toInt(null);
    }
}
