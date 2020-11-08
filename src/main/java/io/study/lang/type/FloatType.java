package io.study.lang.type;



public class FloatType extends DefaultObjectType {
    private static final long serialVersionUID = -9127560638647712015L;

    FloatType(Class var1) {
        super(var1);
    }

    protected Object doConvert(Object var1) {
        return ObjectTypes.convertToFloat(var1);
    }
}
