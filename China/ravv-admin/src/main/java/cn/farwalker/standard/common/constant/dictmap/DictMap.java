package cn.farwalker.standard.common.constant.dictmap;


import cn.farwalker.waka.core.AbstractDictMap;

/**
 * 字典map
 *
 * @author Jason Chen
 * @date 2017-11-06 15:43
 */
public class DictMap extends AbstractDictMap {

    @Override
    public void init() {
        put("dictId","字典名称");
        put("dictName","字典名称");
        put("dictValues","字典内容");
    }

    @Override
    protected void initBeWrapped() {

    }
}
