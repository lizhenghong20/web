package cn.farwalker.ravv.common.constants;

import cn.farwalker.waka.orm.EnumManager;

public enum MessageTypeEnum implements EnumManager.IEnumJsons{

    //一个或多个
    CONSULT("CONSULT","问答"),
    ORDERINFO("ORDERINFO","订单消息"),

    //所有
    PROMOTION("PROMOTION","产品推荐"),
    NOTIFICATION("NOTIFICATION","系统通知"),

    //问答子分类
    QUESTION("QUESTION","推送的问题"),
    ANSWER("ANSWER","推送的回答"),
    ;
    private String key;
    private String desc;

    MessageTypeEnum(String key, String desc){
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
