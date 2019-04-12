package cn.farwalker.waka.core;

/**
 * Created by asus on 2019/3/26.
 */
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import com.alibaba.fastjson.JSON;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


public class RenderUtil {
    public RenderUtil() {
    }

    public static void renderJson(HttpServletResponse response, Object jsonObject) {
        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter e = response.getWriter();
            e.write(JSON.toJSONString(jsonObject));
        } catch (IOException var3) {
            throw new WakaException(RavvExceptionEnum.TOKEN_VERIFICATION_FAILED);
        }
    }
}

