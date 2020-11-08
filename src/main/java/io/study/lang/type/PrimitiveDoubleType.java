package io.study.lang.type;



public class PrimitiveDoubleType extends DefaultObjectType {
    private static final long serialVersionUID = -9127560638647712015L;

    PrimitiveDoubleType(Class type) {
        super(type);
    }

    public Object getDefaultValue() {
        return 0.0D;
    }

    protected Object doConvert(Object value) {
        return ObjectTypes.convertToDouble(value);
    }
}