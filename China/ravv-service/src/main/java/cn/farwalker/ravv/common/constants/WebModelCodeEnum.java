package cn.farwalker.ravv.common.constants;

import cn.farwalker.waka.orm.EnumManager;

/**
 * Created by asus on 2019/3/4.
 */
public enum WebModelCodeEnum implements EnumManager.IEnumJsons {

    NEW_ARRIVAL("11","新品推荐模块")
    ;
    private String key;
    private String desc;

    WebModelCodeEnum(String key, String desc){
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
