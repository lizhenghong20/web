package cn.farwalker.ravv.service.goods.base.model;

import lombok.Data;

import java.util.Date;

/**
 * Created by asus on 2018/12/29.
 */

@Data
public class ActivityField {
    boolean isFrozen;
    boolean isUnderWay;

    //活动title
    String title;

    //活动开始时间，若处于冻结状态则倒计时这个时间。
    Date startTime;

    //活动结束时间，若处于开始状态则倒计时这个时间。
    Date endTime;

}
