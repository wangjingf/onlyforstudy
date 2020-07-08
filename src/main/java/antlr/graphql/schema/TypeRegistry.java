package antlr.graphql.schema;

import antlr.graphql.ast.TypeDefinition;
import antlr.graphql.schema.type.GraphQLScalarType;
import antlr.graphql.schema.type.Scalars;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TypeRegistry {
    Map<String,GraphQLScalarType> scalarTypeMap = new ConcurrentHashMap<>();
    Map<String,TypeDefinition> typeDefinitionMap = new ConcurrentHashMap<>();
    public TypeRegistry(){
        scalarTypeMap.put(Scalars.INT.getName(),Scalars.INT);
        scalarTypeMap.put(Scalars.FLOAT.getName(),Scalars.FLOAT);
        scalarTypeMap.put(Scalars.STRING.getName(),Scalars.STRING);
        scalarTypeMap.put(Scalars.ID.getName(),Scalars.ID);
        scalarTypeMap.put(Scalars.BIG_DECIMAL.getName(),Scalars.BIG_DECIMAL);
        scalarTypeMap.put(Scalars.BIG_INTEGER.getName(),Scalars.BIG_INTEGER);
        scalarTypeMap.put(Scalars.BOOLEAN.getName(),Scalars.BOOLEAN);

    }
   public  void registerScalarType(GraphQLScalarType scalarType){
       scalarTypeMap.put(scalarType.getName(),scalarType);
   }
   public GraphQLScalarType   getScalarType(String name){
        return scalarTypeMap.get(name);
   }
   public void registerType(TypeDefinition typeDefinition){
       typeDefinitionMap.put(typeDefinition.getName(),typeDefinition);
   }
   public TypeDefinition getTypeDefinition(String name){
        return typeDefinitionMap.get(name);
   }
   public boolean containsScalarType(String name){
        return scalarTypeMap.containsKey(name);
   }
   public boolean containsTypeDefinition(String name){
        return typeDefinitionMap.containsKey(name);
   }
   public boolean containsType(String name){
        return containsScalarType(name) || containsTypeDefinition(name);
   }
}
