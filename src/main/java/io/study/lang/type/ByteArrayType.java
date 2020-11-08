package io.study.lang.type;

import io.entropy.lang.IBytesObject;

public class ByteArrayType extends DefaultObjectType {
    private static final long serialVersionUID = -9127560638647712015L;

    ByteArrayType(Class type) {
        super(type);
    }

    protected Object doConvert(Object value) {
        return value instanceof IBytesObject ? ((IBytesObject) value).getBytes() : null;
    }
}