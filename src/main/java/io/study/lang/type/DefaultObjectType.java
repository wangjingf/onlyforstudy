package io.study.lang.type;

public class DefaultObjectType extends AbstractClassType {
    private static final long serialVersionUID = 1018031103950630418L;
    DefaultObjectType(){
        super(Object.class);
    }
    public DefaultObjectType(Class<?> type) {
        super(type);
    }

    protected Object readResolve() {
        int i = 0;

        for(int length = ObjectTypes.allTypes.length; i < length; ++i) {
            if (ObjectTypes.allTypes[i].getRawClassType() == this.clazz) {
                return ObjectTypes.allTypes[i];
            }
        }

        return this;
    }
}