package cn.farwalker.waka.util;

import org.apache.commons.lang3.StringUtils;

public class MD5PasswordUtil {
	public static boolean verify(String password, String salt, String dbEncryptedPassword) {
		String encryptedPassword;
		if (password.length() == 32) {
			encryptedPassword = password;
		}else {
			String str;
			if (StringUtils.isEmpty(salt)) {
				str = password;
			}
			else {
				str = password + "@" + salt;
			}
			encryptedPassword = MD5Util.encrypt(str).toLowerCase();
		}
		return encryptedPassword.equalsIgnoreCase(dbEncryptedPassword);
	}
	
	public static String encrypt(String password) {
		return MD5Util.encrypt(password).toLowerCase();
	}
	
	public static void main(String[] args) {
		String password = "111111";
		String salt = "a123b4";
		String str = password + "@" + salt;
		String encryptedPassword = MD5PasswordUtil.encrypt(str);
		System.out.println(encryptedPassword);
	}
}
