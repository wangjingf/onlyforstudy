package antlr.graphql.ast;
import antlr.g4.GraphQLAstVisitor;
import antlr.graphql.schema.TypeDefinitions;
 import antlr.graphql.schema.entity.ObjectType;
import antlr.graphql.schema.entity.FieldInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Field extends Selection {
    String alias;
    String name;
    List<Argument> arguments = new ArrayList<>();
    List<Directive> directives = new ArrayList<>();
    SelectionSet selectionSet = null;

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

    public List<Argument> getArguments() {
        return arguments;
    }

    public void setArguments(List<Argument> arguments) {
        this.arguments = arguments;
    }

    public List<Directive> getDirectives() {
        return directives;
    }

    public void setDirectives(List<Directive> directives) {
        this.directives = directives;
    }

    public SelectionSet getSelectionSet() {
        return selectionSet;
    }

    public void setSelectionSet(SelectionSet selectionSet) {
        this.selectionSet = selectionSet;
    }

    @Override
    protected void accept0(GraphQLAstVisitor visitor) {
        if(visitor.visit(this)){
            acceptChild(visitor,arguments);
            acceptChild(visitor,directives);
            acceptChild(visitor,selectionSet);
        }
        visitor.endVisit(this);
    }



    public List<FieldInfo> toField(ObjectType parent, TypeDefinitions definitions) {
        FieldInfo field = new FieldInfo();
        field.setAlias(getAlias());
        field.setName(getName());
        field.setType(parent.getField(getName()).getType());
        for (Argument argument : getArguments()) {
            InputValueDefinition definition = new InputValueDefinition();
            definition.setDefaultValue(argument.getValue());
            definition.setName(argument.getName());
            field.getArguments().add(definition);
        }
        field.setDirectives(getDirectives());
        if(selectionSet != null){
            ObjectType o = definitions.getObjectType(parent.getField(getName()).getType().getPrimitiveTypeName());
            for (Field child : selectionSet.getAllFields(definitions)) {
                List<FieldInfo> fieldInfos = child.toField(o, definitions);
                for (FieldInfo fieldInfo : fieldInfos) {
                  field.getChildFields().put(fieldInfo.getName(),fieldInfo);
                }
            }

        }

        return Collections.singletonList(field);
    }

}
