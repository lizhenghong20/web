package cn.farwalker.ravv.common.constants;

import cn.farwalker.waka.orm.EnumManager.IEnumJsons;

public enum OrderMessageEnum implements IEnumJsons {

    PAID_UNSENDGOODS("Your order have paid successfully", "待发货"),
    SENDGOODS_UNCONFIRM("Your order has been dispatched", "已发货"),
    SING_GOODS("Your order has been signed", "已收货")
    ;
    private String key;
    private String desc;

    OrderMessageEnum(String key, String desc){
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
}
