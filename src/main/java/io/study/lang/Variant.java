package io.study.lang;

 
 import io.study.exception.StdException;
 import io.study.helper.JsonTool;
 import io.study.helper.MathHelper;
 import io.study.helper.StringHelper;
import io.study.lang.type.ObjectTypes;


import java.sql.Timestamp;
import java.util.*;

public abstract class Variant implements IVariant {
    public static final Variant NULL = new NullValue((Object)null);
    //static final boolean CHECK_STRICT_BOOL = AppConfig.var("lang.check_strict_bool").toBool(AppConfig.isDebugMode());

    public Variant() {
    }


    public static IVariant valueOf(Object value) {
        if (value == null) {
            return NULL;
        } else {
            return (IVariant)(value instanceof IVariant ? (IVariant)value : new NullValue(value));
        }
    }

    protected abstract Object internalSingleValue();

    protected Object internalMultipleValue() {
        return this.internalSingleValue();
    }

    Object emptyAsNullValue() {
        Object singleValue = this.internalSingleValue();
        if (singleValue == null) {
            return null;
        } else {
            return singleValue instanceof String && singleValue.toString().length() <= 0 ? null : singleValue;
        }
    }

    public Object getValue() {
        return this.internalSingleValue();
    }

    public Object toObject() {
        return this.internalSingleValue();
    }

    public Object toObject(Object defaultValue) {
        Object objValue = this.toObject();
        if (objValue == null) {
            objValue = defaultValue;
        }

        return objValue;
    }

    public String toString() {
        Object singleValue = this.internalSingleValue();
        return singleValue == null ? "" : singleValue.toString();
    }

    public String toString(String defaultValue) {
        Object singleValue = this.internalSingleValue();
        return singleValue == null ? defaultValue : singleValue.toString();
    }

    public String toNullableString() {
        Object singleValue = this.internalSingleValue();
        return singleValue == null ? null : singleValue.toString();
    }

    public String emptyAsNull() {
        String strValue = this.toString();
        if (strValue != null && strValue.toString().length() <= 0) {
            strValue = null;
        }

        return strValue;
    }

    public Object trimObject() {
        Object objValue = this.toObject();
        return objValue instanceof String && objValue.toString().length() <= 0 ? null : objValue;
    }

    public String toStripedString() {
        String strValue = StringHelper.strip(this.toString());
        return strValue;
    }

    public String toStripedString(String defaultValue) {
        String strValue = this.toStripedString();
        if (strValue == null) {
            strValue = defaultValue;
        }

        return strValue;
    }

    public boolean toBool() {
        return toBoolean(this.internalSingleValue());
    }

    public Boolean toBool(Boolean defaultValue) {
        return this.toBool(defaultValue, defaultValue);
    }

    public Boolean toBool(Boolean nullValue, Boolean defaultValue) {
        return ObjectTypes.convertToBoolean(this.internalSingleValue(), nullValue, defaultValue);
    }


    public Character toCharObject() {
        Object value = this.emptyAsNullValue();
        if (value == null) {
            return null;
        } else {
            Object convertValue = ObjectTypes.PRIMITIVE_CHAR_TYPE.tryConvert(value);
            if (convertValue == null) {
                throw (new StdException("lang.err_cast_to_char_fail")).param("defaultValue", value);
            } else {
                return (Character)convertValue;
            }
        }
    }

    public char toChar() {
        Character value = this.toCharObject();
        return value == null ? '\u0000' : value;
    }

    public Character toChar(Character defaultValue) {
        Object var2 = this.emptyAsNullValue();
        if (var2 == null) {
            return defaultValue;
        } else {
            Object value = ObjectTypes.CHARACTER_TYPE.tryConvert(var2);
            if (value == null) {
                value = defaultValue;
            }

            return (Character)value;
        }
    }

    public Integer toIntObject() {
        Object value = this.emptyAsNullValue();
        if (value == null) {
            return null;
        } else {
            Object convertValue = ObjectTypes.INTEGER_TYPE.tryConvert(value);
            if (convertValue == null) {
                throw (new StdException("lang.err_cast_to_int_fail")).param("defaultValue", value);
            } else {
                return (Integer)convertValue;
            }
        }
    }

    public int toInt() {
        Integer var1 = this.toIntObject();
        return var1 == null ? 0 : var1;
    }

    public Integer toInt(Integer defaultValue) {
        Object value = this.emptyAsNullValue();
        if (value == null) {
            return defaultValue;
        } else {
            Object convertValue = ObjectTypes.INTEGER_TYPE.tryConvert(value);
            if (convertValue == null) {
                convertValue = defaultValue;
            }

            return (Integer)convertValue;
        }
    }

    public Double toDoubleObject() {
        Object value = this.emptyAsNullValue();
        if (value == null) {
            return 0.0D;
        } else {
            Object convertValue = ObjectTypes.PRIMITIVE_DOUBLE_TYPE.tryConvert(value);
            if (convertValue == null) {
                throw (new StdException("lang.err_cast_to_double_fail")).param("defaultValue", value);
            } else {
                return (Double)convertValue;
            }
        }
    }

    public double toDouble() {
        Double value = this.toDoubleObject();
        return value == null ? 0.0D : value;
    }

    public Double toDouble(Double defaultValue) {
        Object value = this.emptyAsNullValue();
        if (value == null) {
            return defaultValue;
        } else {
            Object convertValue = ObjectTypes.DOUBLE_TYPE.tryConvert(value);
            if (convertValue == null) {
                convertValue = defaultValue;
            }

            return (Double)convertValue;
        }
    }

    public Long toLongObject() {
        Object value = this.emptyAsNullValue();
        if (value == null) {
            return null;
        } else {
            Object convertValue = ObjectTypes.PRIMITIVE_LONG_TYPE.tryConvert(value);
            if (convertValue == null) {
                throw (new StdException("lang.err_cast_to_long_fail")).param("defaultValue", value);
            } else {
                return (Long)convertValue;
            }
        }
    }

    public long toLong() {
        Long value = this.toLongObject();
        return value == null ? 0L : value;
    }

    public Long toLong(Long defaultValue) {
        Object value = this.emptyAsNullValue();
        if (value == null) {
            return defaultValue;
        } else {
            Object convertValue = ObjectTypes.LONG_TYPE.tryConvert(value);
            if (convertValue == null) {
                convertValue = defaultValue;
            }

            return (Long)convertValue;
        }
    }

    public Float toFloatObject() {
        Object value = this.emptyAsNullValue();
        if (value == null) {
            return null;
        } else {
            Object convertValue = ObjectTypes.FLOAT_TYPE.tryConvert(value);
            if (convertValue == null) {
                throw (new StdException("lang.err_cast_to_float_fail")).param("defaultValue", value);
            } else {
                return (Float)convertValue;
            }
        }
    }

    public float toFloat() {
        Float value = this.toFloatObject();
        return value == null ? 0.0F : value;
    }

    public Float toFloat(Float defaultValue) {
        Object value = this.emptyAsNullValue();
        if (value == null) {
            return defaultValue;
        } else {
            Object convertValue = ObjectTypes.FLOAT_TYPE.tryConvert(value);
            if (convertValue == null) {
                convertValue = defaultValue;
            }

            return (Float)convertValue;
        }
    }

    public Number toNumber() {
        Object value = this.emptyAsNullValue();
        if (value == null) {
            return null;
        } else if (value instanceof Number) {
            return (Number)value;
        } else {
            return (Number)(value instanceof String ? StringHelper.parseNumber(value.toString()) : this.toDouble());
        }
    }

    public Number toNumber(Number defaultValue) {
        Object value = this.emptyAsNullValue();
        if (value == null) {
            return null;
        } else if (value instanceof Number) {
            return (Number)value;
        } else {
            return (Number)(value instanceof String ? StringHelper.parseNumber(value.toString()) : this.toDouble());
        }
    }

    public Timestamp toTimestamp() {
        Object value = this.emptyAsNullValue();
        if (value == null) {
            return null;
        } else {
            Object convertValue = ObjectTypes.TIMESTAMP_TYPE.tryConvert(value);
            if (convertValue == null) {
                throw (new StdException("lang.err_convert_to_timestamp_fail")).param("defaultValue", value);
            } else {
                return (Timestamp)convertValue;
            }
        }
    }

    public Timestamp toTimestamp(Timestamp defaultValue) {
        Object value = this.emptyAsNullValue();
        if (value == null) {
            return defaultValue;
        } else {
            Object convertValue = ObjectTypes.TIMESTAMP_TYPE.tryConvert(value);
            if (convertValue == null) {
                convertValue = defaultValue;
            }

            return (Timestamp)convertValue;
        }
    }

    public Date toDate() {
        Object value = this.emptyAsNullValue();
        if (value == null) {
            return null;
        } else {
            Object convertValue = ObjectTypes.DATE_TYPE.tryConvert(value);
            if (convertValue == null) {
                throw (new StdException("lang.err_convert_to_date_fail")).param("defaultValue", value);
            } else {
                return (Date)convertValue;
            }
        }
    }

    public Date toDate(Date defaultValue) {
        Object value = this.emptyAsNullValue();
        if (value == null) {
            return defaultValue;
        } else {
            Object convertValue = ObjectTypes.DATE_TYPE.tryConvert(value);
            if (convertValue == null) {
                convertValue = defaultValue;
            }

            return (Date)convertValue;
        }
    }

    public String toJson() {
        return JsonTool.serialize(this.internalSingleValue());
    }

    public List<String> toCsvList() {
        Object value = this.internalSingleValue();
        if (value == null) {
            return null;
        } else if (value instanceof List) {
            return (List)value;
        } else {
            String strValue = this.toString();
            return strValue == null ? null : StringHelper.stripedSplit(strValue, ",");
        }
    }

    public Set<String> toCsvSet() {
        Object value = this.internalSingleValue();
        if (value == null) {
            return null;
        } else if (value instanceof Set) {
            return (Set)value;
        } else {
            String strValue = this.toString();
            return strValue == null ? null : new LinkedHashSet(StringHelper.stripedSplit(strValue, ","));
        }
    }

    public Set<String> toCsvSet(Set<String> defaultValue) {
        Set value = this.toCsvSet();
        if (value == null) {
            value = defaultValue;
        }

        return value;
    }

    public List<String> toCsvList(List<String> defaultValue) {
        List value = this.toCsvList();
        if (value == null) {
            value = defaultValue;
        }

        return value;
    }

    public <T> List<T> toList() {
        Object multiValue = this.internalMultipleValue();
        if (multiValue == null) {
            return null;
        } else {
            Object convertValue = ObjectTypes.LIST_TYPE.tryConvert(multiValue);
            if (convertValue == null) {
                throw (new StdException("lang.err_cast_to_list_fail")).param("defaultValue", multiValue);
            } else {
                return (List)convertValue;
            }
        }
    }

    public <T> Collection<T> toCollection() {
        Object value = this.internalMultipleValue();
        if (value == null) {
            return null;
        } else {
            Object convertValue = ObjectTypes.COLLECTION_TYPE.tryConvert(value);
            if (convertValue == null) {
                throw (new StdException("lang.err_cast_to_collection_fail")).param("defaultValue", value);
            } else {
                return (Collection)convertValue;
            }
        }
    }

    public <K, V> Map<K, V> toMap() {
        Object objValue = this.toObject();
        if (objValue == null) {
            return null;
        } else if (objValue instanceof Map) {
            return (Map)objValue;
        } else {
            throw (new StdException("lang.err_cast_to_map_fail")).param("defaultValue", objValue);
        }
    }

    public <T> List<T> toList(List<T> defaultValue) {
        Object multiValue = this.internalMultipleValue();
        if (multiValue == null) {
            return defaultValue;
        } else {
            Object convertValue = ObjectTypes.LIST_TYPE.tryConvert(multiValue);
            if (convertValue == null) {
                convertValue = defaultValue;
            }

            return (List)convertValue;
        }
    }

    public <T> Collection<T> toCollection(Collection<T> defaultValue) {
        Object multiValue = this.internalMultipleValue();
        if (multiValue == null) {
            return defaultValue;
        } else {
            Object convertValue = ObjectTypes.COLLECTION_TYPE.tryConvert(multiValue);
            if (convertValue == null) {
                convertValue = defaultValue;
            }

            return (Collection)convertValue;
        }
    }

    public <T> Set<T> toSet() {
        Object multiValue = this.internalMultipleValue();
        if (multiValue == null) {
            return null;
        } else {
            Object convertValue = ObjectTypes.COLLECTION_TYPE.tryConvert(multiValue);
            if (convertValue == null) {
                throw (new StdException("lang.err_cast_to_set_fail")).param("defaultValue", multiValue);
            } else {
                return (Set)convertValue;
            }
        }
    }

    public <T> Set<T> toSet(Set<T> defaultValue) {
        Object multiValue = this.internalMultipleValue();
        if (multiValue == null) {
            return defaultValue;
        } else {
            Object convertValue = ObjectTypes.COLLECTION_TYPE.tryConvert(multiValue);
            if (convertValue == null) {
                convertValue = defaultValue;
            }

            return (Set)convertValue;
        }
    }

    public <K, V> Map<K, V> toMap(Map<K, V> defaultValue) {
        Object objectValue = this.toObject();
        if (objectValue == null) {
            return defaultValue;
        } else {
            return objectValue instanceof Map ? (Map)objectValue : defaultValue;
        }
    }

    public Object[] toArray() {
        Object multiValue = this.internalMultipleValue();
        if (multiValue == null) {
            return null;
        } else if (multiValue instanceof Object[]) {
            return (Object[])((Object[])multiValue);
        } else if (multiValue instanceof Collection) {
            return ((Collection)multiValue).toArray();
        } else {
            throw (new StdException("lang.err_cast_to_array_fail")).param("defaultValue", multiValue);
        }
    }

    public Object[] toArray(Object[] defaultValue) {
        Object multiValue = this.internalMultipleValue();
        if (multiValue == null) {
            return defaultValue;
        } else if (multiValue instanceof Object[]) {
            return (Object[])((Object[])multiValue);
        } else {
            return multiValue instanceof Collection ? ((Collection)multiValue).toArray() : defaultValue;
        }
    }

    public byte[] toBytes() {
        Object value = this.emptyAsNullValue();
        if (value == null) {
            return null;
        } else if (value instanceof byte[]) {
            return (byte[])((byte[])value);
        } else {
            throw (new StdException("lang.err_cast_to_bytes_fail")).param("defaultValue", value);
        }
    }

    public byte[] toBytes(byte[] defaultValue) {
        Object var2 = this.emptyAsNullValue();
        if (var2 == null) {
            return defaultValue;
        } else {
            return var2 instanceof byte[] ? (byte[])((byte[])var2) : defaultValue;
        }
    }

    public String formatDate(String pattern) {
        Date var2 = this.toDate();
        return StringHelper.formatDate(var2, pattern);
    }

    public String formatNumber(String pattern) {
        Number numberValue = this.toNumber();
        return StringHelper.formatNumber(numberValue, pattern);
    }

    public Object i0(Object defaultValue) {
        int value = this.toInt(0);
        return value == 0 ? defaultValue : value;
    }

    public Object d0(Object defaultValue) {
        double value = this.toDouble(0.0D);
        return value == 0.0D ? defaultValue : value;
    }



    public static boolean toBoolean(Object value) {
        return ObjectTypes.convertToBoolean(value, false, true);
    }

    public static boolean toBoolean(Object value, Boolean nullValue, Boolean defaultValue) {
        return ObjectTypes.convertToBoolean(value, nullValue, defaultValue);
    }

    public static <T> T defaultValue(T value, T defaultValue) {
        return value == null ? defaultValue : value;
    }

    public static String toString(Object value, String defaultValue) {
        return value == null ? defaultValue : value.toString();
    }

    public static boolean eqEx(Object value1, Object value2) {
        return MathHelper.eqEx(value1, value2);
    }

    public static boolean equals(Object value1, Object value2) {
        return value1 == value2 || value1 != null && value1.equals(value2);
    }

    public static Timestamp parseTimestamp(String value) {
        return value != null && value.length() > 0 ? ObjectTypes.parseTimestamp(value) : null;
    }

    public static Collection<?> convertToCollection(Object value) {
        if (value == null) {
            return null;
        } else {
            Collection collection = ObjectTypes.tryConvertToCollection(value);
            if (collection == null) {
                throw (new StdException("lang.err_not_collection")).param("class", value.getClass()).param("obj", value);
            } else {
                return collection;
            }
        }
    }

    public static Object convert(Object value, Class<?> targetType) {
        return ObjectTypes.convert(value, targetType);
    }

    public static Object convertToArray(Object value, Class<?> targetType) {
        if (value == null) {
            return null;
        } else {
            Object arrValue = ObjectTypes.tryConvertToArray(value, targetType);
            if (arrValue == null) {
                throw (new StdException("lang.err_type_convert_to_array_fail")).param("targetElement", targetType).param("source", value.getClass()).param("obj", value);
            } else {
                return arrValue;
            }
        }
    }

    public static void convertAll(Object[] values, Class<?>[] targetTypes) {
        int i = 0;

        for(int length = values.length; i < length; ++i) {
            values[i] = convert(values[i], targetTypes[i]);
        }

    }

    static class NullValue extends Variant {
        final Object value;

        NullValue(Object var1) {
            this.value = var1;
        }

        protected Object internalSingleValue() {
            return this.value ;
        }

        @Override
        public Object convert(Class<?> targetType) {
            return null;
        }
    }
}