//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.study.lang.type;



public interface IConverter {
    /**
     * 将value转换为type
     * @param value
     * @param type
     * @return
     */
    Object tryConvert( Object value, Class<?> type);

}
