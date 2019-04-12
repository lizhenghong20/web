package cn.farwalker.ravv.common.constants;

import cn.farwalker.waka.orm.EnumManager;

/**
 * Created by asus on 2019/1/4.
 */
public enum YoutubeVideoTypeEnum implements EnumManager.IEnumJsons {
    COMPLETED("completed","已直播的视频"),
    LIVE("live","正在直播的视频"),
    UPCOMING("upcoming","未来直播的视频");

    private final String key;
    private final String desc;

    YoutubeVideoTypeEnum(String key, String desc) {
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
