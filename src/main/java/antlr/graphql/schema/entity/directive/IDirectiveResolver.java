package antlr.graphql.schema.entity.directive;

import antlr.graphql.ast.Directive;

import java.util.Map;

public interface IDirectiveResolver {
    public boolean validate(Directive directive);
    public void execute(Directive directive,DirectiveContext context);
}
