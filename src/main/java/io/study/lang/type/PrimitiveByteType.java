package io.study.lang.type;



public class PrimitiveByteType extends DefaultObjectType {
    private static final long serialVersionUID = -9127560638647712015L;

    PrimitiveByteType(Class var1) {
        super(var1);
    }

    public Object getDefaultValue() {
        return this.isPrimitive() ? 0 : null;
    }

    protected Object doConvert(Object value) {
        return ObjectTypes.convertToByte(value);
    }
}
