package io.study.lang.type;


import java.io.Serializable;

public interface IObjectType extends Serializable {
    Class<?> getRawClassType();

    IObjectType getComponentType();

    String getClassName();

    String getClassSimpleName();

    String getCanonicalName();

    boolean isPrimitive();

    boolean isNumberType();

    boolean isArray();

    Object getDefaultValue();

    Object tryConvert(Object fromValue);
}
