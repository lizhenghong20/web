package cn.farwalker.ravv.common.constants;

import cn.farwalker.waka.orm.EnumManager;

public enum InventoryTypeEnum implements EnumManager.IEnumJsons {

    INSALE("INSALE","有库存"),
    SALEOUT("SALEOUT","无库存"),
    OFFLINE("OFFLINE","下架")
    ;
    private String key;
    private String desc;

    InventoryTypeEnum(String key, String desc){
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
