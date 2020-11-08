package io.study.lang.type;



public class NumberType extends DefaultObjectType {
    private static final long serialVersionUID = -9127560638647712015L;

    NumberType(Class type) {
        super(type);
    }

    protected Object doConvert(Object value) {
        return ObjectTypes.convertToDouble(value);
    }
}