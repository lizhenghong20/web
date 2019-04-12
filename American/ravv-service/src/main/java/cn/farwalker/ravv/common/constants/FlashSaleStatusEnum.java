package cn.farwalker.ravv.common.constants;

import cn.farwalker.waka.orm.EnumManager;

/**
 * Created by asus on 2018/11/29.
 */
public enum FlashSaleStatusEnum implements EnumManager.IEnumJsons {
		/**创建期*/
        INVALID("INVALID","从创建到冻结"),
        /**冻结期*/
        FROZEN("FROZEN","从开始冻结到冻结结束即活动开始"),
        /**进行中*/
        UNDERWAY("UNDERWAY","活动开始到活动结束"),
        /**结束期*/
        FINISH("FINISH","活动结束以后");

    private final String key;
    private final String desc;
    FlashSaleStatusEnum(String key, String desc) {
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
