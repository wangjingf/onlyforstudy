package antlr.graphql.schema.type;

import antlr.graphql.ast.StringValue;
import antlr.graphql.schema.exception.CoercingParseValueException;
import antlr.graphql.schema.exception.CoercingSerializeException;
import io.study.util.StringHelper;

import java.util.Date;

public class DatetimeCoercing implements Coercing {
    @Override
    public Object serialize(Object value) {
        Date date = null;
        if(value instanceof String){
            date = StringHelper.parseDate((String) value);
            return StringHelper.formatDate(date,"yyyy-MM-dd HH:mm:ss");
        }else if(value instanceof Date){
            date = (Date) value;
            return StringHelper.formatDate(date,"yyyy-MM-dd HH:mm:ss");
        }else {
            throw new CoercingSerializeException();
        }
    }

    @Override
    public Object parseValue(Object value) {
        Date date = null;
        if(value instanceof String){
            return StringHelper.parseDate((String) value);
        }else if(value instanceof Date){
            return (Date) value;
        }else {
            throw new CoercingParseValueException();
        }
    }

    @Override
    public Object parseLiteral(Object astNode) {
        if(astNode instanceof StringValue){
            String date = ((StringValue) astNode).getValue();
            return StringHelper.parseDate(date);
        }
        return null;
    }
}
