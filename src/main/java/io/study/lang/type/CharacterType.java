package io.study.lang.type;



public class CharacterType extends DefaultObjectType {
    private static final long serialVersionUID = -9127560638647712015L;

    CharacterType(Class type) {
        super(type);
    }

    protected Object doConvert(Object var1) {
        return ObjectTypes.convertToChar(var1);
    }
}
