package cn.farwalker.ravv.service.flash.sale.model;

import java.util.Date;

import cn.farwalker.ravv.common.constants.FlashSaleStatusEnum;

/**
 * Created by asus on 2018/11/29.
 */
public class FlashSaleVo extends FlashSaleBo {
	private static final long serialVersionUID = 7738128977787279909L;
	/**是否有效(未生效、冻结、进行中、结束)
    private FlashSaleStatusEnum status;*/
    
	private long getTime(Date d,long def){
		return (d == null? def: d.getTime());
	}
	/** 是否有效(未生效、冻结、进行中、结束)*/
    public FlashSaleStatusEnum getStatus(){
        long nows = System.currentTimeMillis();
        long freeze = getTime( this.getFreezetime() ,0);
        long start =  getTime( this.getStarttime() ,0);
        long endt =  getTime( this.getEndtime() ,0);
    	
        /*
         * INVALID("INVALID","从创建到冻结"),
        FROZEN("FROZEN","从开始冻结到冻结结束即活动开始"),
        UNDERWAY("UNDERWAY","活动开始到活动结束"),
        FINISH("FINISH","活动结束以后");
        
         */
        if(nows < freeze){
        	return FlashSaleStatusEnum.INVALID;
        }
        else if(nows < start){
        	return FlashSaleStatusEnum.FROZEN;
        }
        else if(nows < endt){
        	return FlashSaleStatusEnum.UNDERWAY;
        }
        else {
        	return FlashSaleStatusEnum.FINISH;
        } 
    }
    /** 是否有效(未生效、冻结、进行中、结束)
    public void setStatus(FlashSaleStatusEnum status){
        this.status =status;
    }*/

}
