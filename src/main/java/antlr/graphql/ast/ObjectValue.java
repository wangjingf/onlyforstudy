package antlr.graphql.ast;
import antlr.graphql.Node;

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
}
