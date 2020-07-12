package antlr.graphql;

import antlr.g4.graphql.GraphqlParser;
import antlr.g4.graphql.GraphqlVisitor;
import antlr.graphql.ast.*;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

public class AstBuildVisitor implements GraphqlVisitor<Node> {
    public AstBuildVisitor(){

    }
    @Override
    public Node visitDocument(GraphqlParser.DocumentContext ctx) {
        Document document = new Document();
        for (GraphqlParser.DefinitionContext definitionContext : ctx.definition()) {
            document.getDefinitions().add((Definition) visitDefinition(definitionContext));
        }
        return document;
    }

    @Override
    public Node visitDefinition(GraphqlParser.DefinitionContext ctx) {
        GraphqlParser.FragmentDefinitionContext fragmentDefinition = ctx.fragmentDefinition();
        GraphqlParser.OperationDefinitionContext operationDefinition = ctx.operationDefinition();
        GraphqlParser.TypeSystemDefinitionContext typeSystemDefinition = ctx.typeSystemDefinition();
        if(fragmentDefinition != null){
            return visitFragmentDefinition(fragmentDefinition);
        }else if(operationDefinition != null){
            return visitOperationDefinition(operationDefinition);
        }else{
            return  visitTypeSystemDefinition(typeSystemDefinition);
        }

    }

    @Override
    public Node visitOperationDefinition(GraphqlParser.OperationDefinitionContext ctx) {
        OperationDefinition definition = new OperationDefinition();
        if(ctx.operationType() != null){
            definition.setOperation(Operation.valueOf(ctx.operationType().getText().toUpperCase()));//待处理

        }
        definition.setSelectionSet((SelectionSet) visitSelectionSet(ctx.selectionSet()));
        if(ctx.name() != null){
            definition.setName(ctx.name().getText());
        }
        if (ctx.directives() != null){
            for (ParseTree directive : ctx.directives().directive()) {
                definition.getDirectives().add((Directive) visitDirective((GraphqlParser.DirectiveContext) directive));
            }
        }
        if(ctx.variableDefinitions() != null){
            for (ParseTree child : ctx.variableDefinitions().variableDefinition()) {
                definition.getVariables().add((VariableDefinition) visitVariable((GraphqlParser.VariableContext) child));
            }
        }

        return definition;
    }

    @Override
    public Node visitOperationType(GraphqlParser.OperationTypeContext ctx) {
        return null;
    }

    @Override
    public Node visitVariableDefinitions(GraphqlParser.VariableDefinitionsContext ctx) {
        return null;
    }

    @Override
    public Node visitVariableDefinition(GraphqlParser.VariableDefinitionContext ctx) {
        VariableDefinition definition = new VariableDefinition();
        definition.setVariable(ctx.variable().getText());
        definition.setType((Type) visitType(ctx.type()));
        definition.setDefaultValue((Value) visitDefaultValue(ctx.defaultValue()));
        return definition;
    }

    @Override
    public Node visitVariable(GraphqlParser.VariableContext ctx) {
        return null;
    }

    @Override
    public Node visitDefaultValue(GraphqlParser.DefaultValueContext ctx) {
        return visitValue(ctx.value());
    }

    @Override
    public Node visitSelectionSet(GraphqlParser.SelectionSetContext ctx) {
        SelectionSet selectionSet = new SelectionSet();
        for (GraphqlParser.SelectionContext context : ctx.selection()) {
            selectionSet.getSelections().add((Selection) visitSelection(context));
        }
        return selectionSet;
    }

    @Override
    public Node visitSelection(GraphqlParser.SelectionContext ctx) {
        if(ctx.field() != null){
            return visitField(ctx.field());
        }else if(ctx.fragmentSpread() != null){
            return visitFragmentSpread(ctx.fragmentSpread());
        }else{
            return visitInlineFragment(ctx.inlineFragment());
        }
    }

    @Override
    public Node visitField(GraphqlParser.FieldContext ctx) {
        Field field = new Field();
        if(ctx.alias() != null){
            field.setAlias(ctx.alias().getText());
        }
        field.setName(ctx.name().getText());
        if(ctx.arguments() != null){
            for (GraphqlParser.ArgumentContext argument : ctx.arguments().argument()) {
                field.getArguments().add((Argument) visitArgument(argument));
            }
        }
         if(ctx.directives() != null){
             for (ParseTree child : ctx.directives().directive()) {
                 field.getDirectives().add((Directive) visitDirective((GraphqlParser.DirectiveContext) child));
             }
         }
        if(ctx.selectionSet() != null){
            field.setSelectionSet((SelectionSet) visitSelectionSet(ctx.selectionSet()));
        }

        return field;
    }

    @Override
    public Node visitAlias(GraphqlParser.AliasContext ctx) {
        return null;
    }

    @Override
    public Node visitArguments(GraphqlParser.ArgumentsContext ctx) {

        return null;
    }

    @Override
    public Node visitArgument(GraphqlParser.ArgumentContext ctx) {
        Argument argument = new Argument();
        argument.setName(ctx.name().getText());
        argument.setValue((Value) visitValueWithVariable(ctx.valueWithVariable()));
        return argument;
    }

    @Override
    public Node visitFragmentSpread(GraphqlParser.FragmentSpreadContext ctx) {
        FragmentSpread fragmentSpread = new FragmentSpread();
        fragmentSpread.setFragmentName(ctx.fragmentName().getText());
        for (ParseTree child : ctx.directives().children) {
            fragmentSpread.getDirectives().add((Directive) visitDirective((GraphqlParser.DirectiveContext) child));
        }
        return fragmentSpread;
    }

    @Override
    public Node visitInlineFragment(GraphqlParser.InlineFragmentContext ctx) {
        InlineFragment inlineFragment = new InlineFragment();
        inlineFragment.setTypeCondition(ctx.typeCondition().typeName().name().getText());
        for (ParseTree child : ctx.directives().children) {
            inlineFragment.getDirectives().add((Directive) visitDirective((GraphqlParser.DirectiveContext) child));
        }
        inlineFragment.setSelectionSet((SelectionSet) visitSelectionSet(ctx.selectionSet()));
        return inlineFragment;
    }

    @Override
    public Node visitFragmentDefinition(GraphqlParser.FragmentDefinitionContext ctx) {
        FragmentDefinition definition = new FragmentDefinition();
        definition.setName(ctx.fragmentName().name().getText());
        definition.setTypeCondition(ctx.typeCondition().typeName().name().getText());
        for (ParseTree child : ctx.directives().children) {
            definition.getDirectives().add((Directive) visitDirective((GraphqlParser.DirectiveContext) child));
        }
        definition.setSelectionSet((SelectionSet) visitSelectionSet(ctx.selectionSet()));
        return definition;
    }

    @Override
    public Node visitFragmentName(GraphqlParser.FragmentNameContext ctx) {
        return null;
    }

    @Override
    public Node visitTypeCondition(GraphqlParser.TypeConditionContext ctx) {

        return null;
    }

    @Override
    public Node visitName(GraphqlParser.NameContext ctx) {
        return null;
    }

    @Override
    public Node visitValue(GraphqlParser.ValueContext ctx) {
        if(ctx.IntValue() != null){
            return new IntValue(Integer.valueOf(ctx.IntValue().getText()));
        }else if(ctx.FloatValue() != null){
            return new FloatValue(Float.valueOf(ctx.FloatValue().getText()));
        }else if(ctx.StringValue() != null){
            return new StringValue(ctx.StringValue().getText());
        }else if(ctx.BooleanValue()!=null){
            return new BooleanValue(Boolean.valueOf(ctx.BooleanValue().getText()));
        }else if(ctx.NullValue() != null){
            return new NullValue();
        }else if(ctx.enumValue() != null ){
            return new EnumValue(ctx.enumValue().getText());
        }else if(ctx.arrayValue() != null){
            ArrayValue arrayValue = new ArrayValue();
            for (ParseTree child : ctx.arrayValue().children) {
                arrayValue.getValue().add((Value) visitValue((GraphqlParser.ValueContext) child));
            }
            return arrayValue;
        }else if(ctx.objectValue() != null){
            ObjectValue objectValue = new ObjectValue();
            for (ParseTree child : ctx.objectValue().children) {
                objectValue.getFields().add((ObjectField) visitField((GraphqlParser.FieldContext) child));
            }
            return objectValue;
        }
        return null;
    }

    @Override
    public Node visitValueWithVariable(GraphqlParser.ValueWithVariableContext ctx) {
        if(ctx.variable() != null){
            return new VariableReference(ctx.variable().getText());
        } if(ctx.IntValue() != null){
            return new IntValue(Integer.valueOf(ctx.IntValue().getText()));
        }else if(ctx.FloatValue() != null){
            return new FloatValue(Float.valueOf(ctx.FloatValue().getText()));
        }else if(ctx.StringValue() != null){
            return new StringValue(ctx.StringValue().getText());
        }else if(ctx.BooleanValue()!=null){
            return new BooleanValue(Boolean.valueOf(ctx.BooleanValue().getText()));
        }else if(ctx.NullValue() != null){
            return new NullValue();
        }else if(ctx.enumValue() != null ){
            return new EnumValue(ctx.enumValue().getText());
        }else if(ctx.arrayValueWithVariable() != null){
            ArrayValue arrayValue = new ArrayValue();
            for (ParseTree child : ctx.arrayValueWithVariable().children) {
                arrayValue.getValue().add((Value) visitValueWithVariable((GraphqlParser.ValueWithVariableContext) child));
            }
        }else if(ctx.objectValueWithVariable() != null){

            return visitObjectValueWithVariable(ctx.objectValueWithVariable());
        }
        return null;
    }

    @Override
    public Node visitEnumValue(GraphqlParser.EnumValueContext ctx) {
        return null;
    }

    @Override
    public Node visitArrayValue(GraphqlParser.ArrayValueContext ctx) {
        return null;
    }

    @Override
    public Node visitArrayValueWithVariable(GraphqlParser.ArrayValueWithVariableContext ctx) {
        return null;
    }

    @Override
    public Node visitObjectValue(GraphqlParser.ObjectValueContext ctx) {
        return null;
    }

    @Override
    public Node visitObjectValueWithVariable(GraphqlParser.ObjectValueWithVariableContext ctx) {
       Object value = new  ObjectValue();
        for (GraphqlParser.ObjectFieldWithVariableContext fieldCtx : ctx.objectFieldWithVariable()) {
            ((ObjectValue) value).getFields().add((ObjectField) visitObjectFieldWithVariable(fieldCtx));
        }
        return null;
    }

    @Override
    public Node visitObjectField(GraphqlParser.ObjectFieldContext ctx) {
        return null;
    }

    @Override
    public Node visitObjectFieldWithVariable(GraphqlParser.ObjectFieldWithVariableContext ctx) {
        ObjectField objectField = new ObjectField();
        objectField.setName(ctx.name().getText());
        objectField.setValue((Value) visitValueWithVariable(ctx.valueWithVariable()));
        return objectField;
    }

    @Override
    public Node visitDirectives(GraphqlParser.DirectivesContext ctx) {
        return null;
    }

    @Override
    public Node visitDirective(GraphqlParser.DirectiveContext ctx) {
        Directive directive = new Directive();
        directive.setName(ctx.name().getText());
        for (GraphqlParser.ArgumentContext argumentContext : ctx.arguments().argument()) {
            directive.getArguments().add((Argument) visitArgument(argumentContext));
        }
        return directive;
    }

    @Override
    public Node visitType(GraphqlParser.TypeContext ctx) {
        if(ctx.typeName() != null){
            return new TypeName(ctx.typeName().name().getText());
        }else if(ctx.listType() != null){
            ListType listType = new ListType();
            listType.setType((Type) visitType(ctx.listType().type()));
            return listType;
        }else if(ctx.nonNullType() != null){
            return new NonNullType((Type) visitNonNullType(ctx.nonNullType()));
        }
        return null;
    }

    @Override
    public Node visitTypeName(GraphqlParser.TypeNameContext ctx) {
        return null;
    }

    @Override
    public Node visitListType(GraphqlParser.ListTypeContext ctx) {
        return null;
    }

    @Override
    public Node visitNonNullType(GraphqlParser.NonNullTypeContext ctx) {
        if(ctx.typeName() != null){
            return new TypeName(ctx.typeName().name().getText());
        }else if(ctx.listType() != null){
            ListType listType = new ListType();
            listType.setType((Type) visitType(ctx.listType().type()));
            return listType;
        }
        return null;
    }

    @Override
    public Node visitTypeSystemDefinition(GraphqlParser.TypeSystemDefinitionContext ctx) {
        if(ctx.schemaDefinition() != null){
            return visitSchemaDefinition(ctx.schemaDefinition());
        }else if(ctx.typeDefinition() != null){
            return visitTypeDefinition(ctx.typeDefinition());
        }else if(ctx.typeExtensionDefinition() != null){
            return visitTypeExtensionDefinition(ctx.typeExtensionDefinition());
        }else{
            return visitDirectiveDefinition(ctx.directiveDefinition());
        }
    }

    @Override
    public Node visitSchemaDefinition(GraphqlParser.SchemaDefinitionContext ctx) {
        SchemaDefinition schemaDefinition = new SchemaDefinition();
        if(ctx.directives() != null){
            for (GraphqlParser.DirectiveContext directiveContext : ctx.directives().directive()) {
                schemaDefinition.getDirectives().add((Directive) visitDirective(directiveContext));
            }
        }
        for (GraphqlParser.OperationTypeDefinitionContext opCtx : ctx.operationTypeDefinition()) {
            schemaDefinition.getOperationTypeDefinitions().add((OperationTypeDefinition) visitOperationTypeDefinition(opCtx));
        }
        return schemaDefinition;
    }

    @Override
    public Node visitOperationTypeDefinition(GraphqlParser.OperationTypeDefinitionContext ctx) {
        OperationTypeDefinition definition = new OperationTypeDefinition();
        definition.setTypeName(ctx.typeName().toString());
        definition.setOperation(Operation.valueOf(ctx.operationType().getText().toUpperCase()));
        return definition;
    }

    @Override
    public Node visitTypeDefinition(GraphqlParser.TypeDefinitionContext ctx) {
        if(ctx.scalarTypeDefinition() !=null){
            return visitScalarTypeDefinition(ctx.scalarTypeDefinition());
        }else if(ctx.objectTypeDefinition() != null){
            return visitObjectTypeDefinition(ctx.objectTypeDefinition());
        }else if(ctx.interfaceTypeDefinition() != null){
            return visitInterfaceTypeDefinition(ctx.interfaceTypeDefinition());
        }else if(ctx.unionTypeDefinition() !=null){
            return visitUnionTypeDefinition(ctx.unionTypeDefinition());
        }else if(ctx.enumTypeDefinition() != null){
            return visitEnumTypeDefinition(ctx.enumTypeDefinition());
        }else{
          return visitInputObjectTypeDefinition(ctx.inputObjectTypeDefinition());
        }
    }

    @Override
    public Node visitScalarTypeDefinition(GraphqlParser.ScalarTypeDefinitionContext ctx) {
        ScalarTypeDefinition scalarTypeDefinition = new ScalarTypeDefinition();
        scalarTypeDefinition.setName(ctx.name().getText());
        if(ctx.directives() != null){
            for (GraphqlParser.DirectiveContext directiveContext : ctx.directives().directive()) {
                scalarTypeDefinition.getDirectives().add((Directive) visitDirective(directiveContext));
            }
        }
        return scalarTypeDefinition;
    }

    @Override
    public Node visitObjectTypeDefinition(GraphqlParser.ObjectTypeDefinitionContext ctx) {
        ObjectTypeDefinition objectTypeDefinition = new ObjectTypeDefinition();
        objectTypeDefinition.setName(ctx.name().getText());
        if(ctx.implementsInterfaces() != null){
            for (GraphqlParser.TypeNameContext typeNameContext : ctx.implementsInterfaces().typeName()) {
                objectTypeDefinition.getImplementsInterfaces().add(typeNameContext.name().getText());
            }
        }if(ctx.directives() != null){
            for (GraphqlParser.DirectiveContext directiveContext : ctx.directives().directive()) {
                objectTypeDefinition.getDirectives().add((Directive) visitDirective(directiveContext));
            }
        }
        for (GraphqlParser.FieldDefinitionContext fieldDefinitionContext : ctx.fieldDefinition()) {
            objectTypeDefinition.getFieldDefinition().add((FieldDefinition) visitFieldDefinition(fieldDefinitionContext));
        }
        return objectTypeDefinition;
    }

    @Override
    public Node visitImplementsInterfaces(GraphqlParser.ImplementsInterfacesContext ctx) {
        return null;
    }

    @Override
    public Node visitFieldDefinition(GraphqlParser.FieldDefinitionContext ctx) {
        FieldDefinition definition = new FieldDefinition();
        definition.setName(ctx.name().getText());
        if(ctx.argumentsDefinition()!=null){
            for (GraphqlParser.InputValueDefinitionContext inputValueDefinitionContext : ctx.argumentsDefinition().inputValueDefinition()) {
                definition.getArgumentsDefinition().add((InputValueDefinition) visitInputValueDefinition(inputValueDefinitionContext));
            }
        }

        definition.setType((Type) visitType(ctx.type()));
        if(ctx.directives() != null){
            for (GraphqlParser.DirectiveContext directiveContext : ctx.directives().directive()) {
                definition.getDirectives().add((Directive) visitDirective(directiveContext));
            }
        }
        return definition;
    }

    @Override
    public Node visitArgumentsDefinition(GraphqlParser.ArgumentsDefinitionContext ctx) {
        return null;
    }

    @Override
    public Node visitInputValueDefinition(GraphqlParser.InputValueDefinitionContext ctx) {
        InputValueDefinition valueDefinition = new InputValueDefinition();
        valueDefinition.setName(ctx.name().getText());
        valueDefinition.setType((Type) visitType(ctx.type()));
        if(ctx.defaultValue()!=null){
            valueDefinition.setDefaultValue((Value) visitValue(ctx.defaultValue().value()));
        }
        if(ctx.directives() != null){
            for (GraphqlParser.DirectiveContext directiveContext : ctx.directives().directive()) {
                valueDefinition.getDirectives().add((Directive) visitDirective(directiveContext));
            }
        }
        return valueDefinition;
    }

    @Override
    public Node visitInterfaceTypeDefinition(GraphqlParser.InterfaceTypeDefinitionContext ctx) {
       InterfaceTypeDefinition typeDefinition = new InterfaceTypeDefinition();
       typeDefinition.setName(ctx.name().getText());
       if(ctx.directives() != null){
           for (GraphqlParser.DirectiveContext directiveContext : ctx.directives().directive()) {
               typeDefinition.getDirectives().add((Directive) visitDirective(directiveContext));
           }
       }
        for (GraphqlParser.FieldDefinitionContext fieldDefinitionContext : ctx.fieldDefinition()) {
            typeDefinition.getDefinitions().add((FieldDefinition) visitFieldDefinition(fieldDefinitionContext));
        }
        return typeDefinition;
    }

    @Override
    public Node visitUnionTypeDefinition(GraphqlParser.UnionTypeDefinitionContext ctx) {
        UnionTypeDefinition typeDefinition = new UnionTypeDefinition();
        typeDefinition.setName(ctx.name().getText());
        if(ctx.directives()!= null){
            for (GraphqlParser.DirectiveContext directiveContext : ctx.directives().directive()) {
                typeDefinition.getDirectives().add((Directive) visitDirective(directiveContext));
            }
        }
        GraphqlParser.UnionMembersContext unionMembersContext = ctx.unionMembers();
        while(unionMembersContext!= null){
            if(unionMembersContext.typeName() != null){
                typeDefinition.getUnionMembers().add(new TypeName(unionMembersContext.typeName().getText()));
                unionMembersContext = unionMembersContext.unionMembers();
            }

        }
        return typeDefinition;
    }

    @Override
    public Node visitUnionMembers(GraphqlParser.UnionMembersContext ctx) {
        return null;
    }

    @Override
    public Node visitEnumTypeDefinition(GraphqlParser.EnumTypeDefinitionContext ctx) {
        EnumTypeDefinition typeDefinition = new EnumTypeDefinition();
        typeDefinition.setName(ctx.name().getText());
        if(ctx.directives() != null){
            for (GraphqlParser.DirectiveContext directiveContext : ctx.directives().directive()) {
                typeDefinition.getDirectives().add((Directive) visitDirective(directiveContext));
            }
        }
        for (GraphqlParser.EnumValueDefinitionContext enumValueDefinitionContext : ctx.enumValueDefinition()) {
            typeDefinition.getEnumValueDefinitions().add((EnumValueDefinition) visitEnumValueDefinition(enumValueDefinitionContext));
        }
        return typeDefinition;
    }

    @Override
    public Node visitEnumValueDefinition(GraphqlParser.EnumValueDefinitionContext ctx) {
        EnumValueDefinition enumValueDefinition = new EnumValueDefinition();
        enumValueDefinition.setEnumValue(ctx.enumValue().name().getText());
        if(ctx.directives() != null){
            for (GraphqlParser.DirectiveContext directiveContext : ctx.directives().directive()) {
                enumValueDefinition.getDirectives().add((Directive) visitDirective(directiveContext));
            }
        }
        return enumValueDefinition;
    }

    @Override
    public Node visitInputObjectTypeDefinition(GraphqlParser.InputObjectTypeDefinitionContext ctx) {
        InputObjectTypeDefinition typeDefinition = new InputObjectTypeDefinition();
        typeDefinition.setName(ctx.name().getText());
        if(ctx.directives() != null){
            for (GraphqlParser.DirectiveContext directiveContext : ctx.directives().directive()) {
                typeDefinition.getDirectives().add((Directive) visitDirective(directiveContext));
            }
        }
        for (GraphqlParser.InputValueDefinitionContext valueDefinitionContext : ctx.inputValueDefinition()) {
            typeDefinition.getInputValueDefinition().add((InputValueDefinition) visitInputValueDefinition(valueDefinitionContext));
        }
        return typeDefinition;
    }

    @Override
    public Node visitTypeExtensionDefinition(GraphqlParser.TypeExtensionDefinitionContext ctx) {
        TypeExtensionDefinition extensionDefinition = new TypeExtensionDefinition();
        extensionDefinition.setObjectTypeDefinition((ObjectTypeDefinition) visitObjectTypeDefinition(ctx.objectTypeDefinition()));
        return extensionDefinition;
    }

    @Override
    public Node visitDirectiveDefinition(GraphqlParser.DirectiveDefinitionContext ctx) {
        DirectiveDefinition directiveDefinition = new DirectiveDefinition();
        directiveDefinition.setName(ctx.name().getText());
        for (GraphqlParser.InputValueDefinitionContext inputValueDefinitionContext : ctx.argumentsDefinition().inputValueDefinition()) {
            directiveDefinition.getArgumentsDefinition().add((InputValueDefinition) visitInputValueDefinition(inputValueDefinitionContext));
        }
        GraphqlParser.DirectiveLocationsContext directiveLocationsContext = ctx.directiveLocations();
        while (directiveLocationsContext.directiveLocation() != null){
            directiveDefinition.getDirectiveLocations().add(directiveLocationsContext.directiveLocation().name().getText());
            directiveLocationsContext = directiveLocationsContext.directiveLocations();
        }
        return directiveDefinition;
    }

    @Override
    public Node visitDirectiveLocation(GraphqlParser.DirectiveLocationContext ctx) {
        return null;
    }

    @Override
    public Node visitDirectiveLocations(GraphqlParser.DirectiveLocationsContext ctx) {
        return null;
    }

    @Override
    public Node visit(ParseTree tree) {
        return null;
    }

    @Override
    public Node visitChildren(RuleNode node) {
        return null;
    }

    @Override
    public Node visitTerminal(TerminalNode node) {
        return null;
    }

    @Override
    public Node visitErrorNode(ErrorNode node) {
        return null;
    }
}
