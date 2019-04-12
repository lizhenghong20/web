package cn.farwalker.ravv.style;

import cn.farwalker.ravv.service.paypal.URLUtils;
import cn.farwalker.waka.core.JsonResult;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by asus on 2019/1/17.
 */
@RestController
@RequestMapping("/style")
public class Style {

    @RequestMapping(method = RequestMethod.POST, value = "/app_style")
    public JsonResult<String> getAppStyle(HttpServletRequest request){
        String styleURL = URLUtils.getBaseURl(request) + "/" + "style_control.css";
        return JsonResult.newSuccess(styleURL);
    }

}