package io.study.lang.type;



public class SqlDateType extends DefaultObjectType {
    private static final long serialVersionUID = -9127560638647712015L;

    SqlDateType(Class type) {
        super(type);
    }

    protected Object doConvert(Object var1) {
        return ObjectTypes.convertToSqlDate(var1);
    }
}
