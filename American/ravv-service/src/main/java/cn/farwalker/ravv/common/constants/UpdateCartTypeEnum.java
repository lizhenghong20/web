package cn.farwalker.ravv.common.constants;

import cn.farwalker.waka.orm.EnumManager;

/**
 * Created by asus on 2018/12/13.
 */
public enum UpdateCartTypeEnum implements EnumManager.IEnumJsons {
    UPDATEQUANTITY("updatequantity","更新数量"),
    PARSESKU("parsesku","解析sku"),
    DELETEONE("deleteone","删除一个项目"),
    DELETEALL("deleteall","删除所有"),
    DELETESELECTED("deletebatch","删除选中"),
    SELECT("select","选中某一个购物车条目");
    private final String key;
    private final String desc;

    UpdateCartTypeEnum(String key, String desc) {
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
