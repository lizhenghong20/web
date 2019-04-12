package cn.farwalker.ravv.common.constants;

import cn.farwalker.waka.orm.EnumManager;

public enum GoodsPointEnum implements EnumManager.IEnumJsons {

    PRODUCT("1","PRODUCT"),
    SHIPMENT("2","SHIPMENT")
    ;
    private final String key;
    private final String desc;

    GoodsPointEnum(String key, String desc){
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

    public boolean compare(int key) {
        return this.key.equals(key);
    }
}
