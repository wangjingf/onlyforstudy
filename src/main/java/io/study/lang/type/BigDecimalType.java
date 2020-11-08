package io.study.lang.type;



public class BigDecimalType extends DefaultObjectType {
    private static final long serialVersionUID = -9127560638647712015L;

    BigDecimalType(Class type) {
        super(type);
    }

    protected Object doConvert(Object value) {
        return ObjectTypes.convertToBigDecimal(value);
    }
}
