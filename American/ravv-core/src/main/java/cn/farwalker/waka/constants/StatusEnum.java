package cn.farwalker.waka.constants;

/**
 * Created by asus on 2019/3/26.
 */

import cn.farwalker.waka.orm.EnumManager;

/**
 * 状态枚举
 * 如果就2个值，建议使用boolean类型
 * @author jiqj
 */
public enum StatusEnum implements EnumManager.IEnumJsons {
    DISABLE("disable","禁用"),
    ENABLE("enable","启用");

    private final String key;
    private final String des;

    StatusEnum(String key, String des) {
        this.key = key;
        this.des = des;
    }

    @Override
    public String getKey() {
        return key;
    }
    @Override
    public String getLabel() {
        return des;
    }
}
