package cn.farwalker.ravv.common.constants;

import cn.farwalker.waka.orm.EnumManager;

/**
 * Created by asus on 2018/12/14.
 */
public enum WebSliderJumpTypeEnum implements EnumManager.IEnumJsons {
   GOODS("goods","跳到商品页"),
    FLASHSALE("flashsale","跳到限时购"),
    NEWARRIVAL("newarrival","跳到新品上市"),
    FIRSTMENUM("firstmenu","跳到第一级菜单");


    private final String key;
    private final String desc;

    WebSliderJumpTypeEnum(String key, String desc) {
        this.key = key;
        this.desc = desc;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getLabel() {
        return desc;
    }

    public boolean compare(String key) {
        return this.key.equals(key);
    }

}