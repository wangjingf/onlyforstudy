package io.study.lang.type;



public class DoubleType  extends DefaultObjectType {
    private static final long serialVersionUID = -9127560638647712015L;

    DoubleType(Class type) {
        super(type);
    }

    protected Object doConvert(Object value) {
        return ObjectTypes.convertToDouble(value);
    }
}