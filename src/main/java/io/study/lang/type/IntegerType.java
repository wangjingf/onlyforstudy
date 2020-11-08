package io.study.lang.type;




public class IntegerType extends DefaultObjectType {
    private static final long serialVersionUID = -9127560638647712015L;

    IntegerType(Class clazz) {
        super(clazz);
    }

    protected Object doConvert(Object var1) {
        return ObjectTypes.convertToInteger(var1);
    }
}