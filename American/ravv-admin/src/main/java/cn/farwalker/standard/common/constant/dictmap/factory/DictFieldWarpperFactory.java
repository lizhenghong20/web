package cn.farwalker.standard.common.constant.dictmap.factory;

import cn.farwalker.standard.common.constant.factory.ConstantFactory;
import cn.farwalker.standard.common.constant.factory.IConstantFactory;
import cn.farwalker.waka.core.BizExceptionEnum;
import cn.farwalker.waka.core.WakaException;

import java.lang.reflect.Method;

/**
 * 字段的包装创建工厂
 *
 * @author Jason Chen
 * @date 2017-11-06 15:12
 */
public class DictFieldWarpperFactory {

    public static Object createFieldWarpper(Object field, String methodName) {
        IConstantFactory me = ConstantFactory.me();
        try {
            Method method = IConstantFactory.class.getMethod(methodName, field.getClass());
            Object result = method.invoke(me, field);
            return result;
        } catch (Exception e) {
            try {
                Method method = IConstantFactory.class.getMethod(methodName, Integer.class);
                Object result = method.invoke(me, Integer.parseInt(field.toString()));
                return result;
            } catch (Exception e1) {
                throw new WakaException(BizExceptionEnum.ERROR_WRAPPER_FIELD);
            }
        }
    }

}
