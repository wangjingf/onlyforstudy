package antlr.graphql.schema.entity;

import antlr.graphql.ast.*;

import java.util.*;

public class FieldInfo {
    String alias;
    String name;
    Type type = null;
    Value defaultValue;
    List<InputValueDefinition> arguments = new ArrayList<>();
    List<Directive> directives=  new ArrayList<>();
    Map<String,FieldInfo> childFields = new HashMap<>();
    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<InputValueDefinition> getArguments() {
        return arguments;
    }

    public void setArguments(List<InputValueDefinition> arguments) {
        this.arguments = arguments;
    }

    public List<Directive> getDirectives() {
        return directives;
    }

    public void setDirectives(List<Directive> directives) {
        this.directives = directives;
    }
   private  Map<String,FieldInfo> getChildFields(Type type){
       if(type instanceof ObjectType){
           return ((ObjectType) type).getFields();
       }else if(type instanceof InterfaceType){
           return ((InterfaceType) type).getFields();
       }else if(type instanceof ListType){
          return getChildFields(((ListType) type).getType());
       }else if(type instanceof NonNullType){
           return getChildFields(((NonNullType) type).getType());
       }else{
           return Collections.emptyMap();
       }
   }
   public void init(){
       this.childFields = getChildFields(type);
   }
    public Map<String,FieldInfo> getChildFields() {
        return childFields;
    }


    public Value getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Value defaultValue) {
        this.defaultValue = defaultValue;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "SelectionField{" +
                "name='" + name + '\'' +
                '}';
    }
}
