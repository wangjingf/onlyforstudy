package io.study.lang.type;



public class PrimitiveShortType extends DefaultObjectType {
    private static final long serialVersionUID = -9127560638647712015L;

    PrimitiveShortType(Class var1) {
        super(var1);
    }

    public Object getDefaultValue() {
        return this.isPrimitive() ? Short.valueOf((short)0) : null;
    }

    protected Object doConvert(Object value) {
        return ObjectTypes.convertToShort(value);
    }
}
