package io.study.lang.type;



public class PrimitiveFloatType extends DefaultObjectType{

    private static final long serialVersionUID = -9127560638647712015L;

    PrimitiveFloatType(Class type) {
        super(type);
    }

    public Object getDefaultValue() {
        return 0.0F;
    }

    protected Object doConvert(Object value) {
        return ObjectTypes.convertToFloat(value);
    }
}
