package cn.farwalker.ravv.common.constants;

import cn.farwalker.waka.orm.EnumManager.IEnumJsons;

public enum ReturnsMessageEnum implements IEnumJsons{

    CHANGEGOODS("Your change application state have been changed", "换货"),
    REGOODS("Your returned application state have been changed", "退款又退货"),
    REFUND("Your refund application state have been changed", "退款不退货")
    ;
    private String key;
    private String desc;

    ReturnsMessageEnum(String key, String desc){
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
