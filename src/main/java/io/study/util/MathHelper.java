package io.study.util;

import io.entropy.config.AppConfig;
import io.entropy.exceptions.EntropyException;
import io.entropy.lang.Guard;
import io.entropy.lang.IRandom;
import io.entropy.lang.util.DefaultSecureRandom;
import io.entropy.lang.util.DefaultThreadLocalRandom;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;

public class MathHelper implements Serializable {
    private static final long serialVersionUID = -2676798062638468488L;
    public static final int DEFAULT_COMPARE_PRECISION = AppConfig.var("math.default_compare_precision").toInt(12);
    public static final Double DEFAULT_COMPARE_GAP;
    private static final int BOOLEAN = 0;
    private static final int BYTE = 1;
    private static final int CHAR = 2;
    private static final int SHORT = 3;
    private static final int INT = 4;
    private static final int LONG = 5;
    private static final int BIGINT = 6;
    private static final int FLOAT = 7;
    private static final int DOUBLE = 8;
    private static final int BIG_DECIMAL = 9;
    private static final int OBJECT = 10;
    public static final Double MAX_DOUBLE_VALUE;
    public static final Double MIN_DOUBLE_VALUE;
    public static final Long MAX_LONG_VALUE;
    public static final Long MIN_LONG_VALUE;
    public static final Integer MAX_INTEGER_VALUE;
    public static final Integer MIN_INTEGER_VALUE;
    public static final Double NaN;
    public static final Double PI;
    public static final Long ZERO_LONG;
    public static final Integer ZERO_INT;
    static IdentityHashMap<Class<?>, Number> primitiveDefaultValue;
    public static final int MIN_REAL_TYPE = 7;
    static IRandom random;
    static IRandom secureRandom;

    public MathHelper() {
    }

    public static Number getPrimitiveDefaultValue(Class<?> type) {
        return (Number) primitiveDefaultValue.get(type);
    }

    public static IRandom random() {
        return random;
    }


    public static void registerRandomImpl(IRandom impl) {
        random = impl;
    }

    public static IRandom secureRandom() {
        return secureRandom;
    }


    public static void registerSecureRandomImpl(IRandom secureRandom) {
        MathHelper.secureRandom = secureRandom;
    }



    public static int nextPowerOfTwo(int power) {
        if (power == 0) {
            return 1;
        } else {
            --power;
            power |= power >> 1;
            power |= power >> 2;
            power |= power >> 4;
            power |= power >> 8;
            power |= power >> 16;
            return power + 1;
        }
    }

    public static long nextPowerOfTwoForLong(long power) {
        return 1L << 64 - Long.numberOfLeadingZeros(power - 1L);
    }

    public static boolean isPowerOfTwoForLong(long power) {
        return (power & power - 1L) == 0L;
    }

    public static int modPowerOfTwo(int val1, int val2) {
        return val1 & val2 - 1;
    }

    public static long modPowerOfTwoForLong(long value1, int value2) {
        return value1 & (long)(value2 - 1);
    }

    /**
     * 类型转换后比较2个数的大小
     * @param value1
     * @param value2
     * @return
     */
    public static int compareWithConversion(Object value1, Object value2) {
        return compareWithConversion(value1, value2, (Double)null);
    }

    public static int compareWithConversion(Object value1, Object value2, Double var2) {
        int result;
        if (value1 == value2) {
            result = 0;
        } else {
            int type1 = getNumericType(value1);
            int type2 = getNumericType(value2);
            int conversionType = conversionType(type1, type2, true);
            switch(conversionType) {
                case BIGINT:
                    result = bigIntValue(value1).compareTo(bigIntValue(value2));
                    break;
                case BIG_DECIMAL:
                    if (var2 == null) {
                        result = bigDecValue(value1).compareTo(bigDecValue(value2));
                        break;
                    }
                case FLOAT:
                case DOUBLE:
                    double val1 = doubleValue(value1);
                    double val2 = doubleValue(value2);
                    if (var2 != null) {
                        boolean var11 = val1 < val2 + var2;
                        if (var11) {
                            boolean var12 = val1 + var2 > val2;
                            return var12 ? 0 : -1;
                        }

                        return 1;
                    }

                    return val1 == val2 ? 0 : (val1 < val2 ? -1 : 1);
                case OBJECT:
                    if (value1 != null && value2 != null) {
                        if (value1.getClass() == value2.getClass() && value1 instanceof Comparable) {
                            result = ((Comparable)value1).compareTo(value2);
                            break;
                        }

                        throw (new EntropyException("utils.err_compareWithConversion_fail")).param("v1", value1).param("v2", value2);
                    }

                    return value1 == value2 ? 0 : 1;
                default:
                    long var15 = longValue(value1);
                    long var13 = longValue(value2);
                    return var15 == var13 ? 0 : (var15 < var13 ? -1 : 1);
            }
        }

        return result;
    }


    public static long longValue(Object value) throws NumberFormatException {
        if (value == null) {
            return 0L;
        } else if (value instanceof Number) {
            return ((Number)value).longValue();
        } else {
            Class var1 = value.getClass();
            if (var1 == Boolean.class) {
                return (Boolean)value ? 1L : 0L;
            } else {
                return var1 == Character.class ? (long)(Character)value : Long.parseLong(stringValue(value, true));
            }
        }
    }

    public static double doubleValue(Object value) throws NumberFormatException {
        if (value == null) {
            return 0.0D;
        } else if (value instanceof Number) {
            return ((Number)value).doubleValue();
        } else {
            Class var1 = value.getClass();
            if (var1 == Boolean.class) {
                return (Boolean)value ? 1.0D : 0.0D;
            } else if (var1 == Character.class) {
                return (double)(Character)value;
            } else {
                String var2 = stringValue(value, true);
                return var2.length() == 0 ? 0.0D : Double.parseDouble(var2);
            }
        }
    }

    public static BigInteger bigIntValue(Object value) throws NumberFormatException {
        if (value == null) {
            return BigInteger.valueOf(0L);
        } else {
            Class valueType = value.getClass();
            if (valueType == BigInteger.class) {
                return (BigInteger)value;
            } else if (valueType == BigDecimal.class) {
                return ((BigDecimal)value).toBigInteger();
            } else if (value instanceof Number) {
                return BigInteger.valueOf(((Number)value).longValue());
            } else if (valueType == Boolean.class) {
                return BigInteger.valueOf((Boolean)value ? 1L : 0L);
            } else {
                return valueType == Character.class ? BigInteger.valueOf((long)(Character)value) : new BigInteger(stringValue(value, true));
            }
        }
    }

    public static BigDecimal bigDecValue(Object value) throws NumberFormatException {
        if (value == null) {
            return null;
        } else {
            Class valueType = value.getClass();
            if (valueType == BigDecimal.class) {
                return (BigDecimal)value;
            } else if (valueType == BigInteger.class) {
                return new BigDecimal((BigInteger)value);
            } else if (value instanceof Number) {
                return new BigDecimal(value.toString());
            } else if (valueType == Boolean.class) {
                return BigDecimal.valueOf((Boolean)value ? 1L : 0L);
            } else {
                return valueType == Character.class ? BigDecimal.valueOf((long)(Character)value) : new BigDecimal(stringValue(value, true));
            }
        }
    }

    public static String stringValue(Object value, boolean trim) {
        String strValue;
        if (value == null) {
            strValue = "";
        } else {
            strValue = value.toString();
            if (trim) {
                strValue = strValue.trim();
            }
        }

        return strValue;
    }

    public static String stringValue(Object value) {
        return stringValue(value, false);
    }

    public static int getNumericType(Object value) {
        if (value != null) {
            Class type = value.getClass();
            if (type == Integer.class) {
                return INT;
            }

            if (type == Double.class) {
                return DOUBLE;
            }

            if (type == Boolean.class) {
                return BOOLEAN;
            }

            if (type == Byte.class) {
                return BYTE;
            }

            if (type == Character.class) {
                return CHAR;
            }

            if (type == Short.class) {
                return SHORT;
            }

            if (type == Long.class) {
                return LONG;
            }

            if (type == Float.class) {
                return FLOAT;
            }

            if (type == BigInteger.class) {
                return BIGINT;
            }

            if (type == BigDecimal.class) {
                return BIG_DECIMAL;
            }


        }

        return OBJECT;
    }

    public static int getNumericType(Object value1, Object value2) {
        return getNumericType(value1, value2, false);
    }

    /**
     * 转换type1与type2，返回优先级高的类型
     * @param type1
     * @param type2
     * @param asObject
     * @return
     */
    private static int conversionType(int type1, int type2, boolean asObject) {
        if (type1 == type2) {
            return type1;
        } else if (asObject && (type1 == OBJECT || type2 == OBJECT || type1 == CHAR || type2 == CHAR)) {
            return OBJECT;
        } else {
            if (type1 == OBJECT) {
                type1 = DOUBLE;
            }

            if (type2 == OBJECT) {
                type2 = DOUBLE;
            }

            if (type1 >= FLOAT) {
                if (type2 >= FLOAT) {
                    return Math.max(type1, type2);
                } else if (type2 < INT) {
                    return type1;
                } else {
                    return type2 == BIGINT ? BIG_DECIMAL : Math.max(DOUBLE, type1);
                }
            } else if (type2 >= FLOAT) {
                if (type1 < INT) {
                    return type2;
                } else {
                    return type1 == BIGINT ? BIG_DECIMAL : Math.max(DOUBLE, type2);
                }
            } else {
                return Math.max(type1, type2);
            }
        }
    }

    public static int getNumericType(Object value1, Object value2, boolean var2) {
        return conversionType(getNumericType(value1), getNumericType(value2), var2);
    }

    public static Number newInteger(int type, long value) {
        switch(type) {
            case BOOLEAN:
            case CHAR:
            case INT:
                return (int)value;
            case BYTE:
                return (byte)((int)value);
            case SHORT:
                return (short)((int)value);
            case BIGINT:
            default:
                return BigInteger.valueOf(value);
            case FLOAT:
                if ((long)((float)value) == value) {
                    return (float)value;
                }
            case DOUBLE:
                if ((long)((double)value) == value) {
                    return (double)value;
                }
            case LONG:
                return value;
        }
    }

    /**
     * 计算value的真值，若type为float,将double转换为float
     * @param type
     * @param value
     * @return
     */
    public static Number newReal(int type, double value) {
        return (Number)(type == FLOAT ? (float)value : value);
    }

    public static Number bor(Object val1, Object val2) {
        if (val1 != null && val2 != null) {
            int var2 = getNumericType(val1, val2);
            return (Number)(var2 != BIGINT && var2 != BIG_DECIMAL ? newInteger(var2, longValue(val1) | longValue(val2)) : bigIntValue(val1).or(bigIntValue(val2)));
        } else {
            return null;
        }
    }

    /**
     * 异或运算
     * @param value
     * @param value2
     * @return
     */
    public static Number bxor(Object value, Object value2) {
        if (value != null && value2 != null) {
            int type = getNumericType(value, value2);
            return (Number)(type != BIGINT && type != BIG_DECIMAL
                    ? newInteger(type, longValue(value) ^ longValue(value2))
                    : bigIntValue(value).xor(bigIntValue(value2)));
        } else {
            return null;
        }
    }

    public static Number band(Object value1, Object valu2) {
        if (value1 != null && valu2 != null) {
            int var2 = getNumericType(value1, valu2);
            return (Number)(var2 != BIGINT && var2 != BIG_DECIMAL ? newInteger(var2, longValue(value1) & longValue(valu2)) : bigIntValue(value1).and(bigIntValue(valu2)));
        } else {
            return null;
        }
    }

    public static boolean eqEx(Object value1, Object value2) {
        return eq(value1, value2, (Double)null);
    }

    public static boolean lt(Object value1, Object value2) {
        if (value1 != null && value2 != null) {
            return compareWithConversion(value1, value2) < 0;
        } else {
            return false;
        }
    }

    public static boolean gt(Object var0, Object var1) {
        if (var0 != null && var1 != null) {
            return compareWithConversion(var0, var1) > 0;
        } else {
            return false;
        }
    }

    public static boolean ge(Object var0, Object var1) {
        if (var0 != null && var1 != null) {
            return compareWithConversion(var0, var1) >= 0;
        } else {
            return false;
        }
    }

    public static boolean le(Object var0, Object var1) {
        if (var0 != null && var1 != null) {
            return compareWithConversion(var0, var1) <= 0;
        } else {
            return false;
        }
    }

    public static boolean gt(Object var0, Object var1, Double var2) {
        if (var0 != null && var1 != null) {
            return compareWithConversion(var0, var1, var2) > 0;
        } else {
            return false;
        }
    }

    public static boolean ge(Object var0, Object var1, Double var2) {
        if (var0 != null && var1 != null) {
            return compareWithConversion(var0, var1, var2) >= 0;
        } else {
            return false;
        }
    }

    public static boolean le(Object var0, Object var1, Double var2) {
        if (var0 != null && var1 != null) {
            return compareWithConversion(var0, var1, var2) <= 0;
        } else {
            return false;
        }
    }

    public static boolean lt(Object var0, Object var1, Double var2) {
        if (var0 != null && var1 != null) {
            return compareWithConversion(var0, var1, var2) < 0;
        } else {
            return false;
        }
    }

    public static boolean eq(Object var0, Object var1, Double var2) {
        if (var0 == var1) {
            return true;
        } else if (var0 != null && var1 != null) {
            if (var0.equals(var1)) {
                return true;
            } else {
                int var3 = getNumericType(var0);
                int var4 = getNumericType(var1);
                int var5 = conversionType(var3, var4, true);
                switch(var5) {
                    case BIGINT:
                        return bigIntValue(var0).equals(bigIntValue(var1));
                    case FLOAT:
                    case DOUBLE:
                        double var6 = doubleValue(var0);
                        double var8 = doubleValue(var1);
                        if (var2 != null) {
                            boolean var10 = var6 < var8 + var2;
                            if (var10) {
                                boolean var11 = var6 + var2 > var8;
                                return var11;
                            }

                            return false;
                        }

                        return var6 == var8;
                    case BIG_DECIMAL:
                    default:
                        long var14 = longValue(var0);
                        long var12 = longValue(var1);
                        return var14 == var12;
                    case OBJECT:
                        return var0.equals(var1);
                }
            }
        } else {
            return false;
        }
    }

    public static Object max(Object var0, Object var1) {
        if (var0 == null) {
            return var0;
        } else if (var1 == null) {
            return var0;
        } else {
            return compareWithConversion(var0, var1) < 0 ? var1 : var0;
        }
    }

    public static Object min(Object var0, Object var1) {
        if (var0 == null) {
            return null;
        } else if (var1 == null) {
            return null;
        } else {
            return compareWithConversion(var0, var1) > 0 ? var1 : var0;
        }
    }

    public static Number sl(Object var0, Object var1) {
        if (var0 == null) {
            return null;
        } else {
            int var2 = getNumericType(var0);
            return (Number)(var2 != BIGINT && var2 != BIG_DECIMAL ? newInteger(var2, longValue(var0) << (int)longValue(var1)) : bigIntValue(var0).shiftLeft((int)longValue(var1)));
        }
    }

    public static Number sr(Object var0, Object var1) {
        if (var0 == null) {
            return null;
        } else {
            int var2 = getNumericType(var0);
            return (Number)(var2 != BIGINT && var2 != BIG_DECIMAL ? newInteger(var2, longValue(var0) >> (int)longValue(var1)) : bigIntValue(var0).shiftRight((int)longValue(var1)));
        }
    }

    public static Number usr(Object var0, Object var1) {
        if (var0 == null) {
            return null;
        } else {
            int type = getNumericType(var0);
            if (type != BIGINT && type != BIG_DECIMAL) {
                return type <= INT ? newInteger(INT, (long)((int)longValue(var0) >>> (int)longValue(var1))) : newInteger(type, longValue(var0) >>> (int)longValue(var1));
            } else {
                return bigIntValue(var0).shiftRight((int)longValue(var1));
            }
        }
    }

    public static Number add(Object var0, Object var1) {
        if (var0 != null && var1 != null) {
            int var2 = getNumericType(var0, var1, false);
            switch(var2) {
                case BIGINT:
                    return bigIntValue(var0).add(bigIntValue(var1));
                case FLOAT:
                case DOUBLE:
                    return doubleValue(var0) + doubleValue(var1);
                case BIG_DECIMAL:
                    return bigDecValue(var0).add(bigDecValue(var1));
                case OBJECT:
                    return null;
                default:
                    return newInteger(var2, longValue(var0) + longValue(var1));
            }
        } else {
            return null;
        }
    }

    public static Number minus(Object value1, Object value2) {
        if (value1 != null && value2 != null) {
            int var2 = getNumericType(value1, value2);
            switch(var2) {
                case BIGINT:
                    return bigIntValue(value1).subtract(bigIntValue(value2));
                case FLOAT:
                case DOUBLE:
                    return doubleValue(value1) - doubleValue(value2);
                case BIG_DECIMAL:
                    return bigDecValue(value1).subtract(bigDecValue(value2));
                default:
                    return newInteger(var2, longValue(value1) - longValue(value2));
            }
        } else {
            return null;
        }
    }

    public static Number multiply(Object value1, Object value2) {
        if (value1 != null && value2 != null) {
            int type = getNumericType(value1, value2);
            switch(type) {
                case BIGINT:
                    return bigIntValue(value1).multiply(bigIntValue(value2));
                case FLOAT:
                case DOUBLE:
                    return doubleValue(value1) * doubleValue(value2);
                case BIG_DECIMAL:
                    return bigDecValue(value1).multiply(bigDecValue(value2));
                default:
                    return newInteger(type, longValue(value1) * longValue(value2));
            }
        } else {
            return null;
        }
    }

    public static Number divide(Object value1, Object value2) {
        if (value1 != null && value2 != null) {
            int type = getNumericType(value1, value2);
            double value = doubleValue(value2);
            if (Math.abs(value) <= 1.0E-10D) {
                return null;
            } else {
                double var5 = doubleValue(value1) / value;
                if (type <= LONG) {
                    long var7 = (long)var5;
                    if (Math.abs(var5 - (double)var7) <= 1.0E-10D) {
                        return newInteger(type, var7);
                    }
                }

                return newReal(type, var5);
            }
        } else {
            return null;
        }
    }

    public static Number divide_int(Object value1, Object value2) {
        if (value1 != null && value2 != null) {
            int var2 = getNumericType(value1, value2);
            switch(var2) {
                case BIGINT:
                    BigInteger var3 = bigIntValue(value2);
                    if (var3.longValue() == 0L) {
                        return null;
                    }

                    return bigIntValue(value1).divide(var3);
                case FLOAT:
                case DOUBLE:
                case BIG_DECIMAL:
                    double var4 = doubleValue(value2);
                    if (Math.abs(var4) <= 1.0E-10D) {
                        return null;
                    }

                    return newReal(var2, doubleValue(value1) / var4);
                default:
                    long var6 = longValue(value2);
                    return var6 == 0L ? null : newInteger(var2, longValue(value1) / var6);
            }
        } else {
            return null;
        }
    }

    public static Number mod(Object value1, Object value2) {
        if (value1 != null && value2 != null) {
            int var2 = getNumericType(value1, value2);
            switch(var2) {
                case BIGINT:
                case BIG_DECIMAL:
                    return bigIntValue(value1).remainder(bigIntValue(value2));
                default:
                    return newInteger(var2, longValue(value1) % longValue(value2));
            }
        } else {
            return null;
        }
    }

    public static Number ceil(Object value) {
        return value == null ? null : Math.ceil(doubleValue(value));
    }

    public static Number floor(Object value) {
        return value == null ? null : Math.floor(doubleValue(value));
    }

    public static Number neg(Object value) {
        if (value == null) {
            return null;
        } else {
            int type = getNumericType(value);
            switch(type) {
                case BIGINT:
                    return bigIntValue(value).negate();
                case FLOAT:
                case DOUBLE:
                    return newReal(type, -doubleValue(value));
                case BIG_DECIMAL:
                    return bigDecValue(value).negate();
                default:
                    return newInteger(type, -longValue(value));
            }
        }
    }

    public static Number bneg(Object value) {
        if (value == null) {
            return null;
        } else {
            int type = getNumericType(value);
            switch(type) {
                case BIGINT:
                case BIG_DECIMAL:
                    return bigIntValue(value).not();
                default:
                    return newInteger(type, ~longValue(value));
            }
        }
    }

    public static Number abs(Object value) {
        if (value == null) {
            return null;
        } else {
            int type = getNumericType(value);
            switch(type) {
                case INT:
                case LONG:
                    return newInteger(type, Math.abs(longValue(value)));
                case BIGINT:
                    return bigIntValue(value).abs();
                case FLOAT:
                case DOUBLE:
                    return Math.abs(doubleValue(value));
                case BIG_DECIMAL:
                    return bigDecValue(value).abs();
                default:
                    return newReal(type, Math.abs(doubleValue(value)));
            }
        }
    }

    public static Number sqrt(Object value) {
        return value == null ? null : Math.sqrt(doubleValue(value));
    }

    public static Number pow(Object a, Object b) {
        return a == null ? null : Math.pow(doubleValue(a), doubleValue(b));
    }

    public static Number sin(Object value) {
        return value == null ? null : Math.sin(doubleValue(value));
    }

    public static Number cos(Object value) {
        return value == null ? null : Math.cos(doubleValue(value));
    }

    public static Number square(Object value) {
        if (value == null) {
            return null;
        } else {
            int var1 = getNumericType(value);
            double var4;
            switch(var1) {
                case INT:
                case LONG:
                    long var6 = longValue(value);
                    return var6 * var6;
                case BIGINT:
                    BigInteger var5 = bigIntValue(value);
                    return var5.multiply(var5);
                case FLOAT:
                case DOUBLE:
                    var4 = doubleValue(value);
                    return var4 * var4;
                case BIG_DECIMAL:
                    BigDecimal var2 = bigDecValue(value);
                    return var2.multiply(var2);
                default:
                    var4 = doubleValue(value);
                    return var4 * var4;
            }
        }
    }

    public static Number exp(Object value) {
        return value == null ? null : Math.exp(doubleValue(value));
    }

    public static Number log(Object value) {
        return value == null ? null : Math.log(doubleValue(value));
    }

    public static Number log10(Object value) {
        return value == null ? null : Math.log10(doubleValue(value));
    }

    public static int gcd(int value1, int value2) {
        return value1 != 0 && value2 != 0 ? gcd(value2, value1 % value2) : value1 + value2;
    }

    public static int gcd(int[] values) {
        int i;
        for(i = 0; i < values.length - 1; ++i) {
            values[i + 1] = gcd(values[i], values[i + 1]);
        }

        return gcd(values[i], values[i - 1]);
    }

    public static Number roundHalfEven(Object value, int scale) {
        if (value == null) {
            return null;
        } else {
            BigDecimal var2 = bigDecValue(value);
            return var2.setScale(scale, RoundingMode.HALF_EVEN);
        }
    }

    public static int checkedCastLongToInt(long value) {
        int intVal = (int)value;
        Guard.assertTrue((long)intVal == value, "math.err_int_value_is_out_of_range:value={}", new Object[]{value});
        return intVal;
    }

    public static Number roundHalfUp(Object value, int scale) {
        if (value == null) {
            return null;
        } else {
            BigDecimal decimalValue = bigDecValue(value);
            return decimalValue.setScale(scale, RoundingMode.HALF_UP);
        }
    }

    public static Number roundDown(Object value, int scale) {
        if (value == null) {
            return null;
        } else {
            BigDecimal decimalVal = bigDecValue(value);
            return decimalVal.setScale(scale, RoundingMode.HALF_DOWN);
        }
    }

    public static int nonNegativeMod(int value, int mod) {
        int ret = value % mod;
        if (ret < 0) {
            ret += mod;
        }

        return ret;
    }

    public static int compareBoolean(boolean value1, boolean value2) {
        return value1 == value2 ? 0 : (value1 ? 1 : -1);
    }

    public static int compareInt(int value1, int value2) {
        return value1 < value2 ? -1 : (value1 == value2 ? 0 : 1);
    }

    public static int compareLong(long value1, long value2) {
        return value1 < value2 ? -1 : (value1 == value2 ? 0 : 1);
    }

    public static int compareUnsignedLong(long value1, long value2) {
        return compareLong(value1 + -9223372036854775808L, value2 + -9223372036854775808L);
    }

    public static int log2Int(int value) {
        return 31 - Integer.numberOfLeadingZeros(value);
    }

    public static int log2Long(long value) {
        return 63 - Long.numberOfLeadingZeros(value);
    }

    public static int divideByAndCeilToInt(double value, int value2) {
        return (int)Math.ceil(value / (double)value2);
    }

    public static long divideByAndCeilToLong(double value1, int value2) {
        return (long)Math.ceil(value1 / (double)value2);
    }

    public static int divideByAndRoundToInt(double val1, int val2) {
        return (int)Math.rint(val1 / (double)val2);
    }

    public static long divideByAndRoundToLong(double val1, int val2) {
        return (long)Math.rint(val1 / (double)val2);
    }

    public static int normalizeInt(int val1, int val2) {
        return divideByAndCeilToInt((double)val1, val2) * val2;
    }

    public static long normalizeLong(long val1, int val2) {
        return divideByAndCeilToLong((double)val1, val2) * (long)val2;
    }

    public static int digitsOfLong(long value) {
        if (value < 10000L) {
            if (value < 100L) {
                return value < 10L ? 1 : 2;
            } else {
                return value < 1000L ? 3 : 4;
            }
        } else if (value < 1000000000000L) {
            if (value < 100000000L) {
                if (value < 1000000L) {
                    return value < 100000L ? 5 : 6;
                } else {
                    return value < 10000000L ? 7 : 8;
                }
            } else if (value < 10000000000L) {
                return value < 1000000000L ? 9 : 10;
            } else {
                return value < 100000000000L ? 11 : 12;
            }
        } else if (value < 10000000000000000L) {
            if (value < 100000000000000L) {
                return value < 10000000000000L ? 13 : 14;
            } else {
                return value < 1000000000000000L ? 15 : 16;
            }
        } else if (value < 1000000000000000000L) {
            return value < 100000000000000000L ? 17 : 18;
        } else {
            return 19;
        }
    }

    public static int digitsOfInt(int value) {
        if (value < 100000) {
            if (value < 100) {
                return value < 10 ? 1 : 2;
            } else if (value < 1000) {
                return 3;
            } else {
                return value < 10000 ? 4 : 5;
            }
        } else if (value < 10000000) {
            return value < 1000000 ? 6 : 7;
        } else if (value < 100000000) {
            return 8;
        } else {
            return value < 1000000000 ? 9 : 10;
        }
    }

    static {
        DEFAULT_COMPARE_GAP = Math.pow(10.0D, (double)(-DEFAULT_COMPARE_PRECISION));
        MAX_DOUBLE_VALUE = Double.MAX_VALUE;;
        MIN_DOUBLE_VALUE = Double.MIN_VALUE;
        MAX_LONG_VALUE = Long.MAX_VALUE;
        MIN_LONG_VALUE = -9223372036854775808L;
        MAX_INTEGER_VALUE = 2147483647;
        MIN_INTEGER_VALUE = -2147483648;
        NaN = 0.0D / 0.0;
        PI = 3.141592653589793D;
        ZERO_LONG = 0L;
        ZERO_INT = 0;
        primitiveDefaultValue = new IdentityHashMap();
        primitiveDefaultValue.put(Byte.TYPE, 0);
        primitiveDefaultValue.put(Short.TYPE, Short.valueOf((short)0));
        primitiveDefaultValue.put(Integer.TYPE, 0);
        primitiveDefaultValue.put(Long.TYPE, 0L);
        primitiveDefaultValue.put(Float.TYPE, 0.0F);
        primitiveDefaultValue.put(Double.TYPE, 0.0D);
        primitiveDefaultValue.put(BigInteger.class, BigInteger.valueOf(0L));
        primitiveDefaultValue.put(BigDecimal.class, BigDecimal.valueOf(0.0D));
        primitiveDefaultValue.put(Boolean.TYPE, 0);
        random = DefaultThreadLocalRandom.INSTANCE;
        secureRandom = new DefaultSecureRandom();
    }
}
