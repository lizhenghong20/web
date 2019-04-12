package cn.farwalker.standard.core.temp;

/**
 * Created by asus on 2019/3/28.
 */
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import cn.farwalker.waka.core.StrKit;

import javax.servlet.ServletRequest;
import java.beans.*;
import java.lang.reflect.Method;
import java.util.*;




import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.servlet.ServletRequest;

public class BeanKit {
    public BeanKit() {
    }





    public static PropertyDescriptor[] getPropertyDescriptors(Class<?> clazz) throws IntrospectionException {
        return Introspector.getBeanInfo(clazz).getPropertyDescriptors();
    }


    public static <T> Map<String, Object> beanToMap(T bean) {
        return beanToMap(bean, false);
    }

    public static <T> Map<String, Object> beanToMap(T bean, boolean isToUnderlineCase) {
        if(bean == null) {
            return null;
        } else {
            HashMap map = new HashMap();

            try {
                PropertyDescriptor[] e = getPropertyDescriptors(bean.getClass());
                PropertyDescriptor[] var7 = e;
                int var6 = e.length;

                for(int var5 = 0; var5 < var6; ++var5) {
                    PropertyDescriptor property = var7[var5];
                    String key = property.getName();
                    if(!key.equals("class")) {
                        Method getter = property.getReadMethod();
                        Object value = getter.invoke(bean, new Object[0]);
                        if(value != null) {
                            map.put(isToUnderlineCase? StrKit.toUnderlineCase(key):key, value);
                        }
                    }
                }

                return map;
            } catch (Exception var11) {
                throw new RuntimeException(var11);
            }
        }
    }





}

