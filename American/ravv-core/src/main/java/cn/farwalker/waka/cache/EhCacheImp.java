package cn.farwalker.waka.cache;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.farwalker.waka.cache.CacheFactory.CacheType;
import cn.farwalker.waka.util.YMException;

class EhCacheImp implements CacheManager {
    private static final Logger log = LoggerFactory.getLogger(EhCacheImp.class);
	private final Cache cache;

	private final net.sf.ehcache.CacheManager manager;

	public EhCacheImp() {
		// 使用默认配置文件创建CacheManager
		// CacheManager manager = CacheManager.create();
		// 通过manager可以生成指定名称的Cache对象
		try (InputStream is = CacheManager.class.getResourceAsStream("/ehcache.xml")) {
			manager = net.sf.ehcache.CacheManager.create(is);
			cache = manager.getCache("yxCache");
			if(cache == null){
				throw new YMException("ehcache[yxCache]初始化不成功:null0");
			}
			CacheRefresh.getInstance().register(this);
		} catch (IOException e) {
			throw new YMException(e.getMessage(), e);
		}
	}

	@Override
	public void put(String key, String value) {
	    //put(key, value, false);
	    putObject(key, value); 
	}

	/**
    @Override
    public void put(String key, String value, boolean share) {
        if(share){
            throw new YMException("EhCache不支持共享缓存!");
        }
        putObject(key, value);        
    }*/


	@Override
	public void putShare(String key, String value) {
	    //throw new YMException("EhCacheImp不支持共享方法");
	    log.warn("EhCacheImp不支持共享方法");
	    this.put(key, value);
	}


	@Override
	public String get(String key) {
	    return getObject(key); 
	}

	@Override
	public String getShare(String key) {
		return get(key); 
	}

	@Override
	public void putObject(String key, Serializable value) {
	    if(value == null){
	        cache.remove(key);
	    }
	    else{
    		Element e = new Element(key, value);
    		cache.put(e);
	    }
	}
	
	/**
    @Override
    public void putObject(String key, Serializable value, boolean share) {
        if(share){
            throw new YMException("EhCache不支持共享缓存!");
        }
        else{
            putObject(key, value);
        }
    }*/
	@Override
	public void putObjectShare(String key, Serializable value) {
	    //throw new YMException("EhCacheImp不支持共享方法");
	    log.warn("EhCacheImp不支持共享方法");
	    this.putObject(key, value);
	}

	@Override
	public <T> T getObjectShare(String key) {
		return getObject(key);
	} 

	@SuppressWarnings("unchecked")
    @Override
	public <T> T getObject(String key) {
		Element e = cache.get(key);
		T value = null;
		if (e != null) {
			value = (T)e.getObjectValue();
		}
		return value;
	}

	@Override
	public void close() {
		manager.shutdown();
	}

	@Override
	public CacheType getCacheType() {
		return CacheType.ehcache;
	}

	@Override
	public boolean remove(String key) {
		return cache.remove(key);
	}

	@Override
	public boolean removeShare(String key) {
		return remove(key);
	}

/*	@Override
	public boolean removeAll() {
		cache.removeAll();
		return true;
	}*/

	@Override
	public boolean removeAll(String preKey) {
	    //refresh();//throw new YMException("ehcache key 模糊删除未实现");
	    cache.removeAll();
        return true;
	}

/*	@Override
	public boolean removeShareAll() {
		;//cache.removeAll();
		return true;
	}*/

	@Override
	public boolean removeShareAll(String preKey) {
	     refresh();//throw new YMException("ehcache key 模糊删除未实现");
	     return true;
	}

	@Override
	public void refresh() { 
		this.removeAll(null);
	}



}