package io.study.gateway.balance;

import java.util.HashMap;
import java.util.Map;

public class LoadBalanceContext {
    Map<String,Object> map = new HashMap<>();
    public Object getAttr(String key){
        return map.get(key);
    }
    public void setAttr(String key,Object val){
        map.put(key,val);
    }
}
