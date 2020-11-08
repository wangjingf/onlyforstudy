package io.study.lang.type;



public class BigIntegerType extends DefaultObjectType {
    private static final long serialVersionUID = -9127560638647712015L;

    BigIntegerType(Class type) {
        super(type);
    }

    protected Object doConvert(Object value) {
        return ObjectTypes.convertToBigInteger(value);
    }
}