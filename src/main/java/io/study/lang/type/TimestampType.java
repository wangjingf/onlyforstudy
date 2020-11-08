package io.study.lang.type;



public class TimestampType extends DefaultObjectType {
    private static final long serialVersionUID = -9127560638647712015L;

    TimestampType(Class type) {
        super(type);
    }

    protected Object doConvert(Object value) {
        return ObjectTypes.convertToTimestamp(value);
    }
}
