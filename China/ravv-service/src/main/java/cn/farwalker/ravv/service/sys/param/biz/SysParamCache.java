package cn.farwalker.ravv.service.sys.param.biz;

import cn.farwalker.waka.cache.CacheManager;
import cn.farwalker.waka.util.Tools;
import cn.farwalker.waka.util.DateUtil.FORMAT;

/**
 * 缓存处理
 * @author Administrator
 */
public class SysParamCache {
    /**默认15分钟*/
    private static final int D_OrderUnfreeze=15;
    /**
     * 取没有支付的订单解除冻结时间(分钟，有缓存-每小时过期) 
     * @return 永远有值(默认15分钟)
     */
    public static int getUnfreezeCacheDelay(ISysParamService service){
    	String key = "order_unfreeze_" + Tools.date.formatDate(FORMAT.YYYYMMDDHHMMSS).substring(0,10);
    	String times = CacheManager.cache.get(key);
    	int delay = 0;//默认
    	if(Tools.number.isInteger(times)){
    		delay = Integer.parseInt(times);
    	}
    	else {
    		Integer tx = service.getOrderUnfreezeTime();
    		delay = Tools.number.nullIf(tx, D_OrderUnfreeze);
    		CacheManager.cache.put(key, String.valueOf(delay));
    	}
    	if(delay <=0){
    		delay =D_OrderUnfreeze;
    	} 
    	return delay;
    }
}
