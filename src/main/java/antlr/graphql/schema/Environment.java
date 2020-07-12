package antlr.graphql.schema;

import antlr.graphql.schema.data.IDataFetcher;
import antlr.graphql.schema.entity.ObjectType;
import antlr.graphql.schema.entity.directive.IncludeDirectiveResolver;
import antlr.graphql.schema.entity.directive.IDirectiveResolver;
import antlr.graphql.schema.entity.directive.SkipDirectiveResolver;
import antlr.graphql.schema.type.GraphQLScalarType;
import antlr.graphql.schema.type.Scalars;
import io.study.exception.StdException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Environment {
    TypeDefinitions typeDefinitions = null;
    public Environment(TypeDefinitions typeDefinitions){
        this.typeDefinitions = typeDefinitions;
        scalarTypeMap.put(Scalars.INT.getName(),Scalars.INT);
        scalarTypeMap.put(Scalars.FLOAT.getName(),Scalars.FLOAT);
        scalarTypeMap.put(Scalars.STRING.getName(),Scalars.STRING);
        scalarTypeMap.put(Scalars.ID.getName(),Scalars.ID);
        scalarTypeMap.put(Scalars.BIG_DECIMAL.getName(),Scalars.BIG_DECIMAL);
        scalarTypeMap.put(Scalars.BIG_INTEGER.getName(),Scalars.BIG_INTEGER);
        scalarTypeMap.put(Scalars.BOOLEAN.getName(),Scalars.BOOLEAN);
        directiveMap.put("include",new IncludeDirectiveResolver());
        directiveMap.put("skip",new SkipDirectiveResolver());
    }
    Map<String,IDirectiveResolver> directiveMap = new ConcurrentHashMap<>();
    Map<String,GraphQLScalarType> scalarTypeMap = new ConcurrentHashMap<>();
    Map<String/* name*/,Map<String,IDataFetcher>> dataFetchers = new ConcurrentHashMap<>();
    public void registerDateFetcher(String name, String fldName, IDataFetcher fetcher){
        ObjectType objectType = typeDefinitions.getObjectTypes().get(name);
        if(objectType == null){
            throw new StdException("graph.err_invalid_object_type").param("typeName",name);
        }
        if(!objectType.existField(fldName)){
            throw new StdException("graph.err_invalid_fld").param("objectType",name).param("fldName",fldName);
        }
        dataFetchers.putIfAbsent(name, new ConcurrentHashMap<>());
        dataFetchers.get(name).put(fldName,fetcher);
    }
    public Map<String,IDataFetcher> getDataFetcher(String typeName){
        return dataFetchers.get(typeName);
    }
    public boolean containsScalarType(String name){
        return scalarTypeMap.containsKey(name);
    }
    public  void registerScalarType(GraphQLScalarType scalarType){
        scalarTypeMap.put(scalarType.getName(),scalarType);
    }
    public GraphQLScalarType   getScalarType(String name){
        return scalarTypeMap.get(name);
    }

    public void registerDirectiveType(String name, IDirectiveResolver resolver){
        directiveMap.put(name,resolver);
    }
    public IDirectiveResolver getDirectiveResolver(String name){
        return directiveMap.get(name);
    }
    public  boolean existDirective(String name){
        return directiveMap.containsKey(name);
    }
}
