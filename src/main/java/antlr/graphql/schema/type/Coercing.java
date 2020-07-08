package antlr.graphql.schema.type;

public interface Coercing {
    /**
     * 将值序列化
     * @param value
     * @return 序列化后的值
     */
    Object serialize(Object value);

    /**
     * 给定某个值，将值转换为目标值
     * @param value
     * @return
     */
    Object parseValue(Object value);

    /**
     * 将ast节点转换为值
     * @param astNode
     * @return
     */
    Object parseLiteral(Object astNode);
}
