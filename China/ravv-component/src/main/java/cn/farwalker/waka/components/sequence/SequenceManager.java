package cn.farwalker.waka.components.sequence;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import cn.farwalker.waka.components.sequence.iface.ISequenceDao;
import cn.farwalker.waka.core.WakaException;
import cn.farwalker.waka.util.Tools;

public class SequenceManager   {
    /** 不使用使用缓存，因为如果切换成redis就会有冲突*/
    private static Map<String,SequenceManager> smap ;
    /** 在spring-context中的配置的bean名称*/
    private final static Lock mangerlock = new ReentrantLock();

    public static SequenceManager getInstance(String name) {
        if (name == null || name.length() == 0) {
            throw new WakaException("序列名不能为空");
        }
        if (smap == null) { 
            smap = newMap(); 
        }
        ////////////////////////////////////////////////

        SequenceManager rs = smap.get(name);
        if (rs == null) {
            rs = newSequenceManager(name);
        }
        return rs;
    }
    /** 加锁的方法*/
    private static Map<String,SequenceManager> newMap(){
        mangerlock.lock();
        try{
            if(smap == null){
                smap = new HashMap<String, SequenceManager>();
            }
            return smap;
        }
        finally{
            mangerlock.unlock();
        }
    }
    /** 加锁的方法*/
    private static SequenceManager newSequenceManager(String name){
        mangerlock.lock();
        try{
            SequenceManager rs = smap.get(name);
            if(rs == null){
                rs = new SequenceManager(name);
                smap.put(name, rs);
            }
            return rs;
        }
        finally{
            mangerlock.unlock();
        }
    }
    
    private volatile SequenceRange currentRange;  
    private final String name;
    /** 只对本表名实例进行加锁*/
    private final Lock lock;
 
 
    private SequenceManager(String name){
        lock = new ReentrantLock();
        this.name = name;
    }
    
    /**
     * 返回 模块名对应序列下一个值 (调用方需把dao传入)
     * @return 
     */
    public long nextValue(){
        if(name == null || name.length()==0){
            throw new WakaException("DefaultSequence对象应该由DefaultSequence.getInstance()取得对象");
        }
        if (currentRange == null) {
            currentRange = getSequenceRange(name,step);
        }

        long value = currentRange.getAndIncrement();
        if (value == -1) {
            value = getSequenceRangeOver(name,step,value);
        }

        if (value < 0) {
            throw new WakaException("Sequence value overflow, value = " + value);
        }

        return value;
    } 
    /** 加锁的方法*/
    private SequenceRange getSequenceRange(String name,int step){
        ISequenceDao dao = null;//
        lock.lock();
        try { 
            if (currentRange == null) { 
                if(dao ==null){
                    dao = Tools.springContext.getBean(ISequenceDao.class);
                	//dao = (ISequenceDao)Tools.web.getBean(ISequenceDao.class);
                }
                currentRange = dao.nextRange(name,step);
            } 
            return currentRange;
        } finally {
            lock.unlock();
        }
    }
    /** 加锁的方法*/
    private long getSequenceRangeOver(String name ,int step,long value){
        ISequenceDao dao = null;//
        lock.lock();
        try {
            while (value == -1) {
                if (currentRange.isOver()) {
                    if(dao ==null){
                        //dao = (ISequenceDao)Tools.web.getBean(BEANNAME_DAO);
                        //dao = SpringContextHolder.getBean(BEANNAME_DAO);
                    	dao = Tools.springContext.getBean(ISequenceDao.class);
                    }
                    currentRange = dao.nextRange(name,step);
                }

                value = currentRange.getAndIncrement(); 
            }
            return value;
        } finally {
            lock.unlock();
        }
    }

    /** 设置每个模块的步长*/
    private int step = ISequenceDao.DEFAULT_STEP;
    /** 设置每个模块的步长*/
    public int getStep() {
        return step;
    }
    /** 设置每个模块的步长*/
    public void setStep(int step) {
        this.step = step;
    }

	public String getName() {
		return name;
	}
	public static void refresh() {
	    if(smap!=null){
	    	smap.clear(); 
	    }
	}
 
} 
