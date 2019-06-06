package cn.farwalker.ravv.common.constants;

/**
 * Created by asus on 2019/6/6.
 */
public enum ModelItem {

    BEFORE_THEY_GO("22","Before They Go"),
    NEW_ARRIVALS("33","New Arrivals"),
    BEST_SELLERS("44","Best sellers"),
    LIVE_STREAMING("55","Live Streaming")
    ;
    private  String code;
    private  String name;

    ModelItem(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }
    public String getName() {
        return name;
    }

    public boolean compare(String code) {
        return this.code.equals(code);
    }

    public static ModelItem getEnumByCode(String code) {
        ModelItem[] all = ModelItem.values();
        for(ModelItem a : all) {
            if(a.getCode().equals(code)) {
                return a;
            }
        }
        return null;
    }
}
