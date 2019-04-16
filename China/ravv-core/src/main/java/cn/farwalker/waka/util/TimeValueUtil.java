package cn.farwalker.waka.util;

import cn.farwalker.waka.core.WakaException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeValueUtil {

    public static final TimeValueUtil util = new TimeValueUtil();

    private TimeValueUtil(){

    }

    /**
     * @description: 将验证码过期时间加入验证码字段
     * @param: code
     * @return string
     * @author Mr.Simple
     * @date 2018/11/10 14:25
     */
    public String codeAppendTime(String code){
        //将过期时间加入激活码中
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date now = new Date();
        Long time = 30 * 60 * 1000L;
        Date expiredTime = new Date(now.getTime() + time);
        String str = format.format(expiredTime);
        StringBuilder sBuilder = new StringBuilder(code);
        sBuilder.append(",").append(str);

        return sBuilder.toString();
    }

    /**
     * @description: 将验证码时间拆解出来
     * @param: code
     * @return string
     * @author Mr.Simple
     * @date 2018/11/10 14:31
     */
    public String getActivationCode(String code) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String[] s = code.split(",");
        String activationCode = s[0];
        String time = s[s.length - 1];
        Date expireTime = null;
        try{
            expireTime = format.parse(time);
            if(expireTime.compareTo(format.parse(format.format(new Date()))) < 0)
                throw new WakaException("验证码过期");
        }catch (ParseException e){
            e.printStackTrace();
        }


        return activationCode;
    }

}
