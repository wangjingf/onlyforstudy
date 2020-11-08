package io.study.lang.type;

public  class AbstractClassType implements IObjectType {
    private static final long serialVersionUID = 1018031103950630418L;
    final Class<?> clazz;

    public AbstractClassType(Class<?> type) {
        this.clazz = type;
    }

    public Class<?> getRawClassType() {
        return this.clazz;
    }

    public String getClassName() {
        return this.clazz.getName();
    }

    public String getClassSimpleName() {
        return this.clazz.getSimpleName();
    }

    public String getCanonicalName() {
        return this.clazz.getCanonicalName();
    }

    public Object getDefaultValue() {
        return null;
    }

    public boolean isPrimitive() {
        return this.clazz.isPrimitive();
    }

    public boolean isArray() {
        return this.clazz.isArray();
    }

    public IObjectType getComponentType() {
        Class componentType = this.clazz.getComponentType();
        return componentType == null ? null : ObjectTypes.makeObjectType(componentType);
    }

    public boolean isNumberType() {
        return Number.class.isAssignableFrom(this.clazz);
    }



    public final Object tryConvert(Object value) {
        return value.getClass() == this.clazz ? value : this.doConvert(value);
    }

    protected Object doConvert(Object value) {
        return this.clazz.isAssignableFrom(value.getClass()) ? value : null;
    }

    public int hashCode() {
        return this.clazz.hashCode();
    }

    public boolean equals(Object value) {
        if (this == value) {
            return true;
        } else if (!(value instanceof AbstractClassType)) {
            return false;
        } else {
            AbstractClassType type = (AbstractClassType) value;
            return this.clazz == type.clazz;
        }
    }
}
