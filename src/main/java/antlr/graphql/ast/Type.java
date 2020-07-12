package antlr.graphql.ast;
import antlr.g4.GraphQLAstVisitor;
import antlr.graphql.Node;
import antlr.graphql.schema.entity.InputType;
import antlr.graphql.schema.entity.InterfaceType;
import antlr.graphql.schema.entity.ObjectType;
import antlr.graphql.schema.entity.UnionType;

public abstract class Type extends Node {

  protected String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public  boolean isListType(){
      return this instanceof ListType;
    }
    public boolean isNonNullType(){
      return this instanceof NonNullType;
    }
    public boolean hasChild(){
      if(this instanceof ObjectType || this instanceof InterfaceType || this instanceof InputType || this instanceof UnionType){
        return false;
      }else if(this instanceof NonNullType){
        return ((NonNullType)this).getType().hasChild();
      }else if(this instanceof ListType){
        return ((ListType)this).getType().hasChild();
      }else{
        return true;
      }
    }
    /**
     * 获取字段对应的原始字段类型
     * @return
     */
    public abstract String getPrimitiveTypeName();
    @Override
    protected void accept0(GraphQLAstVisitor visitor) {

    }

}
