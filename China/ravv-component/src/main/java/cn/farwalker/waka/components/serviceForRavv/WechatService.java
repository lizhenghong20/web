package cn.farwalker.waka.components.serviceForRavv;

import org.dom4j.DocumentException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public interface WechatService {

    public Map<String,String> wechatPayUnifiedorder(HttpServletRequest request, String type);

    public String genQR(HttpServletResponse response, String codeUrl) throws IOException;

    public boolean asyncWechatPay(HttpServletRequest request, HttpServletResponse response) throws IOException, DocumentException;
}
