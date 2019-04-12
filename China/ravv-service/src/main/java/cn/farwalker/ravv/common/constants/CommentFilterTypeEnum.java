package cn.farwalker.ravv.common.constants;


import cn.farwalker.waka.orm.EnumManager;

public enum CommentFilterTypeEnum implements EnumManager.IEnumJsons{

    FIVEPOINT("5","五星"),
    FOURPOINT("4","四星"),
    THREEPOINT("3","三星"),
    TWOPOINT("2","二星"),
    ONEPOINT("1","一星"),
    WITHPIC("WITHPIC","有图"),
    APPEND("APPEND","有追评"),
    ALL("ALL","全部")
            ;

    private String key;
    private String desc;

    CommentFilterTypeEnum(String key, String desc){
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
