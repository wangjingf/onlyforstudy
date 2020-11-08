package io.study.lang.type;


public class CharArrayType extends DefaultObjectType {
    private static final long serialVersionUID = -9127560638647712015L;

    CharArrayType(Class type) {
        super(type);
    }

    protected Object doConvert(Object value) {
        return value instanceof String ? ((String) value).toCharArray() : null;
    }
}
