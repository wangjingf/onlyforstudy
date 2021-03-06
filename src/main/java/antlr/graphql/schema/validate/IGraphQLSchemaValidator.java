package antlr.graphql.schema.validate;

import antlr.g4.GraphQLAstVisitor;
import antlr.graphql.schema.TypeDefinitions;
import antlr.graphql.ast.Document;
import antlr.graphql.schema.TypeRegistry;

public interface IGraphQLSchemaValidator {
    public GraphQLAstVisitor getValidatorVisitor(Document document, TypeDefinitions typeDefinitions);
}
