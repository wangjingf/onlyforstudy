package io.study.helper;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

public class ObjectHelper {
    private static final int a = 7;
    private static final int b = 31;
    private static final String c = "";
    private static final String d = "null";
    private static final String e = "{";
    private static final String f = "}";
    private static final String g = "{}";
    private static final String h = ", ";

    public ObjectHelper() {
    }

    public static boolean isCheckedException(Throwable var0) {
        return !(var0 instanceof RuntimeException) && !(var0 instanceof Error);
    }

    public static boolean isCompatibleWithThrowsClause(Throwable var0, Class... var1) {
        if (!isCheckedException(var0)) {
            return true;
        } else {
            if (var1 != null) {
                Class[] var2 = var1;
                int var3 = var1.length;

                for(int var4 = 0; var4 < var3; ++var4) {
                    Class var5 = var2[var4];
                    if (var5.isInstance(var0)) {
                        return true;
                    }
                }
            }

            return false;
        }
    }

    public static boolean isArray(Object var0) {
        return var0 != null && var0.getClass().isArray();
    }

    public static boolean isEmpty(Object var0) {
        if (var0 == null) {
            return true;
        } else if (var0 instanceof CharSequence) {
            return ((CharSequence)var0).length() == 0;
        } else if (var0 instanceof Collection) {
            return ((Collection)var0).isEmpty();
        } else if (var0 instanceof Map) {
            return ((Map)var0).isEmpty();
        } else if (var0.getClass().isArray()) {
            return Array.getLength(var0) == 0;
        } else {
            return false;
        }
    }

    public static boolean containsElement(Object[] var0, Object var1) {
        if (var0 == null) {
            return false;
        } else {
            Object[] var2 = var0;
            int var3 = var0.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                Object var5 = var2[var4];
                if (nullSafeEquals(var5, var1)) {
                    return true;
                }
            }

            return false;
        }
    }

    public static boolean containsConstant(Enum<?>[] var0, String var1) {
        return containsConstant(var0, var1, false);
    }

    public static boolean containsConstant(Enum<?>[] var0, String var1, boolean var2) {
        Enum[] var3 = var0;
        int var4 = var0.length;
        int var5 = 0;

        while(true) {
            if (var5 >= var4) {
                return false;
            }

            Enum var6 = var3[var5];
            if (var2) {
                if (var6.toString().equals(var1)) {
                    break;
                }
            } else if (var6.toString().equalsIgnoreCase(var1)) {
                break;
            }

            ++var5;
        }

        return true;
    }

    public static <E extends Enum<?>> E caseInsensitiveValueOf(E[] var0, String var1) {
        Enum[] var2 = var0;
        int var3 = var0.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            E var5 = (E) var2[var4];
            if (var5.toString().equalsIgnoreCase(var1)) {
                return var5;
            }
        }

        throw new IllegalArgumentException(String.format("constant [%s] does not exist in enum type %s", var1, var0.getClass().getComponentType().getName()));
    }

    public static <A, O extends A> A[] addObjectToArray(A[] var0, O var1) {
        Class var2 = Object.class;
        if (var0 != null) {
            var2 = var0.getClass().getComponentType();
        } else if (var1 != null) {
            var2 = var1.getClass();
        }

        int var3 = var0 != null ? var0.length + 1 : 1;
        A[] var4 = (A[])((Object[])Array.newInstance(var2, var3));
        if (var0 != null) {
            System.arraycopy(var0, 0, var4, 0, var0.length);
        }

        var4[var4.length - 1] = var1;
        return var4;
    }

    public static Object[] toObjectArray(Object var0) {
        if (var0 instanceof Object[]) {
            return (Object[])((Object[])var0);
        } else if (var0 == null) {
            return new Object[0];
        } else if (!var0.getClass().isArray()) {
            throw new IllegalArgumentException("Source is not an array: " + var0);
        } else {
            int var1 = Array.getLength(var0);
            if (var1 == 0) {
                return new Object[0];
            } else {
                Class var2 = Array.get(var0, 0).getClass();
                Object[] var3 = (Object[])((Object[])Array.newInstance(var2, var1));

                for(int var4 = 0; var4 < var1; ++var4) {
                    var3[var4] = Array.get(var0, var4);
                }

                return var3;
            }
        }
    }

    public static boolean nullSafeEquals(Object var0, Object var1) {
        if (var0 == var1) {
            return true;
        } else if (var0 != null && var1 != null) {
            if (var0.equals(var1)) {
                return true;
            } else {
                return var0.getClass().isArray() && var1.getClass().isArray() ? a(var0, var1) : false;
            }
        } else {
            return false;
        }
    }

    private static boolean a(Object var0, Object var1) {
        if (var0 instanceof Object[] && var1 instanceof Object[]) {
            return Arrays.equals((Object[])((Object[])var0), (Object[])((Object[])var1));
        } else if (var0 instanceof boolean[] && var1 instanceof boolean[]) {
            return Arrays.equals((boolean[])((boolean[])var0), (boolean[])((boolean[])var1));
        } else if (var0 instanceof byte[] && var1 instanceof byte[]) {
            return Arrays.equals((byte[])((byte[])var0), (byte[])((byte[])var1));
        } else if (var0 instanceof char[] && var1 instanceof char[]) {
            return Arrays.equals((char[])((char[])var0), (char[])((char[])var1));
        } else if (var0 instanceof double[] && var1 instanceof double[]) {
            return Arrays.equals((double[])((double[])var0), (double[])((double[])var1));
        } else if (var0 instanceof float[] && var1 instanceof float[]) {
            return Arrays.equals((float[])((float[])var0), (float[])((float[])var1));
        } else if (var0 instanceof int[] && var1 instanceof int[]) {
            return Arrays.equals((int[])((int[])var0), (int[])((int[])var1));
        } else if (var0 instanceof long[] && var1 instanceof long[]) {
            return Arrays.equals((long[])((long[])var0), (long[])((long[])var1));
        } else {
            return var0 instanceof short[] && var1 instanceof short[] ? Arrays.equals((short[])((short[])var0), (short[])((short[])var1)) : false;
        }
    }

    public static int nullSafeHashCode(Object var0) {
        if (var0 == null) {
            return 0;
        } else {
            if (var0.getClass().isArray()) {
                if (var0 instanceof Object[]) {
                    return nullSafeHashCode((Object[])((Object[])var0));
                }

                if (var0 instanceof boolean[]) {
                    return nullSafeHashCode((boolean[])((boolean[])var0));
                }

                if (var0 instanceof byte[]) {
                    return nullSafeHashCode((byte[])((byte[])var0));
                }

                if (var0 instanceof char[]) {
                    return nullSafeHashCode((char[])((char[])var0));
                }

                if (var0 instanceof double[]) {
                    return nullSafeHashCode((double[])((double[])var0));
                }

                if (var0 instanceof float[]) {
                    return nullSafeHashCode((float[])((float[])var0));
                }

                if (var0 instanceof int[]) {
                    return nullSafeHashCode((int[])((int[])var0));
                }

                if (var0 instanceof long[]) {
                    return nullSafeHashCode((long[])((long[])var0));
                }

                if (var0 instanceof short[]) {
                    return nullSafeHashCode((short[])((short[])var0));
                }
            }

            return var0.hashCode();
        }
    }

    public static int nullSafeHashCode(Object[] var0) {
        if (var0 == null) {
            return 0;
        } else {
            int var1 = 7;
            Object[] var2 = var0;
            int var3 = var0.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                Object var5 = var2[var4];
                var1 = 31 * var1 + nullSafeHashCode(var5);
            }

            return var1;
        }
    }

    public static int nullSafeHashCode(boolean[] var0) {
        if (var0 == null) {
            return 0;
        } else {
            int var1 = 7;
            boolean[] var2 = var0;
            int var3 = var0.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                boolean var5 = var2[var4];
                var1 = 31 * var1 + hashCode(var5);
            }

            return var1;
        }
    }

    public static int nullSafeHashCode(byte[] var0) {
        if (var0 == null) {
            return 0;
        } else {
            int var1 = 7;
            byte[] var2 = var0;
            int var3 = var0.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                byte var5 = var2[var4];
                var1 = 31 * var1 + var5;
            }

            return var1;
        }
    }

    public static int nullSafeHashCode(char[] var0) {
        if (var0 == null) {
            return 0;
        } else {
            int var1 = 7;
            char[] var2 = var0;
            int var3 = var0.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                char var5 = var2[var4];
                var1 = 31 * var1 + var5;
            }

            return var1;
        }
    }

    public static int nullSafeHashCode(double[] var0) {
        if (var0 == null) {
            return 0;
        } else {
            int var1 = 7;
            double[] var2 = var0;
            int var3 = var0.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                double var5 = var2[var4];
                var1 = 31 * var1 + hashCode(var5);
            }

            return var1;
        }
    }

    public static int nullSafeHashCode(float[] var0) {
        if (var0 == null) {
            return 0;
        } else {
            int var1 = 7;
            float[] var2 = var0;
            int var3 = var0.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                float var5 = var2[var4];
                var1 = 31 * var1 + hashCode(var5);
            }

            return var1;
        }
    }

    public static int nullSafeHashCode(int[] var0) {
        if (var0 == null) {
            return 0;
        } else {
            int var1 = 7;
            int[] var2 = var0;
            int var3 = var0.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                int var5 = var2[var4];
                var1 = 31 * var1 + var5;
            }

            return var1;
        }
    }

    public static int nullSafeHashCode(long[] var0) {
        if (var0 == null) {
            return 0;
        } else {
            int var1 = 7;
            long[] var2 = var0;
            int var3 = var0.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                long var5 = var2[var4];
                var1 = 31 * var1 + hashCode(var5);
            }

            return var1;
        }
    }

    public static int nullSafeHashCode(short[] var0) {
        if (var0 == null) {
            return 0;
        } else {
            int var1 = 7;
            short[] var2 = var0;
            int var3 = var0.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                short var5 = var2[var4];
                var1 = 31 * var1 + var5;
            }

            return var1;
        }
    }

    public static int hashCode(boolean var0) {
        return var0 ? 1231 : 1237;
    }

    public static int hashCode(double var0) {
        return hashCode(Double.doubleToLongBits(var0));
    }

    public static int hashCode(float var0) {
        return Float.floatToIntBits(var0);
    }

    public static int hashCode(long var0) {
        return (int)(var0 ^ var0 >>> 32);
    }

    public static String identityToString(Object var0) {
        return var0 == null ? "" : var0.getClass().getName() + "@" + getIdentityHexString(var0);
    }

    public static String getIdentityHexString(Object var0) {
        return Integer.toHexString(System.identityHashCode(var0));
    }

    public static String getDisplayString(Object var0) {
        return var0 == null ? "" : nullSafeToString(var0);
    }

    public static String nullSafeClassName(Object var0) {
        return var0 != null ? var0.getClass().getName() : "null";
    }

    public static String nullSafeToString(Object var0) {
        if (var0 == null) {
            return "null";
        } else if (var0 instanceof String) {
            return (String)var0;
        } else if (var0 instanceof Object[]) {
            return nullSafeToString((Object[])((Object[])var0));
        } else if (var0 instanceof boolean[]) {
            return nullSafeToString((boolean[])((boolean[])var0));
        } else if (var0 instanceof byte[]) {
            return nullSafeToString((byte[])((byte[])var0));
        } else if (var0 instanceof char[]) {
            return nullSafeToString((char[])((char[])var0));
        } else if (var0 instanceof double[]) {
            return nullSafeToString((double[])((double[])var0));
        } else if (var0 instanceof float[]) {
            return nullSafeToString((float[])((float[])var0));
        } else if (var0 instanceof int[]) {
            return nullSafeToString((int[])((int[])var0));
        } else if (var0 instanceof long[]) {
            return nullSafeToString((long[])((long[])var0));
        } else if (var0 instanceof short[]) {
            return nullSafeToString((short[])((short[])var0));
        } else {
            String var1 = var0.toString();
            return var1 != null ? var1 : "";
        }
    }

    public static String nullSafeToString(Object[] var0) {
        if (var0 == null) {
            return "null";
        } else {
            int var1 = var0.length;
            if (var1 == 0) {
                return "{}";
            } else {
                StringBuilder var2 = new StringBuilder();

                for(int var3 = 0; var3 < var1; ++var3) {
                    if (var3 == 0) {
                        var2.append("{");
                    } else {
                        var2.append(", ");
                    }

                    var2.append(String.valueOf(var0[var3]));
                }

                var2.append("}");
                return var2.toString();
            }
        }
    }

    public static String nullSafeToString(boolean[] var0) {
        if (var0 == null) {
            return "null";
        } else {
            int var1 = var0.length;
            if (var1 == 0) {
                return "{}";
            } else {
                StringBuilder var2 = new StringBuilder();

                for(int var3 = 0; var3 < var1; ++var3) {
                    if (var3 == 0) {
                        var2.append("{");
                    } else {
                        var2.append(", ");
                    }

                    var2.append(var0[var3]);
                }

                var2.append("}");
                return var2.toString();
            }
        }
    }

    public static String nullSafeToString(byte[] var0) {
        if (var0 == null) {
            return "null";
        } else {
            int var1 = var0.length;
            if (var1 == 0) {
                return "{}";
            } else {
                StringBuilder var2 = new StringBuilder();

                for(int var3 = 0; var3 < var1; ++var3) {
                    if (var3 == 0) {
                        var2.append("{");
                    } else {
                        var2.append(", ");
                    }

                    var2.append(var0[var3]);
                }

                var2.append("}");
                return var2.toString();
            }
        }
    }

    public static String nullSafeToString(char[] var0) {
        if (var0 == null) {
            return "null";
        } else {
            int var1 = var0.length;
            if (var1 == 0) {
                return "{}";
            } else {
                StringBuilder var2 = new StringBuilder();

                for(int var3 = 0; var3 < var1; ++var3) {
                    if (var3 == 0) {
                        var2.append("{");
                    } else {
                        var2.append(", ");
                    }

                    var2.append("'").append(var0[var3]).append("'");
                }

                var2.append("}");
                return var2.toString();
            }
        }
    }

    public static String nullSafeToString(double[] var0) {
        if (var0 == null) {
            return "null";
        } else {
            int var1 = var0.length;
            if (var1 == 0) {
                return "{}";
            } else {
                StringBuilder var2 = new StringBuilder();

                for(int var3 = 0; var3 < var1; ++var3) {
                    if (var3 == 0) {
                        var2.append("{");
                    } else {
                        var2.append(", ");
                    }

                    var2.append(var0[var3]);
                }

                var2.append("}");
                return var2.toString();
            }
        }
    }

    public static String nullSafeToString(float[] var0) {
        if (var0 == null) {
            return "null";
        } else {
            int var1 = var0.length;
            if (var1 == 0) {
                return "{}";
            } else {
                StringBuilder var2 = new StringBuilder();

                for(int var3 = 0; var3 < var1; ++var3) {
                    if (var3 == 0) {
                        var2.append("{");
                    } else {
                        var2.append(", ");
                    }

                    var2.append(var0[var3]);
                }

                var2.append("}");
                return var2.toString();
            }
        }
    }

    public static String nullSafeToString(int[] var0) {
        if (var0 == null) {
            return "null";
        } else {
            int var1 = var0.length;
            if (var1 == 0) {
                return "{}";
            } else {
                StringBuilder var2 = new StringBuilder();

                for(int var3 = 0; var3 < var1; ++var3) {
                    if (var3 == 0) {
                        var2.append("{");
                    } else {
                        var2.append(", ");
                    }

                    var2.append(var0[var3]);
                }

                var2.append("}");
                return var2.toString();
            }
        }
    }

    public static String nullSafeToString(long[] var0) {
        if (var0 == null) {
            return "null";
        } else {
            int var1 = var0.length;
            if (var1 == 0) {
                return "{}";
            } else {
                StringBuilder var2 = new StringBuilder();

                for(int var3 = 0; var3 < var1; ++var3) {
                    if (var3 == 0) {
                        var2.append("{");
                    } else {
                        var2.append(", ");
                    }

                    var2.append(var0[var3]);
                }

                var2.append("}");
                return var2.toString();
            }
        }
    }

    public static String nullSafeToString(short[] var0) {
        if (var0 == null) {
            return "null";
        } else {
            int var1 = var0.length;
            if (var1 == 0) {
                return "{}";
            } else {
                StringBuilder var2 = new StringBuilder();

                for(int var3 = 0; var3 < var1; ++var3) {
                    if (var3 == 0) {
                        var2.append("{");
                    } else {
                        var2.append(", ");
                    }

                    var2.append(var0[var3]);
                }

                var2.append("}");
                return var2.toString();
            }
        }
    }
}
