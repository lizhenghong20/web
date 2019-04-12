package cn.farwalker.waka.util;

import java.util.Random;

public class SaltUtil {

    public static final SaltUtil util = new SaltUtil();

    private SaltUtil(){

    }

    /**
     * 生成24位盐
     */
    public String createSalt(){
        Random random = new Random();
        StringBuilder sBuilder = new StringBuilder(24);
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+=-";
        for (int i = 0; i < 24; i++) {
            int number = random.nextInt(76);
            sBuilder.append(str.charAt(number));
        }

        // 生成最终的加密盐
        String salt = sBuilder.toString();
        System.out.println(sBuilder.toString());
        return salt;
    }

    /**
     *
     * @param:
     * @return String
     * @author Mr.Simple
     * @date 2018/11/9 11:48
     */
    public String createActivationCode(){
        StringBuilder str = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            str.append(random.nextInt(10));
        }
        return str.toString();
    }
}
