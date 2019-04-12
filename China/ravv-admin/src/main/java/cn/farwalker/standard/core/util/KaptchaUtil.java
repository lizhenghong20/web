package cn.farwalker.standard.core.util;

import cn.farwalker.waka.config.properties.WakaProperties;
import cn.farwalker.waka.util.Tools;

/**
 * 验证码工具类
 */
public class KaptchaUtil {

    /**
     * 获取验证码开关
     *
     * @author Jason Chen
     * @Date 2017/5/23 22:34
     */
    public static Boolean getKaptchaOnOff() {
        return Tools.springContext.getBean(WakaProperties.class).getKaptchaOpen();
    }
}