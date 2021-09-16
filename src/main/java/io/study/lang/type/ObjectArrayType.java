package io.study.lang.type;

/*import io.entropy.lang.ICollectionObject;*/

import java.util.Collection;

public class ObjectArrayType extends DefaultObjectType {
    private static final long serialVersionUID = -9127560638647712015L;

    ObjectArrayType(Class type) {
        super(type);
    }

    protected Object doConvert(Object value) {
        return null;
        /*if (value instanceof Object[]) {
            return (Object[])((Object[]) value);
        } else {
            if (value instanceof ICollectionObject) {
                value = ((ICollectionObject) value).toCollection();
            }

            return value instanceof Collection ? ((Collection) value).toArray() : null;
        }*/
    }
}
