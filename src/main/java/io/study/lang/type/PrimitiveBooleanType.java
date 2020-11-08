package io.study.lang.type;



public class PrimitiveBooleanType extends DefaultObjectType {
    private static final long serialVersionUID = -9127560638647712015L;

    PrimitiveBooleanType(Class clazz) {
        super(clazz);
    }

    public Object getDefaultValue() {
        return this.isPrimitive() ? Boolean.FALSE : null;
    }

    protected Object doConvert(Object value) {
        return ObjectTypes.convertToBoolean(value, false, true);
    }
}
