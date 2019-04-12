package cn.farwalker.waka.core;

/**
 * Created by asus on 2019/3/28.
 */
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import java.util.HashMap;

public abstract class AbstractDictMap {
    protected HashMap<String, String> dictory = new HashMap();
    protected HashMap<String, String> fieldWarpperDictory = new HashMap();

    public AbstractDictMap() {
        this.put("id", "主键id");
        this.init();
        this.initBeWrapped();
    }

    public abstract void init();

    protected abstract void initBeWrapped();

    public String get(String key) {
        return (String)this.dictory.get(key);
    }

    public void put(String key, String value) {
        this.dictory.put(key, value);
    }

    public String getFieldWarpperMethodName(String key) {
        return (String)this.fieldWarpperDictory.get(key);
    }

    public void putFieldWrapperMethodName(String key, String methodName) {
        this.fieldWarpperDictory.put(key, methodName);
    }
}

