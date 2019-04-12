package cn.farwalker.standard.core.util;

import cn.farwalker.standard.common.constant.dictmap.factory.DictFieldWarpperFactory;
import cn.farwalker.waka.core.AbstractDictMap;
import cn.farwalker.waka.core.StrKit;
import cn.farwalker.waka.util.DateUtil;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;

/**
 * 对比两个对象的变化的工具类
 *
 * @author Jason Chen
 * @Date 2017/3/31 10:36
 */
public class Contrast {

    //记录每个修改字段的分隔符
    public static final String separator = ";;;";

}