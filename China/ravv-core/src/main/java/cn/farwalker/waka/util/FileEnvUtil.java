package cn.farwalker.waka.util;

import java.io.File;

/**
 * 文件环境工具类
 *
 * @author Administrator
 */
public class FileEnvUtil {
    public static final String WEBINF = "WEB-INF/";

    /** 因为类必须为public，所以只能把构造函数给这样控制 */
    FileEnvUtil() {

    }

    /** 应用服务器的根目录 */
    private String rootPath;

    /** 是否UI应用，与rootPath配合使用 */
    private boolean isGUI;

    /**
     * 读应用服务器的根目录路径,带盘符的<br>
     * 如是是GUI程序(appliction):目录为类的根目录 如是是Web程序:目录为发布目录（webRoot）,最后一字符是"/"
     *
     */
    public String getRootPath() {
        if (rootPath == null) {
            throw new YMException("请使用主类初始化：getRootPath(boolean,class)根目录");
        }
        return rootPath;
    }

    /**
     * 如果setRootPath不是在WEB-INF/下，可以另外设置一个在WEB-INF/下的类<br/>
     * 主要是兼容在开发模式下没有打包引起的错误
     * @param webInfoClazz
     * @return public String setWebinfoPath(Class<?> webInfoClazz) {
    String rs = this.setRootPath(true, webInfoClazz, true);
    if(rs.indexOf(WEBINF)<=0){
    throw new YMException(webInfoClazz.getName() +"不是在"+WEBINF +"下");
    }
    return rs;
    }
     */
    /**
     * web系统在CWListener implements ServletContextListener初始,swing在application中初始
     */
    public String getRootPath(boolean webPath, Class<?> clz) {
        return this.setRootPath(webPath, clz, false);
    }

    private String setRootPath(boolean webPath, Class<?> clz, boolean force) {
        String showMessage = "根目录已初始化getRootPath(boolean,class)";
        try {
            if (rootPath == null || force) {
                showMessage = "ROOT初始化:" + (force ? "强制第n次" : "第一次");
                String path = getRootPath(clz);
                if (webPath && !isGUI)
                    rootPath = path + WEBINF + "classes/";
                else
                    rootPath = path;
            }
            return rootPath;
        } finally {
            // YMException e = new YMException(showMessage);
            StackTraceElement[] es = Thread.currentThread().getStackTrace();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 3 && i < es.length; i++) {
                sb.append(es[i].getFileName()).append('(').append(es[i].getLineNumber()).append(")\t>").append(es[i].getClassName()).append('.')
                        .append(es[i].getMethodName()).append("()");
                if (i == 0) {
                    sb.append(':').append(showMessage);
                }
                sb.append('\n');
            }
            System.out.println(sb.toString());
        }
    }

    /**
     * 取类路径
     *
     * @return
     */
    private synchronized String getRootPath(Class<?> clz) {
        String p = clz.getProtectionDomain().getCodeSource().getLocation().getFile();
        p = Tools.encode.utf8UrlDecode(p);//还原成汉字
        p = p.replace('\\', '/');
        // p = p.toLowerCase();
        // web应用的class文件方式
        int i = p.toUpperCase().indexOf("/" + WEBINF);
        if (i > 0) {
            isGUI = false;
            rootPath = p.substring(0, i + 1);
            File f = new File(rootPath);
            if (!f.exists())
                f.mkdirs();
        } else {
            // 桌面GUI方式
            isGUI = true;
            i = p.lastIndexOf("/");
            if (i > 0 && i + 1 < p.length())
                rootPath = p.substring(0, i + 1);
            else
                rootPath = p;
        }
        /*
		 * System.out.println("=================================");
		 * System.out.println("类路径:" + p); System.out.println("根目录路径:" +
		 * rootPath); System.out.println("isGUI = " + isGUI);
		 * System.out.println("getRootPath(true) = " + getRootPath(true));
		 * System.out.println("=================================");
		 */
        return rootPath;
    }

    /** 是否UI应用，与rootPath配合使用 */
    public boolean isGUI() {
        return isGUI;
    }

}
