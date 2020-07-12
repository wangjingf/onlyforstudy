package antlr.graphql.schema.validate;

import antlr.g4.GraphQLAstVisitor;
import antlr.graphql.schema.TypeDefinitions;
import antlr.graphql.TypeName;
import antlr.graphql.ast.*;
import antlr.graphql.schema.TypeRegistry;
import compiler.expr.SemanticsException;

public class KnownTypeValidator implements IGraphQLSchemaValidator{
    @Override
    public GraphQLAstVisitor getValidatorVisitor(Document document, TypeDefinitions typeDefinitions) {
        return new GraphQLAstVisitor(){
            @Override
            public boolean visit(ScalarTypeDefinition scalarTypeDefinition) {
                if(typeDefinitions.containsScalarType(scalarTypeDefinition.getName())){ //scalar定义了类型后需要能够获取
                    throw new SemanticsException("miss type definition:"+scalarTypeDefinition.getName());
                }
                return super.visit(scalarTypeDefinition);
            }
            @Override
            public boolean visit(TypeName type){
                if(!typeDefinitions.containsType(type.getName())){
                    throw new SemanticsException("miss type definition:"+type.getName());
                }
                return true;
            }
            @Override
            public boolean visit(ListType listType) {
                if(!typeDefinitions.containsType(listType.getType().getName())){
                    throw new SemanticsException("miss type definition:"+listType.getName());
                }
                return super.visit(listType);
            }

            @Override
            public boolean visit(NonNullType nonNullType) {
                if(!typeDefinitions.containsType(nonNullType.getType().getName())){
                    throw new SemanticsException("miss type definition:"+nonNullType.getName());
                }
                return super.visit(nonNullType);
            }
        };
    }


}
