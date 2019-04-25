package cn.farwalker.ravv.service.order.constants;

import cn.farwalker.waka.orm.EnumManager;

public enum WechatPayEnum  implements EnumManager.IEnumJsons {

    NATIVE("Native","网页"),
    APP("App","app")
    ;
    private final String key,label;
    WechatPayEnum(String key, String label) {
        this.key = key;
        this.label = label;
    }
    @Override
    public String getKey() {
        return key;
    }
    @Override
    public String getLabel() {
        return label;
    }
}
