package antlr.graphql.schema;

import antlr.graphql.TypeName;
import antlr.graphql.ast.*;
import antlr.graphql.schema.entity.*;
import antlr.graphql.schema.type.GraphQLScalarType;
import compiler.expr.SemanticsException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TypeDefinitions {
    ObjectType queryType = null;
    TypeDefinition mutationType = null;
    /**
     * 获取所有的标量类型
     */
    Map<String,GraphQLScalarType> scalarTypeMap = new LinkedHashMap<>();
    /**
     * type可以为objectType、unionType
     */
    Map<String,ObjectType> objectTypes = new LinkedHashMap<>();
    Map<String,UnionType> unionTypes = new LinkedHashMap<>();
    Map<String,InterfaceType> interfaces = new LinkedHashMap<>();
    Map<String,InputType> inputs = new LinkedHashMap<>();
    Map<String,EnumType> enums = new LinkedHashMap<>();
    Map<String,FragmentDefinition> fragments = new ConcurrentHashMap<>();
    public Map<String, GraphQLScalarType> getScalarTypeMap() {
        return scalarTypeMap;
    }

    public void setScalarTypeMap(Map<String, GraphQLScalarType> scalarTypeMap) {
        this.scalarTypeMap = scalarTypeMap;
    }

    public Map<String, ObjectType> getObjectTypes() {
        return objectTypes;
    }

    public void setObjectTypes(Map<String, ObjectType> objectTypes) {
        this.objectTypes = objectTypes;
    }

    public Map<String, UnionType> getUnionTypes() {
        return unionTypes;
    }

    public void setUnionTypes(Map<String, UnionType> unionTypes) {
        this.unionTypes = unionTypes;
    }

    public Map<String, InterfaceType> getInterfaces() {
        return interfaces;
    }

    public void setInterfaces(Map<String, InterfaceType> interfaces) {
        this.interfaces = interfaces;
    }
    public GraphQLScalarType getScalarType(String name){
        return scalarTypeMap.get(name);
    }
    public ObjectType getObjectType(String name){
        return objectTypes.get(name);
    }
    public UnionType getUnionType(String name){
        return unionTypes.get(name);
    }
    public InterfaceType getInterfaceType(String name){
        return interfaces.get(name);
    }
    public InputType getInputType(String name){
        return inputs.get(name);
    }
    public EnumType getEnumType(String name){
        return enums.get(name);
    }
    public FragmentDefinition getFragment(String name){
        return fragments.get(name);
    }
    public boolean containsScalarType(String name){
        return scalarTypeMap.containsKey(name);
    }
    public boolean containsObjectType(String name){
        return objectTypes.containsKey(name);
    }
    public boolean containsUnionType(String name){
        return unionTypes.containsKey(name);
    }
    public boolean containsInterfaceType(String name){
        return interfaces.containsKey(name);
    }
    public boolean containsInputType(String name){
        return inputs.containsKey(name);
    }
    public boolean containsType(String name){
        return containsScalarType(name) || containsUnionType(name)
                || containsInterfaceType(name) || containsObjectType(name);
    }
    public void registerType(String name,GraphQLScalarType type){
        scalarTypeMap.put(name,type);
    }
    public void registerType(InputType type){
        inputs.put(type.getName(),type);
    }
    public void registerType(ObjectType objectType){
        objectTypes.put(objectType.getName(),objectType);
    }
    public void registerType(UnionType type){
        unionTypes.put(type.getName(),type);
    }
    public void registerType(EnumType type){
        enums.put(type.getName(),type);
    }
    public void registerType(InterfaceType type){
        interfaces.put(type.getName(),type);
    }

    /**
     * 除了scalar类型之外的所有类型必须被确定
     * @param typeName
     * @return
     */
    private Type requireImplType(TypeName typeName){
        String name = typeName.getName();
        Type type = interfaces.get(name);
        if(type == null){
            type = objectTypes.get(name);
            if(type == null){
                type = unionTypes.get(name);
                if(type == null){
                    type = inputs.get(name);
                    if(type == null){
                        type = enums.get(name);
                        if(type == null){
                            if(!scalarTypeMap.containsKey(name)){
                                throw new SemanticsException("find invalid type:"+name);
                            }
                            return scalarTypeMap.get(name);
                        }
                    }

                }
            }
        }
        return type;
    }
    /**
     * 将TypeName类型换成具体的类型,scalar类型除外
     */
    public void init(){
        for (Map.Entry<String, InterfaceType> entry : interfaces.entrySet()) {
            for (FieldInfo fieldInfo : entry.getValue().getFields().values()) {
                Type type = requireImplType(  fieldInfo.getType());
                if(type != null){ //替换为具体的类型
                    fieldInfo.setType(type);
                }
                fieldInfo.init();
            }
        }
        for (Map.Entry<String, ObjectType> entry : objectTypes.entrySet()) {
            for (FieldInfo fieldInfo : entry.getValue().getFields().values()) {
                Type type = requireImplType(  fieldInfo.getType());
                if(type != null){ //替换为具体的类型
                    fieldInfo.setType(type);
                }
                fieldInfo.init();
            }
        }
        for (Map.Entry<String, InputType> entry : inputs.entrySet()) {
            for (FieldInfo fieldInfo : entry.getValue().getFields().values()) {
                Type type = requireImplType(  fieldInfo.getType());
                if(type != null){ //替换为具体的类型
                    if(!(type instanceof InputType)){
                        throw new SemanticsException("invalid input field Type:").param("type",entry.getKey()).param("field",fieldInfo.getName()).param("fldType",type.getName());
                    }
                    fieldInfo.setType(type);
                }
                fieldInfo.init();
            }
        }
    }
    public  Type requireImplType(Type type){
        if(type instanceof TypeName){
            return requireImplType((TypeName) type);
        }else if(type instanceof NonNullType){
            Type newType = requireImplType((NonNullType) ((NonNullType) type).getType());
            if(newType == null){
                return type;
            }else{
                return new NonNullType(newType);
            }
        }else if(type instanceof ListType){
            Type newType = requireImplType((ListType) ((ListType) type).getType());
            if(newType == null){
                return type;
            }else{
               return new ListType(newType);
            }
        }
        throw new SemanticsException("find invalid type："+type.getName());
    }

    public Map<String, FragmentDefinition> getFragments() {
        return fragments;
    }
    public void registerFragment(FragmentDefinition definition){
        fragments.put(definition.getName(),definition);
    }
    public void setFragments(Map<String, FragmentDefinition> fragments) {
        this.fragments = fragments;
    }

    public ObjectType getQueryType() {
        return queryType;
    }

    public void setQueryType(ObjectType queryType) {
        this.queryType = queryType;
    }

    public TypeDefinition getMutationType() {
        return mutationType;
    }

    public void setMutationType(TypeDefinition mutationType) {
        this.mutationType = mutationType;
    }
}
