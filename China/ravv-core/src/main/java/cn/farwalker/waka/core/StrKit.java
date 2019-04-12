package cn.farwalker.waka.core;

/**
 * Created by asus on 2019/3/25.
 */
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class StrKit {
    public static final String SPACE = " ";
    public static final String DOT = ".";
    public static final String SLASH = "/";
    public static final String BACKSLASH = "\\";
    public static final String EMPTY = "";
    public static final String CRLF = "\r\n";
    public static final String NEWLINE = "\n";
    public static final String UNDERLINE = "_";
    public static final String COMMA = ",";
    public static final String HTML_NBSP = "&nbsp;";
    public static final String HTML_AMP = "&amp";
    public static final String HTML_QUOTE = "&quot;";
    public static final String HTML_LT = "&lt;";
    public static final String HTML_GT = "&gt;";
    public static final String EMPTY_JSON = "{}";

    public StrKit() {
    }

    public static String firstCharToLowerCase(String str) {
        char firstChar = str.charAt(0);
        if(firstChar >= 65 && firstChar <= 90) {
            char[] arr = str.toCharArray();
            arr[0] = (char)(arr[0] + 32);
            return new String(arr);
        } else {
            return str;
        }
    }

    public static String firstCharToUpperCase(String str) {
        char firstChar = str.charAt(0);
        if(firstChar >= 97 && firstChar <= 122) {
            char[] arr = str.toCharArray();
            arr[0] = (char)(arr[0] - 32);
            return new String(arr);
        } else {
            return str;
        }
    }

    public static boolean isBlank(String str) {
        int length;
        if(str != null && (length = str.length()) != 0) {
            for(int i = 0; i < length; ++i) {
                if(!Character.isWhitespace(str.charAt(i))) {
                    return false;
                }
            }

            return true;
        } else {
            return true;
        }
    }

    public static boolean notBlank(String str) {
        return !isBlank(str);
    }

    public static boolean hasBlank(String... strs) {
        if(isArrayEmpty(strs)) {
            return true;
        } else {
            String[] var4 = strs;
            int var3 = strs.length;

            for(int var2 = 0; var2 < var3; ++var2) {
                String str = var4[var2];
                if(isBlank(str)) {
                    return true;
                }
            }

            return false;
        }
    }

    public static boolean isAllBlank(String... strs) {
        if(isArrayEmpty(strs)) {
            return true;
        } else {
            String[] var4 = strs;
            int var3 = strs.length;

            for(int var2 = 0; var2 < var3; ++var2) {
                String str = var4[var2];
                if(notBlank(str)) {
                    return false;
                }
            }

            return true;
        }
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static String nullToEmpty(String str) {
        return nullToDefault(str, "");
    }

    public static String nullToDefault(String str, String defaultStr) {
        return str == null?defaultStr:str;
    }

    public static String emptyToNull(String str) {
        return isEmpty(str)?null:str;
    }

    public static boolean hasEmpty(String... strs) {
        if(isArrayEmpty(strs)) {
            return true;
        } else {
            String[] var4 = strs;
            int var3 = strs.length;

            for(int var2 = 0; var2 < var3; ++var2) {
                String str = var4[var2];
                if(isEmpty(str)) {
                    return true;
                }
            }

            return false;
        }
    }

    public static boolean isAllEmpty(String... strs) {
        if(isArrayEmpty(strs)) {
            return true;
        } else {
            String[] var4 = strs;
            int var3 = strs.length;

            for(int var2 = 0; var2 < var3; ++var2) {
                String str = var4[var2];
                if(isNotEmpty(str)) {
                    return false;
                }
            }

            return true;
        }
    }

    public static String trim(String str) {
        return str == null?null:trim(str, 0);
    }

    public static void trim(String[] strs) {
        if(strs != null) {
            for(int i = 0; i < strs.length; ++i) {
                String str = strs[i];
                if(str != null) {
                    strs[i] = str.trim();
                }
            }

        }
    }

    public static String trimStart(String str) {
        return trim(str, -1);
    }

    public static String trimEnd(String str) {
        return trim(str, 1);
    }

    public static String trim(String str, int mode) {
        if(str == null) {
            return null;
        } else {
            int length = str.length();
            int start = 0;
            int end = length;
            if(mode <= 0) {
                while(start < end && Character.isWhitespace(str.charAt(start))) {
                    ++start;
                }
            }

            if(mode >= 0) {
                while(start < end && Character.isWhitespace(str.charAt(end - 1))) {
                    --end;
                }
            }

            return start <= 0 && end >= length?str:str.substring(start, end);
        }
    }

    public static boolean startWith(String str, String prefix, boolean isIgnoreCase) {
        return isIgnoreCase?str.toLowerCase().startsWith(prefix.toLowerCase()):str.startsWith(prefix);
    }

    public static boolean endWith(String str, String suffix, boolean isIgnoreCase) {
        return isIgnoreCase?str.toLowerCase().endsWith(suffix.toLowerCase()):str.endsWith(suffix);
    }

    public static boolean containsIgnoreCase(String str, String testStr) {
        return str == null?testStr == null:str.toLowerCase().contains(testStr.toLowerCase());
    }

    public static String getGeneralField(String getOrSetMethodName) {
        return !getOrSetMethodName.startsWith("get") && !getOrSetMethodName.startsWith("set")?null:cutPreAndLowerFirst(getOrSetMethodName, 3);
    }

    public static String genSetter(String fieldName) {
        return upperFirstAndAddPre(fieldName, "set");
    }

    public static String genGetter(String fieldName) {
        return upperFirstAndAddPre(fieldName, "get");
    }

    public static String cutPreAndLowerFirst(String str, int preLength) {
        if(str == null) {
            return null;
        } else if(str.length() > preLength) {
            char first = Character.toLowerCase(str.charAt(preLength));
            return str.length() > preLength + 1?first + str.substring(preLength + 1):String.valueOf(first);
        } else {
            return null;
        }
    }

    public static String upperFirstAndAddPre(String str, String preString) {
        return str != null && preString != null?preString + upperFirst(str):null;
    }

    public static String upperFirst(String str) {
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }

    public static String lowerFirst(String str) {
        return isBlank(str)?str:Character.toLowerCase(str.charAt(0)) + str.substring(1);
    }

    public static String removePrefix(String str, String prefix) {
        return !isEmpty(str) && !isEmpty(prefix)?(str.startsWith(prefix)?str.substring(prefix.length()):str):str;
    }

    public static String removePrefixIgnoreCase(String str, String prefix) {
        return !isEmpty(str) && !isEmpty(prefix)?(str.toLowerCase().startsWith(prefix.toLowerCase())?str.substring(prefix.length()):str):str;
    }

    public static String removeSuffix(String str, String suffix) {
        return !isEmpty(str) && !isEmpty(suffix)?(str.endsWith(suffix)?str.substring(0, str.length() - suffix.length()):str):str;
    }

    public static byte[] getBytes(String str, Charset charset) {
        return str == null?null:(charset == null?str.getBytes():str.getBytes(charset));
    }

    public static String removeSuffixIgnoreCase(String str, String suffix) {
        return !isEmpty(str) && !isEmpty(suffix)?(str.toLowerCase().endsWith(suffix.toLowerCase())?str.substring(0, str.length() - suffix.length()):str):str;
    }

    public static String addPrefixIfNot(String str, String prefix) {
        if(!isEmpty(str) && !isEmpty(prefix)) {
            if(!str.startsWith(prefix)) {
                str = prefix + str;
            }

            return str;
        } else {
            return str;
        }
    }

    public static String addSuffixIfNot(String str, String suffix) {
        if(!isEmpty(str) && !isEmpty(suffix)) {
            if(!str.endsWith(suffix)) {
                str = str + suffix;
            }

            return str;
        } else {
            return str;
        }
    }

    public static String cleanBlank(String str) {
        return str == null?null:str.replaceAll("\\s*", "");
    }

    public static List<String> split(String str, char separator) {
        return split(str, separator, 0);
    }

    public static List<String> split(String str, char separator, int limit) {
        if(str == null) {
            return null;
        } else {
            ArrayList list = new ArrayList(limit == 0?16:limit);
            if(limit == 1) {
                list.add(str);
                return list;
            } else {
                boolean isNotEnd = true;
                int strLen = str.length();
                StringBuilder sb = new StringBuilder(strLen);

                for(int i = 0; i < strLen; ++i) {
                    char c = str.charAt(i);
                    if(isNotEnd && c == separator) {
                        list.add(sb.toString());
                        sb.delete(0, sb.length());
                        if(limit != 0 && list.size() == limit - 1) {
                            isNotEnd = false;
                        }
                    } else {
                        sb.append(c);
                    }
                }

                list.add(sb.toString());
                return list;
            }
        }
    }

    public static String[] split(String str, String delimiter) {
        if(str == null) {
            return null;
        } else if(str.trim().length() == 0) {
            return new String[]{str};
        } else {
            int dellen = delimiter.length();
            int maxparts = str.length() / dellen + 2;
            int[] positions = new int[maxparts];
            int j = 0;
            int count = 0;

            int i;
            for(positions[0] = -dellen; (i = str.indexOf(delimiter, j)) != -1; j = i + dellen) {
                ++count;
                positions[count] = i;
            }

            ++count;
            positions[count] = str.length();
            String[] result = new String[count];

            for(i = 0; i < count; ++i) {
                result[i] = str.substring(positions[i] + dellen, positions[i + 1]);
            }

            return result;
        }
    }

    public static String sub(String string, int fromIndex, int toIndex) {
        int len = string.length();
        if(fromIndex < 0) {
            fromIndex += len;
            if(fromIndex < 0) {
                fromIndex = 0;
            }
        } else if(fromIndex >= len) {
            fromIndex = len - 1;
        }

        if(toIndex < 0) {
            toIndex += len;
            if(toIndex < 0) {
                toIndex = len;
            }
        } else if(toIndex > len) {
            toIndex = len;
        }

        if(toIndex < fromIndex) {
            int strArray = fromIndex;
            fromIndex = toIndex;
            toIndex = strArray;
        }

        if(fromIndex == toIndex) {
            return "";
        } else {
            char[] strArray1 = string.toCharArray();
            char[] newStrArray = Arrays.copyOfRange(strArray1, fromIndex, toIndex);
            return new String(newStrArray);
        }
    }

    public static String subPre(String string, int toIndex) {
        return sub(string, 0, toIndex);
    }

    public static String subSuf(String string, int fromIndex) {
        return isEmpty(string)?null:sub(string, fromIndex, string.length());
    }

    public static boolean isSurround(String str, String prefix, String suffix) {
        return isBlank(str)?false:(str.length() < prefix.length() + suffix.length()?false:str.startsWith(prefix) && str.endsWith(suffix));
    }

    public static boolean isSurround(String str, char prefix, char suffix) {
        return isBlank(str)?false:(str.length() < 2?false:str.charAt(0) == prefix && str.charAt(str.length() - 1) == suffix);
    }

    public static String repeat(char c, int count) {
        char[] result = new char[count];

        for(int i = 0; i < count; ++i) {
            result[i] = c;
        }

        return new String(result);
    }

    public static String repeat(String str, int count) {
        int len = str.length();
        long longSize = (long)len * (long)count;
        int size = (int)longSize;
        if((long)size != longSize) {
            throw new ArrayIndexOutOfBoundsException("Required String length is too large: " + longSize);
        } else {
            char[] array = new char[size];
            str.getChars(0, len, array, 0);

            int n;
            for(n = len; n < size - n; n <<= 1) {
                System.arraycopy(array, 0, array, n, n);
            }

            System.arraycopy(array, 0, array, n, size - n);
            return new String(array);
        }
    }

    public static boolean equals(String str1, String str2) {
        return str1 == null?str2 == null:str1.equals(str2);
    }

    public static boolean equalsIgnoreCase(String str1, String str2) {
        return str1 == null?str2 == null:str1.equalsIgnoreCase(str2);
    }

    public static String format(String template, Object... values) {
        if(!isArrayEmpty(values) && !isBlank(template)) {
            StringBuilder sb = new StringBuilder();
            int length = template.length();
            int valueIndex = 0;

            for(int i = 0; i < length; ++i) {
                if(valueIndex >= values.length) {
                    sb.append(sub(template, i, length));
                    break;
                }

                char currentChar = template.charAt(i);
                if(currentChar == 123) {
                    ++i;
                    char nextChar = template.charAt(i);
                    if(nextChar == 125) {
                        sb.append(values[valueIndex++]);
                    } else {
                        sb.append('{').append(nextChar);
                    }
                } else {
                    sb.append(currentChar);
                }
            }

            return sb.toString();
        } else {
            return template;
        }
    }

    public static String format(String template, Map<?, ?> map) {
        if(map != null && !map.isEmpty()) {
            Map.Entry entry;
            for(Iterator var3 = map.entrySet().iterator(); var3.hasNext(); template = template.replace("{" + entry.getKey() + "}", entry.getValue().toString())) {
                entry = (Map.Entry)var3.next();
            }

            return template;
        } else {
            return template;
        }
    }

    public static byte[] bytes(String str, String charset) {
        return bytes(str, isBlank(charset)?Charset.defaultCharset():Charset.forName(charset));
    }

    public static byte[] bytes(String str, Charset charset) {
        return str == null?null:(charset == null?str.getBytes():str.getBytes(charset));
    }

    public static String str(byte[] bytes, String charset) {
        return str(bytes, isBlank(charset)?Charset.defaultCharset():Charset.forName(charset));
    }

    public static String str(byte[] data, Charset charset) {
        return data == null?null:(charset == null?new String(data):new String(data, charset));
    }

    public static String str(ByteBuffer data, String charset) {
        return data == null?null:str(data, Charset.forName(charset));
    }

    public static String str(ByteBuffer data, Charset charset) {
        if(charset == null) {
            charset = Charset.defaultCharset();
        }

        return charset.decode(data).toString();
    }

    public static ByteBuffer byteBuffer(String str, String charset) {
        return ByteBuffer.wrap(bytes(str, charset));
    }

    public static String join(String conjunction, Object... objs) {
        StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        Object[] var7 = objs;
        int var6 = objs.length;

        for(int var5 = 0; var5 < var6; ++var5) {
            Object item = var7[var5];
            if(isFirst) {
                isFirst = false;
            } else {
                sb.append(conjunction);
            }

            sb.append(item);
        }

        return sb.toString();
    }

    public static String toUnderlineCase(String camelCaseStr) {
        if(camelCaseStr == null) {
            return null;
        } else {
            int length = camelCaseStr.length();
            StringBuilder sb = new StringBuilder();
            boolean isPreUpperCase = false;

            for(int i = 0; i < length; ++i) {
                char c = camelCaseStr.charAt(i);
                boolean isNextUpperCase = true;
                if(i < length - 1) {
                    isNextUpperCase = Character.isUpperCase(camelCaseStr.charAt(i + 1));
                }

                if(!Character.isUpperCase(c)) {
                    isPreUpperCase = false;
                } else {
                    if((!isPreUpperCase || !isNextUpperCase) && i > 0) {
                        sb.append("_");
                    }

                    isPreUpperCase = true;
                }

                sb.append(Character.toLowerCase(c));
            }

            return sb.toString();
        }
    }

    public static String toCamelCase(String name) {
        if(name == null) {
            return null;
        } else if(name.contains("_")) {
            name = name.toLowerCase();
            StringBuilder sb = new StringBuilder(name.length());
            boolean upperCase = false;

            for(int i = 0; i < name.length(); ++i) {
                char c = name.charAt(i);
                if(c == 95) {
                    upperCase = true;
                } else if(upperCase) {
                    sb.append(Character.toUpperCase(c));
                    upperCase = false;
                } else {
                    sb.append(c);
                }
            }

            return sb.toString();
        } else {
            return name;
        }
    }

    public static String wrap(String str, String prefix, String suffix) {
        return format("{}{}{}", new Object[]{prefix, str, suffix});
    }

    public static boolean isWrap(String str, String prefix, String suffix) {
        return str.startsWith(prefix) && str.endsWith(suffix);
    }

    public static boolean isWrap(String str, String wrapper) {
        return isWrap(str, wrapper, wrapper);
    }

    public static boolean isWrap(String str, char wrapper) {
        return isWrap(str, wrapper, wrapper);
    }

    public static boolean isWrap(String str, char prefixChar, char suffixChar) {
        return str.charAt(0) == prefixChar && str.charAt(str.length() - 1) == suffixChar;
    }

    public static String padPre(String str, int minLength, char padChar) {
        if(str.length() >= minLength) {
            return str;
        } else {
            StringBuilder sb = new StringBuilder(minLength);

            for(int i = str.length(); i < minLength; ++i) {
                sb.append(padChar);
            }

            sb.append(str);
            return sb.toString();
        }
    }

    public static String padEnd(String str, int minLength, char padChar) {
        if(str.length() >= minLength) {
            return str;
        } else {
            StringBuilder sb = new StringBuilder(minLength);
            sb.append(str);

            for(int i = str.length(); i < minLength; ++i) {
                sb.append(padChar);
            }

            return sb.toString();
        }
    }

    public static StringBuilder builder() {
        return new StringBuilder();
    }

    public static StringBuilder builder(int capacity) {
        return new StringBuilder(capacity);
    }

    public static StringBuilder builder(String... strs) {
        StringBuilder sb = new StringBuilder();
        String[] var5 = strs;
        int var4 = strs.length;

        for(int var3 = 0; var3 < var4; ++var3) {
            String str = var5[var3];
            sb.append(str);
        }

        return sb;
    }

    public static StringReader getReader(String str) {
        return new StringReader(str);
    }

    public static StringWriter getWriter() {
        return new StringWriter();
    }

    public static byte[] encode(String str, String charset) {
        if(str == null) {
            return null;
        } else if(isBlank(charset)) {
            return str.getBytes();
        } else {
            try {
                return str.getBytes(charset);
            } catch (UnsupportedEncodingException var3) {
                throw new RuntimeException(format("Charset [{}] unsupported!", new Object[]{charset}));
            }
        }
    }

    public static String decode(byte[] data, String charset) {
        if(data == null) {
            return null;
        } else if(isBlank(charset)) {
            return new String(data);
        } else {
            try {
                return new String(data, charset);
            } catch (UnsupportedEncodingException var3) {
                throw new RuntimeException(format("Charset [{}] unsupported!", new Object[]{charset}));
            }
        }
    }

    public static Boolean isArrayEmpty(Object[] array){
            return array == null || array.length == 0;

    }
}
