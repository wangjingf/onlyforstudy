package io.study.lang.type;




public class StringType extends DefaultObjectType {
    private static final long serialVersionUID = -9127560638647712015L;

    StringType(Class type) {
        super(type);
    }

    protected Object doConvert(Object value) {
        return ObjectTypes.convertToString(value);
    }
}