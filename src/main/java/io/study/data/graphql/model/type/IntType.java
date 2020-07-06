package io.study.data.graphql.model.type;

import io.study.data.graphql.model.exception.TypeCaseException;
import io.study.helper.Variant;

public class IntType implements ScaleType<Integer>{
    @Override
    public Integer cast(Object value) throws TypeCaseException {
        if(value == null){
            return null;
        }
        Integer ret = Variant.valueOf(value).toInt(null);
        if(ret == null){
            throw new TypeCaseException("can not cast value to int,value="+value);
        }
        return ret;
    }
}
