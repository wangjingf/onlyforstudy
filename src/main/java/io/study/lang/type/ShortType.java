package io.study.lang.type;



public class ShortType extends DefaultObjectType {

    ShortType(Class type) {
        super(type);
    }

    protected Object doConvert(Object var1) {
        return ObjectTypes.convertToShort(var1);
    }
}
