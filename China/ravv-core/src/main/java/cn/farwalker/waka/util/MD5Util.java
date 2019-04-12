package cn.farwalker.waka.util;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

/**
 * @author Administrator
 */
public class MD5Util {
    private static final Charset UTF8 = Charset.forName("UTF-8");
    
    /**
     * 加盐参数
     */
    public final static String hashAlgorithmName = "MD5";

    /**
     * 循环次数
     */
    public final static int hashIterations = 1024;

    /** 因为类必须为public，所以只能把构造函数给这样控制 */
    MD5Util() {
    }

    private String hex(final byte[] array) {
        final StringBuilder hex = new StringBuilder();
        for (final byte aByte : array) {
            hex.append(Integer.toHexString((aByte & 0xFF) | 0x100).substring(1, 3));
        }
        return hex.toString();
    }

    /** 获取字符串的MD5, 这个方法使用UTF-8 的编码获取byte, 最后会把MD5 字符串转成大写形式 */
    public String md5Hex(String msg) {
        final byte[] utf8 = msg.getBytes(UTF8);
        final String hex = md5Hex(utf8);
        return hex.toUpperCase();
    }

    // 来自stackoverflow的MD5计算方法，调用了MessageDigest库函数，并把byte数组结果转换成16进制

    /** 获取bytes 的MD5 */
    public String md5Hex(final byte[] bytes) {
        if (bytes == null) {
            return "";
        }

        final byte[] array = md5(bytes);
        return hex(array);
    }

    /** 获取bytes 的MD5 */
    public byte[] md5(final byte[] bytes) {
        if (bytes == null) {
            return new byte[0];
        }

        try {
            final MessageDigest md = MessageDigest.getInstance("MD5");
            return md.digest(bytes);
        } catch (final NoSuchAlgorithmException e) {
            return new byte[0];
        }
    }
    
    /**
     * shiro密码加密工具类
     *
     * @param credentials 密码
     * @param saltSource 密码盐
     * @return
     */
    public static String md5(String credentials, String saltSource) {
        ByteSource salt = new Md5Hash(saltSource);
        return new SimpleHash(hashAlgorithmName, credentials, salt, hashIterations).toString();
    }

    public static String encrypt(String source) {
        return encodeMd5(source.getBytes());
    }

    private static String encodeMd5(byte[] source) {
        try {
            return encodeHex(MessageDigest.getInstance("MD5").digest(source));
        } catch (NoSuchAlgorithmException var2) {
            throw new IllegalStateException(var2.getMessage(), var2);
        }
    }

    private static String encodeHex(byte[] bytes) {
        StringBuffer buffer = new StringBuffer(bytes.length * 2);

        for(int i = 0; i < bytes.length; ++i) {
            if ((bytes[i] & 255) < 16) {
                buffer.append("0");
            }

            buffer.append(Long.toString((long)(bytes[i] & 255), 16));
        }

        return buffer.toString();
    }

    public static String getRandomString(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();

        for(int i = 0; i < length; ++i) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }

        return sb.toString();
    }



}
