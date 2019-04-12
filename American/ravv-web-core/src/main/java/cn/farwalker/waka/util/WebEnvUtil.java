package cn.farwalker.waka.util;


import java.io.File;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import cn.farwalker.waka.core.HttpKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.cangwu.frame.web.IOnlineUser;


 

/**
 * Web环境变量工具 环境变量已注入spring的bean管理器 注意：这个类不是唯一实例的
 * 所以类变量尽量使用静态变量
 */
public class WebEnvUtil extends AbsWebEnvUtil {
    private static final Logger          log        = LoggerFactory.getLogger(WebEnvUtil.class);

    /** 应用服务器的根目录(可以通过http访问的根目录) */
    private static String                rootWebPath;

    /** nginx过来的域名,测试环境 访问地址 yomnn.wicp.net:8080,返回是 yomnn.wicp.net<br>
     * 这些key要在nginx.conf中配置*/
    public static final String           NGINX_HOST = "Host";

    /** nginx过来的域名,测试环境 访问地址 yomnn.wicp.net:8080,返回是 yomnn.wicp.net<br>
     * 这些key要在nginx.conf中配置
     */
    public static final String           NGINX_IP   = "X-Real-IP";
 

    /** 保持单例 */
    private static WebEnvUtil            webenv;

    private static int                   count      = 0;

    private static String                contextPath, contextNginxPath;

    public static WebEnvUtil getInstance() {
        if (count == 0) {
            webenv = new WebEnvUtil();
        }
        return webenv;
    }

    /** 不要手工艺new对象,请使用getInstance(),保持单例 */
    public WebEnvUtil() {
        if (webenv == null) {
            webenv = this;
        }
        count++;
    }
 

    /** 应用服务器的根目录(可以通过http访问的根目录)
     * String virtPath = request.getServletPath();//虚拟路径 
       String realPath = request.getRealPath(virtPath);//物理路径
     */
    @Override
    public String getRootWebPath() {
        return rootWebPath;
    }

    /** 应用服务器的根目录(可以通过http访问的根目录) */
    protected void setRootWebPath(String jspRootPath) {
        if (jspRootPath == null || jspRootPath.length() == 0) {
            ;
        }
        else {
            char l = jspRootPath.charAt(jspRootPath.length() - 1);
            if (l != File.separatorChar) {
                jspRootPath = jspRootPath + File.separatorChar;
            }
            rootWebPath = jspRootPath;
        }
    } 
    
    private static final ThreadLocal<Long> pOnlineId = new ThreadLocal<>();
	private static final String D_OnlineUserId="online_userid"; 
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
	
    /**@Override
    public IOnlineUser getOnlineUser() { 
    	return SessionManagerUtil.getOnlineUser();
    }*/
    /** 
     * 取得session中的用户,每隔5分钟会刷新一下在session的在线状态
     * @param request 在新版中可以为空
     * @return

    @Override
    public IOnlineUser getOnlineUser(HttpServletRequest request) { 
    	return SessionManagerUtil.getOnlineUser();
    }     */
    /** 设置在线用户  
    @Override
    public void setOnlineUser(IOnlineUser userBo) { 
    	setOnlineUser(null,userBo);
    }
     设置在线用户
    @Override
    public void setOnlineUser(HttpServletRequest request, IOnlineUser userBo) { 
        try {
        	SessionManagerUtil.setOnlineUser(userBo);
        }
        catch (Exception e) {
            log.error("在线用户保存异常，默认保存到请求的session中");
        }

    } */

    /**
    public void removeOnlineUser(HttpServletRequest request){

    	try{ 
    		HttpSession sin = request.getSession();
    		sin.removeAttribute(IOnlineUser.KEY_ONLINE);
    		 
    	}catch (Exception e){
    		log.error("sessio会话移除失败",e);

    	}
    }
    */
    /** 清除在线用户
     * 请使用 VmUtil.clearCoolieUser(rundata );
     */
    @Override
    public void clearUser(HttpServletRequest request) {
        if (request != null) {
            request.getSession().removeAttribute(IOnlineUser.KEY_ONLINE);
        }
    }

    @Override
    public <T> T getBean(Class<T> clazz) {
        return Tools.springContext.getBean(clazz);
    }

    @Override
    public Object getBean(String beanName) {
        return Tools.springContext.getBean(beanName);
    }

    @Override
    public boolean isMoblie(HttpServletRequest request) {
        String agent = request.getHeader("User-Agent");
        if (Tools.string.isEmpty(agent)) {
            return false;
        }

        String[] mobileAgents = { "iphone", "android", "phone", "mobile", "wap", "netfront", "java", "opera mobi",
                "opera mini", "ucweb", "windows ce", "symbian", "series", "webos", "sony", "blackberry", "dopod",
                "nokia", "samsung", "palmsource", "xda", "pieplus", "meizu", "midp", "cldc", "motorola", "foma",
                "docomo", "up.browser", "up.link", "blazer", "helio", "hosin", "huawei", "novarra", "coolpad", "webos",
                "techfaith", "palmsource", "alcatel", "amoi", "ktouch", "nexian", "ericsson", "philips", "sagem",
                "wellcom", "bunjalloo", "maui", "smartphone", "iemobile", "spice", "bird", "zte-", "longcos",
                "pantech", "gionee", "portalmmm", "jig browser", "hiptop", "benq", "haier", "^lct", "320x320",
                "240x320", "176x220", "w3c ", "acs-", "alav", "alca", "amoi", "audi", "avan", "benq", "bird", "blac",
                "blaz", "brew", "cell", "cldc", "cmd-", "dang", "doco", "eric", "hipt", "inno", "ipaq", "java", "jigs",
                "kddi", "keji", "leno", "lg-c", "lg-d", "lg-g", "lge-", "maui", "maxo", "midp", "mits", "mmef", "mobi",
                "mot-", "moto", "mwbp", "nec-", "newt", "noki", "oper", "palm", "pana", "pant", "phil", "play", "port",
                "prox", "qwap", "sage", "sams", "sany", "sch-", "sec-", "send", "seri", "sgh-", "shar", "sie-", "siem",
                "smal", "smar", "sony", "sph-", "symb", "t-mo", "teli", "tim-", "tosh", "tsm-", "upg1", "upsi", "vk-v",
                "voda", "wap-", "wapa", "wapi", "wapp", "wapr", "webc", "winw", "winw", "xda", "xda-",
                "Googlebot-Mobile" };

        boolean isMobile = false;
        agent = agent.toLowerCase();
        for (String mobileAgent : mobileAgents) {
            if (agent.indexOf(mobileAgent) >= 0) {
                isMobile = true;
                break;
            }
        }
        return isMobile;
    }

    /**
     * ServletContext 上下文(没有后缀)
     * @return
    */
    @Override
    public String getContextPath() {
        String rs = contextPath;
        return rs;
    }
    /**
     * ServletContext 上下文 + 访问路径
     * @return
     */
    @Override
    public String getContextPath(String access) {
        String rs = getContextPath();
        if (Tools.string.isNotEmpty(access)) {
            if (access.charAt(0) != '/') {
                rs = rs + "/";
            }
            rs = rs + access;
        }
        return rs;
    }

    /**
     * 包含http://的完整路径,已处理nginx代理的情况 
     * 有"/"后缀
     * @return
     */
    @Override
    public String getContextPathFull() {
        String rs = contextNginxPath;
        if (checkFullURL(rs)) {
            contextNginxPath = rs;
            return rs;
        }
        else {
            throw new YMException("生成的全路径不完整:" + rs);
        }
    }

    protected void setContextPath(String p) {
        contextPath = p;
    }
 
    /**
     * 设置路径,已处理nginx代理的情况
     * 后缀是"/"
     * @param request
     * return http://www.xiangin.cn;
     */
    @Override
    public String setContextPathFull(HttpServletRequest request) {
        if (checkFullURL(contextNginxPath)) {
            return contextNginxPath;
        }
        if(getRootWebPath()==null){
            ServletContext sc =request.getServletContext() ;
            setContextPath(sc.getContextPath());
            setRootWebPath(sc.getRealPath("/"));// jsp的路径
        }
        /**
         * 在nginx.conf中配置的key:
         * proxy_set_header Host $host:$server_port; # $host转发来源的host 及端口$server_port
         * proxy_set_header X-Real-IP $remote_addr; #tomcat需要从request.getHeader("X-Real-IP") 获取
         */
        String serverNginx = request.getHeader(NGINX_HOST);//yomnn.wicp.net
        //String ip = request.getHeader(NGINX_IP);
        String scheme = request.getScheme();
        StringBuilder url = new StringBuilder("://");

        if (Tools.string.isEmpty(serverNginx)) {
            url.append(request.getServerName());
            int port = request.getServerPort();
            if (port != 80) {
                url.append(':').append(port);
            }
            //url.append('/');
        }
        else if ("juno_server".equalsIgnoreCase(serverNginx)) {
            StringBuffer url2 = request.getRequestURL();
            throw new YMException(url2.toString() + ":nginx对此连接没有注入:" + NGINX_HOST);
        }
        else {
        	int l = serverNginx.length();
        	if(serverNginx.endsWith(":443")){
        		url.append(serverNginx.substring(0,l-4));
        		if(!"https".equals(scheme)){
        			scheme ="https";
        		}
        	}
        	else if(serverNginx.endsWith(":80")){
        		url.append(serverNginx.substring(0,l-3));
        		if(!"http".equals(scheme)){
        			scheme ="http";
        		}
        	}
        	else{
        		url.append(serverNginx);//.append('/');
        	}
        }
        url.insert(0,scheme);

        String contextPath = request.getContextPath();
        if (Tools.string.isNotEmpty(contextPath)) {
            //            if(contextPath.charAt(0)=='/'){
            //                contextPath = contextPath.substring(1);
            //            }
            url.append(contextPath);//.append('/');
        }

        int l = url.length();
        if (l > 0 && url.charAt(l - 1) != '/') {
            url.append('/');
        }

        String rs = url.toString();
        if (checkFullURL(rs)) {
            contextNginxPath = rs;
            return rs;
        }
        else {
            throw new YMException("生成的全路径不完整:" + rs);
        }
    }

    /** 检查路径是否完整,true完整，false不完整*/
    private boolean checkFullURL(String url) {
        if (url == null || url.length() <= "http://".length() + 3) {
            return false;
        }
        else {
            return true;
        }
    }

    /**
     * 取得远程的id，已处理nginx代理的情况
     * @param request
     * @return
     */
    @Override
    public String getRemoteIp(final HttpServletRequest request) {
        final String UNKNOWN = "unknown";
        String ip = request.getHeader(NGINX_IP);
        if (Tools.string.isEmpty(ip)) {
            ip = request.getHeader("x-forwarded-for");
        }
        if (Tools.string.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (Tools.string.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (Tools.string.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        ip = ip.equals("0:0:0:0:0:0:0:1")?"127.0.0.1":ip;
        return ip;
    }
/*
    @Override
    public void refresh() {
        //contextPath="";
        //contextNginxPath=""; //清除这值后，没有地址初始化，是比较麻烦的
    }*/
}
