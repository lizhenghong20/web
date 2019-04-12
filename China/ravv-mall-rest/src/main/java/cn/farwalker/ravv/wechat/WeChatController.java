package cn.farwalker.ravv.wechat;


import cn.farwalker.waka.components.serviceForRavv.WechatService;
import cn.farwalker.waka.core.JsonResult;
import cn.farwalker.waka.core.WakaException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/test/wechat")
public class WeChatController {

    @Autowired
    private WechatService wechatService;

    @RequestMapping("/wechat_pay_unifiedorder")
    public JsonResult<Map<String, String>> wechatPayUnifiedorder(HttpServletRequest request, String type){
        try{
            return JsonResult.newSuccess(wechatService.wechatPayUnifiedorder(request,type));
        }
        catch(WakaException e){
            log.error("",e);
            return JsonResult.newFail(e.getCode(),e.getMessage());
        }catch(Exception e){
            log.error("",e);
            return JsonResult.newFail(e.getMessage());
        }
    }

    /**
     * @description: 获取二维码链接
     * @param: request,type
     * @return type
     * @author Mr.Simple
     * @date 2018/11/29 11:43
     */
    @RequestMapping("/wechat_pay_for_codeurl")
    public JsonResult<String> wechatPayForCodeurl(HttpServletRequest request, String type) throws IOException{

        try{
            Map<String,String> result = wechatService.wechatPayUnifiedorder(request, type);
            String codeUrl = result.get("code_url");
//            String codeUrl = "https://github.com/hbbliyong/QRCode.git";
//            //生成二维码
//            int width = 300;
//            int height = 300;
//            String format = "jpg";
//            HashMap hits = new HashMap();
//            hits.put(EncodeHintType.CHARACTER_SET,"utf-8");
//            hits.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
//            hits.put(EncodeHintType.MARGIN, 2);
//            ServletOutputStream out = response.getOutputStream();
//            BitMatrix bitMatrix = new MultiFormatWriter().encode(codeUrl, BarcodeFormat.QR_CODE, width, height,hits);
//            //如果做网页版输出可以用输出到流
//            BufferedImage image = new BufferedImage(bitMatrix.getWidth(),bitMatrix.getHeight(),BufferedImage.TYPE_INT_RGB);
//            for(int x = 0;x < bitMatrix.getWidth();x++)
//                for (int y = 0; y < bitMatrix.getHeight();y++)
//                    image.setRGB(x,y,bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
//            ImageIO.write(image,"jpg",out);
            return JsonResult.newSuccess(codeUrl);
        }
        catch(WakaException e){
            log.error("",e);
            return JsonResult.newFail(e.getMessage());

        }catch(Exception e){
            log.error("",e);
            return JsonResult.newFail(e.getMessage());
        }

    }

    /**
     * @description: 生成二维码
     * @param: type,request,response
     * @return string,outputStream
     * @author Mr.Simple
     * @date 2018/11/29 11:45
     */
    @RequestMapping("/wechat_pay_for_QR")
    public JsonResult<String> wechatPayForQR(HttpServletRequest request,HttpServletResponse response, String type) throws IOException{
        try{
            Map<String,String> result = wechatService.wechatPayUnifiedorder(request, type);
            String codeUrl = result.get("code_url");
//            String codeUrl = "https://github.com/hbbliyong/QRCode.git";
            return JsonResult.newSuccess(wechatService.genQR(response,codeUrl));
        }
        catch(WakaException e){
            log.error("",e);
            return JsonResult.newFail(e.getMessage());

        }catch(Exception e){
            log.error("",e);
            return JsonResult.newFail(e.getMessage());
        }

    }

    @RequestMapping("/async_wechat_pay")
    public void asyncWechatPay(HttpServletRequest request, HttpServletResponse response) throws IOException{
        try{
            if(wechatService.asyncWechatPay(request,response))
                response.getWriter().print("WechatPay success");
            else
                response.getWriter().print("WechatPay fail");
        }
        catch(WakaException e){
            log.error("",e);
            response.getWriter().print("fail" + e);

        }catch(Exception e){
            log.error("",e);
            response.getWriter().print("fail" + e);
        }
    }


}
