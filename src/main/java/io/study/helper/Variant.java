package io.study.helper;

import io.study.exception.StdException;

public class Variant {
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
