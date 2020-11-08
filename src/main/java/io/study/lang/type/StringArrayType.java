package io.study.lang.type;



public class StringArrayType extends DefaultObjectType {
    private static final long serialVersionUID = -9127560638647712015L;

    StringArrayType(Class type) {
        super(type);
    }

    protected Object doConvert(Object value) {
        return value instanceof String[] ? (String[])((String[]) value) : ObjectTypes.tryConvertToArray(value, String.class);
    }
}