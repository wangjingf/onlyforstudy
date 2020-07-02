package antlr.graphql.ast;
import antlr.graphql.Node;

public abstract class  Value<T> extends Node {
   public     T getValue(){return null;};
    public   void setValue(T value){};
}
