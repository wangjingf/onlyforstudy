package antlr.graphql.ast;
import antlr.g4.GraphQLAstVisitor;
import antlr.graphql.Node;
import antlr.graphql.schema.TypeDefinitions;
import io.study.exception.StdException;

import java.util.ArrayList;
import java.util.List;

public class SelectionSet extends Node {
     List<Selection> selections = new ArrayList<>();

    public List<Selection> getSelections() {
        return selections;
    }

    public void setSelections(List<Selection> selections) {
        this.selections = selections;
    }

    public List<Field> getAllFields(TypeDefinitions definitions){
        List<Field> list = new ArrayList<>();
        for (Selection selection : selections) {
            if(selection instanceof Field){
                list.add((Field) selection);
            }else if(selection instanceof FragmentSpread){
                list.addAll(((FragmentSpread) selection).getFields(definitions));
            }else{
                InlineFragment fragment = (InlineFragment) selection;
                list.addAll(fragment.getFields(definitions));
            }
        }
        return list;
    }

    @Override
    public void accept0(GraphQLAstVisitor visitor) {
        if(visitor.visit(this)){
            acceptChild(visitor,selections);
        }
        visitor.endVisit(this);
    }
}
