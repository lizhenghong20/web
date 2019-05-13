package cn.farwalker.ravv.pay;

import cn.farwalker.ravv.service.payment.callback.StripeCallbackService;
import cn.farwalker.waka.core.JsonResult;
import cn.farwalker.waka.core.WakaException;
import com.google.gson.JsonSyntaxException;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.ApiResource;
import com.stripe.net.Webhook;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

@Slf4j
@Controller
@RequestMapping("/stripe_callback")
public class StripeCallbackController {

    @Autowired
    private StripeCallbackService stripeCallbackService;

    @RequestMapping("/pay_succeeded")
    @ResponseBody
    public String paySucceeded(HttpServletRequest request, HttpServletResponse response){

        try {
            Enumeration<String> requestHeader = request.getHeaderNames();
            log.info("------- header -------");
            while(requestHeader.hasMoreElements()){
                String headerKey = requestHeader.nextElement().toString();
                //打印所有Header值
                log.info("-----------------headerKey={},value={}----------------", headerKey, request.getHeader(headerKey));
            }

            log.info("------- parameter -------");


            String endpointSecret = "whsec_Scn2X5UvfTOQhjRnzzNdpaqEtUc1m7ZJ";
            BufferedReader br = request.getReader();
            String wholeRequest, payload = "";
            StringBuffer req = new StringBuffer();
            Event event = null;
            while ((wholeRequest = br.readLine()) != null){
                req.append(wholeRequest);
            }
            payload = req.toString();
            log.info("========================request:{}", payload);
            String sigHeader = request.getHeader("stripe-signature");
            log.info("========================request:{}", sigHeader);
            event = (Event) ApiResource.GSON.fromJson(payload, Event.class);/*Webhook.constructEvent(
                    payload, sigHeader, endpointSecret, 24 * 60L
            );*/
            PaymentIntent intent = null;
            switch(event.getType()) {
                case "payment_intent.succeeded":
                    log.info("===================成功");
                    intent = (PaymentIntent) event.getData().getObject();
                    System.out.println("Succeeded: " + intent.getId());
                    break;
                // Fulfil the customer's purchase

                case "payment_intent.payment_failed":
                    log.info("===================失败");
                    intent = (PaymentIntent) event.getData().getObject();
                    System.out.println("Failed: " + intent.getId());
                    break;
                // Notify the customer that payment failed

                default:
                    // Handle other event types
                    break;
            }
            //获取订单id，执行更新订单状态
            Map<String, String> metadata = intent.getMetadata();
            Long orderId =  Long.parseLong(metadata.get("orderId"));
            stripeCallbackService.doSuccess(orderId);
            response.setStatus(200);
            return "OK";

        } catch (WakaException e) {
            log.error("", e);
            return "";
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JsonSyntaxException e) {
            // Invalid payload
            response.setStatus(400);
            return "Invalid payload";
        } /*catch (SignatureVerificationException e) {
            // Invalid signature
            e.printStackTrace();
            log.error("=================================={}", e);
            response.setStatus(400);
            return "Invalid signature";
        }*/
        catch (Exception e) {
            log.error("", e);
            return "";
        }
        log.info("======================返回失败");
        return "";
    }

    @RequestMapping("/before_pay")
    @ResponseBody
    public JsonResult<String> beforePay(){
        try{
//            Stripe.apiKey = "sk_test_aAcqEpgzPmQYqplFVNErNS3U004xRfetnl";
//            Map<String, Object> paymentIntentParams = new HashMap<String, Object>();
//            paymentIntentParams.put("amount", 1099);
//            paymentIntentParams.put("currency", "usd");
//            ArrayList<String> paymentMethodTypes = new ArrayList<>();
//            paymentMethodTypes.add("card");
//            paymentIntentParams.put("payment_method_types", paymentMethodTypes);
//
//            PaymentIntent intent = PaymentIntent.create(paymentIntentParams);
//            log.info("=====================this intent id:{}", intent.getId());

            return JsonResult.newSuccess(/*intent.getClientSecret()*/);
        }
        catch(WakaException e){
            log.error("",e);
            return JsonResult.newFail(e.getCode(),e.getMessage());
        }
    }

    private long getTimestamp(String sigHeader) {
        String[] items = sigHeader.split(",", -1);
        String[] var2 = items;
        int var3 = items.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            String item = var2[var4];
            String[] itemParts = item.split("=", 2);
            if (itemParts[0].equals("t")) {
                log.info("========================t:{}", itemParts[1]);
                return Long.parseLong(itemParts[1]);
            }
        }

        return -1L;
    }


    private String changeSigHeader(String sigHeader, Long afterTime) {
        String[] items = sigHeader.split(",", -1);
        String[] var2 = items;
        int var3 = items.length;
        StringBuffer stringBuffer = new StringBuffer();

        for(int var4 = 0; var4 < var3; ++var4) {
            String item = var2[var4];
            String[] itemParts = item.split("=", 2);
            if (itemParts[0].equals("t")) {
                itemParts[1] = String.valueOf(afterTime);
                log.info("========================t:{}", itemParts[1]);
            }
            stringBuffer.append(itemParts[0]).append("=").append(itemParts[1]);
            if(var4 != var3 - 1)
                stringBuffer.append(",");
        }

        return stringBuffer.toString();
    }

}
