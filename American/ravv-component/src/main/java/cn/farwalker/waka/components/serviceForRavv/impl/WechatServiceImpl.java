package cn.farwalker.waka.components.serviceForRavv.impl;

import cn.farwalker.waka.components.serviceForRavv.WechatService;
import cn.farwalker.waka.components.wechatpay.mp.api.WxMpInMemoryConfigStorage;
import cn.farwalker.waka.components.wechatpay.mp.api.WxMpService;
import cn.farwalker.waka.util.Tools;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class WechatServiceImpl implements WechatService {

    @Autowired
    private WxMpService wxMpService;

    @Override
    public Map<String, String> wechatPayUnifiedorder(HttpServletRequest request, String type) {
        Map<String,String> parameters = new HashMap<>();
        parameters.put("out_trade_no","asdfghjklqwertyuiop");
        try {
            parameters.put("spbill_create_ip", Tools.clientIP.getRemoteHost(request));
        } catch (IOException e) {
            e.printStackTrace();
        }
        parameters.put("trade_type", type);
        parameters.put("notify_url","/wechat/async_wechat_pay");
        parameters.put("total_fee", "100");

        WxMpInMemoryConfigStorage wechatConfig = new WxMpInMemoryConfigStorage();
        wechatConfig.setAppId("wxdb42e68590ec2a1a");
        wechatConfig.setSecret("76183c28ae16660113a1252deb42bf69");
        wechatConfig.setAccessToken("xkmen");
        wechatConfig.setAesKey("xkmen");
        wechatConfig.setPartnerId("1485160762");
        wechatConfig.setPartnerKey("xkmen2017072109119012707102nemkx");
        wechatConfig.setTmpDirFile(new File("/mnt/tomcat/xkzp/xkzp_file/temp"));

        wxMpService.setWxMpConfigStorage(wechatConfig);

        Map<String,String> result = wxMpService.getJSSDKPayInfoWithSandBox(parameters);

        return result;
    }

    @Override
    public String genQR(HttpServletResponse response, String codeUrl) throws IOException {
        byte[] captchaChallengeAsJpeg = null;
        ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
        //生成二维码
        int width = 300;
        int height = 300;
        String format = "jpg";
        HashMap hits = new HashMap();
        hits.put(EncodeHintType.CHARACTER_SET,"utf-8");
        hits.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        hits.put(EncodeHintType.MARGIN, 2);
        BitMatrix bitMatrix = null;
        try {
            bitMatrix = new MultiFormatWriter().encode(codeUrl, BarcodeFormat.QR_CODE, width, height,hits);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        //如果做网页版输出可以用输出到流
        BufferedImage image = new BufferedImage(bitMatrix.getWidth(),bitMatrix.getHeight(),BufferedImage.TYPE_INT_RGB);
        for(int x = 0;x < bitMatrix.getWidth();x++)
            for (int y = 0; y < bitMatrix.getHeight();y++)
                image.setRGB(x,y,bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
        ImageIO.write(image,format,jpegOutputStream);
        captchaChallengeAsJpeg = jpegOutputStream.toByteArray();
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        ServletOutputStream responseOutputStream = response.getOutputStream();
        responseOutputStream.write(captchaChallengeAsJpeg);
        responseOutputStream.flush();
        responseOutputStream.close();
        return "success";
    }

    @Override
    public boolean asyncWechatPay(HttpServletRequest request, HttpServletResponse response) throws IOException, DocumentException {
        log.info("############################### request.getRequestURI:{}",request.getRequestURI());
        Map<String,String> map = new HashMap<>();
        InputStream is = request.getInputStream();
        SAXReader reader = new SAXReader();
        Document document = reader.read(is);
        Element root = document.getRootElement();
        List<Element> elementList = root.elements();
        for (Element e: elementList) {
            map.put(e.getName(), e.getText());
        }
        is.close();
        is = null;
        String strReturnCode = map.get("return_code");
        log.info("############################### return_code :{}",strReturnCode);
        String strOrderCode = map.get("out_trade_no");
        String total_fee = map.get("total_fee");
        String transaction_id = map.get("transaction_id");
        BigDecimal fee = new BigDecimal(total_fee).multiply(new BigDecimal(0.01)).setScale(2, BigDecimal.ROUND_DOWN);
        if(!"SUCCESS".equals(strReturnCode))
            return false;
        return true;
    }
}
