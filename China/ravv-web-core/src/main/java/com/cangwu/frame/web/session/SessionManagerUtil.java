//package com.cangwu.frame.web.session;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpSession;
//
//import cn.farwalker.waka.util.Tools;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.cangwu.frame.web.IOnlineUser;
//import cn.farwalker.waka.util.YMException;
//
//public class SessionManagerUtil{
//    private static final Logger log = LoggerFactory.getLogger(SessionManagerUtil.class);
//    //使用ThreadLocal有时会产生2个相同sessionid的对象
//    private static final ThreadLocal<ISessionManger>  threadLocal=new ThreadLocal<>();
//    //private static final ThreadLocal<String>  threadLocalSessionId=new ThreadLocal<>();
//    //private static final String K_WeixinSession="weixin_session";
//
//    private SessionManagerUtil(){
//
//    }
//	/** 创建线程session对象
//	 * 每次请求需带sessionId，就可以通用了
//	 * https://www.cnblogs.com/zengxiaoliang/p/6945055.html
//	 */
//	public static ISessionManger createSession(HttpServletRequest request,String token){
//		//String tokenHeader = HttpKit.getRequest().getHeader(JwtTokenHader);//jwtProperties.getHeader());
//        if(Tools.string.isEmpty(token) ) {
//        	//throw new WakaException(BizExceptionEnum.TOKEN_ERROR);
//        	log.debug("未创建token");
//        	return null;
//        }
//
//		HttpSession session = request.getSession();
//	    SessionShareStore mysin = (SessionShareStore)threadLocal.get();
//	    String requestSessionId = token;//request.getRequestedSessionId();
//
//	    /**
//	    String requestSessionId = session.getId();
//	    if(mysin == null  || mysin.store.isEmpty() || !(requestSessionId.equals(mysin.getId()))){
//	    	//使用ThreadLocal有时会产生2个相同sessionid的对象
//            if(mysin!=null){//不同的sessionId就要清除
//                log.debug("sin.id=" + mysin.getId() + ",request.id=" + requestSessionId);
//                mysin.store.clear();//旧的清除掉
//            }
//            mysin = new SessionShareStore(session);
//            //threadLocal.set(mysin);
//        }*/
//	    if(requestSessionId!=null && !requestSessionId.equals(session.getId())){
//	    	log.debug("request.getRequestedSessionId() != session.getId()");
//	    }
//	    mysin = new SessionShareStore(session,requestSessionId);
//	    threadLocal.set(mysin);
//	    //threadLocalSessionId.set(requestSessionId);
//		return mysin;
//	}
//
//
//	/** 取得线程session对象*/
//	public static ISessionManger getSession(){
//		ISessionManger sin  = getSession(false);
//		return sin;
//	}
//	public static ISessionManger getSession(boolean checked){
//		//String sid = threadLocalSessionId.get();
//		ISessionManger sin  = threadLocal.get();
//		if(checked && sin == null){
//			throw new YMException("没有线程session");
//		}
//		return sin;
//	}
//
//	/** 提交session对象*/
//	public static void commit(){
//	    ISessionManger sin = getSession(true);
//		boolean commit = sin.commit();
//		/*if(commit){//有改变就清除线
//
//		}*/
//	}
//
//	public static void setOnlineUser(IOnlineUser u){
//		ISessionManger s = getSession(true);
//		if(u == null){
//			s.removeAttribute(IOnlineUser.KEY_ONLINE);
//		}
//		else{
//			s.setAttribute(IOnlineUser.KEY_ONLINE, u);
//		}
//	}
//	public static IOnlineUser getOnlineUser(){
//		ISessionManger s = getSession(true);
//		IOnlineUser rs = (IOnlineUser)s.getAttribute(IOnlineUser.KEY_ONLINE);
//		return rs;
//	}
//}