//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.study.lang.type;

import io.entropy.expr.IEvalScope;
import io.entropy.lang.annotation.Nonnull;

public interface IConverter {
    /**
     * 将value转换为type
     * @param value
     * @param type
     * @return
     */
    Object tryConvert(@Nonnull Object value, Class<?> type);

}
