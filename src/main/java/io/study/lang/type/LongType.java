package io.study.lang.type;



public class LongType extends DefaultObjectType {
    private static final long serialVersionUID = -9127560638647712015L;

    LongType(Class clazz) {
        super(clazz);
    }

    protected Object doConvert(Object value) {
        return ObjectTypes.convertToLong(value);
    }
}