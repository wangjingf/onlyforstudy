package io.study.lang.type;



public class ComponentArrayType extends AbstractClassType {
    private static final long serialVersionUID = 6627300789794057229L;
    IObjectType componentType;
    ComponentArrayType(Class type, IObjectType componentType) {
        super(type);
        this.componentType = componentType;
    }

    public IObjectType getComponentType() {
        return this.componentType;
    }

    protected Object doConvert(Object value) {
        return this.clazz.isAssignableFrom(value.getClass()) ? value : ObjectTypes.tryConvertToArray(value, this.clazz.getComponentType());
    }
}
