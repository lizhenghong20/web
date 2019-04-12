package cn.farwalker.standard.common.constant.dictmap;

import cn.farwalker.waka.core.AbstractDictMap;

/**
 * 日志的字典
 *
 * @author Jason Chen
 * @date 2017-11-06 15:01
 */
public class LogDict extends AbstractDictMap {

    @Override
    public void init() {
        put("tips","备注");
    }

    @Override
    protected void initBeWrapped() {

    }
}
