package cn.farwalker.standard.core.temp;

/**
 * Created by asus on 2019/3/28.
 */
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import cn.farwalker.waka.util.Tools;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Scope(
        scopeName = "session"
)
public class LogObjectHolder implements Serializable {
    private Object object = null;

    public LogObjectHolder() {
    }

    public void set(Object obj) {
        this.object = obj;
    }

    public Object get() {
        return this.object;
    }

    public static LogObjectHolder me() {
        LogObjectHolder bean = (LogObjectHolder) Tools.springContext.getBean(LogObjectHolder.class);
        return bean;
    }
}

