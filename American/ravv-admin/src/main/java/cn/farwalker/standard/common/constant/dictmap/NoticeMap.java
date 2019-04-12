package cn.farwalker.standard.common.constant.dictmap;


import cn.farwalker.waka.core.AbstractDictMap;

/**
 * 通知的映射
 *
 * @author Jason Chen
 * @date 2017-11-06 15:01
 */
public class NoticeMap extends AbstractDictMap {

    @Override
    public void init() {
        put("title", "标题");
        put("content", "内容");
    }

    @Override
    protected void initBeWrapped() {
    }
}
