package io.study.lang.type;



public class FileType  extends DefaultObjectType {
    private static final long serialVersionUID = -9127560638647712015L;

    FileType(Class type) {
        super(type);
    }

    protected Object doConvert(Object value) {
        return ObjectTypes.covertToFile(value);
    }
}

