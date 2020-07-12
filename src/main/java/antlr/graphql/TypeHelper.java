package antlr.graphql;

import antlr.graphql.ast.ListType;
import antlr.graphql.ast.Type;
import antlr.graphql.schema.entity.FieldInfo;
import antlr.graphql.schema.entity.InputType;
import antlr.graphql.schema.exception.CoercingParseValueException;
import antlr.graphql.schema.type.GraphQLScalarType;
import io.study.exception.StdException;

import java.util.*;

public class TypeHelper {
    public static Object parseValue(Object value,Type type){
        if(type.isListType()){
            List list = new ArrayList();
            if(!(value instanceof Collection)){
                throw new StdException("invalid input value,expected collection ").param("type",value);
            }
            Collection o = (Collection) value;
            for (Object o1 : o) {
                list.add(parseValue(o1, ((ListType) type).getType()));
            }
            return list;
        }else {
            if(type instanceof GraphQLScalarType){
                return ((GraphQLScalarType) type).getCoercing().parseValue(value);
            }else if(type instanceof InputType){
                return parseInputValue(value, (InputType) type);
            }else{
                throw new StdException("unexpected type").param("type",type.getName());
            }
        }
    }
    public static Object parseInputValue(Object value,InputType type){
        if(!(value instanceof Map)){
            throw new CoercingParseValueException("unexpected value："+value);
        }
        Map<String,Object> m = (Map) value;
        Map<String,Object> ret = new LinkedHashMap<>();
        for (Map.Entry<String,Object> entry : m.entrySet()) {
            if(!type.getFields().containsKey(entry.getKey())){
                throw new StdException("unexpected field for input type").param("field",entry.getKey()).param("inputType",type.getName());
            }
            Type fieldType = type.getFields().get(entry.getKey()).getType();
            Object o = parseValue(m.get(entry.getKey()), fieldType);
            ret.put(entry.getKey(),o);
        }
        for (Map.Entry<String, FieldInfo> entry : type.getFields().entrySet()) {
            if(entry.getValue().getType().isNonNullType()){
                Object fldValue = ret.get(entry.getKey());
                if(value == null){
                    throw new StdException("graphql.err_miss_required_field_for_input_type").param("inputType",type.getName()).param("field",entry.getKey());
                }
            }
        }
        return ret;
    }
}
