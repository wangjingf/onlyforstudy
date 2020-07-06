package io.study.data.graphql.model.type;

import io.study.data.graphql.model.exception.TypeCaseException;

public interface ScaleType<T> extends Type {
    T cast(Object value) throws TypeCaseException;
}
