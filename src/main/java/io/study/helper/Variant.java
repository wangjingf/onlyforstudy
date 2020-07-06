package io.study.helper;

import io.study.data.graphql.model.exception.TypeCaseException;
import io.study.exception.StdException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Variant {
    static final Logger logger = LoggerFactory.getLogger(Variant.class);
    Object value;
    public Variant(Object value){
        this.value = value;
    }
    public static Variant valueOf(Object value){
        return new Variant(value);
    }
    public Double doubleValue(){
        return doubleValue(null);
    }
    public Integer toInt(Integer defaultVal){
        if(value == null){
            return null;
        }
        try{
            if(value instanceof String){
                return Integer.valueOf((String)value);
            }else if(value instanceof Boolean){
                if((Boolean)value == true){
                    return 1;
                }else {
                    return 0;
                }
            }else if(value instanceof Number){
                return (int)value;
            }else if(value instanceof Integer){
                return (int)value;
            }else{
                throw new StdException("variant.err_invalid_int_value");
            }
        }catch (TypeCaseException e){
            logger.debug("variant.err_cast_type:value={},type={}",value,"int");
            return defaultVal;
        }
    }
    public Double doubleValue(Double defaultVal){
        if(value == null){
            return null;
        }else if(value instanceof Integer){
            return Double.valueOf((Integer) value);
        }else if(value instanceof Float){
            return Double.valueOf((Float) value);
        }else if(value instanceof Short){
            return Double.valueOf((Short) value);
        }else if(value instanceof Double){
            return (Double) value;
        }else if(value instanceof String){
            try{
                Double.valueOf((String)value);
            }catch (Exception e){
                return defaultVal;
            }
        }
        throw new StdException("variant.err_cast_to_double_fail").param("value",this.value);
    }
}
