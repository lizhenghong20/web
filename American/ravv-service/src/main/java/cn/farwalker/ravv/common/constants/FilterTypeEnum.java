package cn.farwalker.ravv.common.constants;

import cn.farwalker.waka.orm.EnumManager;

public enum FilterTypeEnum implements EnumManager.IEnumJsons{

    MENU("MENU","分类"),
    INVENTORY("INVENTORY","库存")
    ;
    private String key;
    private String desc;
    FilterTypeEnum(String key, String desc){
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
