//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.study.lang.type;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;


import io.study.exception.StdException;
import io.study.helper.ArrayHelper;
import io.study.helper.CollectionHelper;
import io.study.helper.StringHelper;
import io.study.lang.IVariant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ConcurrentReferenceHashMap;

public class ObjectTypes {

    static final Logger LOG = LoggerFactory.getLogger(ObjectTypes.class);
    public static final IObjectType OBJECT_TYPE = new DefaultObjectType();
    public static IObjectType PRIMITIVE_BOOLEAN_TYPE;
    public static IObjectType BOOLEAN_TYPE;
    public static IObjectType PRIMITIVE_INT_TYPE;
    public static IObjectType INTEGER_TYPE;
    public static IObjectType STRING_TYPE;
    public static IObjectType PRIMITIVE_LONG_TYPE;
    public static IObjectType LONG_TYPE;
    public static IObjectType PRIMITIVE_SHORT_TYPE;
    public static IObjectType SHORT_TYPE;
    public static IObjectType PRIMITIVE_FLOAT_TYPE;
    public static IObjectType FLOAT_TYPE;
    public static IObjectType PRIMITIVE_DOUBLE_TYPE;
    public static IObjectType DOUBLE_TYPE;
    public static IObjectType PRIMITIVE_BYTE_TYPE;
    public static IObjectType BYTE_TYPE;
    public static IObjectType PRIMITIVE_CHAR_TYPE;
    public static IObjectType CHARACTER_TYPE;
    public static IObjectType SQL_DATE_TYPE;
    public static IObjectType DATE_TYPE;
    public static IObjectType TIMESTAMP_TYPE;
    public static IObjectType NUMBER_TYPE;
    public static IObjectType BIGDECIMAL_TYPE;
    public static IObjectType BIGINTEGER_TYPE;
    public static IObjectType BYTE_ARRAY_TYPE;
    public static IObjectType CHAR_ARRAY_TYPE;
    public static IObjectType OBJECT_ARRAY_TYPE;
    public static IObjectType STRING_ARRAY_TYPE;
    public static IObjectType LIST_TYPE;
    public static IObjectType MAP_TYPE;
    public static IObjectType COLLECTION_TYPE;
    public static IObjectType COLLECTION_SET_TYPE;
    public static IObjectType FILE_TYPE;

    static final IObjectType[] allTypes;
    private static IConverter[] converters;
    private static Map<String, IObjectType> typeNameMap;
    private static Map<Class<?>, IObjectType> classTypeMap;
    private static final ConcurrentMap<Class<?>, IObjectType> extensionObjectTypes;

    public ObjectTypes() {
    }

    static Object convertToInteger(Object value) {
        if (value instanceof Number) {
            return ((Number)value).intValue();
        } else if (value instanceof Boolean) {
            return Boolean.TRUE.equals(value) ? 1 : 0;
        } else if (value instanceof Character) {
            return Integer.valueOf((Character)value);
        } else {
            try {
                return (int)Double.parseDouble(value.toString());
            } catch (Exception e) {
                LOG.debug("lang.err_convert_to_int_fail:value={}", value, e);
                return null;
            }
        }
    }

    static Object convertToLong(Object value) {
        if (value instanceof Number) {
            return ((Number)value).longValue();
        } else if (value instanceof Boolean) {
            return Boolean.TRUE.equals(value) ? 1L : 0L;
        } else if (value instanceof Date) {
            return ((Date)value).getTime();
        } else if (value instanceof Character) {
            return (long)(Character)value;
        } else {
            try {
                return Long.parseLong(value.toString());
            } catch (Exception e) {
                try {
                    return (long)Double.parseDouble(value.toString());
                } catch (Exception ex) {
                    LOG.debug("lang.err_convert_to_long_fail:value={}", value, e);
                    return null;
                }
            }
        }
    }

    static Object convertToDouble(Object value) {
        if (value instanceof Number) {
            return ((Number)value).doubleValue();
        } else if (value instanceof Boolean) {
            return Boolean.TRUE.equals(value) ? 1.0D : 0.0D;
        } else if (value instanceof Character) {
            return (double)(Character)value;
        } else {
            try {
                return Double.parseDouble(value.toString());
            } catch (Exception e) {
                LOG.debug("lang.err_convert_to_double_fail:value={}", value, e);
                return null;
            }
        }
    }
    static String convertToString(Object value){
        if(value == null){
            return null;
        }
        return value.toString();
    }
    static Object convertToFloat(Object value) {
        if (value instanceof Number) {
            return ((Number)value).floatValue();
        } else if (value instanceof Boolean) {
            return Boolean.TRUE.equals(value) ? 1.0F : 0.0F;
        } else if (value instanceof Character) {
            return (float)(Character)value;
        } else {
            try {
                return (float)Double.parseDouble(value.toString());
            } catch (Exception e) {
                LOG.debug("lang.err_convert_to_float_fail:value={}", value, e);
                return null;
            }
        }
    }

    static Object convertToShort(Object value) {
        if (value instanceof Number) {
            return ((Number)value).shortValue();
        } else if (value instanceof Boolean) {
            return Short.valueOf((short)(Boolean.TRUE.equals(value) ? 1 : 0));
        } else if (value instanceof Character) {
            return (short)(char)value;
        } else {
            try {
                return (short)((int)Double.parseDouble(value.toString()));
            } catch (Exception e) {
                LOG.debug("lang.err_convert_to_short_fail:value={}", value, e);
                return null;
            }
        }
    }

    static Object convertToByte(Object value) {
        if (value instanceof Number) {
            return ((Number)value).byteValue();
        } else if (value instanceof Boolean) {
            return Byte.valueOf((byte)(Boolean.TRUE.equals(value) ? 1 : 0));
        } else if (value instanceof Character) {
            return (byte)(char)value;
        } else {
            try {
                return Byte.parseByte(value.toString());
            } catch (Exception e) {
                LOG.debug("lang.err_convert_to_byte_fail:value={}", value, e);
                return null;
            }
        }
    }

    static Object convertToChar(Object value) {
        if (value instanceof Character) {
            return value;
        } else {
            return value instanceof String && value.toString().length() == 1 ? value.toString().charAt(0) : null;
        }
    }

    static Object convertToBigDecimal(Object value) {
        try {
            return new BigDecimal(value.toString());
        } catch (Exception e) {
            LOG.debug("lang.err_convert_to_BigDecimal_fail:value={}", value, e);
            return null;
        }
    }

    static Object convertToBigInteger(Object value) {
        if (value instanceof BigDecimal) {
            return ((BigDecimal)value).toBigInteger();
        } else if (value instanceof Number) {
            return BigInteger.valueOf(((Number)value).longValue());
        } else {
            try {
                return new BigInteger(value.toString());
            } catch (Exception e) {
                LOG.debug("lang.err_convert_to_BigInteger_fail:value={}", value, e);
                return null;
            }
        }
    }



    static Object convertToSqlDate(Object value) {
        if (java.sql.Date.class.isAssignableFrom(value.getClass())) {
            return value;
        } else if (value instanceof Number) {
            return new java.sql.Date(((Number)value).longValue());
        } else if (value instanceof Calendar) {
            return new java.sql.Date(((Calendar)value).getTimeInMillis());
        } else if (value instanceof Date) {
            return new java.sql.Date(((Date)value).getTime());
        } else {
            Timestamp timestamp = parseTimestamp(value.toString());
            return timestamp == null ? null : new java.sql.Date(timestamp.getTime());
        }
    }

    static Object convertToDate(Object value) {
        if (Date.class.isAssignableFrom(value.getClass())) {
            return value;
        } else if (value instanceof Number) {
            return new Timestamp(((Number)value).longValue());
        } else {
            return value instanceof Calendar ? new Timestamp(((Calendar)value).getTimeInMillis()) : parseTimestamp(value.toString());
        }
    }

    static Object convertToTimestamp(Object value) {
        if (Date.class.isAssignableFrom(value.getClass())) {
            return new Timestamp(((Date)value).getTime());
        } else if (value instanceof Number) {
            return new Timestamp(((Number)value).longValue());
        } else {
            return value instanceof Calendar ? new Timestamp(((Calendar)value).getTimeInMillis()) : parseTimestamp(value.toString());
        }
    }

    public static Timestamp parseTimestamp(String value) {
        if (StringHelper.isDigits(value)) {
            try {
                long longValue = Long.parseLong(value);
                return new Timestamp(longValue);
            } catch (NumberFormatException e) {
                LOG.debug("lang.err_parse_timestamp_fail:value={}", value, e);
                return null;
            }
        } else {
            int date = 1;
            int hour = 0;
            int minute = 0;
            int second = 0;
            int nano = 0;
            int hourIndex = 0;
            int minuteIndex = 0;
            int nanoIndex = 0;
            String maxNanoLength = "000000000";
            value = value.trim();
            if (value.length() <= 0) {
                return null;
            } else {
                int dateSplitIndex = value.indexOf(32); // 空格
                if (dateSplitIndex < 0) {
                    dateSplitIndex = value.indexOf(84); // T
                }

                String datePart;
                String timepart;
                if (dateSplitIndex > 0) {
                    datePart = value.substring(0, dateSplitIndex);
                    timepart = value.substring(dateSplitIndex + 1);
                } else {
                    datePart = value;
                    timepart = null;
                }

                int yearIndex = datePart.indexOf(45); // -符号
                if (yearIndex < 0) {
                    yearIndex = datePart.indexOf(46); // .符号
                }

                if (yearIndex < 0) {
                    yearIndex = datePart.indexOf(47); // /符号
                }

                int monthIndex = datePart.indexOf(45, yearIndex + 1);
                if (monthIndex < 0) {
                    monthIndex = datePart.indexOf(46, yearIndex + 1);
                }

                if (monthIndex < 0) {
                    monthIndex = datePart.indexOf(47, yearIndex + 1);
                }

                if (timepart != null) {
                    hourIndex = timepart.indexOf(58); // :号
                    minuteIndex = timepart.indexOf(58, hourIndex + 1);
                    nanoIndex = timepart.indexOf(46, minuteIndex + 1);
                }

                if (yearIndex <= 0) {
                    return null;
                } else {
                    String yearVal = datePart.substring(0, yearIndex);
                    if (yearVal.length() == 2) {
                        if (yearVal.charAt(0) < '4' && yearVal.charAt(0) >= '0') {
                            yearVal = "20" + yearVal;
                        } else {
                            yearVal = "19" + yearVal;
                        }
                    } else if (yearVal.length() == 1) {
                        yearVal = "200" + yearVal;
                    }

                    int year = Integer.parseInt(yearVal) - 1900;

                    int month;
                    if (monthIndex < 0) {
                        month = Integer.parseInt(datePart.substring(yearIndex + 1)) - 1;
                    } else {
                        month = Integer.parseInt(datePart.substring(yearIndex + 1, monthIndex)) - 1;
                        String dayStr = datePart.substring(monthIndex + 1);
                        if (dayStr.length() > 0) {
                            date = Integer.parseInt(dayStr);
                        }
                    }

                    if (timepart != null && hourIndex > 0) {
                        hour = Integer.parseInt(timepart.substring(0, hourIndex));
                        if (minuteIndex < 0) {
                            minuteIndex = timepart.length();
                        }

                        String minuteStr = timepart.substring(hourIndex + 1, minuteIndex);
                        if (minuteStr.length() > 0) {
                            minute = Integer.parseInt(minuteStr);
                        }

                        if (nanoIndex > 0 & nanoIndex < timepart.length() - 1) {
                            second = Integer.parseInt(timepart.substring(minuteIndex + 1, nanoIndex));
                            String nanoStr = timepart.substring(nanoIndex + 1);
                            if (nanoStr.length() > 9) {
                                return null;
                            }

                            if (!Character.isDigit(nanoStr.charAt(0))) {
                                return null;
                            }

                            nanoStr = nanoStr + maxNanoLength.substring(0, 9 - nanoStr.length());
                            nano = Integer.parseInt(nanoStr);
                        } else if (nanoIndex <= 0 && minuteIndex < timepart.length()) {
                            String secondStr = timepart.substring(minuteIndex + 1);
                            if (secondStr.length() > 0) {
                                second = Integer.parseInt(secondStr);
                            }
                        }
                    }

                    return new Timestamp(year, month, date, hour, minute, second, nano);
                }
            }
        }
    }

    /**
     *  将value转换为boolean类型
     * @param value 要转换的值
     * @param nullValue 值为空时的默认值
     * @param defaultValue 转换失败时的值
     * @return
     */
    public static Boolean convertToBoolean(Object value, Boolean nullValue, Boolean defaultValue) {
        if (value == null) {
            return nullValue;
        } else if (value instanceof Boolean) {
            return (Boolean)value;
        } else if (value instanceof String) {
            String strValue = value.toString();
            if (strValue.length() <= 0) {
                return nullValue;
            } else if (!strValue.equalsIgnoreCase("true") && !strValue.equalsIgnoreCase("y")) {
                return !strValue.equalsIgnoreCase("false") && !strValue.equalsIgnoreCase("n") ? defaultValue : false;
            } else {
                return true;
            }
        } else {
            return value instanceof Number ? ((Number)value).doubleValue() != 0.0D : defaultValue;
        }
    }

    static List<?> convertToList(Object value) {
        if (value instanceof List) {
            return (List)value;
        } else if (value instanceof Collection) {
            return new ArrayList((Collection)value);
        } else if (value instanceof Object[]) {
            return Arrays.asList((Object[])((Object[])value));
        } else if (value.getClass().isArray()) {
            return ArrayHelper.toList(value);
        } /*else if (value instanceof ICollectionObject) {
            List listValue = ((ICollectionObject)value).toList();
            return listValue;
        }*/ else if (value instanceof Iterable) {
            return CollectionHelper.toList((Iterable)value);
        } else {
            return value instanceof String ? StringHelper.stripedSplit(value.toString(), ",") : null;
        }
    }

    public static Collection<?> tryConvertToCollection(Object value) {
        if (value instanceof Collection) {
            return (Collection)value;
        } else if (value instanceof Object[]) {
            return Arrays.asList((Object[])((Object[])value));
        } else if (value.getClass().isArray()) {
            return ArrayHelper.toList(value);
        }/* else if (value instanceof ICollectionObject) {
            return ((ICollectionObject)value).toCollection();
        } */else if (value instanceof Iterable) {
            return CollectionHelper.toList((Iterable)value);
        } else {
            return value instanceof String ? StringHelper.stripedSplit(value.toString(), ",") : null;
        }
    }

    static Collection<?> convertToSet(Object value) {
        Object collection = tryConvertToCollection(value);
        if (collection != null && !(collection instanceof Set)) {
            collection = new LinkedHashSet((Collection)collection);
        }

        return (Collection)collection;
    }

    static File covertToFile(Object value) {
        throw new StdException("variant.err_not_support");
    }

    public static Object getPrimitiveInitValue(Class<?> value) {
        if (value == Integer.TYPE) {
            return 0;
        } else if (value == Long.TYPE) {
            return 0L;
        } else if (value == Double.TYPE) {
            return 0.0D;
        } else if (value == Boolean.class) {
            return Boolean.FALSE;
        } else if (value == Float.TYPE) {
            return 0.0F;
        } else if (value == Byte.TYPE) {
            return 0;
        } else {
            return value == Character.TYPE ? '\u0000' : null;
        }
    }

    public static void registerConverter(IConverter converter) {
        converters = (IConverter[])ArrayHelper.append(converters, converter, IConverter.class);
    }



    public static IObjectType requireDefaultType(String type) {
        if (type == null) {
            return OBJECT_TYPE;
        } else {
            IObjectType objectType = (IObjectType) typeNameMap.get(type);
            if (objectType == null) {
                throw (new StdException("lang.err_unknown_object_type")).param("name", type);
            } else {
                return objectType;
            }
        }
    }

    public static IObjectType makeArrayType(IObjectType type) {
        if (type == PRIMITIVE_BYTE_TYPE) {
            return BYTE_ARRAY_TYPE;
        } else if (type == PRIMITIVE_CHAR_TYPE) {
            return CHAR_ARRAY_TYPE;
        } else if (type == OBJECT_TYPE) {
            return OBJECT_ARRAY_TYPE;
        } else if (type == STRING_TYPE) {
            return STRING_ARRAY_TYPE;
        } else {
            Class newArrType = Array.newInstance(type.getRawClassType(), 0).getClass();
            IObjectType componentArrayType = new ComponentArrayType(newArrType, type);
            IObjectType oldType =  extensionObjectTypes.putIfAbsent(newArrType, componentArrayType);
            if (oldType != null) {
                componentArrayType = oldType;
            }

            return componentArrayType;
        }
    }

    public static IObjectType makeObjectType(Class<?> type) {
        IObjectType objectType = classTypeMap.get(type);
        if (objectType == null) {
            objectType = extensionObjectTypes.get(type);
            if (objectType == null) {
                IObjectType var2;
                if (type.isArray()) {
                    var2 = makeObjectType(type.getComponentType());
                    objectType = makeArrayType(var2);
                } else {
                    objectType = new DefaultObjectType(type);
                    var2 = extensionObjectTypes.putIfAbsent(type, objectType);
                    if (var2 != null) {
                        objectType = var2;
                    }
                }
            }
        }

        return objectType;
    }

    public static IObjectType[] getDefaultConvertables(Class<?>[] clazz) {
        IObjectType[] array = new IObjectType[clazz.length];
        int i = 0;

        for(int length = clazz.length; i < length; ++i) {
            array[i] = makeObjectType(clazz[i]);
        }

        return array;
    }

    public static IObjectType getDefaultType(String value) {
        if (value == null) {
            return OBJECT_TYPE;
        } else {
            IObjectType type = (IObjectType) typeNameMap.get(value);
            return type;
        }
    }

    static Object tryConvert( Object value,  Class<?> type) {
        Class sourceType = value.getClass();
        if (sourceType != type && !type.isAssignableFrom(sourceType)) {
            if (value instanceof IVariant) {
                value = ((IVariant)value).toObject();
                if (value == null) {
                    return type.isPrimitive() ? getPrimitiveInitValue(type) : null;
                } else {
                    return tryConvert(value, type);
                }
            } else {
                IObjectType objectType = (IObjectType) classTypeMap.get(type);
                if (objectType != null) {
                    return objectType.tryConvert(value);
                } else if (type.isArray()) {
                    return tryConvertToArray(value, type.getComponentType());
                } else if (type.isEnum() && value instanceof String) {
                    return Enum.valueOf((Class)type, value.toString());
                } else {
                    if (converters != null) {

                        for(int i = 0; i < converters.length; ++i) {
                            Object var6 = converters[i].tryConvert(value, type);
                            if (var6 != null) {
                                return var6;
                            }
                        }
                    }

                    return null;
                }
            }
        } else {
            return value;
        }
    }

    public static Object convert(Object value, Class<?> type) {
        if (value == null) {
            return type.isPrimitive() ? getPrimitiveInitValue(type) : null;
        } else if (type == Void.class) {
            return null;
        } else {
            Object convertValue = tryConvert(value, type);
            if (convertValue == null) {
                throw (new StdException("lang.err_type_convert_fail")).param("target", type).param("source", value.getClass()).param("obj", value);
            } else {
                return convertValue;
            }
        }
    }

    public static Object tryConvertToArray(Object value, Class<?> componentType) {
        /*if (value instanceof ICollectionObject) {
            value = ((ICollectionObject)value).toCollection();
        }*/

        if (value == null) {
            return null;
        } else {
            Object arr = null;
            int index;
            if (value instanceof Collection) {
                Collection collection = (Collection)value;
                arr = Array.newInstance(componentType, collection.size());
                index = 0;

                for(Iterator iter = collection.iterator(); iter.hasNext(); ++index) {
                    Object elm = iter.next();
                    elm = convert(elm, componentType);
                    Array.set(arr, index, elm);
                }
            } else if (value.getClass().isArray()) {
                if (componentType == value.getClass().getComponentType() || componentType.isAssignableFrom(value.getClass().getComponentType())) {
                    return value;
                }

                int length = Array.getLength(value);
                arr = Array.newInstance(componentType, length);

                for(index = 0; index < length; ++index) {
                    Object elm = Array.get(value, index);
                    elm = convert(elm, componentType);
                    Array.set(arr, index, elm);
                }
            } else if (componentType == Character.TYPE && value instanceof String) {
                return value.toString().toCharArray();
            }

            return arr;
        }
    }

    static {
        PRIMITIVE_BOOLEAN_TYPE = new PrimitiveBooleanType(Boolean.TYPE);
        BOOLEAN_TYPE = new BooleanType(Boolean.class);
        PRIMITIVE_INT_TYPE = new PrimitiveIntType(Integer.TYPE);
        INTEGER_TYPE = new IntegerType(Integer.class);
        STRING_TYPE = new StringType(String.class);
        PRIMITIVE_LONG_TYPE = new PrimitiveLongType(Long.TYPE);
        LONG_TYPE = new LongType(Long.class);
        PRIMITIVE_SHORT_TYPE = new PrimitiveShortType(Short.TYPE);
        SHORT_TYPE = new ShortType(Short.class);
        PRIMITIVE_FLOAT_TYPE = new PrimitiveFloatType(Float.TYPE);
        FLOAT_TYPE = new FloatType(Float.class);
        PRIMITIVE_DOUBLE_TYPE = new PrimitiveDoubleType(Double.TYPE);
        DOUBLE_TYPE = new DoubleType(Double.class);
        PRIMITIVE_BYTE_TYPE = new PrimitiveByteType(Byte.TYPE);
        BYTE_TYPE = new ByteType(Byte.class);
        PRIMITIVE_CHAR_TYPE = new PrimitiveCharType(Character.TYPE);
        CHARACTER_TYPE = new CharacterType(Character.class);
        SQL_DATE_TYPE = new SqlDateType(java.sql.Date.class);
        DATE_TYPE = new DateType(Date.class);
        TIMESTAMP_TYPE = new TimestampType(Timestamp.class);
        NUMBER_TYPE = new NumberType(Number.class);
        BIGDECIMAL_TYPE = new BigDecimalType(BigDecimal.class);
        BIGINTEGER_TYPE = new BigIntegerType(BigInteger.class);
        BYTE_ARRAY_TYPE = new ByteArrayType(byte[].class);
        CHAR_ARRAY_TYPE = new CharArrayType(byte[].class);
        OBJECT_ARRAY_TYPE = new ObjectArrayType(Object[].class);
        STRING_ARRAY_TYPE = new StringArrayType(String[].class);
        LIST_TYPE = new ListType(List.class);
        MAP_TYPE = new MapType(Map.class);
        COLLECTION_TYPE = new CollectionType(Collection.class);
        COLLECTION_SET_TYPE = new CollectionSetType(Set.class);
        FILE_TYPE = new FileType(File.class);

        allTypes = new IObjectType[]{PRIMITIVE_INT_TYPE, INTEGER_TYPE, STRING_TYPE, PRIMITIVE_BOOLEAN_TYPE, BOOLEAN_TYPE, PRIMITIVE_LONG_TYPE, LONG_TYPE, PRIMITIVE_DOUBLE_TYPE, DOUBLE_TYPE, PRIMITIVE_SHORT_TYPE, SHORT_TYPE, PRIMITIVE_FLOAT_TYPE, FLOAT_TYPE, PRIMITIVE_BYTE_TYPE, BYTE_TYPE, PRIMITIVE_CHAR_TYPE, CHARACTER_TYPE, SQL_DATE_TYPE, DATE_TYPE, TIMESTAMP_TYPE, NUMBER_TYPE, BIGINTEGER_TYPE, BIGDECIMAL_TYPE, BYTE_ARRAY_TYPE, CHAR_ARRAY_TYPE, OBJECT_ARRAY_TYPE, STRING_ARRAY_TYPE, LIST_TYPE, COLLECTION_TYPE, COLLECTION_SET_TYPE, FILE_TYPE,  OBJECT_TYPE};
        typeNameMap = new HashMap();
        classTypeMap = new IdentityHashMap();
        //typeDescriptors = new IdentityHashMap();
        IObjectType[] var0 = allTypes;
        int var1 = var0.length;

        for(int var2 = 0; var2 < var1; ++var2) {
            IObjectType var3 = var0[var2];
            typeNameMap.put(var3.getClassName(), var3);
            typeNameMap.put(var3.getClassSimpleName(), var3);
            classTypeMap.put(var3.getRawClassType(), var3);
            //typeDescriptors.put(var3.getRawClassType(), ITypeDescriptor.fromJavaType(var3.getRawClassType()));
        }

        extensionObjectTypes = new ConcurrentReferenceHashMap(100, ConcurrentReferenceHashMap.ReferenceType.WEAK);
    }



}
