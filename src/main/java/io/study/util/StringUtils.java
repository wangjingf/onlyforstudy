package io.study.util;


import io.study.exception.StdException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class StringUtils {
    public static String capitalize(String s) {
        if (s == null) {
            return null;
        }
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }



    public static String decapitalize(String var0) {
        return var0 != null && var0.length() > 0 ? Character.toLowerCase(var0.charAt(0)) + var0.substring(1) : var0;
    }

    public static String firstPart(String var0, char var1) {
        if (var0 == null) {
            return null;
        } else {
            int var2 = var0.indexOf(var1);
            return var2 < 0 ? var0 : var0.substring(0, var2);
        }
    }

    public static String lastPart(String var0, char var1, boolean var2) {
        if (var0 == null) {
            return null;
        } else {
            int var3 = var0.lastIndexOf(var1);
            if (var3 < 0) {
                return var2 ? "" : var0;
            } else {
                return var0.substring(var3 + 1);
            }
        }
    }

    /***
     * 类似slf4j的log,将{}占位符变为参数
     * @param messagePattern transfer:{},b:{}
     * @param argArray
     * @return
     */
    public static String formatMsg(String messagePattern, Object... argArray) {
        if (messagePattern == null) {
            return null;
        } else if (argArray == null) {
            return messagePattern;
        } else {
            int i = 0;
            StringBuilder sbuf = new StringBuilder(messagePattern.length() + 50);

            for (int L = 0; L < argArray.length; ++L) {
                int j = messagePattern.indexOf("{}", i);
                if (j == -1) {
                    if (i == 0) {
                        return messagePattern;
                    }
                    sbuf.append(messagePattern, i, messagePattern.length());
                    return sbuf.toString();
                }

                sbuf.append(messagePattern, i, j);
                sbuf.append(argArray[L] == null ? null : argArray[L].toString());
                i = j + 2;
            }
            sbuf.append(messagePattern, i, messagePattern.length());
            return sbuf.toString();
        }
    }


    /**
     * 将{{name}} 类型的变量替换掉
     *
     * @param template
     * @param args
     */
    public static String format(String template, Map<String, Object> args) {
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
        if (!StringUtils.isEmpty(var)) {
            sb.append("{{" + var);
        }
        return sb.toString();
    }

    public static Date parseDate(String dateStr) {
        String pattern = null;
        if (dateStr.length() == 4) {
            pattern = "yyyy";
        } else if (dateStr.length() == 7) {
            pattern = "yyyy-MM";
        } else if (dateStr.length() == 10) {
            pattern = "yyyy-MM-dd";
        } else if (dateStr.length() == 13) {
            pattern = "yyyy-MM-dd HH";
        } else if (dateStr.length() == 16) {
            pattern = "yyyy-MM-dd HH:mm";
        } else {
            pattern = "yyyy-MM-dd HH:mm:ss";
        }
        return parseDate(dateStr, pattern);
    }

    public static Date parseDate(String dateStr, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        try {
            return dateFormat.parse(dateStr);
        } catch (ParseException e) {
            throw StdException.adapt(e);
        }
    }

    public static String formatDate(Date date, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(date);

    }
    private static final int c = 10;
    public static final String a = " \n\r\f\t";
    public static final String b = "$$BATCH_ID_PLACEHOLDER$$";
    private static final char[] d = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private StringUtils() {
    }

    public static String getQuerySpaceType(String querySpace) {
        if (querySpace == null) {
            return null;
        } else {
            int pos = querySpace.indexOf(58);
            if (pos < 0) {
                return querySpace;
            } else {
                return pos > 0 ? querySpace.substring(0, pos) : "default";
            }
        }
    }

    public static String generateAlias(String description) {
        return a(description) + '_';
    }

    public static String generateAlias(String description, int unique) {
        return a(description) + Integer.toString(unique) + '_';
    }

    private static String a(String description) {
        String result = truncate(unqualifyEntityName(description), 10).toLowerCase().replace('/', '_').replace('$', '_');
        result = b(result);
        return Character.isDigit(result.charAt(result.length() - 1)) ? result + "x" : result;
    }

    private static String b(String alias) {
        char[] chars = alias.toCharArray();
        if (!Character.isLetter(chars[0])) {
            for(int i = 1; i < chars.length; ++i) {
                if (Character.isLetter(chars[i])) {
                    return alias.substring(i);
                }
            }
        }

        return alias;
    }

    public static final String toCamelCase(String name) {
        name = name.toLowerCase();
        StringBuilder result = new StringBuilder();
        boolean nextIsUpper = false;
        if (name != null && name.length() > 0) {
            if (name.length() > 1 && name.substring(1, 2).equals("_")) {
                result.append(name.substring(0, 1).toUpperCase());
            } else {
                result.append(name.substring(0, 1));
            }

            for(int i = 1; i < name.length(); ++i) {
                String s = name.substring(i, i + 1);
                if (s.equals("_")) {
                    nextIsUpper = true;
                } else if (nextIsUpper) {
                    result.append(s.toUpperCase());
                    nextIsUpper = false;
                } else {
                    result.append(s);
                }
            }
        }

        return result.toString();
    }

    public static int lastIndexOfLetter(String string) {
        for(int i = 0; i < string.length(); ++i) {
            char character = string.charAt(i);
            if (!Character.isLetter(character)) {
                return i - 1;
            }
        }

        return string.length() - 1;
    }


    public static String joinWithQualifier(String[] values, String qualifier, String deliminator) {
        int length = values.length;
        if (length == 0) {
            return "";
        } else {
            StringBuilder buf = (new StringBuilder(length * values[0].length())).append(qualify(qualifier, values[0]));

            for(int i = 1; i < length; ++i) {
                buf.append(deliminator).append(qualify(qualifier, values[i]));
            }

            return buf.toString();
        }
    }

    public static String join(Iterable<?> var0, String var1) {
        if (var0 == null) {
            return null;
        } else {
            StringBuilder var2 = new StringBuilder();
            boolean var3 = true;

            Object var5;
            for(Iterator var4 = var0.iterator(); var4.hasNext(); var2.append(var5)) {
                var5 = var4.next();
                if (!var3) {
                    var2.append(var1);
                } else {
                    var3 = false;
                }
            }

            return var2.toString();
        }
    }

    public static String[] add(String[] x, String sep, String[] y) {
        String[] result = new String[x.length];

        for(int i = 0; i < x.length; ++i) {
            result[i] = x[i] + sep + y[i];
        }

        return result;
    }

    public static String repeat(String string, int times) {
        StringBuilder buf = new StringBuilder(string.length() * times);

        for(int i = 0; i < times; ++i) {
            buf.append(string);
        }

        return buf.toString();
    }

    public static String repeat(String string, int times, String deliminator) {
        StringBuilder buf = (new StringBuilder(string.length() * times + deliminator.length() * (times - 1))).append(string);

        for(int i = 1; i < times; ++i) {
            buf.append(deliminator).append(string);
        }

        return buf.toString();
    }

    public static String repeat(char character, int times) {
        char[] buffer = new char[times];
        Arrays.fill(buffer, character);
        return new String(buffer);
    }

    public static String replace(String template, String placeholder, String replacement) {
        return replace(template, placeholder, replacement, false);
    }

    public static String[] replace(String[] templates, String placeholder, String replacement) {
        String[] result = new String[templates.length];

        for(int i = 0; i < templates.length; ++i) {
            result[i] = replace(templates[i], placeholder, replacement);
        }

        return result;
    }

    public static String replace(String template, String placeholder, String replacement, boolean wholeWords) {
        return replace(template, placeholder, replacement, wholeWords, false);
    }

    public static String replace(String template, String placeholder, String replacement, boolean wholeWords, boolean encloseInParensIfNecessary) {
        if (template == null) {
            return template;
        } else {
            int loc = template.indexOf(placeholder);
            if (loc < 0) {
                return template;
            } else {
                String beforePlaceholder = template.substring(0, loc);
                String afterPlaceholder = template.substring(loc + placeholder.length());
                return replace(beforePlaceholder, afterPlaceholder, placeholder, replacement, wholeWords, encloseInParensIfNecessary);
            }
        }
    }

    public static String replace(String beforePlaceholder, String afterPlaceholder, String placeholder, String replacement, boolean wholeWords, boolean encloseInParensIfNecessary) {
        boolean actuallyReplace = !wholeWords || afterPlaceholder.length() == 0 || !Character.isJavaIdentifierPart(afterPlaceholder.charAt(0));
        boolean encloseInParens = actuallyReplace && encloseInParensIfNecessary && getLastNonWhitespaceCharacter(beforePlaceholder) != '(' && getFirstNonWhitespaceCharacter(afterPlaceholder) != ')';
        StringBuilder buf = new StringBuilder(beforePlaceholder);
        if (encloseInParens) {
            buf.append('(');
        }

        buf.append(actuallyReplace ? replacement : placeholder);
        if (encloseInParens) {
            buf.append(')');
        }

        buf.append(replace(afterPlaceholder, placeholder, replacement, wholeWords, encloseInParensIfNecessary));
        return buf.toString();
    }

    public static char getLastNonWhitespaceCharacter(String str) {
        if (str != null && str.length() > 0) {
            for(int i = str.length() - 1; i >= 0; --i) {
                char ch = str.charAt(i);
                if (!Character.isWhitespace(ch)) {
                    return ch;
                }
            }
        }

        return '\u0000';
    }

    public static char getFirstNonWhitespaceCharacter(String str) {
        if (str != null && str.length() > 0) {
            for(int i = 0; i < str.length(); ++i) {
                char ch = str.charAt(i);
                if (!Character.isWhitespace(ch)) {
                    return ch;
                }
            }
        }

        return '\u0000';
    }

    public static String replaceOnce(String template, String placeholder, String replacement) {
        if (template == null) {
            return template;
        } else {
            int loc = template.indexOf(placeholder);
            return loc < 0 ? template : template.substring(0, loc) + replacement + template.substring(loc + placeholder.length());
        }
    }

    public static String[] split(String seperators, String list) {
        return split(seperators, list, false);
    }

    public static String[] split(String seperators, String list, boolean include) {
        StringTokenizer tokens = new StringTokenizer(list, seperators, include);
        String[] result = new String[tokens.countTokens()];

        for(int var5 = 0; tokens.hasMoreTokens(); result[var5++] = tokens.nextToken()) {
            ;
        }

        return result;
    }

    public static String unqualify(String qualifiedName) {
        int loc = qualifiedName.lastIndexOf(".");
        return loc < 0 ? qualifiedName : qualifiedName.substring(loc + 1);
    }

    public static String qualifier(String qualifiedName) {
        int loc = qualifiedName.lastIndexOf(".");
        return loc < 0 ? "" : qualifiedName.substring(0, loc);
    }

    public static String collapse(String name) {
        if (name == null) {
            return null;
        } else {
            int breakPoint = name.lastIndexOf(46);
            return breakPoint < 0 ? name : collapseQualifier(name.substring(0, breakPoint), true) + name.substring(breakPoint);
        }
    }

    public static String collapseQualifier(String qualifier, boolean includeDots) {
        StringTokenizer tokenizer = new StringTokenizer(qualifier, ".");

        String collapsed;
        for(collapsed = Character.toString(tokenizer.nextToken().charAt(0)); tokenizer.hasMoreTokens(); collapsed = collapsed + tokenizer.nextToken().charAt(0)) {
            if (includeDots) {
                collapsed = collapsed + '.';
            }
        }

        return collapsed;
    }

    public static String partiallyUnqualify(String name, String qualifierBase) {
        return name != null && name.startsWith(qualifierBase) ? name.substring(qualifierBase.length() + 1) : name;
    }

    public static String collapseQualifierBase(String name, String qualifierBase) {
        return name != null && name.startsWith(qualifierBase) ? collapseQualifier(qualifierBase, true) + name.substring(qualifierBase.length()) : collapse(name);
    }

    public static String[] suffix(String[] columns, String suffix) {
        if (suffix == null) {
            return columns;
        } else {
            String[] qualified = new String[columns.length];

            for(int i = 0; i < columns.length; ++i) {
                qualified[i] = a(columns[i], suffix);
            }

            return qualified;
        }
    }

    private static String a(String name, String suffix) {
        return suffix == null ? name : name + suffix;
    }

    public static String root(String qualifiedName) {
        int loc = qualifiedName.indexOf(".");
        return loc < 0 ? qualifiedName : qualifiedName.substring(0, loc);
    }

    public static String unroot(String qualifiedName) {
        int loc = qualifiedName.indexOf(".");
        return loc < 0 ? qualifiedName : qualifiedName.substring(loc + 1, qualifiedName.length());
    }

    public static boolean booleanValue(String tfString) {
        String trimmed = tfString.trim().toLowerCase();
        return trimmed.equals("true") || trimmed.equals("t");
    }

    public static String toString(Object[] array) {
        int len = array.length;
        if (len == 0) {
            return "";
        } else {
            StringBuilder buf = new StringBuilder(len * 12);

            for(int i = 0; i < len - 1; ++i) {
                buf.append(array[i]).append(", ");
            }

            return buf.append(array[len - 1]).toString();
        }
    }

    public static String[] multiply(String string, Iterator<String> placeholders, Iterator<String[]> replacements) {
        String[] result;
        for(result = new String[]{string}; placeholders.hasNext(); result = a(result, (String)placeholders.next(), (String[])replacements.next())) {
            ;
        }

        return result;
    }

    private static String[] a(String[] strings, String placeholder, String[] replacements) {
        String[] results = new String[replacements.length * strings.length];
        int n = 0;

        for(int i = 0; i < replacements.length; ++i) {
            for(int j = 0; j < strings.length; ++j) {
                results[n++] = replaceOnce(strings[j], placeholder, replacements[i]);
            }
        }

        return results;
    }

    public static int countUnquoted(String string, char character) {
        if ('\'' == character) {
            throw new IllegalArgumentException("Unquoted count of quotes is invalid");
        } else if (string == null) {
            return 0;
        } else {
            int count = 0;
            int stringLength = string.length();
            boolean inQuote = false;

            for(int indx = 0; indx < stringLength; ++indx) {
                char c = string.charAt(indx);
                if (inQuote) {
                    if ('\'' == c) {
                        inQuote = false;
                    }
                } else if ('\'' == c) {
                    inQuote = true;
                } else if (c == character) {
                    ++count;
                }
            }

            return count;
        }
    }

    /*public static int[] locateUnquoted(String string, char character) {
        if ('\'' == character) {
            throw new IllegalArgumentException("Unquoted count of quotes is invalid");
        } else if (string == null) {
            return new int[0];
        } else {
            ArrayList<Integer> locations = new ArrayList(20);
            int stringLength = string.length();
            boolean inQuote = false;

            for(int indx = 0; indx < stringLength; ++indx) {
                char c = string.charAt(indx);
                if (inQuote) {
                    if ('\'' == c) {
                        inQuote = false;
                    }
                } else if ('\'' == c) {
                    inQuote = true;
                } else if (c == character) {
                    locations.add(indx);
                }
            }

            return ArrayHelper.toIntArray(locations);
        }
    }*/

    public static boolean isNotEmpty(String string) {
        return string != null && string.length() > 0;
    }

    public static boolean isEmpty(String string) {
        return string == null || string.length() == 0;
    }

    public static String qualify(String prefix, String name) {
        if (name != null && prefix != null) {
            return (new StringBuilder(prefix.length() + name.length() + 1)).append(prefix).append('.').append(name).toString();
        } else {
            throw new NullPointerException();
        }
    }

    public static String[] qualify(String prefix, String[] names) {
        if (prefix == null) {
            return names;
        } else {
            int len = names.length;
            String[] qualified = new String[len];

            for(int i = 0; i < len; ++i) {
                qualified[i] = qualify(prefix, names[i]);
            }

            return qualified;
        }
    }

    public static int firstIndexOfChar(String sqlString, String string, int startindex) {
        int matchAt = -1;

        for(int i = 0; i < string.length(); ++i) {
            int curMatch = sqlString.indexOf(string.charAt(i), startindex);
            if (curMatch >= 0) {
                if (matchAt == -1) {
                    matchAt = curMatch;
                } else {
                    matchAt = Math.min(matchAt, curMatch);
                }
            }
        }

        return matchAt;
    }

    public static String truncate(String string, int length) {
        return string.length() <= length ? string : string.substring(0, length);
    }

    public static String unqualifyEntityName(String entityName) {
        String result = unqualify(entityName);
        int slashPos = result.indexOf(47);
        if (slashPos > 0) {
            result = result.substring(0, slashPos - 1);
        }

        return result;
    }

    public static String toUpperCase(String str) {
        return str == null ? null : str.toUpperCase();
    }

    public static String toLowerCase(String str) {
        return str == null ? null : str.toLowerCase();
    }

    public static String moveAndToBeginning(String filter) {
        if (filter.trim().length() > 0) {
            filter = filter + " and ";
            if (filter.startsWith(" and ")) {
                filter = filter.substring(4);
            }
        }

        return filter;
    }

    public static boolean isQuoted(String name) {
        return name != null && name.length() != 0 && name.charAt(0) == '`' && name.charAt(name.length() - 1) == '`';
    }

    public static String quote(String name) {
        if (!isEmpty(name) && !isQuoted(name)) {
            if (name.startsWith("\"") && name.endsWith("\"")) {
                name = name.substring(1, name.length() - 1);
            }

            return (new StringBuilder(name.length() + 2)).append('`').append(name).append('`').toString();
        } else {
            return name;
        }
    }

    public static String unquote(String name) {
        return isQuoted(name) ? name.substring(1, name.length() - 1) : name;
    }




    public static boolean isNullOrEmptyString(Object o) {
        if (o == null) {
            return true;
        } else {
            return o instanceof String && ((String)o).length() <= 0;
        }
    }

    public static int countOccurrencesOf(String str, String sub) {
        if (str != null && sub != null && str.length() != 0 && sub.length() != 0) {
            int count = 0;
            int pos = 0;

            int idx;
            for(boolean var4 = false; (idx = str.indexOf(sub, pos)) != -1; pos = idx + sub.length()) {
                ++count;
            }

            return count;
        } else {
            return 0;
        }
    }

    public static String toHex(byte[] binaryData) {
        if (binaryData == null) {
            return null;
        } else {
            int n = binaryData.length;
            char[] buffer = new char[binaryData.length * 2];

            for(int i = 0; i < n; ++i) {
                int low = binaryData[i] & 15;
                int high = (binaryData[i] & 240) >> 4;
                buffer[i * 2] = d[high];
                buffer[i * 2 + 1] = d[low];
            }

            return new String(buffer);
        }
    }
}
