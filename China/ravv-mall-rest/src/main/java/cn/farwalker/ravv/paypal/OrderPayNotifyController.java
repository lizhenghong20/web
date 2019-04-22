package cn.farwalker.ravv.paypal;

import cn.farwalker.ravv.service.payment.wechatpaycallback.WechatPayCallbackService;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/order_pay_notify")
public class OrderPayNotifyController {

    @Autowired
    private WechatPayCallbackService wechatPayCallbackService;

    @RequestMapping("asyncWechatPay")
    public void asyncWechatPay(HttpServletRequest request, HttpServletResponse response) throws IOException, DocumentException, DocumentException {
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        log.info("############################### 微信支付异步接口 Start #############################");
        log.info(request.getRequestURI());
        log.info("############################### 微信支付异步接口 End #############################");

        // 解析结果存储在HashMap
        Map<String, String> map = new HashMap<String, String>();
        InputStream inputStream = request.getInputStream();
        // 读取输入流
        SAXReader reader = new SAXReader();
        Document document = reader.read(inputStream);
        // 得到xml根元素
        Element root = document.getRootElement();
        // 得到根元素的所有子节点
        List<Element> elementList = root.elements();

        // 遍历所有子节点
        for (Element e : elementList) {
            map.put(e.getName(), e.getText());
        }
        // 释放资源
        inputStream.close();
        inputStream = null;

        String strReturnCode = map.get("return_code");
        log.info("############################### return_code :" + strReturnCode);
        if (!"SUCCESS".equals(strReturnCode)) {
            return;
        }
        //业务逻辑处理
        wechatPayCallbackService.doSuccess(map);

        response.getWriter().print("success");
    }

}
