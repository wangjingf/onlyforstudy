package io.study.lang.type;



public class CollectionType extends DefaultObjectType {
    private static final long serialVersionUID = -9127560638647712015L;

    CollectionType(Class var1) {
        super(var1);
    }

    protected Object doConvert(Object var1) {
        return ObjectTypes.tryConvertToCollection(var1);
    }
}