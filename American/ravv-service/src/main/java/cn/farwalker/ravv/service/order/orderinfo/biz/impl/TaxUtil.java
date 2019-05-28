package cn.farwalker.ravv.service.order.orderinfo.biz.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.farwalker.ravv.service.order.paymemt.model.OrderPaymemtBo;
import cn.farwalker.waka.core.RavvExceptionEnum;
import cn.farwalker.waka.core.WakaException;
import cn.farwalker.waka.util.Tools;

import com.taxjar.Taxjar;
import com.taxjar.exception.TaxjarException;
import com.taxjar.model.taxes.Tax;
import com.taxjar.model.taxes.TaxResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TaxUtil {


	private static final String D_From_country="from_country",D_From_zip="from_zip"
			,D_From_state="from_state",D_From_city="from_city",D_From_street="from_street";
	private static final String D_To_country="to_country",D_To_zip="to_zip",D_To_state="to_state"
			,D_To_city="to_city",D_To_street="to_street";
	private static final String D_Amount="amount",D_Shipping="shipping";
	
	/**配送地址*/
	public static class Address{
		private String country;
		private String zip;
		private String state;
		private String city;
		private String street;
		
		/**
		 * 必须的参数（默认美国US）
		 * @param state 州
		 * @param zip 邮编
		 */
		public Address(String state,String zip){
			this("US",state,zip);
		}
		/**
		 * 必须的参数
		 * @param country 国家
		 * @param state 州
		 * @param zip 邮编
		 */
		public Address(String country,String state,String zip){
			this.setCountry(country);
			this.setState(state);
			this.setZip(zip);
		}
		/**为了兼容*/
		public Address(){
		}
		
		/**2位国家编码，只能是美国或者加拿大(US|CA)*/
		public String getCountry() {
			return country;
		}
		/**2位国家编码，只能是美国或者加拿大(US|CA)*/
		public void setCountry(String country) {
			if("CA".equalsIgnoreCase(country) || "US".equalsIgnoreCase(country)){
				this.country = country.toUpperCase();	
			}
			else{
				throw new WakaException("2位国家编码，只能是美国或者加拿大(US|CA):" + country);
			}
			
		}
		/**邮政编码*/
		public String getZip() {
			return zip;
		}
		/**邮政编码*/
		public void setZip(String zip) {
			this.zip = zip;
		}
		/**州、省(2位州名缩写)*/
		public String getState() {
			return state;
		}
		/**州、省(2位州名缩写)*/
		public void setState(String state) {
			this.state = state;
		}
		/**市*/
		public String getCity() {
			return city;
		}
		/**市*/
		public void setCity(String city) {
			this.city = city;
		}
		/**街道*/
		public String getStreet() {
			return street;
		}
		/**街道*/
		public void setStreet(String street) {
			this.street = street;
		}
	}
	/**统计应交税的总价 {@link OrderPaymemtBo#getPayTotalFee(OrderPaymemtBo)}
    public static int getPayTotalFee(OrderPaymemtBo pbo){
    	int rs = OrderPaymemtBo.getPayTotalFee(pbo) 
    			- ifnull(pbo.getPostFee()) 
    			- ifnull(pbo.getTaxFee());
    	return rs;
    }
    private static int ifnull(BigDecimal b  ){
    	if(b == null){
    		return 0;
    	}
    	return (int)(b.doubleValue() * 100);
    }*/
    
    /**token 后期可以改为配置
     * https://developers.taxjar.com/api/reference/#taxes
     * */
    public static String getTaxToken(){
    	return "d6ce482f299afefec05ce5e6deff0aed";//后期可以改为配置
    }
	/**
	 * 计算税金
	 * @param amount 被征税的总额(除了税费、物流外的金额){@link OrderPaymemtBo#getPayTotalFee(OrderPaymemtBo)}
	 * @param from 发货地址
	 * @param to 收货地址
	 * @return
	 */
    public static BigDecimal calcTax (BigDecimal amount,  Address from,Address to){
    	Tax tax = calcTaxResponse(amount,  from, to);
    	Float fx = tax.getAmountToCollect();
    	BigDecimal rs = new BigDecimal(fx.doubleValue());
    	BigDecimal result = rs.setScale(2,BigDecimal.ROUND_HALF_UP);
    	return result;
    }
    
   /** 能正常计算税费 
  	* http://52.53.127.206:8080/ravv-mall-rest/web/test?from_country=US&from_state=OK&from_zip=74965&amount=50&shipping=0&to_zip=92093&from_city=Adairville&to_country=US&to_city=Sandiego&from_street=%E5%8D%97%E6%96%B9%E8%BD%AF%E4%BB%B6%E5%9B%AD&to_street=address&500&to_state=CA
    * 校验邮政编码是否正确
 	* https://usa.youbianku.com/zh-hans/zipcode/74965
 	*/ 
    public static String calcTaxJson(BigDecimal amount,  Address from,Address to){
    	Tax tax = calcTaxResponse(amount,  from, to);
    	return Tools.json.toJson(tax);
    }
    public static Tax calcTaxResponse(BigDecimal amount,  Address from,Address to){
    	try {
            Map<String, Object> params = new HashMap<>();
            params.put(D_From_country, isRequired(from.getCountry(),"国家"));
            params.put(D_From_zip, isRequired(from.getZip(),"邮编"));
            params.put(D_From_state,isRequired(from.getState(),"州"));
            params.put(D_From_city, isNull(from.getCity(),""));
            params.put(D_From_street, isNull(from.getStreet(),""));
            
            params.put(D_To_country,isRequired(to.getCountry(),"国家"));
            params.put(D_To_zip, isRequired(to.getZip(),"邮编"));
            params.put(D_To_state,isRequired(to.getState(),"州"));
            params.put(D_To_city, isNull(to.getCity(),""));
            params.put(D_To_street,isNull( to.getStreet(),""));
            
            String amounts = (amount == null ? "0":amount.toString());
            params.put(D_Amount, amounts);
            //String postFees =(postFee == null ? "0":postFee.toString());
            params.put(D_Shipping, "0");

            /*
            List<Map> nexusAddresses = new ArrayList<>();
            Map<String, Object> nexusAddress = new HashMap<>();
            nexusAddress.put("country", "US");
            nexusAddress.put("zip", "92093");
            nexusAddress.put("state", "CA");
            nexusAddress.put("city", "La Jolla");
            nexusAddress.put("street", "9500 Gilman Drive");
            nexusAddresses.add(nexusAddress);
            */
            /*
            List<Map> lineItems = new ArrayList<>();
            Map<String, Object> lineItem = new HashMap<>();
            lineItem.put("id", 1);
            lineItem.put("quantity", 1);
            lineItem.put("product_tax_code", "20010");
            lineItem.put("unit_price", 15);
            lineItem.put("discount", 0);
            lineItems.add(lineItem);

            //params.put("nexus_addresses", nexusAddresses);
            params.put("line_items", lineItems);
			*/
            
            Taxjar client = new Taxjar(getTaxToken());
            //被征税的总额 taxable_amount	Amount of the order to be taxed.
            //税金 amount_to_collect	Amount of sales tax to collect.
            //税率 rate Overall sales tax rate of the order (amount_to_collect ÷ taxable_amount).
            TaxResponse res = client.taxForOrder(params);
            if(res == null){
            	throw new WakaException("计税接口出错,返回null:");
            }
            //Float fx = res.tax.getAmountToCollect();
            //return new BigDecimal(fx);
            return res.tax ;//Tools.json.toJson(res.tax);
            
        } catch (TaxjarException e) {
    		e.printStackTrace();
    		log.info("===============税金:{}",e);
            throw new WakaException(RavvExceptionEnum.ADDRESS_IS_INVAILD);
        }
    }
    
    private static String isNull(String s,String def){
    	return (Tools.string.isEmpty(s) ? def: s);
    }
    private static String isRequired(String s,String field){
    	if(Tools.string.isEmpty(s)){
    		throw new WakaException(field + "为空,不能计算税费");
    	}
    	return s;
    }
    
	public static void main(String[] args) {
		Taxjar client = new Taxjar("b08fd0d7ca7573e7fc95dd3070d628c0");

        try {
            Map<String, Object> params = new HashMap<>();
            params.put("from_country", "US");
            params.put("from_zip", "92093");
            params.put("from_state", "CA");
            params.put("from_city", "La Jolla");
            params.put("from_street", "9500 Gilman Drive");
            
            params.put("to_country", "US");
            params.put("to_zip", "90002");
            params.put("to_state", "CA");
            params.put("to_city", "Los Angeles");
            params.put("to_street", "1335 E 103rd St");
            params.put("amount", 15);
            params.put("shipping", 1.5);

            List<Map> nexusAddresses = new ArrayList<>();
            Map<String, Object> nexusAddress = new HashMap<>();
            nexusAddress.put("country", "US");
            nexusAddress.put("zip", "92093");
            nexusAddress.put("state", "CA");
            nexusAddress.put("city", "La Jolla");
            nexusAddress.put("street", "9500 Gilman Drive");
            nexusAddresses.add(nexusAddress);

            List<Map> lineItems = new ArrayList<>();
            Map<String, Object> lineItem = new HashMap<>();
            lineItem.put("id", 1);
            lineItem.put("quantity", 1);
            lineItem.put("product_tax_code", "20010");
            lineItem.put("unit_price", 15);
            lineItem.put("discount", 0);
            lineItems.add(lineItem);

            params.put("nexus_addresses", nexusAddresses);
            params.put("line_items", lineItems);

            TaxResponse res = client.taxForOrder(params);
        } catch (TaxjarException e) {
            e.printStackTrace();
        }
    }
}
