package antlr.graphql.schema.entity.directive;

import antlr.graphql.ast.Directive;

import java.util.Map;

public class SkipDirectiveResolver extends IncludeDirectiveResolver {
    @Override
    public boolean keepField(Directive directive, Map<String, Object> args) {
        return !super.keepField(directive, args);
    }
}
