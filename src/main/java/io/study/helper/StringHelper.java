package io.study.helper;


 
import com.google.common.base.CaseFormat;
import io.study.exception.StdException;
import io.study.lang.IRandom;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;


import java.io.IOException;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Pattern;

public class StringHelper {
    public static final String ENCODING_UTF8 = "UTF-8";
    public static final Charset CHARSET_UTF8 = Charset.forName("UTF-8");
    public static final byte[] EMPTY_BYTES = new byte[0];
    public static final String[] EMPTY_STRINGS = new String[0];
    public static final String EMPTY_STRING = "";

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
    static final String[] WINDOWS_CRLF = new String[]{"", "\r\n"};



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



    
    public static String escapeJava(String str) {
        return escape(str, JAVA_ESCAPE_FROM_CHARS, JAVA_ESCAPE_TO_STRS);
    }

    
    public static String unescapeJava(String str) {
        return StringEscapeUtils.unescapeJava(str);
    }

    /**
     * Escapes any values it finds into their String form. So internalMatchWildcard tab becomes the characters '\\' and 't'.
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
        List list = split(str, sep);
        return list == null ? null : (String[])list.toArray(new String[list.size()]);
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
                int value = bytes[index] & 255;
                arr[index * 2] = HEX_CHARS[value >>> 4];
                arr[index * 2 + 1] = HEX_CHARS[value & 15];
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
     * Helper to decode half of internalMatchWildcard hexadecimal number from internalMatchWildcard string.
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
        if (str.length() >= len) {
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
        if (str.length() >= len) {
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
                    int charIndex = searchChars.indexOf(c);
                    if (charIndex >= 0) {
                        if (sb == null) {
                            sb = new StringBuilder(length);
                            sb.append(str.substring(0, index));
                        }

                        sb.append(replaceChars.charAt(charIndex));
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
                    char c = str.charAt(i);
                    if (!isDigit(c) && !isAsciiLetter(c) && c != '_') {
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

            char[] arr = new char[n];
            IRandom random = MathHelper.random();
            int charLength = allowChars.length();

            for(int index = 0; index < n; ++index) {
                arr[index] = allowChars.charAt(random.nextInt(charLength));
            }

            return new String(arr);

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

            for(int size = strs.size(); i < size; ++i) {
                String str = (String)strs.get(i);
                if (str != null) {
                    strs.set(i, str);
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




    public static final String camelCase(String str, boolean firstUpper) {
        return camelCase(str, '_', firstUpper);
    }


    public static final String camelCase(String str, char separator, boolean firstUpper) {
        if (str != null && !str.isEmpty()) {
            str = str.toLowerCase();
            StringBuilder sb = new StringBuilder();
            boolean matchSeparator = false;
            char firstChar;
            if (str.length() > 1 && str.charAt(1) == separator) {
                firstChar = firstUpper ? Character.toUpperCase(str.charAt(0)) : str.charAt(0);
                sb.append(firstChar);
            } else {
                firstChar = firstUpper ? Character.toUpperCase(str.charAt(0)) : str.charAt(0);
                sb.append(firstChar);
            }

            int i = 1;

            for(int length = str.length(); i < length; ++i) {
                char c = str.charAt(i);
                if (c == separator) {
                    matchSeparator = true;
                } else if (matchSeparator) {
                    sb.append(Character.toUpperCase(c));
                    matchSeparator = false;
                } else {
                    sb.append(c);
                }
            }

            return sb.toString();
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
    public static String camelCaseToUnderscore(String str, boolean lower) {
        return str == null ? null : CaseFormat.LOWER_CAMEL.to(lower ? CaseFormat.LOWER_UNDERSCORE : CaseFormat.UPPER_UNDERSCORE, str);
    }
    public static int indexOfIgnoreCase(String str, String subStr) {
        if (str != null && subStr != null) {
            if (str.length() < subStr.length()) {
                return -1;
            } else {
                int i = 0;

                for(int maxIndex = str.length() - subStr.length(); i < maxIndex; ++i) {
                    if (str.regionMatches(true, i, subStr, 0, subStr.length())) {
                        return i;
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

            StringBuilder sb = new StringBuilder(str.length() * count);

            for(int i = 0; i < count; ++i) {
                sb.append(str);
            }

            return sb.toString();

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
        int length = str.length();
        if (pos < 0) {
            pos = length;
        }

        int size = Math.max(pos - len / 2, 0);
        int endIndex = Math.min(size + len, length);
        size = Math.min(Math.max(0, length - len), size);
        StringBuilder sb = new StringBuilder(length + 2);
        if (pos < 0) {
            sb.append("[]");
        } else {
            sb.append(str, size, pos);
            sb.append('[');
            if (pos < length) {
                sb.append(str.charAt(pos));
            }

            sb.append(']');
        }

        if (pos < endIndex) {
            sb.append(str, pos + 1, endIndex);
        }

        return sb.toString();
    }


    public static byte[] utf8Bytes(String str) {
        if (str == null) {
            return null;
        } else {
            return str.length() <= 0 ? EMPTY_BYTES : str.getBytes(CHARSET_UTF8);
        }
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
     * <p>Checks whether the String a valid Java number.</p>
     *
     * <p>Valid numbers include hexadecimal marked with the <code>0x</code>
     * qualifier, scientific notation and numbers marked with a type
     * qualifier (e.g. 123L).</p>
     *
     * <p><code>Null</code> and empty String will return
     * <code>false</code>.</p>
     *
     * @param str  the <code>String</code> to check
     * @return <code>true</code> if the string is a correctly formatted number
     */
    public static boolean isNumber(String str) {
        if (isEmpty(str)) {
            return false;
        }
        char[] chars = str.toCharArray();
        int sz = chars.length;
        boolean hasExp = false;
        boolean hasDecPoint = false;
        boolean allowSigns = false;
        boolean foundDigit = false;
        // deal with any possible sign up front
        int start = (chars[0] == '-') ? 1 : 0;
        if (sz > start + 1) {
            if (chars[start] == '0' && chars[start + 1] == 'x') {
                int i = start + 2;
                if (i == sz) {
                    return false; // str == "0x"
                }
                // checking hex (it can't be anything else)
                for (; i < chars.length; i++) {
                    if ((chars[i] < '0' || chars[i] > '9')
                            && (chars[i] < 'a' || chars[i] > 'f')
                            && (chars[i] < 'A' || chars[i] > 'F')) {
                        return false;
                    }
                }
                return true;
            }
        }
        sz--; // don't want to loop to the last char, check it afterwords
        // for type qualifiers
        int i = start;
        // loop to the next to last char or to the last char if we need another digit to
        // make a valid number (e.g. chars[0..5] = "1234E")
        while (i < sz || (i < sz + 1 && allowSigns && !foundDigit)) {
            if (chars[i] >= '0' && chars[i] <= '9') {
                foundDigit = true;
                allowSigns = false;

            } else if (chars[i] == '.') {
                if (hasDecPoint || hasExp) {
                    // two decimal points or dec in exponent
                    return false;
                }
                hasDecPoint = true;
            } else if (chars[i] == 'e' || chars[i] == 'E') {
                // we've already taken care of hex.
                if (hasExp) {
                    // two E's
                    return false;
                }
                if (!foundDigit) {
                    return false;
                }
                hasExp = true;
                allowSigns = true;
            } else if (chars[i] == '+' || chars[i] == '-') {
                if (!allowSigns) {
                    return false;
                }
                allowSigns = false;
                foundDigit = false; // we need a digit after the E
            } else {
                return false;
            }
            i++;
        }
        if (i < chars.length) {
            if (chars[i] >= '0' && chars[i] <= '9') {
                // no type qualifier, OK
                return true;
            }
            if (chars[i] == 'e' || chars[i] == 'E') {
                // can't have an E at the last byte
                return false;
            }
            if (!allowSigns
                    && (chars[i] == 'd'
                    || chars[i] == 'D'
                    || chars[i] == 'f'
                    || chars[i] == 'F')) {
                return foundDigit;
            }
            if (chars[i] == 'l'
                    || chars[i] == 'L') {
                // not allowing L with an exponent
                return foundDigit && !hasExp;
            }
            // last character is illegal
            return false;
        }
        // allowSigns is true iff the val ends in 'E'
        // found digit it to make sure weird stuff like '.' and '1E-' doesn't pass
        return !allowSigns && foundDigit;
    }


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
                char c = value.charAt(value.length() - 1);
                int dotIndex = value.indexOf(46); // .的位置
                int eIndex = value.indexOf(101) + value.indexOf(69) + 1; // 69时候E 109是E
                String intPart;
                String scalePart;
                if (dotIndex > -1) {
                    if (eIndex > -1) {
                        if (eIndex < dotIndex) {
                            throw (new StdException("util.err_not_valid_number")).param("value", value);
                        }

                        scalePart = value.substring(dotIndex + 1, eIndex);
                    } else {
                        scalePart = value.substring(dotIndex + 1);
                    }

                    intPart = value.substring(0, dotIndex);
                } else {
                    if (eIndex > -1) {
                        intPart = value.substring(0, eIndex);
                    } else {
                        intPart = value;
                    }

                    scalePart = null;
                }

                String powerPart;
                if (!Character.isDigit(c)) {
                    if (eIndex > -1 && eIndex < value.length() - 1) {
                        powerPart = value.substring(eIndex + 1, value.length() - 1);
                    } else {
                        powerPart = null;
                    }

                    String strValue = value.substring(0, value.length() - 1);
                    boolean isZero = isAllZero(intPart) && isAllZero(powerPart);
                    switch(c) {
                        case 'D':
                        case 'd':
                            break;
                        case 'F':
                        case 'f':
                            try {
                                Float floatValue = Float.valueOf(strValue);
                                if (floatValue.isInfinite() || floatValue == 0.0F && !isZero) {
                                    break;
                                }

                                return floatValue;
                            } catch (NumberFormatException ex) {
                                break;
                            }
                        case 'L':
                        case 'l':
                            if (scalePart != null || powerPart != null || !isDigits(strValue) || strValue.charAt(0) != '-' && !Character.isDigit(strValue.charAt(0))) {
                                throw (new StdException("util.err_not_valid_number")).param("value", strValue);
                            } else {
                                try {
                                    return Long.valueOf(strValue);
                                } catch (NumberFormatException ex) {
                                    return new BigInteger(strValue);
                                }
                            }
                        default:
                            throw (new StdException("util.err_not_valid_number")).param("value", value);
                    }

                    try {
                        Double doubleValue = Double.valueOf(strValue);
                        if (!doubleValue.isInfinite() && ((double)doubleValue.floatValue() != 0.0D || isZero)) {
                            return doubleValue;
                        }
                    } catch (NumberFormatException ex) {
                        ;
                    }

                    try {
                        return new BigDecimal(strValue);
                    } catch (NumberFormatException ex) {
                        throw (new StdException("util.err_not_valid_number")).param("value", value);
                    }
                } else {
                    if (eIndex > -1 && eIndex < value.length() - 1) {
                        powerPart = value.substring(eIndex + 1, value.length());
                    } else {
                        powerPart = null;
                    }

                    if (scalePart == null && powerPart == null) {
                        try {
                            return Integer.decode(value);
                        } catch (NumberFormatException ex) {
                            try {
                                return Long.valueOf(value);
                            } catch (NumberFormatException e) {
                                return new BigInteger(value);
                            }
                        }
                    } else {
                        boolean isAllZero = isAllZero(intPart) && isAllZero(powerPart);

                        try {
                            Double doubleValue = Double.valueOf(value);
                            if (!doubleValue.isInfinite() && (doubleValue != 0.0D || isAllZero)) {
                                return doubleValue;
                            }
                        } catch (NumberFormatException ex) {
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
            for(int i = 0; i < str.length(); ++i) {
                if (!isDigit(str.charAt(i))) {
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
            int i = 0;

            for(int length = str.length(); i < length; ++i) {
                if (str.charAt(i) != c) {
                    return false;
                }
            }

            return true;
        } else {
            return false;
        }
    }

    static boolean isAllZero(String str) {
        if (str == null) {
            return true;
        } else {
            for(int i = str.length() - 1; i >= 0; --i) {
                if (str.charAt(i) != '0') {
                    return false;
                }
            }

            return str.length() > 0;
        }
    }

    /**
     * Makes sure that the POSTed data is conforms to certain rules. These rules are:
     * The data always ends with internalMatchWildcard newline (some browsers, such as NS4.x series, does not send internalMatchWildcard newline at the end, which makes the diffs internalMatchWildcard bit strange sometimes.
     * The CR/LF/CRLF mess is normalized to plain CRLF.
     * @param str
     * @return
     */
    public static String normalizeCRLF(String str) {
        return escape(str, CRLF, WINDOWS_CRLF);
    }

    
    public static String normalizeLF(String str) {
        return escape(str, CRLF, WINDOWS_CRLF);
    }

    
    public static boolean isValidClassName(String str) {
        if (str != null && str.length() > 0) {
            if (!Character.isJavaIdentifierStart(str.charAt(0))) {
                return false;
            } else {
                int i = 1;

                for(int length = str.length(); i < length; ++i) {
                    char c = str.charAt(i);
                    if (!Character.isJavaIdentifierPart(c) && c != '.' && c != '$') {
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
                int i = 1;

                for(int length = s.length(); i < length; ++i) {
                    if (!Character.isJavaIdentifierPart(s.charAt(i))) {
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

    /**
     * 是否为合肥的属性名a.b也为合法的属性名
     * @param name
     * @return
     */
    public static boolean isValidPropName(String name) {
        if (name != null && name.length() != 0) {
            if (!Character.isJavaIdentifierStart(name.charAt(0))) {
                return false;
            } else {
                boolean isFirst = false;
                int i = 1;

                for(int length = name.length(); i < length; ++i) {
                    char c = name.charAt(i);
                    if (isFirst) {
                        if (!Character.isJavaIdentifierStart(c)) {
                            return false;
                        }

                        isFirst = false;
                    } else if (!Character.isJavaIdentifierPart(c)) {
                        if (c != '.') {
                            return false;
                        }

                        isFirst = true;
                    }
                }

                return true;
            }
        } else {
            return false;
        }
    }



    
    public static boolean isValidHtmlAttrName(String s) {
        if (s != null && s.length() != 0) {
            char c = s.charAt(0);
            return c != '$' && (Character.isJavaIdentifierStart(c) || c == ':' || c == '@') ? isValidXmlName(s, true, true) : false;
        } else {
            return false;
        }
    }

    
    public static boolean isValidXmlName(String s, boolean allowColon, boolean allowDot) {
        if (s != null && s.length() != 0) {
            char c = s.charAt(0);
            if (Character.isJavaIdentifierStart(c) && c != '$') {
                int i = 1;

                int length;
                for(length = s.length() - 1; i < length; ++i) {
                    c = s.charAt(i);
                    if (c != '-') {
                        if (allowColon && c == ':') {
                            if (!Character.isJavaIdentifierPart(s.charAt(i - 1))) {
                                return false;
                            }
                        } else if (allowDot && c == '.') {
                            if (!Character.isJavaIdentifierPart(s.charAt(i - 1))) {
                                return false;
                            }
                        } else if (!Character.isJavaIdentifierPart(c) || c == '$') {
                            return false;
                        }
                    }
                }

                if (!Character.isJavaIdentifierPart(s.charAt(length))) {
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
                int i = 1;

                for(int length = s.length(); i < length; ++i) {
                    char c = s.charAt(i);
                    if (!Character.isJavaIdentifierPart(c) && c != '.' && c != '-' && c != '/') {
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
     * @param pattern
     * @return
     */
    public static boolean matchSimplePattern(String str, String pattern) {
        if (str != null && pattern != null) {
            if (pattern.startsWith("*")) {
                if (pattern.length() == 1) {
                    return true;
                } else if (pattern.endsWith("*")) {
                    String subPattern = pattern.substring(1, pattern.length() - 1);
                    return str.indexOf(subPattern) >= 0;
                } else {
                    return str.regionMatches(str.length() - pattern.length() + 1, pattern, 1, pattern.length() - 1);
                }
            } else {
                return pattern.endsWith("*") ? str.regionMatches(0, pattern, 0, pattern.length() - 1) : str.equals(pattern);
            }
        } else {
            return false;
        }
    }


    public static boolean matchSimplePatterns(String str, String[] patterns) {
        if (str != null && patterns != null) {
            int length = patterns.length;

            for(int i = 0; i < length; ++i) {
                String pattern = patterns[i];
                if (matchSimplePattern(str, pattern)) {
                    return true;
                }
            }

            return false;
        } else {
            return false;
        }
    }

    





    
    public static String quote(String text) {
        return text == null ? null : "\"" + escapeJava(text) + '"';
    }

    
    public static String unquote(String text) {
        if (text != null && text.length() > 2) {
            char firstChar = text.charAt(0);
            char lastChar = text.charAt(text.length() - 1);
            return (firstChar != '\'' || lastChar != '\'') && (firstChar != '"' || lastChar != '"') ? text : unescapeJava(text.substring(0, text.length() - 1));
        } else {
            return text;
        }
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
            } catch (Exception ex) {
                throw StdException.adapt(ex);
            }
        } else {
            return str;
        }
    }

    
    public static String decodeURL(String str, String encoding) {
        if (str != null && !str.isEmpty()) {
            try {
                return URLDecoder.decode(str, encoding == null ? "UTF-8" : encoding);
            } catch (Exception ex) {
                throw StdException.adapt(ex);
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
            LinkedHashMap map = new LinkedHashMap();
            int fromIndex = 0;

            while(true) {
                int pos = query.indexOf(38, fromIndex);// 找到&符号的位置
                if (pos < 0) {
                    pos = query.length();
                }

                if (fromIndex >= pos) {
                    if (pos >= query.length()) {
                        break;
                    }

                    fromIndex = pos + 1;
                } else {
                    String param = query.substring(fromIndex, pos);
                    int eqPos = param.indexOf(61); // 找到=号的位置
                    String name;
                    String value;
                    if (eqPos < 0) {
                        name = param;
                        if (allowNullValue) {
                            value = null;
                        } else {
                            value = "";
                        }
                    } else {
                        name = param.substring(0, eqPos);
                        value = param.substring(eqPos + 1);
                    }

                    name = decodeURL(name, encoding);
                    if (value != null && !value.isEmpty()) {
                        value = decodeURL(value, encoding);
                    }

                    Object paramValue = map.get(name);
                    if (paramValue == null) {
                        map.put(name, value);
                    } else if (paramValue instanceof List) {
                        ((List)paramValue).add(value);
                    } else {
                        ArrayList list = new ArrayList();
                        list.add((String)paramValue);
                        list.add(value);
                        map.put(name, list);
                    }

                    if (pos >= query.length()) {
                        break;
                    }

                    fromIndex = pos + 1;
                }
            }

            return map;
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
            StringBuilder sb = new StringBuilder();
            boolean isFirst = true;
            Iterator iter = query.entrySet().iterator();

            while(true) {
                while(iter.hasNext()) {
                    Entry entry = (Entry)iter.next();
                    String item = encodeURL((String)entry.getKey(), encoding);
                    Object value = entry.getValue();
                    if (value instanceof List) {
                        List list = (List)value;
                        Iterator valueIter = list.iterator();

                        while(valueIter.hasNext()) {
                            Object valueItem = valueIter.next();
                            if (isFirst) {
                                isFirst = false;
                            } else {
                                sb.append('&');
                            }

                            sb.append(item);
                            sb.append('=');
                            sb.append(encodeURL(toString(valueItem, "")));
                        }
                    } else {
                        if (isFirst) {
                            isFirst = false;
                        } else {
                            sb.append('&');
                        }

                        sb.append(item);
                        if (value != null) {
                            sb.append('=');
                            sb.append(encodeURL(toString(value, "")));
                        }
                    }
                }

                return sb.toString();
            }
        }
    }

    
    public static String appendQuery(String url, String query) {
        if (url == null) {
            return null;
        } else if (query != null && query.length() > 0) {
            int index = url.indexOf(63); // ?
            if (index < 0) {
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
                char[] arr = name.toCharArray();
                arr[0] = Character.toLowerCase(arr[0]);
                return new String(arr);
            }
        } else {
            return name;
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
            String newPath = path.replace('\\', '/');
            int index = newPath.indexOf(":");
            String part = "";
            if (index != -1) {
                part = newPath.substring(0, index + 1);
                if (part.contains("/")) {
                    part = "";
                } else {
                    newPath = newPath.substring(index + 1);
                }
            }

            if (newPath.length() == 0) {
                return part;
            } else if (newPath.indexOf("/.") < 0) {
                return part.length() <= 0 ? newPath : part + newPath;
            } else {
                if (newPath.charAt(0) == '/') {
                    part = part + '/';
                    newPath = newPath.substring(1);
                }

                List pathList = split(newPath, "/");
                ArrayDeque deque = new ArrayDeque(pathList.size());
                int level = 0;

                for(int i = pathList.size() - 1; i >= 0; --i) {
                    String item = (String)pathList.get(i);
                    if (!".".equals(item)) {
                        if ("..".equals(item)) {
                            ++level;
                        } else if (level > 0) {
                            --level;
                        } else {
                            item = replace(item, "..", "__");
                            deque.addFirst(item);
                        }
                    }
                }

                return part + join(deque, "/");
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
            boolean baseEndWithSlash = base.endsWith("/");
            boolean pathEndWithSlash = path.endsWith("/");
            if (baseEndWithSlash) {
                base = base.substring(0, base.length() - 1);
            }

            if (base.length() <= 0) {
                return path;
            } else {
                if (pathEndWithSlash) {
                    path = path.substring(0, path.length() - 1);
                }

                List basePart = split(base, "/");
                List pathPart = split(path, "/");
                int matchSize = Math.min(basePart.size(), pathPart.size());

                int i;
                for(i = 0; i < matchSize && ((String)basePart.get(i)).equals(pathPart.get(i)); ++i) {
                    ;
                }

                StringBuilder sb = new StringBuilder();
                int index = baseEndWithSlash ? i : i + 1;

                int size;
                for(size = basePart.size(); index < size; ++index) {
                    sb.append("..");
                    if (index != size - 1) {
                        sb.append('/');
                    }
                }

                index = i;

                for(size = pathPart.size(); index < size; ++index) {
                    if (sb.length() > 0) {
                        sb.append('/');
                    }

                    sb.append((String)pathPart.get(index));
                }

                if (pathEndWithSlash) {
                    sb.append('/');
                }

                return sb.toString();
            }
        } else {
            return path;
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



    
    public static String fileExt(String path) {
        return lastPart(fileFullName(path), '.', true);
    }

    
    public static String removeFileExt(String path) {
        if (path == null) {
            return null;
        } else {
            String fullName = fileFullName(path);
            int lastIndex = fullName.lastIndexOf(46);
            return lastIndex < 0 ? null : path.substring(0, path.length() - fullName.length() + lastIndex);
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
            int index = path.lastIndexOf(47);
            return index < 0 ? "" : path.substring(0, index);
        }
    }

    
    public static String firstPart(String str, char c) {
        if (str == null) {
            return null;
        } else {
            int index = str.indexOf(c);
            return index < 0 ? str : str.substring(0, index);
        }
    }

    
    public static String lastPart(String str, char c, boolean emptyIfNoSep) {
        if (str == null) {
            return null;
        } else {
            int index = str.lastIndexOf(c);
            if (index < 0) {
                return emptyIfNoSep ? "" : str;
            } else {
                return str.substring(index + 1);
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
            int index = str.lastIndexOf(c);
            return index < 0 ? "" : str.substring(0, index);
        }
    }

    
    public static String lastPart(String str, char c) {
        return lastPart(str, c, false);
    }

    
    public static String nextPart(String str, char c) {
        if (str == null) {
            return null;
        } else {
            int index = str.indexOf(c);
            return index < 0 ? "" : str.substring(index + 1);
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
            long displaySize = size * 10L / 1024L;
            if (displaySize < 10240L) {
                return dropZero((double)displaySize / 10.0D) + "K";
            } else {
                displaySize /= 1024L;
                return displaySize < 10240L ? dropZero((double)displaySize / 10.0D) + "M" : dropZero((double)(displaySize / 1024L) / 10.0D) + "G";
            }
        }
    }


    public static String dropZero(double value) {
        String str = String.valueOf(value);
        return str.endsWith(".0") ? str.substring(0, str.length() - 2) : str;
    }


    public static String trimSuffix(String str, String suffix) {
        return str.endsWith(suffix) ? str.substring(0, str.length() - suffix.length()) : str;
    }



    public static Integer parseInt(String s, int radix) {
        return s == null ? null : Integer.parseInt(s, radix);
    }

    public static String formatDate(Date date, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return date == null ? null : format.format(date);
    }

    public static Date parseDate(String date,String pattern){
        if(StringHelper.isBlank(date)){
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        try {
            return format.parse(date);
        } catch (ParseException e) {
            throw StdException.adapt(e);
        }
    }

    /**
     * 解析日期范围数据如 2020-11-12 ~2020-12-23
     * @param val
     * @param split
     * @param format
     * @return
     */
    public static Date[] parseRangeDate(String val, String split,String format){
        if(StringHelper.isBlank(val)){
            return new Date[0];
        }
        List<String> vals = StringHelper.split(val, split);
        if(vals.size() != 2){
            return new Date[0];
        }
        Date[] ret = new Date[2];
        ret[0] = StringHelper.parseDate(vals.get(0),format);
        ret[1] = StringHelper.parseDate(vals.get(1),format);
        return ret;
    }
    public static Date[] parseRangeDate(String val, String split){
        return parseRangeDate(val,split,"yyyy-MM-dd HH:mm:ss");
    }
    public static Date[] parseRangeDate(String val){
        return parseRangeDate(val,"~");
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
            Class type = num.getClass();
            if (type != Integer.class && type != Long.class && type != BigInteger.class) {
                if (type == BigDecimal.class) {
                    BigDecimal value = (BigDecimal)num;
                    if (value.scale() < 20) {
                        return value.toString();
                    }
                }

                NumberFormat format = NumberFormat.getInstance();
                format.setMaximumFractionDigits(20);
                format.setGroupingUsed(false);
                return format.format(num.doubleValue());
            } else {
                return num.toString();
            }
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
            StringTokenizer tokenizer = new StringTokenizer(str, delimiters);
            ArrayList list = new ArrayList();

            while(true) {
                String token;
                do {
                    if (!tokenizer.hasMoreTokens()) {
                        return toStringArray(list);
                    }

                    token = tokenizer.nextToken();
                    if (trimTokens) {
                        token = token.trim();
                    }
                } while(ignoreEmptyTokens && token.length() <= 0);

                list.add(token);
            }
        }
    }

    public static String[] toStringArray(Collection<String> collection) {
        return collection == null ? null : (String[])collection.toArray(new String[collection.size()]);
    }

    /**
     * 将{{name}} 类型的变量替换掉
     *
     * @param template
     * @param args
     */
    public static String formatTemplate(String template, Map<String, Object> args) {
        if (template == null) {
            return template;
        }
        StringBuilder sb = new StringBuilder();
        boolean isMatchMode = false;
        StringBuilder variable = new StringBuilder();
        for (int i = 0; i < template.length(); i++) {
            if (isMatchMode) {
                if (template.charAt(i) != '}') {
                    variable.append(template.charAt(i));
                    continue;
                } else { // i = '}'
                    if (i + 1 < template.length() && template.charAt(i + 1) == '}') { //i+1 == '}'

                        String var = variable.toString().trim();
                        if (!isEmpty(var)) {
                            Object value = args.get(var);
                            if (value == null) {
                                throw new StdException("template.err_miss_var").param("var", var);
                            } else {
                                sb.append(value.toString());
                            }
                        }
                        variable = new StringBuilder();
                        i++;
                        isMatchMode = false;
                    } else {
                        sb.append("{{" + variable.toString());
                        variable = new StringBuilder();
                        isMatchMode = false;
                    }

                }

            } else {
                if (i + 1 < template.length() && template.charAt(i) == '{' && template.charAt(i + 1) == '{') {
                    isMatchMode = true;
                    i++;
                }
                if (!isMatchMode) {
                    sb.append(template.charAt(i));
                }
            }


        }
        String var = variable.toString();
        if (!isEmpty(var)) {
            sb.append("{{" + var);
        }
        return sb.toString();
    }

    /**
     * 获得脱敏字符串
     * 规则为:通用规则是显示前1/3和后1/3，其他用*号代替（可以是一个*或者固定个数的*）。
     * 内容长度不能被3整除时，显示前1/3（向上取整数）和后1/3（向下取整数）
     * (此方法在不能整除的情况下，显示前1/3（向上取整数）和后1/3（向下取整数），中间使用固定两个*)
     *
     * @param nickname
     * @return
     */
    public static String getDesensitizationString(String nickname) {
        if(StringHelper.isBlank(nickname)){
            return nickname;
        }
        int i = nickname.length();
        int j = i / 3;
        double m = j;
        int k = i % 3;
        StringBuilder sb = new StringBuilder();
        String right = "";
        String left = "";
        String star = "*";
        if (j == 0) {
            if (i == 1) {
                return star;
            }
            //小于3字
            right = StringUtils.right(nickname, 1);
            nickname = StringUtils.leftPad(right, i, star);
        } else if (j >= 1 && k > 0) {
            //非整除
            right = StringUtils.right(nickname, (int) Math.ceil(m));
            left = StringUtils.left(nickname, (int) Math.floor(m));
            nickname = sb.append(left).append(star).append(star).append(right).toString();
        } else if (j >= 1 && k == 0) {
            //整除
            right = StringUtils.right(nickname, j);
            left = StringUtils.left(nickname, j);
            sb = sb.append(left);
            for (int b = 0; b < j; b++) {
                sb.append(star);
            }
            nickname = sb.append(right).toString();
        }
        return nickname;
    }
}

