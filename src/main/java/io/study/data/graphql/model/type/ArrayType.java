package io.study.data.graphql.model.type;

public class ArrayType implements Type {
    boolean required;
    Type itemType;
    boolean itemRequired;
}
