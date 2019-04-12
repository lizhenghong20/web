package cn.farwalker.ravv.common.constants;

import cn.farwalker.waka.orm.EnumManager;

/**
 * Created by asus on 2018/12/26.
 */
public enum FlashSaleCategoryStatusEnum implements EnumManager.IEnumJsons {

    NOW("NOW","已开始,且离当前时间最近"),

    UPCOMING("UPCOMING","今天，即将开始"),

    STARTED("STARTED","进行中"),

    TOMORROW("TOMORROW","明天开始"),

    AFTERTOMORROW("AFTERTOMORROW","后天或之后开始的活动");


    private final String key;
    private final String desc;
    FlashSaleCategoryStatusEnum(String key, String desc) {
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
