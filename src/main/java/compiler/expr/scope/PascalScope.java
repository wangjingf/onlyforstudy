package compiler.expr.scope;

import java.util.LinkedHashMap;
import java.util.Map;

public class PascalScope {
    Map<String,Object> vars = new LinkedHashMap<>();
    PascalScope parent = null;
    public void addVar(String  name,Object value){
        vars.put(name,value);
    }
    public Object getVar(String name){
        if(vars.containsKey(name)){
            return vars.get(name);
        }
        if(parent != null){
            return parent.getVar(name);
        }
        return null;
    }
    public boolean containsVar(String name){
        if(vars.containsKey(name)){
            return true;
        }
        if(parent != null){
            return parent.containsVar(name);
        }
        return false;
    }
    public Map<String,Object> getAllVars(){
        Map<String,Object> ret = new LinkedHashMap<>();
        ret.putAll(vars);;
        if(parent != null){
            ret.putAll(parent.getAllVars());
        }
        return ret;
    }

    public PascalScope getParent() {
        return parent;
    }

    public void setParent(PascalScope parent) {
        this.parent = parent;
    }
}
