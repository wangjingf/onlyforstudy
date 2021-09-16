package io.study.helper;


import io.study.lang.IVariant;

import java.util.Map;

public class MapGetter {
    Map<String,Object> value = null;
    public MapGetter(Map map){
        this.value = map;
    }
    public static MapGetter valueOf(Map data){
        return new MapGetter(data);
    }
    public IVariant get(Object key){
       return get(value,key);
    }
    public static IVariant get(Map data,Object key){
        if(data == null){
            return Variant.valueOf(null);
        }
        return Variant.valueOf(data.get(key));
    }
}
