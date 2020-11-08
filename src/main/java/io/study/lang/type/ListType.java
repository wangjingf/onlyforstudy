package io.study.lang.type;



public class ListType extends DefaultObjectType {
    private static final long serialVersionUID = -9127560638647712015L;

    ListType(Class type) {
        super(type);
    }

    protected Object doConvert(Object value) {
        return ObjectTypes.convertToList(value);
    }
}
