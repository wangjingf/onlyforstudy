package io.study.lang.type;



public class BooleanType extends DefaultObjectType {
    private static final long serialVersionUID = -9127560638647712015L;

    BooleanType(Class clazz) {
        super(clazz);
    }

    protected Object doConvert(Object value) {
        return ObjectTypes.convertToBoolean(value, false, true);
    }
}