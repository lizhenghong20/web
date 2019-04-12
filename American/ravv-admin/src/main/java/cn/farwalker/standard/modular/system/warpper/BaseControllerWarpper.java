package cn.farwalker.standard.modular.system.warpper;

/**
 * Created by asus on 2019/3/26.
 */
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public abstract class BaseControllerWarpper {
    public Object obj = null;

    public BaseControllerWarpper(Object obj) {
        this.obj = obj;
    }

    public Object warp() {
        if(!(this.obj instanceof List)) {
            if(this.obj instanceof Map) {
                Map map2 = (Map)this.obj;
                this.warpTheMap(map2);
                return map2;
            } else {
                return this.obj;
            }
        } else {
            List map = (List)this.obj;
            Iterator var3 = map.iterator();

            while(var3.hasNext()) {
                Map map1 = (Map)var3.next();
                this.warpTheMap(map1);
            }

            return map;
        }
    }

    protected abstract void warpTheMap(Map<String, Object> var1);
}

