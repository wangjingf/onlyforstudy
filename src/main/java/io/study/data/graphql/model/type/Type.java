package io.study.data.graphql.model.type;

public interface Type {
    /**
     * 校验值的有效性
     * @param value
     * @return
     */
    default boolean validate(Object value){
        return true;
    }
}
