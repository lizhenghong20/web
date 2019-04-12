package cn.farwalker.waka.util;

import java.lang.reflect.Method;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 总的工具类
 *
 * @author Administrator
 */
public final class Tools {

    private static final Logger LOG = LoggerFactory.getLogger(Tools.class);
    static {
        // 尝试初始化web util, 这个一定要放在最前面, 因为后面ConfigUtil 里面用到了这个
        // 这个类放在ymf-wb 进行定义, 如果没有引用这个模块, 则不会有这个类, web 就是null 的
        // 如果需要这个工具, 则需要引用这个模块
        web = initUtil(AbsWebEnvUtil.BEAN_NAME);
    }

    /** 集合工具类 */
    public final static CollectionUtil collection = new CollectionUtil();

    /** IO工具类 */
    public final static IOUtil io = new IOUtil();

    /** json工具类 */
    public final static JsonUtil json = JsonUtil.util;

    /** 数字型工具类 */
    public final static NumberUtil number = new NumberUtil();

    /** 操作系统工具类 */
    //public final static OSUtil os = new OSUtil();

    /** 字符串工具类 */
    public final static StringUtil string = new StringUtil();

    /** 线程工具类 */
    public final static ThreadUtil thread = ThreadUtil.util;

    /** 获取上下文引用工具类 */
    public final static SpringContextUtil springContext = new SpringContextUtil();

    /** 二维码工具类 */
    public final static QRCodeUtil qrCode = new QRCodeUtil();

    // ===========上面是不依赖别人的工具类，下面是一级依赖的工具类=============

    /** Map工具类 */
    //public final static MapUtil map = new MapUtil();

    /** MD5工具类 */
    public final static MD5Util md5 = new MD5Util();

    /** 编码工具类 */
    public final static EncodeUtil encode = new EncodeUtil();

    /** 文件环境工具类 */
    public final static FileEnvUtil env = new FileEnvUtil();

    /** Web环境工具类 */
    public final static AbsWebEnvUtil web;

    /** 文件工具类 */
    //public final static FileUtil file = new FileUtil();

    /** 图片工具类 */
    public final static ImageUtil image = ImageUtil.util;

    /** 日期工具类 */
    public final static DateUtil date = new DateUtil();

    /** 对象拷贝工具类 */
    public final static BeanUtil bean = BeanUtil.util;

    /** BigDecimal工具类 */
    public final static BigDecimalUtil bigDecimal = BigDecimalUtil.util;

    /** 加密盐工具类 */
    public final static SaltUtil salt = SaltUtil.util;

    /** 获取IP工具类 */
    public final static ClientIPUtil clientIP = ClientIPUtil.util;

    /** 验证码有效时间工具类 */
    public final static  TimeValueUtil timeValue = TimeValueUtil.util;

    /** 拼音工具类 */
    //public final static PinyinUtil pinyin = new PinyinUtil();

    /** HTTP 请求工具类 */
    //public final static HttpUtil http = new HttpUtil();

    /** SHA1 工具类 */
    //public final static SHA1Util sha1 = new SHA1Util();

    /** RSA 工具类 */
    //public final static RSAUtil rsa = new RSAUtil();

    /** GEO 工具类 */
    //public final static GeoUtil geo = new GeoUtil();
    public final static TimerKeyId timerKey = TimerKeyId.util;
    // ===========下面是二级依赖的工具类=============

    /** 管理config.properties文件工具类 */
    //public final static ConfigUtil config = new ConfigUtil();

    // ========================

    private static int uuidCount = 99;

    /**
     * 生成新的UUID
     */
    public static String uuid() {
        uuidCount++;
        if (uuidCount > 999) {
            uuidCount = 100;
        }
        String rs = String.valueOf(uuidCount) + UUID.randomUUID().toString();
        return rs.replace('-', 'M');
    }

    /**
     * 尝试初始化指定的类:判断如果类有定义静态的getInstance()，没有才new对象
     *
     * @param className 类名
     * @param <T>       实例的类型
     * @return 指定类的对象, 如果没有这个类的定义, 则是null
     */
    @SuppressWarnings("unchecked")
    private static <T> T initUtil(final String className) {
        final Class<?>  aClass = isPresent(className);//
        if (aClass == null) {
            return null;// 如果没有这个类的定义, 或者上面的初始化出错了.
        }
        
        try {
            // 如果有定义getInstance() 方法, 则尝试使用这个方法进行类的实例化
            final Method factory = aClass.getMethod("getInstance");
            T rs;
            if(factory == null){// 如果没有, 则直接调用默认的构造函数 
                rs = (T) aClass.newInstance(); 
            }else{ 
                rs = (T) factory.invoke(null);
            }
            return rs;

        } catch (Exception e) {
            LOG.error("尝试初始化指定的类:判断如果类有定义静态的getInstance()，没有才new对象",e );
            return null;
        }   
    }

    /**
     * 检查类是不是存在
     *
     * @param className 类名, 需要带有包的全路径
     */
    private static Class<?> isPresent(final String className) {
        try {
            Class<?> rs =  Class.forName(className);
            return rs;
        } catch (final Throwable ex) {
            LOG.error("检查类是不是存在:" + className,ex);
            return null;
        }
    }

}
