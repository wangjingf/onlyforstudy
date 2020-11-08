package io.study.lang.type;




public class PrimitiveIntType extends DefaultObjectType {
    private static final long serialVersionUID = -9127560638647712015L;

    PrimitiveIntType(Class clazz) {
        super(clazz);
    }

    public Object getDefaultValue() {
        return this.isPrimitive() ? 0 : null;
    }

    protected Object doConvert(Object value) {
        return ObjectTypes.convertToInteger(value);
    }
}