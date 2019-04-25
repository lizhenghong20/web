package cn.farwalker.ravv.common.constants;

import cn.farwalker.waka.orm.EnumManager;

public enum WechatLoginTypeEnum implements EnumManager.IEnumJsons  {

    WEB("Web","网页应用"),

    APP("App","移动应用"),

    ;
    private final String key;
    private final String desc;
    WechatLoginTypeEnum(String key, String desc) {
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
