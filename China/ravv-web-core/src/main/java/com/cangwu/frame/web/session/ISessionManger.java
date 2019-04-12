package com.cangwu.frame.web.session;

import java.io.Serializable;
import java.util.Enumeration;

/**
 * session用户管理
 * @author juno
 *  需要在线程开始时创建session对象<br/>
 *  {@link SessionShareStore#(javax.servlet.http.HttpServletRequest, boolean)}
 *  线程结束时提交修改<br/>
 *  {@link ISessionManger#commit()} {@link SessionShareStore#commit()}
 */
public interface ISessionManger extends Serializable{ 
	public void setAttribute(String name, Object value);
 
	public void removeAttribute(String name);
 
	public Object getAttribute(String name) ;
 
	public Enumeration<String> getAttributeNames();
 
	/** 
	 * 请求结束时提交session的内容
	 * @return 返回是否有提交(没有提交表示session的内容没有变化)
	 */
	public boolean commit() ;
	
	public String getId();
	/**最后访问时间*/
	public long getLastAccessedTime() ;
	
	 

}
