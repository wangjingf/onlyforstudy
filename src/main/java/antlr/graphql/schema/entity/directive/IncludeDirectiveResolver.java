package antlr.graphql.schema.entity.directive;

import antlr.graphql.ast.Argument;
import antlr.graphql.ast.Directive;
import antlr.graphql.ast.Value;
import antlr.graphql.ast.VariableReference;
import compiler.expr.SemanticsException;

import java.util.Map;

public class IncludeDirectiveResolver implements IDirectiveResolver {
    @Override
    public boolean validate(Directive directive) {
        boolean hasIf = false;
        if( directive.getArguments().isEmpty() || directive.getArguments().size()>1){
            throw new SemanticsException("graph.err_include_directive_only_allow_if_argument");
        }
        for (Argument argument : directive.getArguments()) {
            if("if".equals(argument.getName())){
                hasIf = true;
            }
        }
        if(!hasIf){
            throw new SemanticsException("graphql.err_include_directive_miss_if").param("directive",directive.getName());
        }
        return false;
    }

    @Override
    public void execute(Directive directive, DirectiveContext context) {
        if(!keepField(directive,context.getVars())){
            context.getValues().remove(context.getFieldInfo().getName());
        }
    }


    public boolean keepField(Directive directive, Map<String, Object> args) {
        Argument argument = directive.getArguments().get(0);
        Value value = argument.getValue();
        if(value instanceof VariableReference){
            String name = ((VariableReference) value).getName();
            Object o = args.get(name);
            if(o == null){
                return false;
            }
            return o instanceof Boolean && (Boolean) o == true;
        }else{
            Object val = value.getValue();
            if(val == null || val instanceof Boolean){
                return false;
            }

            return (Boolean) val == true;
        }
    }


}
