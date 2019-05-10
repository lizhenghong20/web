package cn.farwalker.ravv.pay;

import cn.farwalker.ravv.service.order.paymemt.biz.IOrderPaymemtBiz;
import cn.farwalker.ravv.service.paypal.service.IPaypalService;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.*;

/**
 * 此类只负责paypal的回调接口
 * Created by asus on 2018/12/12.
 */
@Controller
@RequestMapping("/paypal_callback")
public class PaypalCallbackController {
    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private IPaypalService paypalService;

    @Autowired
    private IOrderPaymemtBiz iOrderPaymemtBiz;




    @RequestMapping(method = RequestMethod.GET)
    public String index(){
        return "index";
    }

    @RequestMapping(method = RequestMethod.GET,value = "/no")
    public String indexNo(){
        return "index_noid";
    }

    @RequestMapping(method = RequestMethod.GET,value = "/url")
    public String URL(){
        return "index_url";
    }


    /**
     * 取消支付
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/pay_cancel")
    public String cancelPay(){
        return "cancel";
    }

    /**
     * 下单成功后 pay 回调执行支付
     * @param paymentId
     * @param payerId
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "pay_return")
    public String executePay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId){
        try {
            /**
             * 支付
             */
            Payment payment = paypalService.executePayment(paymentId, payerId);
            log.info("================支付成功============");

            /**
             * 保存支付信息,更新订单状态
             */
            paypalService.updatePayment(payment);
            log.info("============更新订单状态成功=============");
            /**
             * 支付成功重定向页面
             */
            if(payment.getState().equals("approved")){
                return "success";
            }
        } catch (PayPalRESTException e) {
            log.error(e.getMessage());
            return "failed";
        }
        return "failed";
    }




    //生成美国州和市的数据查询语句
    public static void main(String[] args) {
        File file = new File("f:/city.txt");

        BufferedReader reader = null;
        Set<String>  states = new HashSet<>();
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            tempString = reader.readLine();
            List<String> statesList  = Arrays.asList(tempString.split("value=\"")) ;
            for(String item:statesList){

                if(item.charAt(0)>='a'&&item.charAt(0) <= 'z'||
                        item.charAt(0)>='A'&&item.charAt(0) <= 'Z'||item.charAt(0)==' '){

                    StringBuilder temp = new StringBuilder();
                    for(int i = 0; i < item.length(); i++){
                        if(item.charAt(i)>='a'&&item.charAt(i) <= 'z'||
                                item.charAt(i)>='A'&&item.charAt(i) <= 'Z'||item.charAt(i) == ' '){
                            temp.append(item.charAt(i));
                        }
                        else{
                            if(temp.toString() != null)
                                states.add(temp.toString());

                            break;
                        }
                    }
                }

            }

            System.out.println("states num:" + states.size());
            Map<String,List<String>> map = new HashMap<>();
            for(String item : states){
                map.put(item,new ArrayList<>());
                System.out.println(item);
            }




            String s = null;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                if(states.contains(tempString.trim())) {
                    s = tempString.trim();
                    continue;
                }
                    List<String> cityList = Arrays.asList(tempString.split("value=\""));
                    for (String item : cityList) {
                        if (item.charAt(0) >= 'a' && item.charAt(0) <= 'z' ||
                                item.charAt(0) >= 'A' && item.charAt(0) <= 'Z' || item.charAt(0) == ' ') {

                            StringBuilder temp = new StringBuilder();
                            for (int i = 0; i < item.length(); i++) {
                                if (item.charAt(i) >= 'a' && item.charAt(i) <= 'z' ||
                                        item.charAt(i) >= 'A' && item.charAt(i) <= 'Z' || item.charAt(i) == ' ') {
                                    temp.append(item.charAt(i));
                                } else {
                                    if (temp.toString() != null)
                                        map.get(s).add(temp.toString());
                                    break;
                                }
                            }
                        }
                    }
            }
            String insertStatesSql = "";
            String insertCitySql = "";
            long stateBaseId = 1081313764983054337L;
            long cityBaseId = 2071317231617253377L;

            int num = 0;
            for(Map.Entry<String,List<String>> item : map.entrySet()){
                num += item.getValue().size();
//                System.out.println("state num = " + num++);
//                System.out.print(item.getKey().toString() + ":  ");
//                insertStatesSql = insertStatesSql + "(" + stateBaseId + "," + "'"+  item.getKey().toString() + "'" + "," +   "'" + "USA" +"'" + ","+ "0" +","+  "0" + ")" + "," + "\n"     ;
//                for(String one:item.getValue()){
//                    insertCitySql = insertCitySql + "(" + cityBaseId++ + "," + "'"+  one + "'" + "," +   "'" + "USA" +"'" + ","+ "1" +","+  stateBaseId + ")" + "," + "\n"     ;
//                }
//                stateBaseId++;
            }
            System.out.println("num == " + num);

            System.out.println("=======================================================");
            insertStatesSql =
            "insert into base_area\n"
            +"(" + "id"  + ","+  "name"   + ","  + "country_code"  +"," +  "is_leaf" +","+  "pid" + ")"  + "\n"
                    + "VALUES\n"
             +insertStatesSql;
            System.out.println(insertStatesSql);
            System.out.println("=======================================================");

            insertCitySql =
                    "insert into base_area\n"
                            +"(" + "id"  + ","+  "name"   + ","  + "country_code"  +"," +  "is_leaf" +","+  "pid" + ")"  + "\n"
                            + "VALUES\n"
                            +insertCitySql;
            System.out.println(insertCitySql);
            File out = new File("f:/insert_city_sql.txt");
            BufferedWriter bw = new BufferedWriter(new FileWriter(out));
            bw.write(insertCitySql);


            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    }
}

