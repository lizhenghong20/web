package cn.farwalker.standard.core.temp;

import cn.farwalker.standard.common.constant.dictmap.SystemDict;
import cn.farwalker.waka.core.AbstractDictMap;

import java.lang.annotation.*;

/**
 * Created by asus on 2019/3/28.
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface BussinessLog {
    String value() default "";

    String key() default "id";

    Class<? extends AbstractDictMap> dict() default SystemDict.class;
}
