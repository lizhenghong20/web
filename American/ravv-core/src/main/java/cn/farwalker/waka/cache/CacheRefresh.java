package cn.farwalker.waka.cache;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.farwalker.waka.util.Tools;

/**
 * 缓存刷管理器<br/>
 * 业务层实现ICacheRegister,并且注册到CacheRefresh中，则可以进行统一刷处理
 *
 * @author juno
 */
public class CacheRefresh {
    /**默认的延迟时间*/
    public static final int DelayMinute=5;
    public interface ICacheRegister {
        public void refresh();
    }
    /** 优先级*/
    public enum Priority{
        /** 最先，但前面已经有最先时，就跟在后面*/
        FIRST,
        /** 在某类的前面 */
        BEFORE,
        /** 在某类的后面 */
        AFTER,
        /** 最后 */
        LAST;
    }
    private static final Logger log = LoggerFactory.getLogger(CacheRefresh.class);
    private static CacheRefresh cf;

    public static CacheRefresh getInstance() {
        if (cf == null) {
            cf = new CacheRefresh();
        }
        return cf;
    }


    private final List<ICacheRegister> caches;

    private CacheRefresh() {
        caches = new ArrayList<>();
        delayRefresh = new DelayRefresh(this);
    }

    public ICacheRegister register(ICacheRegister r) {
        if (!caches.contains(r)) {
            caches.add(r);
        }
        return r;
    }

    public boolean remove(ICacheRegister r) {
        return caches.remove(r);
    }

    /** 统一刷处理 */
    public void refresh() {
        delayRefresh.cancel();//取消了...
        this.refreshInside();
    }
    
    /**内部使用的刷新*/
    protected void refreshInside() {
        for (ICacheRegister r : caches) {
            try {
                r.refresh();
            } catch (Throwable e) {
                log.error("刷新缓存", e);
            }
        } 
    }
    /**延迟刷新时间(0不延迟)*/
    private final DelayRefresh delayRefresh;
    /**
     * 统一刷处理 延迟n分钟才刷新，如果上传为执行刷新，又接到新指令，就按新指令的时间再延伸，避免频繁刷新 
     * @param delayMinute
     */
    public void refresh(int delayMinute) {
        if(delayMinute <=0){
            this.refresh();
        }
        else{
            delayRefresh.delayStart(delayMinute);
        }
    }
}

class DelayRefresh implements Runnable{
    private final CacheRefresh owner ;
    /**0是没有任务状态*/
    private int delayMinute;
    private boolean cancel = false;
    protected DelayRefresh(CacheRefresh owner){
        this.owner = owner;
        delayMinute = 0;
    }
    public void delayStart(int minute){
        if(delayMinute<0 ){
            return ;//指定的时间比第一次的更短( this.delayMinute > delayMinute)
        }
        cancel = false;
        if(this.delayMinute <= 0){//建立新任务
            Tools.thread.runSingle(this, "cacheRefresh", 1);//一分钟心跳一次    
        }        
        this.delayMinute = minute ;//更新延迟时间
    }
    /**取消任务*/
    protected void cancel(){
        cancel = true;
    }
    @Override
    public void run(){
        if(cancel){
            this.delayMinute =0;
            cancel = false;//结束了
        }
        else if(this.delayMinute>0){//循环心跳
            this.delayMinute --;
            Tools.thread.runSingle(this, "cacheRefresh", 1);    
        }
        else{
            owner.refreshInside();
        }
    }
}
