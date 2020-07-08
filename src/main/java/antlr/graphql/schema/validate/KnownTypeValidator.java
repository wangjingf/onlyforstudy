package antlr.graphql.schema.validate;

import antlr.g4.GraphQLAstVisitor;
import antlr.graphql.TypeName;
import antlr.graphql.ast.Document;
import antlr.graphql.ast.ListType;
import antlr.graphql.ast.NonNullType;
import antlr.graphql.ast.ScalarTypeDefinition;
import antlr.graphql.schema.TypeRegistry;
import compiler.expr.SemanticsException;

public class KnownTypeValidator implements IGraphQLSchemaValidator{
    @Override
    public GraphQLAstVisitor getValidatorVisitor(Document document, TypeRegistry typeRegistry) {
        return new GraphQLAstVisitor(){
            @Override
            public boolean visit(ScalarTypeDefinition scalarTypeDefinition) {
                if(typeRegistry.containsScalarType(scalarTypeDefinition.getName())){ //scalar定义了类型后需要能够获取
                    throw new SemanticsException("miss type definition:"+scalarTypeDefinition.getName());
                }
                return super.visit(scalarTypeDefinition);
            }
            @Override
            public boolean visit(TypeName type){
                if(!typeRegistry.containsType(type.getName())){
                    throw new SemanticsException("miss type definition:"+type.getName());
                }
                return true;
            }
            @Override
            public boolean visit(ListType listType) {
                if(!typeRegistry.containsType(listType.getType().getName())){
                    throw new SemanticsException("miss type definition:"+listType.getName());
                }
                return super.visit(listType);
            }

            @Override
            public boolean visit(NonNullType nonNullType) {
                if(!typeRegistry.containsType(nonNullType.getType().getName())){
                    throw new SemanticsException("miss type definition:"+nonNullType.getName());
                }
                return super.visit(nonNullType);
            }
        };
    }
}
