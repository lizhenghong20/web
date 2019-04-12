package cn.farwalker.waka.util;

import cn.farwalker.waka.core.WakaException;

import java.math.BigDecimal;
import java.util.ArrayList;


/**
 * Created by asus on 2018/11/7.
 */
public class BigDecimalUtil {
    public static final BigDecimalUtil util = new BigDecimalUtil();

    private BigDecimalUtil() {

    }

    /**加(v1+v2)*/
    public static BigDecimal add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2);
    }
    /**减(v1-v2)*/
    public static BigDecimal sub(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2);
    }

    /**乘(v1*v2)*/
    public static BigDecimal mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2);
    }
    /**除(v1/v2)*/
    public static BigDecimal div(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, 2, BigDecimal.ROUND_HALF_UP);//四舍五入,保留2位小数 
    }
    
    private static ArrayList<BigDecimal> ifnull(BigDecimal... v1){
    	if(v1 == null){
    		throw new WakaException("操作数据不能为空");
    	}
    	ArrayList<BigDecimal> rs = new ArrayList<>(v1.length);
    	for(BigDecimal v : v1){
    		if(v==null){
    			v = BigDecimal.ZERO;
    		}
    		rs.add(v);
    	}
    	if(rs.isEmpty()){
    		throw new WakaException("操作数据不能为空");
    	}
    	return rs;
    }
    
    /**加(v1+v2)*/
    public BigDecimal add(BigDecimal... v1) {
    	ArrayList<BigDecimal> rvs = ifnull(v1); 
    	BigDecimal rs = rvs.get(0);
    	int size = rvs.size();
    	for(int i = 1;i < size;i++){
    		rs = rs.add(rvs.get(i));
    	}
    	return rs;
    }
    
    /**减(v1-v2-v3)*/
    public BigDecimal sub(BigDecimal... v1) {
    	ArrayList<BigDecimal> rvs = ifnull(v1); 
    	BigDecimal rs = rvs.get(0);
    	int size = rvs.size();
    	for(int i = 1;i < size;i++){
    		rs = rs.subtract(rvs.get(i));
    	}
    	return rs;
    }

    /**乘(v1*v2*v3)*/
    public BigDecimal mul(BigDecimal... v1) {
    	ArrayList<BigDecimal> rvs = ifnull(v1); 
    	BigDecimal rs = rvs.get(0);
    	int size = rvs.size();
    	for(int i = 1;i < size;i++){
    		rs = rs.multiply(rvs.get(i));
    	}
    	return rs; 
    }
    /**除(v1/v2/v3...)*/
    public BigDecimal div(BigDecimal... v1) {
    	ArrayList<BigDecimal> rvs = ifnull(v1); 
    	BigDecimal rs = rvs.get(0);
    	int size = rvs.size();
    	for(int i = 1;i < size;i++){
    		rs = rs.divide(rvs.get(i));
    	}
    	return rs; 
    }
}