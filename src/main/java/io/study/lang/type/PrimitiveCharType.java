package io.study.lang.type;



public class PrimitiveCharType  extends DefaultObjectType {
    private static final long serialVersionUID = -9127560638647712015L;

    PrimitiveCharType(Class type) {
        super(type);
    }

    public Object getDefaultValue() {
        return this.isPrimitive() ? '\u0000' : null;
    }

    protected Object doConvert(Object value) {
        return ObjectTypes.convertToChar(value);
    }
}
