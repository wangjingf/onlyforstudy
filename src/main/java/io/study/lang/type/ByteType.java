package io.study.lang.type;



public class ByteType extends DefaultObjectType {
    private static final long serialVersionUID = -9127560638647712015L;

    ByteType(Class type) {
        super(type);
    }

    protected Object doConvert(Object value) {
        return ObjectTypes.convertToByte(value);
    }
}