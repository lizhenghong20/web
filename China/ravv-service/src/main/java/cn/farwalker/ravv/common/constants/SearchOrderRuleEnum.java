package cn.farwalker.ravv.common.constants;

import cn.farwalker.waka.orm.EnumManager;

/**
 * Created by asus on 2019/1/10.
 */
public enum SearchOrderRuleEnum implements EnumManager.IEnumJsons {

    BESTMATCH("bestmatch","最佳匹配"),
    ORDER("order","根据销量，降序"),
    PRICEDESC("pricedesc","根据价格，降序"),
    PRICEASC("priceasc","根据价格，升序"),
    ;
    private final String key;
    private final String desc;

    SearchOrderRuleEnum(String key, String desc) {
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

    public static SearchOrderRuleEnum getEnumByKey(String key) {
        SearchOrderRuleEnum[] all = SearchOrderRuleEnum.values();
                for(SearchOrderRuleEnum a : all) {
                    if(a.getKey().equals(key)) {
                    return a;}
                }
                return null;
    }

}
