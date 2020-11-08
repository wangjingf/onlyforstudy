package io.study.lang.type;



public class CollectionSetType extends DefaultObjectType {
    private static final long serialVersionUID = -9127560638647712015L;

    CollectionSetType(Class type) {
        super(type);
    }

    protected Object doConvert(Object value) {
        return ObjectTypes.convertToSet(value);
    }
}