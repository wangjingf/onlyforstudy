package io.study.util;
 

import com.google.common.base.CaseFormat;
import io.entropy.config.AppConfig;
import io.entropy.core.mix.ka;
import io.entropy.crypto.HashHelper;
import io.entropy.json.JsonTool;
import io.entropy.lang.IRandom;
import io.entropy.lang.IReference;
import io.entropy.lang.support.Transformers;
import io.entropy.lang.util.BytesObject;
import io.entropy.text.code.Utf8;
import io.entropy.text.tpl.SimpleTextTemplate;
import io.study.exception.StdException;
import org.apache.commons.lang.StringEscapeUtils;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.regex.Pattern;

public class StringHelper {
    public static final String ENCODING_UTF8 = "UTF-8";
    public static final Charset CHARSET_UTF8 = Charset.forName("UTF-8");
    public static final byte[] EMPTY_BYTES = new byte[0];
    public static final String[] EMPTY_STRINGS = new String[0];
    public static final String EMPTY_STRING = "";
    static final IReference<Integer> abR = AppConfig.varRef("util.string_max_repeat_len", 1024);
    static final IReference<Integer> abS = AppConfig.varRef("util.string_max_pad_len", 1024);
    static final char[] CRLF = new char[]{'\r', '\n'};
    static final char[] HTML_ESCAPED = new char[]{'<', '>', '&', ' ', '"', '\'', '\r', '\n'};
    static final String[] HTML_TO_ESCAPED = new String[]{"&lt;", "&gt;", "&amp;", "&#160;", "&quot;", "&apos;", "", "<br/>"};
    static final char[] XML_ESCAPED_CHARS = new char[]{'<', '>', '&', ' ', '"', '\''};
    static final String[] XML_TO_ESCAPED_STRS = new String[]{"&lt;", "&gt;", "&amp;", "&#160;", "&quot;", "&apos;"};
    static final char[] XML_VALUE_ESCAPED_CHARS = new char[]{'<', '>', '&', ' '};
    static final String[] XML_VALUE_ESCAPED_TO_STRS = new String[]{"&lt;", "&gt;", "&amp;", "&#160;"};
    static final char[] XML_ATTR_ESCAPED = new char[]{'<', '>', '&', ' ', '"', '\n'};
    static final String[] XML_TO_ATTR_STRS = new String[]{"&lt;", "&gt;", "&amp;", "&#160;", "&quot;", "&#10;"};
    static final char[] JAVA_ESCAPE_FROM_CHARS = new char[]{'\b', '\r', '\n', '\t', '\f', '"', '\'', '\\'};
    static final String[] JAVA_ESCAPE_TO_STRS = new String[]{"\\b", "\\r", "\\n", "\\t", "\\f", "\\\"", "\\'", "\\\\"};
    static final char[] SQL_ESCAPE_FROM_CHARS = new char[]{'\'', '"', '\\', '\u0000'};
    static final String[] SQL_ESCCAPE_TO_STRS = new String[]{"\\'", "\\\"", "\\\\", "\\0"};
    static final char[] JSON_ESCAPE_FROM_CHARS = new char[]{'\b', '\r', '\n', '\t', '\f', '"', '\\'};
    static final String[] JSON_ESCAPE_TO_STRS = new String[]{"\\b", "\\r", "\\n", "\\t", "\\f", "\\\"", "\\\\"};
    private static final char[] HEX_CHARS = "0123456789abcdef".toCharArray();
    static final Pattern HTML_PAGA_TAG_PATTERN = Pattern.compile("<p.*?>");
    static final Pattern HTML_BR_TAG_PETTERN = Pattern.compile("<br\\s*/?>");
    static final Pattern HTML_OPEN_PETTERN = Pattern.compile("<.*?>");
    static final String[] acm = new String[]{"", "\r\n"};
    static final String[] acn = new String[]{"", "\n"};
    static final char[] aco = new char[]{'/', '\\', ':', '*', '?', '"', '<', '>', '|', '\u0000'};
    static final String acp;
    static final String[] acq;
    static final char[] acr;
    static final String acs;
    private static final String act = "**";

    public StringHelper() {
    }

    static int indexOf(char[] arr, char elm) {
        int index = 0;

        for(int length = arr.length; index < length; ++index) {
            if (arr[index] == elm) {
                return index;
            }
        }

        return -1;
    }

     
    public static Charset toCharset(String encoding) {
        if (encoding == null) {
            return CHARSET_UTF8;
        } else {
            return "UTF-8".equals(encoding) ? CHARSET_UTF8 : Charset.forName(encoding);
        }
    }

    /**
     * 将fromChars转换为toStrs
     * @param str
     * @param fromChars
     * @param toStrs
     * @return
     */
    public static String escape(CharSequence str,
                                char[] fromChars, String[] toStrs) {
        if (str != null && str.length() != 0 && fromChars != null && fromChars.length != 0) {
            if (toStrs != null && fromChars.length == toStrs.length) {
                int length = str.length();
                StringBuilder sb = null;

                for(int index = 0; index < length; ++index) {
                    char elm = str.charAt(index);
                    int charIndex = indexOf(fromChars, elm);
                    if (charIndex < 0) {
                        if (sb != null) {
                            sb.append(elm);
                        }
                    } else {
                        if (sb == null) {
                            sb = new StringBuilder(2 * length);
                            if (index > 0) {
                                sb.append(str.subSequence(0, index));
                            }
                        }

                        sb.append(toStrs[charIndex]);
                    }
                }

                if (sb == null) {
                    return str.toString();
                } else {
                    return sb.toString();
                }
            } else {
                throw (new StdException("util.err_escape_fromChars_and_toChars_length_not_match")).param("fromChars", fromChars).param("toStrs", toStrs);
            }
        } else {
            return str == null ? null : str.toString();
        }
    }

    public static String escapeCRLF(String str, String replaced) {
        return str != null && str.length() > 0 ? escape(str, CRLF, new String[]{"", replaced}) : str;
    }

    /**
     * 转义html字符
     * @param str
     * @return
     */
    public static String escapeHtml(String str) {
        return escape(str, HTML_ESCAPED, HTML_TO_ESCAPED);
    }


    public static String escapeXml(String str) {
        return escape(str, XML_ESCAPED_CHARS, XML_TO_ESCAPED_STRS);
    }

    
    public static String escapeXmlValue(String str) {
        return escape(str, XML_VALUE_ESCAPED_CHARS, XML_VALUE_ESCAPED_TO_STRS);
    }

    
    public static String escapeXmlAttr(String str) {
        return escape(str, XML_ATTR_ESCAPED, XML_TO_ATTR_STRS);
    }

    public static void escapeCharTo(char c, char[] fromChars,
                                    String[] toStrs, Appendable buf) throws IOException {
        int index = indexOf(fromChars, c);
        if (index < 0) {
            buf.append(c);
        } else {
            buf.append(toStrs[index].toString());
        }

    }

    public static void escapeJsonCharTo(char c, Appendable buf) throws IOException {
        escapeCharTo(c, JSON_ESCAPE_FROM_CHARS, JSON_ESCAPE_TO_STRS, buf);
    }

    public static void escapeTo(CharSequence str, char[] fromChars,
                                String[] toStrs, Appendable buf) throws IOException {
        if (str != null && str.length() != 0 && fromChars != null && fromChars.length != 0) {
            if (toStrs != null && fromChars.length == toStrs.length) {
                int length = str.length();
                int currIndex = 0;

                for(int index = 0; index < length; ++index) {
                    char c = str.charAt(index);
                    int charIndex = indexOf(fromChars, c);
                    if (charIndex >= 0) {
                        if (index > currIndex) {
                            buf.append(str, currIndex, index);
                        }

                        currIndex = index + 1;
                        buf.append(toStrs[charIndex]);
                    }
                }

                if (currIndex < length) {
                    buf.append(str, currIndex, length);
                }

            } else {
                throw (new StdException("util.err_escape_fromChars_and_toChars_length_not_match")).param("fromChars", fromChars).param("toStrs", toStrs);
            }
        }
    }

    public static void escapeJsonTo(CharSequence str, Appendable buf) throws IOException {
        if (str == null) {
            buf.append("null");
        } else {
            escapeTo(str, JSON_ESCAPE_FROM_CHARS, JSON_ESCAPE_TO_STRS, buf);
        }
    }

    public static void escapeXmlTo(CharSequence str, Appendable buf) throws IOException {
        escapeTo(str, XML_ESCAPED_CHARS, XML_TO_ESCAPED_STRS, buf);
    }

    
    public static String unescapeXml(String str) {
        return StringEscapeUtils.unescapeXml(str);
    }

    static final int a(char var0, char var1, char var2, char var3) {
        if (var0 >= '0' && var0 <= '9') {
            int var4 = var0 - 48;
            if (var1 >= '0' && var1 <= '9') {
                var4 = var4 * 10 + (var1 - 48);
                if (var2 >= '0' && var2 <= '9') {
                    var4 = var4 * 10 + (var2 - 48);
                    if (var3 == ';') {
                        return var4;
                    }
                }
            }
        }

        return -1;
    }

    
    public static String escapeJava(String str) {
        return escape(str, JAVA_ESCAPE_FROM_CHARS, JAVA_ESCAPE_TO_STRS);
    }

    
    public static String unescapeJava(String str) {
        return StringEscapeUtils.unescapeJava(str);
    }

    /**
     * Escapes any values it finds into their String form. So a tab becomes the characters '\\' and 't'.
     * @param str
     * @return
     */
    public static String escapeUnicode(String str) {
        int length = str.length();
        StringBuilder sb = new StringBuilder(2 * length);

        for(int index = 0; index < length; ++index) {
            char c = str.charAt(index);
            if (c > 4095) {
                sb.append("\\u" + Integer.toHexString(c));
            } else if (c > 255) {
                sb.append("\\u0" + Integer.toHexString(c));
            } else if (c > 127) {
                sb.append("\\u00" + Integer.toHexString(c));
            } else if (c < ' ') {
                switch(c) {
                    case '\b':
                        sb.append('\\');
                        sb.append('b');
                        break;
                    case '\t':
                        sb.append('\\');
                        sb.append('t');
                        break;
                    case '\n':
                        sb.append('\\');
                        sb.append('n');
                        break;
                    case '\u000b':
                    default:
                        if (c > 15) {
                            sb.append("\\u00" + Integer.toHexString(c));
                        } else {
                            sb.append("\\u000" + Integer.toHexString(c));
                        }
                        break;
                    case '\f':
                        sb.append('\\');
                        sb.append('f');
                        break;
                    case '\r':
                        sb.append('\\');
                        sb.append('r');
                }
            } else {
                switch(c) {
                    case '"':
                        sb.append('\\');
                        sb.append('"');
                        break;
                    case '\'':
                        sb.append('\\');
                        sb.append('\'');
                        break;
                    case '\\':
                        sb.append('\\');
                        sb.append('\\');
                        break;
                    default:
                        sb.append(c);
                }
            }
        }

        return sb.toString();
    }

    
    public static String escapeSql(String str) {
        return escape(str, SQL_ESCAPE_FROM_CHARS, SQL_ESCCAPE_TO_STRS);
    }

    
    public static String escapeJson(String json) {
        return escape(json, JSON_ESCAPE_FROM_CHARS, JSON_ESCAPE_TO_STRS);
    }

    
    public static String unescapeJson(String code) {
        return unescapeJava(code);
    }

    
    public static boolean isWhitespace(char c) {
        return c <= ' ' && (c == ' ' || c == '\n' || c == '\r' || c == '\t' || c == '\f' || c == '\b');
    }

    
    public static boolean onlyContainsWhitespace(String s) {
        if (s == null) {
            return true;
        } else {
            int i = 0;

            for(int length = s.length(); i < length; ++i) {
                if (!isWhitespace(s.charAt(i))) {
                    return false;
                }
            }

            return true;
        }
    }

    
    public static String strip(String str) {
        if (str == null) {
            return null;
        } else {
            int length = str.length();

            int i;
            for(i = 0; i < length && isWhitespace(str.charAt(i)); ++i) {
                ;
            }

            int j;
            for(j = length - 1; j > i && isWhitespace(str.charAt(j)); --j) {
                ;
            }

            return i > j ? null : str.substring(i, j + 1);
        }
    }

    
    public static List<String> split(String str, String sep) {
        if (str != null && sep != null) {
            if (str.length() == 0) {
                return Collections.emptyList();
            } else {
                int index = str.indexOf(sep);
                if (index < 0) {
                    return Collections.singletonList(str);
                } else {
                    ArrayList list = new ArrayList();
                    list.add(str.substring(0, index));
                    int length = sep.length();
                    int fromIndex = index + length;

                    while(true) {
                        index = str.indexOf(sep, fromIndex);
                        if (index < 0) {
                            list.add(str.substring(fromIndex));
                            return list;
                        }

                        list.add(str.substring(fromIndex, index));
                        fromIndex = index + length;
                    }
                }
            }
        } else {
            return null;
        }
    }

    
    public static String[] splitToArray(String str, String sep) {
        List var2 = split(str, sep);
        return var2 == null ? null : (String[])var2.toArray(new String[var2.size()]);
    }

    
    public static List<String> splitOn(String str, char sep) {
        if (str == null) {
            return null;
        } else if (str.length() == 0) {
            return Collections.emptyList();
        } else {
            int index = str.indexOf(sep);
            if (index < 0) {
                return Collections.singletonList(str);
            } else {
                ArrayList list = new ArrayList();
                list.add(str.substring(0, index));
                byte byteLength = 1;
                int fromIndex = index + byteLength;

                while(true) {
                    index = str.indexOf(sep, fromIndex);
                    if (index < 0) {
                        list.add(str.substring(fromIndex));
                        return list;
                    }

                    list.add(str.substring(fromIndex, index));
                    fromIndex = index + byteLength;
                }
            }
        }
    }

    /**
     * 将str分隔后的值trim一下
     * @param str
     * @param sep
     * @return
     */
    public static List<String> stripedSplit(String str, String sep) {
        if (str != null && sep != null) {
            if (str.length() == 0) {
                return Collections.emptyList();
            } else {
                int index = str.indexOf(sep);
                if (index < 0) {
                    str = strip(str);
                    return str == null ? Collections.emptyList() : Collections.singletonList(str);
                } else {
                    ArrayList list = new ArrayList();
                    String subStr = strip(str.substring(0, index));
                    if (subStr != null) {
                        list.add(subStr);
                    }

                    int sepLength = sep.length();
                    int fromIndex = index + sepLength;

                    while(true) {
                        index = str.indexOf(sep, fromIndex);
                        if (index < 0) {
                            subStr = strip(str.substring(fromIndex));
                            if (subStr != null) {
                                list.add(subStr);
                            }

                            return list;
                        }

                        subStr = strip(str.substring(fromIndex, index));
                        if (subStr != null) {
                            list.add(subStr);
                        }

                        fromIndex = index + sepLength;
                    }
                }
            }
        } else {
            return null;
        }
    }

    
    public static String join(Iterable<?> list, String sep) {
        if (list == null) {
            return null;
        } else {
            StringBuilder sb = new StringBuilder();
            boolean isFirst = true;

            Object elm;
            for(Iterator iter = list.iterator(); iter.hasNext(); sb.append(elm)) {
                elm = iter.next();
                if (!isFirst) {
                    sb.append(sep);
                } else {
                    isFirst = false;
                }
            }

            return sb.toString();
        }
    }

    public static String joinArray(Object list, String sep) {
        if (list == null) {
            return null;
        } else {
            StringBuilder sb = new StringBuilder();
            int length = Array.getLength(list);

            for(int index = 0; index < length; ++index) {
                if (index != 0) {
                    sb.append(sep);
                }

                Object elm = Array.get(list, index);
                sb.append(elm);
            }

            return sb.toString();
        }
    }

    
    public static String bytesToHex(byte[] bytes) {
        if (bytes == null) {
            return null;
        } else {
            int length = bytes.length;
            char[] arr = new char[length * 2];

            for(int index = 0; index < length; ++index) {
                int var4 = bytes[index] & 255;
                arr[index * 2] = HEX_CHARS[var4 >>> 4];
                arr[index * 2 + 1] = HEX_CHARS[var4 & 15];
            }

            return new String(arr);
        }
    }

    
    public static byte[] hexToBytes(String str) {
        if (str == null) {
            return null;
        } else if (str.length() == 0) {
            return EMPTY_BYTES;
        } else {
            if (str.startsWith("0x")) {
                str = str.substring(2);
            }

            int arrLength = str.length() / 2;
            byte[] arr = new byte[arrLength];

            for(int index = 0; index < arrLength; ++index) {
                int pos = index * 2;
                arr[index] = decodeHexByte(str, pos);
            }

            return arr;
        }
    }

    
    public static String longToHex(long value, int padLen) {
        return leftPad(Long.toHexString(value), padLen, '0');
    }

    
    public static String intToHex(int value, int padLen) {
        return leftPad(Integer.toHexString(value), padLen, '0');
    }

    /**
     * Helper to decode half of a hexadecimal number from a string.
     * @param c
     * @return
     */
    public static int decodeHexNibble(char c) {
        if (c >= '0' && c <= '9') {
            return c - 48;
        } else if (c >= 'A' && c <= 'F') {
            return c - 65 + 10;
        } else {
            return c >= 'a' && c <= 'f' ? c - 97 + 10 : -1;
        }
    }

    public static byte decodeHexByte(CharSequence s, int pos) {
        int value1 = decodeHexNibble(s.charAt(pos));
        int value2 = decodeHexNibble(s.charAt(pos + 1));
        if (value1 != -1 && value2 != -1) {
            return (byte)((value1 << 4) + value2);
        } else {
            throw new IllegalArgumentException(String.format("invalid hex byte '%s' at index %d of '%s'", s.subSequence(pos, pos + 2), pos, s));
        }
    }

    
    public static String leftPad(String str, int len, char padChar) {
        if (len > (Integer)abS.get()) {
            throw (new StdException("util.err_string_pad_len_is_too_large")).param("len", len);
        } else if (str.length() >= len) {
            return str;
        } else {
            StringBuilder sb = new StringBuilder(len);
            int index = 0;

            for(int size = len - str.length(); index < size; ++index) {
                sb.append(padChar);
            }

            sb.append(str);
            return sb.toString();
        }
    }

    
    public static String rightPad(String str, int len, char padChar) {
        if (len > (Integer)abS.get()) {
            throw (new StdException("util.err_string_pad_len_is_too_large")).param("len", len);
        } else if (str.length() >= len) {
            return str;
        } else {
            StringBuilder sb = new StringBuilder(len);
            sb.append(str);
            int index = 0;

            for(int size = len - str.length(); index < size; ++index) {
                sb.append(padChar);
            }

            return sb.toString();
        }
    }

    
    public static String replaceChars(String str, String searchChars, String replaceChars) {
        if (!isEmpty(str) && !isEmpty(searchChars)) {
            if (replaceChars != null && searchChars.length() == replaceChars.length()) {
                int length = str.length();
                StringBuilder sb = null;

                for(int index = 0; index < length; ++index) {
                    char c = str.charAt(index);
                    int var7 = searchChars.indexOf(c);
                    if (var7 >= 0) {
                        if (sb == null) {
                            sb = new StringBuilder(length);
                            sb.append(str.substring(0, index));
                        }

                        sb.append(replaceChars.charAt(var7));
                    } else if (sb != null) {
                        sb.append(c);
                    }
                }

                if (sb != null) {
                    return sb.toString();
                } else {
                    return str;
                }
            } else {
                throw (new StdException("util.err_searchChars_and_replaceChars_length_not_match")).param("searchChars", searchChars).param("replaceChars", replaceChars);
            }
        } else {
            return str;
        }
    }

    
    public static String replace(String str, String oldSub, String newSub) {
        if (str != null && oldSub != null && oldSub.length() > 0) {
            if (newSub == null) {
                newSub = "";
            }

            int index = str.indexOf(oldSub);
            if (index < 0) {
                return str;
            } else {
                StringBuilder sb = new StringBuilder(oldSub.length() > newSub.length() ? str.length() : str.length() * 2);
                int offset = 0;

                do {
                    sb.append(str.substring(offset, index));
                    sb.append(newSub);
                    offset = index + oldSub.length();
                    index = str.indexOf(oldSub, offset);
                } while(index >= 0);

                if (offset < str.length()) {
                    sb.append(str.substring(offset));
                }

                return sb.toString();
            }
        } else {
            return str;
        }
    }

    
    public static int indexOfAnyChar(CharSequence str, String chars) {
        if (str != null && str.length() > 0) {
            if (chars != null && chars.length() > 0) {


                for(int i = 0; i < str.length(); ++i) {
                    if (chars.indexOf(str.charAt(i)) >= 0) {
                        return i;
                    }
                }

                return -1;
            } else {
                return -1;
            }
        } else {
            return -1;
        }
    }

    
    public static boolean containsAnyChar(CharSequence str, String chars) {
        return indexOfAnyChar(str, chars) >= 0;
    }

    public static boolean isAsciiLetter(char c) {
        return c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z';
    }

    public static boolean isAsiciiString(String s) {
        if (s != null && s.length() > 0) {
            int i = 0;

            for(int length = s.length(); i < length; ++i) {
                char c = s.charAt(i);
                if (isAsciiLetter(c)) {
                    return false;
                }
            }

            return true;
        } else {
            return false;
        }
    }

    public static boolean isSafeAsciiToken(String str) {
        if (str != null && str.length() > 0) {
            if (isDigit(str.charAt(0))) {
                return false;
            } else {
                int i = 0;

                for(int length = str.length(); i < length; ++i) {
                    char var3 = str.charAt(i);
                    if (!isDigit(var3) && !isAsciiLetter(var3) && var3 != '_') {
                        return false;
                    }
                }

                return true;
            }
        } else {
            return false;
        }
    }

    public static String random(int n, String allowChars) {
        if (n > (Integer)abR.get()) {
            throw (new StdException("util.err_string_random_count_too_large")).param("count", n);
        } else {
            char[] arr = new char[n];
            IRandom random = MathHelper.random();
            int charLength = allowChars.length();

            for(int index = 0; index < n; ++index) {
                arr[index] = allowChars.charAt(random.nextInt(charLength));
            }

            return new String(arr);
        }
    }

    public static String generateUUID() {
        return replace(UUID.randomUUID().toString(), "-", "");
    }

    
    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() <= 0;
    }

    /**
     * 是否为null或者仅包含空白字符
     * @param str
     * @return
     */
    public static boolean isBlank(CharSequence str) {
        int length;
        if (str != null && (length = str.length()) != 0) {
            for(int i = 0; i < length; ++i) {
                if (!isWhitespace(str.charAt(i))) {
                    return false;
                }
            }

            return true;
        } else {
            return true;
        }
    }

    public static boolean hasText(CharSequence text) {
        return !isBlank(text);
    }

    
    public static boolean isNotBlank(CharSequence text) {
        return !isBlank(text);
    }

    public static void internList(List<String> strs) {
        if (strs != null) {
            int i = 0;

            for(int var2 = strs.size(); i < var2; ++i) {
                String var3 = (String)strs.get(i);
                if (var3 != null) {
                    strs.set(i, var3);
                }
            }

        }
    }

    
    public static String capitalize(String str) {
        return str != null && str.length() > 0 ? Character.toUpperCase(str.charAt(0)) + str.substring(1) : str;
    }

    
    public static String decapitalize(String str) {
        return str != null && str.length() > 0 ? Character.toLowerCase(str.charAt(0)) + str.substring(1) : str;
    }

    public static String camelCaseToUnderscore(String str, boolean lower) {
        return str == null ? null : CaseFormat.LOWER_CAMEL.to(lower ? CaseFormat.LOWER_UNDERSCORE : CaseFormat.UPPER_UNDERSCORE, str);
    }

    public static String camelCaseToHyphen(String str) {
        return str == null ? null : CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_HYPHEN, str);
    }

    
    public static final String camelCase(String str, boolean firstUpper) {
        return camelCase(str, '_', firstUpper);
    }

    
    public static final String camelCase(String str, char separator, boolean firstUpper) {
        if (str != null && !str.isEmpty()) {
            str = str.toLowerCase();
            StringBuilder var3 = new StringBuilder();
            boolean var4 = false;
            char var5;
            if (str.length() > 1 && str.charAt(1) == separator) {
                var5 = firstUpper ? Character.toUpperCase(str.charAt(0)) : str.charAt(0);
                var3.append(var5);
            } else {
                var5 = firstUpper ? Character.toUpperCase(str.charAt(0)) : str.charAt(0);
                var3.append(var5);
            }

            int var8 = 1;

            for(int var6 = str.length(); var8 < var6; ++var8) {
                char var7 = str.charAt(var8);
                if (var7 == separator) {
                    var4 = true;
                } else if (var4) {
                    var3.append(Character.toUpperCase(var7));
                    var4 = false;
                } else {
                    var3.append(var7);
                }
            }

            return var3.toString();
        } else {
            return str;
        }
    }

    
    public static boolean startsWithIgnoreCase(String str, String subStr) {
        return str != null && subStr != null ? str.regionMatches(true, 0, subStr, 0, subStr.length()) : false;
    }

    
    public static boolean endsWithIgnoreCase(String str, String subStr) {
        if (str != null && subStr != null) {
            return str.length() < subStr.length() ? false : str.regionMatches(true, str.length() - subStr.length(), subStr, 0, subStr.length());
        } else {
            return false;
        }
    }

    public static int indexOfIgnoreCase(String str, String subStr) {
        if (str != null && subStr != null) {
            if (str.length() < subStr.length()) {
                return -1;
            } else {
                int var2 = 0;

                for(int var3 = str.length() - subStr.length(); var2 < var3; ++var2) {
                    if (str.regionMatches(true, var2, subStr, 0, subStr.length())) {
                        return var2;
                    }
                }

                return -1;
            }
        } else {
            return -1;
        }
    }

    
    public static boolean startsWithAt(String str, String sub, int pos) {
        return str != null && sub != null && str.length() >= sub.length() ? str.regionMatches(pos, sub, 0, sub.length()) : false;
    }

    
    public static boolean startWith(CharSequence s, char c) {
        if (isEmpty(s)) {
            return false;
        } else {
            return s.charAt(0) == c;
        }
    }

    
    public static boolean endWith(CharSequence s, char c) {
        if (isEmpty(s)) {
            return false;
        } else {
            return s.charAt(s.length() - 1) == c;
        }
    }

    
    public static String repeat(String str, int count) {
        if (count > (Integer)abR.get()) {
            throw (new StdException("util.err_string_repeat_count_too_large")).param("count", count);
        } else {
            StringBuilder sb = new StringBuilder(str.length() * count);

            for(int var3 = 0; var3 < count; ++var3) {
                sb.append(str);
            }

            return sb.toString();
        }
    }

    
    public static int countChar(String str, char c) {
        if (str == null) {
            return 0;
        } else {
            int count = 0;
            int index = 0;

            for(int length = str.length(); index < length; ++index) {
                if (str.charAt(index) == c) {
                    ++count;
                }
            }

            return count;
        }
    }

    /**
     * 删除字符串中的html标签，仅保留文本。对p和br进行特殊处理，将它们替换为回车换行。
     * @param str
     * @return
     */
    public static String removeHtmlTag(String str) {
        if (str == null) {
            return null;
        } else {
            str = HTML_PAGA_TAG_PATTERN.matcher(str).replaceAll("\r\n");
            str = HTML_BR_TAG_PETTERN.matcher(str).replaceAll("\r\n");
            str = HTML_OPEN_PETTERN.matcher(str).replaceAll("");
            return str;
        }
    }

    /**
     * 尽量在pos位置两侧取文本，文本总长度不超过len
     * @param str
     * @param pos
     * @param len
     * @return
     */
    public static String shortText(CharSequence str, int pos, int len) {
        int var3 = str.length();
        if (pos < 0) {
            pos = var3;
        }

        int var4 = Math.max(pos - len / 2, 0);
        int var5 = Math.min(var4 + len, var3);
        var4 = Math.min(Math.max(0, var3 - len), var4);
        StringBuilder var6 = new StringBuilder(var3 + 2);
        if (pos < 0) {
            var6.append("[]");
        } else {
            var6.append(str, var4, pos);
            var6.append('[');
            if (pos < var3) {
                var6.append(str.charAt(pos));
            }

            var6.append(']');
        }

        if (pos < var5) {
            var6.append(str, pos + 1, var5);
        }

        return var6.toString();
    }

    
    public static String limitLen(CharSequence str, int offset, int maxWidth) {
        if (str == null) {
            return null;
        } else {
            if (maxWidth < 4) {
                maxWidth = 4;
            }

            if (str.length() <= maxWidth) {
                return str.toString();
            } else {
                if (offset < 0) {
                    offset = 0;
                }

                if (offset > str.length()) {
                    offset = str.length();
                }

                if (str.length() - offset < maxWidth - 3) {
                    offset = str.length() - (maxWidth - 3);
                }

                String var3 = "...";
                if (offset <= 4) {
                    return str.subSequence(0, maxWidth - 3).toString() + "...";
                } else {
                    if (maxWidth < 7) {
                        maxWidth = 7;
                    }

                    return offset + maxWidth - 3 < str.length() ? "..." + limitLen(str.subSequence(offset, str.length()), maxWidth - 3) : "..." + str.subSequence(str.length() - (maxWidth - 3), str.length());
                }
            }
        }
    }

    
    public static String limitLen(CharSequence str, int maxWidth) {
        return limitLen(str, 0, maxWidth);
    }

    
    public static byte[] utf8Bytes(String str) {
        if (str == null) {
            return null;
        } else {
            return str.length() <= 0 ? EMPTY_BYTES : str.getBytes(CHARSET_UTF8);
        }
    }

    
    public static String md5Hash(String str) {
        byte[] bytes = utf8Bytes(str);
        return bytes == null ? null : bytesToHex(HashHelper.md5(bytes));
    }

    
    public static String sha256Hash(String str) {
        byte[] bytes = utf8Bytes(str);
        return bytes == null ? null : bytesToHex(HashHelper.sha256(bytes, (byte[])null));
    }

    
    public static String sha512Hash(String str) {
        byte[] var1 = utf8Bytes(str);
        return var1 == null ? null : bytesToHex(HashHelper.sha512(var1, (byte[])null));
    }

    
    public static String encodeBase64(byte[] bytes) {
        return bytes == null ? null : Base64.getEncoder().encodeToString(bytes);
    }

    
    public static String encodeBase64Url(byte[] bytes) {
        return bytes == null ? null : Base64.getUrlEncoder().encodeToString(bytes);
    }

    
    public static byte[] decodeBase64(String str) {
        if (str == null) {
            return null;
        } else {
            return str.indexOf(10) >= 0 ? Base64.getMimeDecoder().decode(str) : Base64.getDecoder().decode(str);
        }
    }

    /**
     * Checks whether the String a valid Java number. Valid numbers include hexadecimal marked with the "0x" qualifier, scientific notation and numbers marked with a type qualifier (e.g. 123L).
     *
     * Null and blank string will return false.
     * @param str
     * @return
     */
    public static boolean isNumber(CharSequence str) {
        if (str != null && str.length() != 0) {
            int var1 = str.length();
            boolean var2 = false;
            boolean var3 = false;
            boolean var4 = false;
            boolean var5 = false;
            int var6 = str.charAt(0) == '-' ? 1 : 0;
            int var7;
            char var8;
            if (var1 > var6 + 1 && str.charAt(var6) == '0' && str.charAt(var6 + 1) == 'x') {
                var7 = var6 + 2;
                if (var7 == var1) {
                    return false;
                } else {
                    while(var7 < var1) {
                        var8 = str.charAt(var7);
                        if ((var8 < '0' || var8 > '9') && (var8 < 'a' || var8 > 'f') && (var8 < 'A' || var8 > 'F')) {
                            return false;
                        }

                        ++var7;
                    }

                    return true;
                }
            } else {
                --var1;

                for(var7 = var6; var7 < var1 || var7 < var1 + 1 && var4 && !var5; ++var7) {
                    var8 = str.charAt(var7);
                    if (var8 >= '0' && var8 <= '9') {
                        var5 = true;
                        var4 = false;
                    } else if (var8 == '.') {
                        if (var3 || var2) {
                            return false;
                        }

                        var3 = true;
                    } else if (var8 != 'e' && var8 != 'E') {
                        if (var8 != '+' && var8 != '-') {
                            return false;
                        }

                        if (!var4) {
                            return false;
                        }

                        var4 = false;
                        var5 = false;
                    } else {
                        if (var2) {
                            return false;
                        }

                        if (!var5) {
                            return false;
                        }

                        var2 = true;
                        var4 = true;
                    }
                }

                if (var7 < str.length()) {
                    var8 = str.charAt(var7);
                    if (var8 >= '0' && var8 <= '9') {
                        return true;
                    }

                    if (var8 == 'e' || var8 == 'E') {
                        return false;
                    }

                    if (!var4 && (var8 == 'd' || var8 == 'D' || var8 == 'f' || var8 == 'F')) {
                        return var5;
                    }

                    if (var8 == 'l' || var8 == 'L') {
                        return var5 && !var2;
                    }

                    if (var8 == '.') {
                        return !var3 && var5 && !var4;
                    }
                }

                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Turns a string value into a java.lang.Number. First, the value is examined for a type qualifier on the end ( 'f','F','d','D','l','L'). If it is found, it starts trying to create succissively larger types from the type specified until one is found that can hold the value.
     *
     * If a type specifier is not found, it will check for a decimal point and then try successively larger types from Integer to BigInteger and from Float to BigDecimal.
     *
     * If the string starts with "0x" or "-0x", it will be interpreted as a hexadecimal integer. Values with leading 0's will not be interpreted as octal.
     * @param value
     * @return
     */
    public static Number parseNumber(String value) {
        if (value == null) {
            return null;
        } else if (value.length() == 0) {
            throw new StdException("util.err_empty_string_not_number");
        } else if (value.startsWith("--")) {
            return null;
        } else {
            value = value.trim();
            if (!value.startsWith("0x") && !value.startsWith("-0x")) {
                char var1 = value.charAt(value.length() - 1);
                int var5 = value.indexOf(46);
                int var6 = value.indexOf(101) + value.indexOf(69) + 1;
                String var2;
                String var3;
                if (var5 > -1) {
                    if (var6 > -1) {
                        if (var6 < var5) {
                            throw (new StdException("util.err_not_valid_number")).param("value", value);
                        }

                        var3 = value.substring(var5 + 1, var6);
                    } else {
                        var3 = value.substring(var5 + 1);
                    }

                    var2 = value.substring(0, var5);
                } else {
                    if (var6 > -1) {
                        var2 = value.substring(0, var6);
                    } else {
                        var2 = value;
                    }

                    var3 = null;
                }

                String var4;
                if (!Character.isDigit(var1)) {
                    if (var6 > -1 && var6 < value.length() - 1) {
                        var4 = value.substring(var6 + 1, value.length() - 1);
                    } else {
                        var4 = null;
                    }

                    String var17 = value.substring(0, value.length() - 1);
                    boolean var18 = bZ(var2) && bZ(var4);
                    switch(var1) {
                        case 'D':
                        case 'd':
                            break;
                        case 'F':
                        case 'f':
                            try {
                                Float var9 = Float.valueOf(var17);
                                if (var9.isInfinite() || var9 == 0.0F && !var18) {
                                    break;
                                }

                                return var9;
                            } catch (NumberFormatException var15) {
                                break;
                            }
                        case 'L':
                        case 'l':
                            if (var3 != null || var4 != null || !isDigits(var17) || var17.charAt(0) != '-' && !Character.isDigit(var17.charAt(0))) {
                                throw (new StdException("util.err_not_valid_number")).param("value", var17);
                            } else {
                                try {
                                    return Long.valueOf(var17);
                                } catch (NumberFormatException var11) {
                                    return new BigInteger(var17);
                                }
                            }
                        default:
                            throw (new StdException("util.err_not_valid_number")).param("value", value);
                    }

                    try {
                        Double var19 = Double.valueOf(var17);
                        if (!var19.isInfinite() && ((double)var19.floatValue() != 0.0D || var18)) {
                            return var19;
                        }
                    } catch (NumberFormatException var14) {
                        ;
                    }

                    try {
                        return new BigDecimal(var17);
                    } catch (NumberFormatException var13) {
                        throw (new StdException("util.err_not_valid_number")).param("value", value);
                    }
                } else {
                    if (var6 > -1 && var6 < value.length() - 1) {
                        var4 = value.substring(var6 + 1, value.length());
                    } else {
                        var4 = null;
                    }

                    if (var3 == null && var4 == null) {
                        try {
                            return Integer.decode(value);
                        } catch (NumberFormatException var12) {
                            try {
                                return Long.valueOf(value);
                            } catch (NumberFormatException var10) {
                                return new BigInteger(value);
                            }
                        }
                    } else {
                        boolean var7 = bZ(var2) && bZ(var4);

                        try {
                            Double var8 = Double.valueOf(value);
                            if (!var8.isInfinite() && (var8 != 0.0D || var7)) {
                                return var8;
                            }
                        } catch (NumberFormatException var16) {
                            ;
                        }

                        return new BigDecimal(value);
                    }
                }
            } else {
                return Integer.decode(value);
            }
        }
    }

    
    public static boolean isDigit(char c) {
        return c <= '9' && c >= '0';
    }

    
    public static boolean isDigits(String str) {
        if (str != null && str.length() != 0) {
            for(int var1 = 0; var1 < str.length(); ++var1) {
                if (!isDigit(str.charAt(var1))) {
                    return false;
                }
            }

            return true;
        } else {
            return false;
        }
    }

    
    public static boolean isAllChar(String str, char c) {
        if (str != null && str.length() > 0) {
            int var2 = 0;

            for(int var3 = str.length(); var2 < var3; ++var2) {
                if (str.charAt(var2) != c) {
                    return false;
                }
            }

            return true;
        } else {
            return false;
        }
    }

    static boolean bZ(String var0) {
        if (var0 == null) {
            return true;
        } else {
            for(int var1 = var0.length() - 1; var1 >= 0; --var1) {
                if (var0.charAt(var1) != '0') {
                    return false;
                }
            }

            return var0.length() > 0;
        }
    }

    /**
     * Makes sure that the POSTed data is conforms to certain rules. These rules are:
     * The data always ends with a newline (some browsers, such as NS4.x series, does not send a newline at the end, which makes the diffs a bit strange sometimes.
     * The CR/LF/CRLF mess is normalized to plain CRLF.
     * @param str
     * @return
     */
    public static String normalizeCRLF(String str) {
        return escape(str, CRLF, acm);
    }

    
    public static String normalizeLF(String str) {
        return escape(str, CRLF, acm);
    }

    
    public static boolean isValidClassName(String str) {
        if (str != null && str.length() > 0) {
            if (!Character.isJavaIdentifierStart(str.charAt(0))) {
                return false;
            } else {
                int var1 = 1;

                for(int var2 = str.length(); var1 < var2; ++var1) {
                    char var3 = str.charAt(var1);
                    if (!Character.isJavaIdentifierPart(var3) && var3 != '.' && var3 != '$') {
                        return false;
                    }
                }

                return true;
            }
        } else {
            return false;
        }
    }

    
    public static boolean isValidJavaVarName(String s) {
        if (s != null && s.length() != 0) {
            if (!Character.isJavaIdentifierStart(s.charAt(0))) {
                return false;
            } else {
                int var1 = 1;

                for(int var2 = s.length(); var1 < var2; ++var1) {
                    if (!Character.isJavaIdentifierPart(s.charAt(var1))) {
                        return false;
                    }
                }

                return true;
            }
        } else {
            return false;
        }
    }

    
    public static boolean isValidSimpleVarName(String s) {
        return isValidJavaVarName(s) && s.indexOf(36) < 0;
    }

    
    public static boolean isValidPropName(String name) {
        if (name != null && name.length() != 0) {
            if (!Character.isJavaIdentifierStart(name.charAt(0))) {
                return false;
            } else {
                boolean var1 = false;
                int var2 = 1;

                for(int var3 = name.length(); var2 < var3; ++var2) {
                    char var4 = name.charAt(var2);
                    if (var1) {
                        if (!Character.isJavaIdentifierStart(var4)) {
                            return false;
                        }

                        var1 = false;
                    } else if (!Character.isJavaIdentifierPart(var4)) {
                        if (var4 != '.') {
                            return false;
                        }

                        var1 = true;
                    }
                }

                return true;
            }
        } else {
            return false;
        }
    }

    
    public static boolean isValidConfigVar(String var0) {
        if (isEmpty(var0)) {
            return false;
        } else {
            byte var1 = 0;
            int var4 = var1 + 1;
            char var2 = var0.charAt(var1);
            if (!isAsciiLetter(var2)) {
                return false;
            } else {
                char var3 = var2;

                while(true) {
                    while(var4 < var0.length()) {
                        var2 = var0.charAt(var4++);
                        if (!isAsciiLetter(var2) && !isDigit(var2)) {
                            if (var2 != '.' && var2 != '-') {
                                return false;
                            }

                            if (var2 == '.' && (var3 == '.' || var3 == '-')) {
                                return false;
                            }

                            if (var2 == '-' && (var3 == '.' || var3 == '-')) {
                                return false;
                            }

                            var3 = var2;
                        } else {
                            var3 = var2;
                        }
                    }

                    return true;
                }
            }
        }
    }

    
    public static boolean isValidHtmlAttrName(String s) {
        if (s != null && s.length() != 0) {
            char var1 = s.charAt(0);
            return var1 != '$' && (Character.isJavaIdentifierStart(var1) || var1 == ':' || var1 == '@') ? isValidXmlName(s, true, true) : false;
        } else {
            return false;
        }
    }

    
    public static boolean isValidXmlName(String s, boolean allowColon, boolean allowDot) {
        if (s != null && s.length() != 0) {
            char var3 = s.charAt(0);
            if (Character.isJavaIdentifierStart(var3) && var3 != '$') {
                int var4 = 1;

                int var5;
                for(var5 = s.length() - 1; var4 < var5; ++var4) {
                    var3 = s.charAt(var4);
                    if (var3 != '-') {
                        if (allowColon && var3 == ':') {
                            if (!Character.isJavaIdentifierPart(s.charAt(var4 - 1))) {
                                return false;
                            }
                        } else if (allowDot && var3 == '.') {
                            if (!Character.isJavaIdentifierPart(s.charAt(var4 - 1))) {
                                return false;
                            }
                        } else if (!Character.isJavaIdentifierPart(var3) || var3 == '$') {
                            return false;
                        }
                    }
                }

                if (!Character.isJavaIdentifierPart(s.charAt(var5))) {
                    return false;
                } else {
                    return true;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    
    public static boolean isValidIdentifier(String s) {
        if (s != null && s.length() > 0) {
            if (!Character.isJavaIdentifierStart(s.charAt(0))) {
                return false;
            } else {
                int var1 = 1;

                for(int var2 = s.length(); var1 < var2; ++var1) {
                    char var3 = s.charAt(var1);
                    if (!Character.isJavaIdentifierPart(var3) && var3 != '.' && var3 != '-' && var3 != '/') {
                        return false;
                    }
                }

                return true;
            }
        } else {
            return false;
        }
    }

    
    public static boolean isValidInternalIdentifier(String s) {
        if (s != null && s.length() > 0) {
            int var1 = 0;
            if (s.charAt(0) == '@') {
                ++var1;
                if (s.length() <= 1) {
                    return false;
                }
            }

            if (!Character.isJavaIdentifierStart(s.charAt(var1))) {
                return false;
            } else {
                int var2 = var1 + 1;

                for(int var3 = s.length(); var2 < var3; ++var2) {
                    char var4 = s.charAt(var2);
                    if (!Character.isJavaIdentifierPart(var4) && var4 != '.' && var4 != ':' && var4 != '-') {
                        return false;
                    }
                }

                return true;
            }
        } else {
            return false;
        }
    }

    /**
     * 匹配 *xxx, xxx* 或者*xxx*模式
     * @param str
     * @param pattertn
     * @return
     */
    public static boolean matchSimplePattern(String str, String pattertn) {
        if (str != null && pattertn != null) {
            if (pattertn.startsWith("*")) {
                if (pattertn.length() == 1) {
                    return true;
                } else if (pattertn.endsWith("*")) {
                    String var2 = pattertn.substring(1, pattertn.length() - 1);
                    return str.indexOf(var2) >= 0;
                } else {
                    return str.regionMatches(str.length() - pattertn.length() + 1, pattertn, 1, pattertn.length() - 1);
                }
            } else {
                return pattertn.endsWith("*") ? str.regionMatches(0, pattertn, 0, pattertn.length() - 1) : str.equals(pattertn);
            }
        } else {
            return false;
        }
    }


    public static boolean matchSimplePatterns(String str, String[] patterns) {
        if (str != null && patterns != null) {
            String[] var2 = patterns;
            int var3 = patterns.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                String var5 = var2[var4];
                if (matchSimplePattern(str, var5)) {
                    return true;
                }
            }

            return false;
        } else {
            return false;
        }
    }

    
    public static boolean matchSimplePatternSet(String str, Collection<String> patterns) {
        if (str != null && patterns != null) {
            Iterator var2 = patterns.iterator();

            String var3;
            do {
                if (!var2.hasNext()) {
                    return false;
                }

                var3 = (String)var2.next();
            } while(!matchSimplePattern(str, var3));

            return true;
        } else {
            return false;
        }
    }

    static String an(Object var0) {
        return var0 == null ? null : var0.toString();
    }

    public static SimpleTextTemplate buildTemplate(String tpl, String leftBrace, String rightBrace) {
        return new SimpleTextTemplate(tpl, leftBrace, rightBrace);
    }

    public static String renderTemplate(String tpl, String leftBrace, String rightBrace, Function<Object, Object> transformer) {
        return tpl != null && tpl.length() != 0 ? buildTemplate(tpl, leftBrace, rightBrace).render(Transformers.forFunction(transformer)) : "";
    }

    /**
     * 将模板文本中的{varName}形式的部分替换为varName对应的变量值
     * @param template
     * @param vars
     * @return
     */
    public static String renderTemplate(String template, Map<String, Object> vars) {
        return template != null && template.length() != 0 ? buildTemplate(template, "{", "}").render(Transformers.forMap(vars)) : "";
    }

    
    public static String quote(String text) {
        return text == null ? null : "\"" + escapeJava(text) + '"';
    }

    
    public static String unquote(String text) {
        if (text != null && text.length() > 2) {
            char var1 = text.charAt(0);
            char var2 = text.charAt(text.length() - 1);
            return (var1 != '\'' || var2 != '\'') && (var1 != '"' || var2 != '"') ? text : unescapeJava(text.substring(0, text.length() - 1));
        } else {
            return text;
        }
    }

    public Object parseJson(String var1) {
        return JsonTool.deserialize(var1);
    }






    
    public static String encodeURL(String str) {
        return encodeURL(str, "UTF-8");
    }

    
    public static String decodeURL(String str) {
        return decodeURL(str, "UTF-8");
    }

    
    public static String encodeURL(String str, String encoding) {
        if (str != null && !str.isEmpty()) {
            try {
                return URLEncoder.encode(str, encoding == null ? "UTF-8" : encoding);
            } catch (Exception var3) {
                throw StdException.adapt(var3);
            }
        } else {
            return str;
        }
    }

    
    public static String decodeURL(String str, String encoding) {
        if (str != null && !str.isEmpty()) {
            try {
                return URLDecoder.decode(str, encoding == null ? "UTF-8" : encoding);
            } catch (Exception var3) {
                throw StdException.adapt(var3);
            }
        } else {
            return str;
        }
    }










    
    public static Map<String, Object> parseQuery(String query, String encoding) {
        return parseQuery(query, encoding, false);
    }

    
    public static Map<String, Object> parseQuery(String query, String encoding,
                                                 boolean allowNullValue) {
        if (query != null && query.length() > 0) {
            LinkedHashMap var3 = new LinkedHashMap();
            int var4 = 0;

            while(true) {
                int var5 = query.indexOf(38, var4);
                if (var5 < 0) {
                    var5 = query.length();
                }

                if (var4 >= var5) {
                    if (var5 >= query.length()) {
                        break;
                    }

                    var4 = var5 + 1;
                } else {
                    String var6 = query.substring(var4, var5);
                    int var7 = var6.indexOf(61);
                    String var8;
                    String var9;
                    if (var7 < 0) {
                        var8 = var6;
                        if (allowNullValue) {
                            var9 = null;
                        } else {
                            var9 = "";
                        }
                    } else {
                        var8 = var6.substring(0, var7);
                        var9 = var6.substring(var7 + 1);
                    }

                    var8 = decodeURL(var8, encoding);
                    if (var9 != null && !var9.isEmpty()) {
                        var9 = decodeURL(var9, encoding);
                    }

                    Object var10 = var3.get(var8);
                    if (var10 == null) {
                        var3.put(var8, var9);
                    } else if (var10 instanceof List) {
                        ((List)var10).add(var9);
                    } else {
                        ArrayList var11 = new ArrayList();
                        var11.add((String)var10);
                        var11.add(var9);
                        var3.put(var8, var9);
                    }

                    if (var5 >= query.length()) {
                        break;
                    }

                    var4 = var5 + 1;
                }
            }

            return var3;
        } else {
            return null;
        }
    }

    public static String toString(Object value, String defaultValue) {
        return value == null ? defaultValue : value.toString();
    }

    public static String encodeQuery(Map<String, ?> query, String encoding) {
        if (query == null) {
            return null;
        } else if (query.isEmpty()) {
            return "";
        } else {
            StringBuilder var2 = new StringBuilder();
            boolean var3 = true;
            Iterator var4 = query.entrySet().iterator();

            while(true) {
                while(var4.hasNext()) {
                    Entry var5 = (Entry)var4.next();
                    String var6 = encodeURL((String)var5.getKey(), encoding);
                    Object var7 = var5.getValue();
                    if (var7 instanceof List) {
                        List var8 = (List)var7;
                        Iterator var9 = var8.iterator();

                        while(var9.hasNext()) {
                            Object var10 = var9.next();
                            if (var3) {
                                var3 = false;
                            } else {
                                var2.append('&');
                            }

                            var2.append(var6);
                            var2.append('=');
                            var2.append(encodeURL(toString(var10, "")));
                        }
                    } else {
                        if (var3) {
                            var3 = false;
                        } else {
                            var2.append('&');
                        }

                        var2.append(var6);
                        if (var7 != null) {
                            var2.append('=');
                            var2.append(encodeURL(toString(var7, "")));
                        }
                    }
                }

                return var2.toString();
            }
        }
    }

    
    public static String appendQuery(String url, String query) {
        if (url == null) {
            return null;
        } else if (query != null && query.length() > 0) {
            int var2 = url.indexOf(63);
            if (var2 < 0) {
                return url + "?" + query;
            } else {
                return url.endsWith("?") ? url + query : url + "&" + query;
            }
        } else {
            return url;
        }
    }

    public static String beanPropName(String name) {
        if (name != null && name.length() != 0) {
            if (name.length() > 1 && Character.isUpperCase(name.charAt(1)) && Character.isUpperCase(name.charAt(0))) {
                return name;
            } else {
                char[] var1 = name.toCharArray();
                var1[0] = Character.toLowerCase(var1[0]);
                return new String(var1);
            }
        } else {
            return name;
        }
    }

    


    static boolean ca(String var0) {
        int var1 = 0;

        for(int var2 = var0.length(); var1 < var2; ++var1) {
            char var3 = var0.charAt(var1);
            if (var3 >= 0 && var3 <= 31) {
                return true;
            }
        }

        return false;
    }

    static String cb(String var0) {
        StringBuilder var1 = new StringBuilder(var0.length());
        int var2 = 0;

        for(int var3 = var0.length(); var2 < var3; ++var2) {
            char var4 = var0.charAt(var2);
            if (var4 >= 0 && var4 <= 31) {
                var1.append('_');
            } else {
                var1.append(var4);
            }
        }

        return var1.toString();
    }

    
    public static boolean isValidFileName(String path) {
        if (isEmpty(path)) {
            return false;
        } else if (ca(path)) {
            return false;
        } else {
            return !containsAnyChar(path, acp);
        }
    }

    
    public static boolean isValidFilePath(String path) {
        if (isEmpty(path)) {
            return false;
        } else if (ca(path)) {
            return false;
        } else {
            return !containsAnyChar(path, acs) && path.indexOf("//") < 0;
        }
    }

    
    public static String safeFileName(String fileName) {
        if (isEmpty(fileName)) {
            return fileName;
        } else {
            if (ca(fileName)) {
                fileName = cb(fileName);
            }

            return escape(fileName, aco, acq);
        }
    }

    
    public static String appendPath(String path, String relativePath) {
        if (relativePath != null && relativePath.length() > 0) {
            if (!path.endsWith("/")) {
                return !relativePath.startsWith("/") ? path + "/" + relativePath : path + relativePath;
            } else {
                return !relativePath.startsWith("/") ? path + relativePath : path + relativePath.substring(1);
            }
        } else {
            return path;
        }
    }

    
    public static String normalizePath(String path) {
        if (path == null) {
            return null;
        } else {
            String var1 = path.replace('\\', '/');
            int var2 = var1.indexOf(":");
            String var3 = "";
            if (var2 != -1) {
                var3 = var1.substring(0, var2 + 1);
                if (var3.contains("/")) {
                    var3 = "";
                } else {
                    var1 = var1.substring(var2 + 1);
                }
            }

            if (var1.length() == 0) {
                return var3;
            } else if (var1.indexOf("/.") < 0) {
                return var3.length() <= 0 ? var1 : var3 + var1;
            } else {
                if (var1.charAt(0) == '/') {
                    var3 = var3 + '/';
                    var1 = var1.substring(1);
                }

                List var4 = split(var1, "/");
                ArrayDeque var5 = new ArrayDeque(var4.size());
                int var6 = 0;

                for(int var7 = var4.size() - 1; var7 >= 0; --var7) {
                    String var8 = (String)var4.get(var7);
                    if (!".".equals(var8)) {
                        if ("..".equals(var8)) {
                            ++var6;
                        } else if (var6 > 0) {
                            --var6;
                        } else {
                            var8 = replace(var8, "..", "__");
                            var5.addFirst(var8);
                        }
                    }
                }

                return var3 + join(var5, "/");
            }
        }
    }

    public static boolean isAbsolutePath(String path) {
        if (path == null) {
            return false;
        } else if (!path.startsWith("/")) {
            return false;
        } else if (path.indexOf(92) < 0 && path.indexOf(58) < 0) {
            if (path.indexOf("/./") >= 0) {
                return false;
            } else if (path.indexOf("/../") >= 0) {
                return false;
            } else {
                return !path.endsWith("/.") && !path.endsWith("/..");
            }
        } else {
            return false;
        }
    }

    
    public static String relativizePath(String base, String path) {
        if (path == null) {
            return null;
        } else if (base == null) {
            return path;
        } else if (path.startsWith("/") && base.startsWith("/")) {
            base = base.substring(1);
            path = path.substring(1);
            boolean var2 = base.endsWith("/");
            boolean var3 = path.endsWith("/");
            if (var2) {
                base = base.substring(0, base.length() - 1);
            }

            if (base.length() <= 0) {
                return path;
            } else {
                if (var3) {
                    path = path.substring(0, path.length() - 1);
                }

                List var4 = split(base, "/");
                List var5 = split(path, "/");
                int var7 = Math.min(var4.size(), var5.size());

                int var6;
                for(var6 = 0; var6 < var7 && ((String)var4.get(var6)).equals(var5.get(var6)); ++var6) {
                    ;
                }

                StringBuilder var8 = new StringBuilder();
                int var9 = var2 ? var6 : var6 + 1;

                int var10;
                for(var10 = var4.size(); var9 < var10; ++var9) {
                    var8.append("..");
                    if (var9 != var10 - 1) {
                        var8.append('/');
                    }
                }

                var9 = var6;

                for(var10 = var5.size(); var9 < var10; ++var9) {
                    if (var8.length() > 0) {
                        var8.append('/');
                    }

                    var8.append((String)var5.get(var9));
                }

                if (var3) {
                    var8.append('/');
                }

                return var8.toString();
            }
        } else {
            return path;
        }
    }

    
    public static String absolutePath(String currentPath, String path) {
        if (currentPath == null) {
            return normalizePath(path);
        } else if (path == null) {
            return null;
        } else {
            int var2 = path.indexOf(58);
            if (var2 >= 0) {
                return normalizePath(path);
            } else {
                return path.startsWith("/") ? normalizePath(path) : s(currentPath, path);
            }
        }
    }

    private static String s(String var0, String var1) {
        if (var0.endsWith("/")) {
            return normalizePath(var0 + var1);
        } else {
            int var2 = var0.lastIndexOf(47);
            if (var2 < 0) {
                var2 = var0.indexOf(58);
                return var2 > 0 ? normalizePath(var0.substring(0, var2 + 1) + var1) : normalizePath("/" + var1);
            } else {
                return normalizePath(var0.substring(0, var2 + 1) + var1);
            }
        }
    }

    
    public static String fileNameNoExt(String path) {
        String fullFileName = fileFullName(path);
        if (fullFileName != null && fullFileName.length() != 0) {
            int index = fullFileName.lastIndexOf(46);
            return index < 0 ? fullFileName : fullFileName.substring(0, index);
        } else {
            return fullFileName;
        }
    }

    /**
     * 对于一般文件，返回文件后缀名。
     * @param path
     * @return
     */
    public static String fileType(String path) {
        String var1 = fileFullName(path);
        String var2 = fileExt(var1);
        if (var2.equals("xml")) {
            int var3 = var1.length() - "xml".length() - 1;
            if (var3 == 0) {
                return "";
            } else {
                int var4 = var1.lastIndexOf(46, var3 - 1);
                return var4 < 0 ? var1.substring(0, var3) : var1.substring(var4 + 1, var3);
            }
        } else {
            return var2;
        }
    }

    
    public static String fileExt(String path) {
        String var1 = lastPart(fileFullName(path), '.', true);
        return var1;
    }

    
    public static String removeFileExt(String path) {
        if (path == null) {
            return null;
        } else {
            String var1 = fileFullName(path);
            int var2 = var1.lastIndexOf(46);
            return var2 < 0 ? null : path.substring(0, path.length() - var1.length() + var2);
        }
    }

    public static String replaceFileExt(String path, String fileExt) {
        return path != null && fileExt != null ? removeFileExt(path) + (fileExt.startsWith(".") ? "" : ".") + fileExt : path;
    }

    
    public static String fileFullName(String path) {
        return lastPart(path, '/', false);
    }

    
    public static String filePath(String path) {
        if (path == null) {
            return null;
        } else {
            int var1 = path.lastIndexOf(47);
            return var1 < 0 ? "" : path.substring(0, var1);
        }
    }

    
    public static String firstPart(String str, char c) {
        if (str == null) {
            return null;
        } else {
            int var2 = str.indexOf(c);
            return var2 < 0 ? str : str.substring(0, var2);
        }
    }

    
    public static String lastPart(String str, char c, boolean emptyIfNoSep) {
        if (str == null) {
            return null;
        } else {
            int var3 = str.lastIndexOf(c);
            if (var3 < 0) {
                return emptyIfNoSep ? "" : str;
            } else {
                return str.substring(var3 + 1);
            }
        }
    }

    /**
     * 移除str被c分隔的最后一部分
     * @param str
     * @param c
     * @return
     */
    public static String removeLastPart(String str, char c) {
        if (str == null) {
            return null;
        } else {
            int var2 = str.lastIndexOf(c);
            return var2 < 0 ? "" : str.substring(0, var2);
        }
    }

    
    public static String lastPart(String str, char c) {
        return lastPart(str, c, false);
    }

    
    public static String nextPart(String str, char c) {
        if (str == null) {
            return null;
        } else {
            int var2 = str.indexOf(c);
            return var2 < 0 ? "" : str.substring(var2 + 1);
        }
    }

    public static String head(String s, int offset) {
        if (s == null) {
            return null;
        } else {
            return s.length() <= offset ? s : s.substring(0, offset);
        }
    }

    public static String tail(String str, int n) {
        if (str == null) {
            return null;
        } else {
            return str.length() <= n ? str : str.substring(str.length() - n);
        }
    }

    public static String removeTail(String s, int n) {
        if (s == null) {
            return null;
        } else {
            return s.length() <= n ? "" : s.substring(0, s.length() - n);
        }
    }

    public static String skip(String s, int n) {
        if (s == null) {
            return null;
        } else {
            return s.length() <= n ? "" : s.substring(n);
        }
    }

    
    public static Double parseDegree(String str) {
        str = strip(str);
        if (str == null) {
            return null;
        } else {
            str = replaceChars(str, "\"'º‘“", "″′°′″");
            double var1 = 0.0D;
            int var3 = str.indexOf(176);
            if (var3 >= 0) {
                String var4 = str.substring(0, var3);
                str = str.substring(var3 + 1);
                var1 = parseNumber(var4).doubleValue();
            }

            int var7 = str.indexOf(8242);
            if (var7 >= 0) {
                String var5 = str.substring(0, var7);
                str = str.substring(var7 + 1);
                var1 += parseNumber(var5).doubleValue() / 60.0D;
            }

            int var8 = str.indexOf(8243);
            if (var8 >= 0) {
                String var6 = str.substring(0, var8);
                str.substring(var8 + 1);
                var1 += parseNumber(var6).doubleValue() / 3600.0D;
            }

            return var1;
        }
    }

    
    public static String formatDegree(Number n) {
        if (n == null) {
            return null;
        } else {
            int var1 = n.intValue();
            double var2 = n.doubleValue() - (double)var1;
            int var4 = (int)(var2 * 60.0D);
            double var5 = var2 * 60.0D - (double)var4;
            int var7 = (int)((var5 + 1.0E-8D) * 60.0D);
            StringBuilder var8 = new StringBuilder();
            if (var1 > 0) {
                var8.append(var1).append('°');
            }

            if (var4 > 0 || var7 > 0) {
                var8.append(var4).append('′');
            }

            if (var7 > 0) {
                var8.append(var7).append('″');
            }

            return var8.toString();
        }
    }

    
    public static int utf8Length(CharSequence seq) {
        return seq == null ? 0 : Utf8.encodedLength(seq);
    }

    
    public static long parseFileSizeString(String str) {
        if (str != null && str.length() > 0) {
            if (str.endsWith("G")) {
                return (long)parseNumber(str.substring(0, str.length() - 1)).doubleValue() * 1024L * 1024L * 1024L;
            } else if (str.endsWith("M")) {
                return (long)parseNumber(str.substring(0, str.length() - 1)).doubleValue() * 1024L * 1024L;
            } else {
                return str.endsWith("K") ? (long)parseNumber(str.substring(0, str.length() - 1)).doubleValue() * 1024L : parseNumber(str).longValue();
            }
        } else {
            return -1L;
        }
    }

    
    public static String fileSizeString(long size) {
        if (size < 0L) {
            return "-";
        } else if (size == 0L) {
            return "0K";
        } else if (size < 1024L) {
            return size + "B";
        } else {
            long var2 = size * 10L / 1024L;
            if (var2 < 10240L) {
                return dropZero((double)var2 / 10.0D) + "K";
            } else {
                var2 /= 1024L;
                return var2 < 10240L ? dropZero((double)var2 / 10.0D) + "M" : dropZero((double)(var2 / 1024L) / 10.0D) + "G";
            }
        }
    }

    
    public static String dropZero(double value) {
        String var2 = String.valueOf(value);
        return var2.endsWith(".0") ? var2.substring(0, var2.length() - 2) : var2;
    }

    
    public static boolean matchPath(String path, String pattern) {
        List var2 = splitOn(path, '/');
        List var3 = splitOn(pattern, '/');
        return c(var2, var3);
    }

    private static boolean c(List<String> var0, List<String> var1) {
        int var2 = 0;
        int var3 = var1.size() - 1;
        int var4 = 0;

        int var5;
        String var6;
        for(var5 = var0.size() - 1; var2 <= var3 && var4 <= var5; ++var4) {
            var6 = (String)var1.get(var2);
            if (var6.equals("**")) {
                break;
            }

            if (!matchWildcard((CharSequence)var0.get(var4), var6)) {
                return false;
            }

            ++var2;
        }

        int var14;
        if (var4 > var5) {
            for(var14 = var2; var14 <= var3; ++var14) {
                if (!((String)var1.get(var14)).equals("**")) {
                    return false;
                }
            }

            return true;
        } else if (var2 > var3) {
            return false;
        } else {
            while(var2 <= var3 && var4 <= var5) {
                var6 = (String)var1.get(var3);
                if (var6.equals("**")) {
                    break;
                }

                if (!matchWildcard((CharSequence)var0.get(var5), var6)) {
                    return false;
                }

                --var3;
                --var5;
            }

            if (var4 > var5) {
                for(var14 = var2; var14 <= var3; ++var14) {
                    if (!((String)var1.get(var14)).equals("**")) {
                        return false;
                    }
                }

                return true;
            } else {
                while(var2 != var3 && var4 <= var5) {
                    var14 = -1;

                    int var7;
                    for(var7 = var2 + 1; var7 <= var3; ++var7) {
                        if (((String)var1.get(var7)).equals("**")) {
                            var14 = var7;
                            break;
                        }
                    }

                    if (var14 == var2 + 1) {
                        ++var2;
                    } else {
                        var7 = var14 - var2 - 1;
                        int var8 = var5 - var4 + 1;
                        int var9 = -1;
                        int var10 = 0;

                        label106:
                        while(var10 <= var8 - var7) {
                            for(int var11 = 0; var11 < var7; ++var11) {
                                String var12 = (String)var1.get(var2 + var11 + 1);
                                String var13 = (String)var0.get(var4 + var10 + var11);
                                if (!matchWildcard(var13, var12)) {
                                    ++var10;
                                    continue label106;
                                }
                            }

                            var9 = var4 + var10;
                            break;
                        }

                        if (var9 == -1) {
                            return false;
                        }

                        var2 = var14;
                        var4 = var9 + var7;
                    }
                }

                for(var14 = var2; var14 <= var3; ++var14) {
                    if (!((String)var1.get(var14)).equals("**")) {
                        return false;
                    }
                }

                return true;
            }
        }
    }

    
    public static boolean matchWildcard(CharSequence string, CharSequence pattern) {
        return a(string, pattern, 0, 0);
    }

    private static boolean a(CharSequence var0, CharSequence var1, int var2, int var3) {
        int var4 = var1.length();
        if (var4 == 1 && var1.charAt(0) == '*') {
            return true;
        } else {
            int var5 = var0.length();
            boolean var6 = false;

            while(var2 < var5) {
                if (var3 >= var4) {
                    return false;
                }

                char var7 = var1.charAt(var3);
                if (!var6) {
                    if (var7 == '\\') {
                        ++var3;
                        var6 = true;
                        continue;
                    }

                    if (var7 == '?') {
                        ++var2;
                        ++var3;
                        continue;
                    }

                    if (var7 == '*') {
                        char var8 = 0;
                        if (var3 + 1 < var4) {
                            var8 = var1.charAt(var3 + 1);
                        }

                        if (var8 != '*') {
                            ++var3;

                            for(int var9 = var0.length(); var9 >= var2; --var9) {
                                if (a(var0, var1, var9, var3)) {
                                    return true;
                                }
                            }

                            return false;
                        }

                        ++var3;
                        continue;
                    }
                } else {
                    var6 = false;
                }

                if (var7 != var0.charAt(var2)) {
                    return false;
                }

                ++var2;
                ++var3;
            }

            while(var3 < var4 && var1.charAt(var3) == '*') {
                ++var3;
            }

            return var3 >= var4;
        }
    }

    public static String trimSuffix(String str, String suffix) {
        return str.endsWith(suffix) ? str.substring(0, str.length() - suffix.length()) : str;
    }



    public static Integer parseInt(String s, int radix) {
        return s == null ? null : Integer.parseInt(s, radix);
    }

    public static String formatDate(Date date, String pattern) {
        return date == null ? null : ka.cc(pattern).format(date);
    }

    public static String formatNumber(Number num, String pattern) {
        if (num == null) {
            return null;
        } else {
            DecimalFormat format = new DecimalFormat(pattern);
            return format.format(num);
        }
    }

    public static String formatNumber(Number num) {
        if (num == null) {
            return null;
        } else {
            Class var1 = num.getClass();
            if (var1 != Integer.class && var1 != Long.class && var1 != BigInteger.class) {
                if (var1 == BigDecimal.class) {
                    BigDecimal var2 = (BigDecimal)num;
                    if (var2.scale() < 20) {
                        return var2.toString();
                    }
                }

                NumberFormat var3 = NumberFormat.getInstance();
                var3.setMaximumFractionDigits(20);
                var3.setGroupingUsed(false);
                return var3.format(num.doubleValue());
            } else {
                return num.toString();
            }
        }
    }

    public static String encodeHeaderValue(Serializable s) {
        if (s == null) {
            return null;
        } else if (s instanceof String) {
            String var1 = s.toString();
            if (var1.isEmpty()) {
                return "";
            } else {
                return var1.charAt(0) == '\'' ? "'" + var1 : var1;
            }
        } else if (s instanceof Long) {
            return "$" + s;
        } else {
            return s instanceof BytesObject ? "#" + ((BytesObject)s).hex() : s.toString();
        }
    }

    public static Serializable decodeHeaderValue(String s) {
        if (s != null && s.length() > 0) {
            char var1 = s.charAt(0);
            if (var1 == '$') {
                return Long.parseLong(s.substring(1));
            } else if (var1 == '#') {
                return BytesObject.decodeHex(s.substring(1));
            } else {
                return var1 == '\'' ? s.substring(1) : s;
            }
        } else {
            return s;
        }
    }

    /**
     * 获取xml的头
     * @param encoding
     * @return
     */
    public static String getXmlProlog(String encoding) {
        if (encoding == null) {
            encoding = "UTF-8";
        }

        return "<?xml version=\"1.0\" encoding=\"" + encoding + "\" ?>\n";
    }

    public static String[] tokenizeToStringArray(String str, String delimiters) {
        return tokenizeToStringArray(str, delimiters, true, true);
    }

    public static String[] tokenizeToStringArray(String str, String delimiters,
                            boolean trimTokens, boolean ignoreEmptyTokens) {
        if (str == null) {
            return null;
        } else {
            StringTokenizer var4 = new StringTokenizer(str, delimiters);
            ArrayList var5 = new ArrayList();

            while(true) {
                String var6;
                do {
                    if (!var4.hasMoreTokens()) {
                        return toStringArray(var5);
                    }

                    var6 = var4.nextToken();
                    if (trimTokens) {
                        var6 = var6.trim();
                    }
                } while(ignoreEmptyTokens && var6.length() <= 0);

                var5.add(var6);
            }
        }
    }

    public static String[] toStringArray(Collection<String> collection) {
        return collection == null ? null : (String[])collection.toArray(new String[collection.size()]);
    }



    static {
        acp = new String(aco);
        acq = new String[]{"_", "_", "_", "_", "_", "_", "_", "_", "_", "_"};
        acr = new char[]{'\\', ':', '*', '?', '"', '<', '>', '|', '\u0000'};
        acs = new String(acr);
    }
}

