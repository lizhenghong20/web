package cn.farwalker.ravv.common.constants;

import cn.farwalker.waka.orm.EnumManager;

public enum GoodsDisplayEnum implements EnumManager.IEnumJsons {

    DISPLAY("1","DISPLAY"),
    UNDISPLAY("2","UNDISPLAY")
    ;

    private final String key;
    private final String desc;
    GoodsDisplayEnum(String key, String desc){
        this.key = key;
        this.desc = desc;
    }

    @Override
    public String getLabel() {
        return desc;
    }

    @Override
    public String getKey() {
        return key;
    }

    public boolean compare(int key) {
        return this.key.equals(key);
    }
}
