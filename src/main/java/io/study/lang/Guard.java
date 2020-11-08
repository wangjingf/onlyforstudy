package io.study.lang;

import io.study.exception.StdException;
import io.study.helper.ObjectHelper;
import io.study.helper.Variant;

public class Guard {
    public Guard() {
    }

    public static void assertTrue(boolean var0) {
        assertTrue(var0, "lang.err_assert_fail");
    }

    public static void assertTrue(boolean var0, String var1, Object... var2) {
        if (!var0) {
            throw (new StdException(var1)).args(var2);
        }
    }

    public static void assertTrue(boolean var0, String var1) {
        if (!var0) {
            throw new StdException(var1);
        }
    }

    public static void assertEquals(Object var0, Object var1) {
        if (!Variant.equals(var0, var1)) {
            throw (new StdException("lang.err_value_not_equals")).param("expected", var0).param("actual", var1);
        }
    }

    public static <T> T notNull(T var0, String var1, Object... var2) {
        if (var0 == null) {
            throw (new StdException(var1)).param("value", var0).args(var2);
        } else {
            return var0;
        }
    }

    public static <T> T notNull(T var0, String var1) {
        if (var0 == null) {
            throw (new StdException(var1)).param("value", var0);
        } else {
            return var0;
        }
    }

    public static <T> T notEmpty(T var0, String var1, Object... var2) {
        if (ObjectHelper.isEmpty(var0)) {
            throw (new StdException(var1)).param("value", var0).args(var2);
        } else {
            return var0;
        }
    }

    public static <T> T notEmpty(T var0, String var1) {
        if (ObjectHelper.isEmpty(var0)) {
            throw (new StdException(var1)).param("value", var0);
        } else {
            return var0;
        }
    }

    public static int nonNegativeInt(int var0, String var1, Object... var2) {
        if (var0 < 0) {
            throw (new StdException(var1)).param("value", var0).args(var2);
        } else {
            return var0;
        }
    }

    public static long nonNegativeLong(long var0, String var2, Object... var3) {
        if (var0 < 0L) {
            throw (new StdException(var2)).param("value", var0).args(var3);
        } else {
            return var0;
        }
    }
}
