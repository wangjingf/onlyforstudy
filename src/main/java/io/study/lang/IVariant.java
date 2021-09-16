//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.study.lang;

//import io.entropy.util.StringHelper;
import io.study.helper.StringHelper;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IVariant {
    Object toObject();

    Object toObject(Object value);

    String toString();

    String toString(String value);

    String toNullableString();

    String emptyAsNull();

    Object trimObject();

    String toStripedString();

    String toStripedString(String value);

    default String toJavaString() {
        return String.valueOf(this.toObject());
    }

    boolean toBool();

    Boolean toBool(Boolean value, Boolean var2);

    Boolean toBool(Boolean value);


    char toChar();

    Character toCharObject();

    Character toChar(Character value);

    int toInt();

    Integer toIntObject();

    Integer toInt(Integer value);

    double toDouble();

    Double toDoubleObject();

    Double toDouble(Double value);

    long toLong();

    Long toLongObject();

    Long toLong(Long var1);

    float toFloat();

    Float toFloatObject();

    Float toFloat(Float value);

    Number toNumber();

    Number toNumber(Number value);

    Timestamp toTimestamp();

    Timestamp toTimestamp(Timestamp value);

    Date toDate();

    Date toDate(Date value);

    String toJson();

    List<String> toCsvList();

    Set<String> toCsvSet();

    List<String> toCsvList(List<String> value);

    Set<String> toCsvSet(Set<String> value);

    <T> List<T> toList();

    <T> List<T> toList(List<T> var1);

    <T> Collection<T> toCollection();

    <T> Collection<T> toCollection(Collection<T> value);

    <T> Set<T> toSet();

    <T> Set<T> toSet(Set<T> value);

    <K, V> Map<K, V> toMap();

    <K, V> Map<K, V> toMap(Map<K, V> value);

    Object[] toArray();

    Object[] toArray(Object[] value);

    byte[] toBytes();

    byte[] toBytes(byte[] value);

    String formatDate(String value);

    String formatNumber(String value);

    default String formatNumber() {
        return StringHelper.formatNumber(this.toNumber());
    }

    Object i0(Object value);

    Object d0(Object value);

    public Object convert( Class<?> targetType);
}
