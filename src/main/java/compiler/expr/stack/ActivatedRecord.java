package compiler.expr.stack;

import compiler.expr.Token;
import compiler.expr.TokenType;

import java.util.LinkedHashMap;
import java.util.Map;

public class ActivatedRecord  {
    int nestLevel;
    String name;
    /**
     * 是produce还是function?
     */
    TokenType type;
    Map<String,Object> members = new LinkedHashMap<>();

    public ActivatedRecord(int nestLevel, String name, TokenType type) {
        this.nestLevel = nestLevel;
        this.name = name;
        this.type = type;
    }
    public void put(String key,Object value){
        members.put(key,value);
    }
    public Object get(String key){
        return members.get(key);
    }
    public boolean contains(String key){
        return members.containsKey(key);
    }
    public Map<String,Object> getMembers(){
        return members;
    }
}
