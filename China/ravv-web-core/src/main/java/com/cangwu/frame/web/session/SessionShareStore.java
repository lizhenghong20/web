package com.cangwu.frame.web.session;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.farwalker.waka.cache.CacheManager;
import cn.farwalker.waka.util.Tools;
import cn.farwalker.waka.util.YMException;

/**
 * session保存处理(所有需要分布式的session都使用本类处理)
 * 单应用示例也建议使用本类
 * @author juno
 * <br/>
 * 另一种的扩展方式扩展tomcatd服务器, 
 * tomcat提供了org.apache.catalina.session.StandardManager 和org.apache.catalina.session.PersistentManager
 * 用于Session对象的管理，可以自定义PersistentManager的
 * 由于需要配置tomcat，换了jetty就不知怎么处理，所以不采用
 * http://soledede.iteye.com/blog/1938206
 * 
 */
public class SessionShareStore extends LocalSession implements Serializable,ISessionManger{
    private static final Logger log = LoggerFactory.getLogger(SessionShareStore.class);
    private static final long serialVersionUID = 2315657754654995569L; 
    
    protected Map<String,Object> store;
    /** 修改标准*/
    protected boolean change;
    /**
     * 
     * @param s
     * @param requestedSessionId 有些情况request.getRequestedSessionId() != session.getId()
     * 例如ajax的请求：
     * wx.request({
      url: url, 
      header: {
        'Cookie': 'JSESSIONID=' + this._sessionId,
        'content-type': 'application/x-www-form-urlencoded',//兼容get方式      
        "x-requested-with": "XMLHttpRequest" //Ajax 异步请求。
      }, 
     */
    protected SessionShareStore(HttpSession s,String requestedSessionId){
        super(s,requestedSessionId);
        store = this.load(getId());
    }
    
    @Override
    public Object getAttribute(String name) { 
        return store.get(name);
    } 

    @Override
    public Enumeration<String> getAttributeNames() {
        final Iterator<String> n = store.keySet().iterator(); 
        Enumeration<String> rs = new Enumeration<String>() { 
            @Override
            public boolean hasMoreElements() { 
                return n.hasNext();
            }

            @Override
            public String nextElement() { 
                return n.next();
            } 
        };
        return rs;
    }

    /** 对象太大，就不要写session了*/
    @Override
    public void setAttribute(String name, Object value) {
        store.put(name, value);
        this.session.setAttribute(name, value);
        setChangeValue(true);
    }


    @Override
    public void removeAttribute(String name) {
        store.remove(name);
        this.session.removeAttribute(name);
        setChangeValue(true);
    }
    private Map<String,Object> load(String sid){
        //Map<String,Object> share = CacheManager.cache.getObjectShare(sid);
        //redis配置麻烦，所以还是用本地保存
    	log.debug("load:" + sid);
        String json = CacheManager.cache.getShare(sid);
        if(Tools.string.isEmpty(json)){
            return new HashMap<>();
        }
        //Tools.bean.toClassObject(json) ;//
        Map<String,Object> share =Tools.json.toClassObject(json);
        setChangeValue(false);
        return share; 
    }
    private boolean setChangeValue(boolean c){
        change = c;
        return change;
    }
    
    @Override
    /** 请求结束时提交session的内容*/
    public boolean commit(){
        if(!change || this.store.isEmpty()){
            return false;
        }
        // Tools.bean.toJsonClass(this.store);//
        StringBuilder json =  Tools.json.toJsonClass(this.store);
        if(json.length()>4*1024){
            log.info("session的信息大于4kb," + json);
        }
        this.store.clear();//已经没作用了，留着只是为了调试(好像不能清)
        String sid = this.getId();
      //redis配置麻烦，所以还是用本地保存
        CacheManager.cache.putShare(sid, json.toString());
        log.debug("put:" + sid);
        //可以直接使用
        //CacheManager.cache.putObjectShare(sid, this.store);

        setChangeValue(false);
        
        return true;
    }
}
abstract class  LocalSession implements HttpSession{ 
    protected final HttpSession session; 
    protected final String requestedSessionId;
    protected LocalSession(HttpSession s){
        if(s==null){
            throw new YMException("session不能为空");
        }
        session =s;
        requestedSessionId = null;
    }
    /**
     * @param s
     * @param requestedSessionId 有些情况 request.getRequestedSessionId() != session.getId() 
     */
    protected LocalSession(HttpSession s,String requestedSessionId){
        if(s==null){
            throw new YMException("session不能为空");
        }
        session =s;
        this.requestedSessionId = requestedSessionId;
    }
    @Override
    public long getCreationTime() { 
        return session.getCreationTime();
    }

    /** requestedSessionId 有些情况 request.getRequestedSessionId() != session.getId() */
    @Override
    public String getId() {
    	if(requestedSessionId == null || requestedSessionId.trim().length()==0){
    		return session.getId();	
    	}
    	else{
    		return requestedSessionId;
    	}
    }

    @Override
    public long getLastAccessedTime() { 
        return session.getLastAccessedTime();
    }

    @Override
    public ServletContext getServletContext() { 
        return session.getServletContext();
    }

    @Override
    public void setMaxInactiveInterval(int interval) {
        session.setMaxInactiveInterval(interval);
    }

    @Override
    public int getMaxInactiveInterval() { 
        return session.getMaxInactiveInterval();
    }

    @Override
    public HttpSessionContext getSessionContext() { 
        return session.getSessionContext();
    }
    @Override
    public Object getValue(String name) { 
        return session.getValue(name);
    }
    @Override
    public void removeValue(String name) { 
        session.removeValue(name);
    }

    @Override
    public void putValue(String name, Object value) {
        session.putValue(name, value); 
    }

    @Override
    public String[] getValueNames() {
        return session.getValueNames();
    }
    @Override
    public boolean isNew() { 
        return session.isNew();
    }
    @Override
    public void invalidate() { 
        session.invalidate();
    }
}
