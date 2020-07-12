package antlr.graphql.ast;
import antlr.g4.GraphQLAstVisitor;

import java.util.ArrayList;
import java.util.List;

public class ObjectValue extends Value {
    List<ObjectField> fields = new ArrayList<>();

    public List<ObjectField> getFields() {
        return fields;
    }

    public void setFields(List<ObjectField> fields) {
        this.fields = fields;
    }

    @Override
    public void accept0(GraphQLAstVisitor visitor) {
        if(visitor.visit(this)){
            acceptChild(visitor,fields);
        }
        visitor.endVisit(this);
    }
}
