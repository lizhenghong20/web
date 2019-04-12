package cn.farwalker.waka.util;
import org.apache.tomcat.util.codec.binary.Base64;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * 编码工具类
 *
 * @author Administrator
 */
public class EncodeUtil {

    public final static String ISO8859 = "ISO-8859-1";

    public final static String UTF8 = "UTF-8";

    /** 因为类必须为public，所以只能把构造函数给这样控制 */
    EncodeUtil() {

    }

    /**
     * 转换函数
     *
     * @param s 以ISO-8859-1编码的字符串
     * @return 以GBK编码的字符串
     */
    public String codeISO2GBK(String s) {
        return convertEncoding(s, "GBK");
    }

    /**
     * 转换函数
     *
     * @param s 以ISO-8859-1编码的字符串
     * @return 以GBK编码的字符串
     */
    public String convertEncoding(String s, String encode) {
        if (s == null || s.length() == 0)
            return "";
        boolean isISO = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c > 127 && c < 255) {
                isISO = true;
                break;
            }
        }
        if (!isISO)
            return s;

        try {
            byte[] bytes = s.getBytes(ISO8859);
            return new String(bytes, encode);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return s;
        }
    }

    /** 解码:将 BASE64 编码的字符串 str 进行解码 :GBK */
    public String base64Encode(final String str) {
        return base64Encode(str, "GBK");
    }

    /** 加密:将 str 进行 BASE64 编码 :GBK */
    @SuppressWarnings("restriction")
    public String base64Encode(final String str, final String charset) {
        if (Tools.string.isEmpty(str)) {
            return "";
        }
        final byte[] bytes = Tools.string.getBytes(str, charset);
        final byte[] encodedBytes = base64Encode(bytes);
        // sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
        final String rs = new String(encodedBytes);
        return rs;
    }

    /** 解码:将 BASE64 编码的字符串 str 进行解码 :GBK */
    public String base64Decoder(final String str) {
        return base64Decoder(str, "GBK");
    }

    /** 解码:将 BASE64 编码的字符串 str 进行解码 :GBK */
    @SuppressWarnings("restriction")
    public String base64Decoder(final String str, final String charset) {
        try {
            if (Tools.string.isEmpty(str)) {
                return "";
            }
            // sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
            final byte[] bytes = str.getBytes();
            final byte[] decodedBytes = base64Decode(bytes);
            final String rs = new String(decodedBytes, charset);
            return rs;
        } catch (final IOException e) {
            throw new RuntimeException("BASE64解码出错:" + e.getMessage());
        }
    }

    /**
     * BASE64编码
     *
     * @param inputByte 待编码数据
     * @return 解码后的数据
     */
    public byte[] base64Encode(final byte[] inputByte) {
        return Base64.encodeBase64(inputByte);
    }

    /**
     * BASE64解码
     *
     * @param inputByte 待解码数据
     * @return 解码后的数据
     */
    public byte[] base64Decode(final byte[] inputByte) {
        return Base64.decodeBase64(inputByte);
    }

    public String utf8UrlEncode(final String str) {
        return urlEncode(str, UTF8);
    }

    public String urlEncode(final String str, final String charset) {
        if (Tools.string.isEmpty(str)) {
            return "";
        }
        try {
            return URLEncoder.encode(str, charset);
        } catch (final UnsupportedEncodingException e) {
            throw new YMException("encoder出错:" + str, e);
        }
    }

    public String utf8UrlDecode(final String str) {
        return urlDecode(str, UTF8);
    }

    public String urlDecode(final String str, final String charset) {
        if (Tools.string.isEmpty(str)) {
            return "";
        }
        try {
            return URLDecoder.decode(str, charset);
        } catch (final UnsupportedEncodingException e) {
            throw new YMException("decode出错:" + str, e);
        }
    }
}
