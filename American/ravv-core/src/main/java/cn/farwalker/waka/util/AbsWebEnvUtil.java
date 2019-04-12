package cn.farwalker.waka.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import cn.farwalker.waka.core.HttpKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * Web环境变量工具 环境变量已注入spring的bean管理器 注意：这个类不是唯一实例的
 * 所以类变量尽量使用静态变量
 */
public abstract class AbsWebEnvUtil {
    /** 实现AbsWebEnvUtil类的包名及类名*/
    public static final String BEAN_NAME="cn.farwalker.waka.util.WebEnvUtil";
    
    public abstract String getRootWebPath();
    /** 取得session中的用户,每隔5分钟会刷新一下在session的在线状态  */
    public abstract Long getOnlineUser();
    
    /** 取得session中的用户,每隔5分钟会刷新一下在session的在线状态
     * deprecated v1.0新版不需要request 参数 
    
    public abstract IOnlineUser getOnlineUser(HttpServletRequest request);
    public abstract void setOnlineUser(IOnlineUser userBo);
    
    /** 设置在线用户 */
    public abstract Long setOnlineUser(String onlineUserId);

    /**
     * 清除在线用户
     * 请使用 VmUtil.clearCoolieUser(rundata );
     */
    public abstract void clearUser(HttpServletRequest request);

    public abstract <T> T getBean(Class<T> clazz);

    public abstract Object getBean(String beanName);

    public abstract boolean isMoblie(HttpServletRequest request);

    /**
     * ServletContext 上下文(没有后缀)
     */
    public abstract String getContextPath();

    /**
     * ServletContext 上下文 + 访问路径
     */
    public abstract String getContextPath(String access);

    /**
     * 包含http://的完整路径,已处理nginx代理的情况
     * 有"/"后缀
     */
    public abstract String getContextPathFull();

    /**
     * 设置路径,已处理nginx代理的情况
     * 后缀是"/"
     *
     * @param request return http://www.xiangin.cn;
     */
    public abstract String setContextPathFull(HttpServletRequest request);

    /**
     * 取得远程的id，已处理nginx代理的情况
     */
    public abstract String getRemoteIp(final HttpServletRequest request);
}
class DefaultWebEnvUtil extends AbsWebEnvUtil{
	private static final Logger log = LoggerFactory.getLogger(DefaultWebEnvUtil.class);
	@Override
	public String getRootWebPath() {
		//throw new YMException("未实现"  + AbsWebEnvUtil.BEAN_NAME +"的方法");
		log.info("未实现"  + AbsWebEnvUtil.BEAN_NAME +"的方法");;
		return "";
	}
	private static final ThreadLocal<Long> pOnlineId = new ThreadLocal<>();
	private static final String D_OnlineUserId="online_userid";
	
	/**这里只是示例，可以注释*/
	@Override
	public Long getOnlineUser() {
		Long userId = pOnlineId.get();
		if(userId == null){
			HttpSession sin =HttpKit.getRequest().getSession();
			Object s = (sin == null ? "" :sin.getAttribute(D_OnlineUserId));
			this.setOnlineUser(s.toString());
			userId = pOnlineId.get();
		}
		return userId;
	}
	/**这里只是示例，可以注释*/
	@Override
	public Long setOnlineUser(String userId){
		Long onlineUserId = null;
		if(Tools.number.isInteger(userId)){
			onlineUserId = Long.valueOf(userId);
		}
		
		HttpSession sin = HttpKit.getRequest().getSession();
		if(onlineUserId==null){
			pOnlineId.remove();
			if(sin!=null){
				sin.removeAttribute(D_OnlineUserId);
			}
		}
		else{
			pOnlineId.set(onlineUserId);
			if(sin!=null){
				sin.setAttribute(D_OnlineUserId,onlineUserId.toString());
			}
		}
		
		return onlineUserId;
	}
	
	/*
	@Override
	public IOnlineUser getOnlineUser(HttpServletRequest request) {
		throw new YMException("未实现"  + AbsWebEnvUtil.BEAN_NAME +"的方法");
	}
	@Override
	public void setOnlineUser(IOnlineUser userBo) { 
		setOnlineUser(null,userBo);
	}
	@Override
	public void setOnlineUser(HttpServletRequest request, IOnlineUser userBo) {
		throw new YMException("未实现"  + AbsWebEnvUtil.BEAN_NAME +"的方法"); 
	}
	*/
	@Override
	public void clearUser(HttpServletRequest request) {
		throw new YMException("未实现"  + AbsWebEnvUtil.BEAN_NAME +"的方法"); 
	}

	@Override
	public <T> T getBean(Class<T> clazz) {
		throw new YMException("未实现"  + AbsWebEnvUtil.BEAN_NAME +"的方法");
	}

	@Override
	public Object getBean(String beanName) {
		throw new YMException("未实现"  + AbsWebEnvUtil.BEAN_NAME +"的方法");
	}

	@Override
	public boolean isMoblie(HttpServletRequest request) {
		throw new YMException("未实现"  + AbsWebEnvUtil.BEAN_NAME +"的方法");
	}

	@Override
	public String getContextPath() {
		throw new YMException("未实现"  + AbsWebEnvUtil.BEAN_NAME +"的方法");
	}

	@Override
	public String getContextPath(String access) {
		throw new YMException("未实现"  + AbsWebEnvUtil.BEAN_NAME +"的方法");
	}

	@Override
	public String getContextPathFull() {
		throw new YMException("未实现"  + AbsWebEnvUtil.BEAN_NAME +"的方法");
	}

	@Override
	public String setContextPathFull(HttpServletRequest request) {
		throw new YMException("未实现"  + AbsWebEnvUtil.BEAN_NAME +"的方法");
	}

	@Override
	public String getRemoteIp(HttpServletRequest request) {
		throw new YMException("未实现"  + AbsWebEnvUtil.BEAN_NAME +"的方法");
	}



	
}