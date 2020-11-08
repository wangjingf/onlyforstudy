package io.study.lang.type;



public class DateType extends DefaultObjectType {
    private static final long serialVersionUID = -9127560638647712015L;

    DateType(Class type) {
        super(type);
    }

    protected Object doConvert(Object var1) {
        return ObjectTypes.convertToDate(var1);
    }
}