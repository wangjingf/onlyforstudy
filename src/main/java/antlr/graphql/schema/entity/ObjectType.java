package antlr.graphql.schema.entity;

import antlr.graphql.ast.Directive;
import antlr.graphql.ast.Type;

import java.util.*;

/**
 *接口类型和objectType统称为objectType
 */
public class ObjectType extends Type {
    String name;
    List<InterfaceType> superTypes = new LinkedList<>();
    Map<String,FieldInfo> fields = new HashMap<>();
    Map<String,FieldInfo> allFields = new HashMap<>();
    List<Directive> directives = new ArrayList<>();
    public ObjectType(String name,Map<String,FieldInfo> fields){
         this.name = name;
         this.fields = fields;
    }
    public void init(){
        for (InterfaceType type : superTypes) {
            allFields.putAll(type.getFields());
        }
        allFields.putAll(fields);

    }

    public Map<String, FieldInfo> getFields() {
        return fields;
    }

    public void setFields(Map<String, FieldInfo> fields) {
        this.fields = fields;
    }

    public void setAllFields(Map<String, FieldInfo> allFields) {
        this.allFields = allFields;
    }

    public Map<String, FieldInfo> getAllFields() {
        return allFields;
    }
    public FieldInfo getField(String name){
        return allFields.get(name);
    }
    public boolean existField(String name){
        return allFields.containsKey(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean isListType() {
        return false;
    }

    @Override
    public String getPrimitiveTypeName() {
        return getName();
    }

    public List<InterfaceType> getSuperTypes() {
        return superTypes;
    }

    public void setSuperTypes(List<InterfaceType> superTypes) {
        this.superTypes = superTypes;
    }


    public List<Directive> getDirectives() {
        return directives;
    }

    public void setDirectives(List<Directive> directives) {
        this.directives = directives;
    }
}
