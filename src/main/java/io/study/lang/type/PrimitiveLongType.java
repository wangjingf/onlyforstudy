package io.study.lang.type;



public class PrimitiveLongType extends DefaultObjectType {
    private static final long serialVersionUID = -9127560638647712015L;

    PrimitiveLongType(Class clazz) {
        super(clazz);
    }

    public Object getDefaultValue() {
        return this.isPrimitive() ? 0L : null;
    }

    protected Object doConvert(Object value) {
        return ObjectTypes.convertToLong(value);
    }
}