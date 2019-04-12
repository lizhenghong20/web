package cn.farwalker.waka.util;

import java.net.InetAddress;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.farwalker.waka.util.DateUtil.FORMAT;

/**
 * 时间作为key的处理
 * @author Administrator
 *
 */
public class TimerKeyId {
	private static final Logger log = LoggerFactory.getLogger(TimerKeyId.class);
	public static final TimerKeyId  util = new TimerKeyId();
	/**不相同的时分秒，就重新计算*/
	private String dateKey;
	private final AtomicInteger value;
	private final String lastIp ;
	
	private TimerKeyId(){
		value = new AtomicInteger(); 
		lastIp =  getAddressIp();
	}
	/**不相同的时分秒，就重新计算*/
	private int getIncrement(String timeKey){
		if(!timeKey.equals(dateKey)){
			dateKey = timeKey;
			value.set(0);
		}
		int rs = value.getAndIncrement();
		return rs;
	}
	private static final String HEX="0123456789ABCDEF";
	/** 取ip末位(2位16进制)*/
	private String getAddressIp() {
		try {
			InetAddress address = InetAddress.getLocalHost();// 获取的是本地的IP地址
																// //PC-20140317PXKX/192.168.0.121
			String hostAddress = address.getHostAddress();// 192.168.0.121
			// InetAddress address1 =
			// InetAddress.getByName("www.wodexiangce.cn");//获取的是该网站的ip地址，比如我们所有的请求都通过nginx的，所以这里获取到的其实是nginx服务器的IP地
			// String hostAddress1 =
			// address1.getHostAddress());//124.237.121.122
			// InetAddress[] addresses =
			// InetAddress.getAllByName("www.baidu.com");//根据主机名返回其可能的所有InetAddress对象
			// for(InetAddress addr:addresses){
			// System.out.println(addr);//www.baidu.com/14.215.177.38
			// }
			int idx = hostAddress.lastIndexOf('.');
			String ips = hostAddress.substring(idx+1);
			int ip = Integer.parseInt(ips);
			int f1 = ip / 16, f2 = ip - f1 *16;
			char[] c = { HEX.charAt(f1),HEX.charAt(f2)};
			String rs =new String(c);
			return rs;
		}
		catch (Exception e) {
			throw new YMException("取ip出错:",e);
		}
	}
    /**
     * 以时间作为id + 8位流水号(已增加了ip，支持分布式服务)
     * 总长度14+8=22
     */
    public String getTimeId() {
    	Calendar c = Calendar.getInstance() ;
    	StringBuilder rs = Tools.date.formatDate(c,FORMAT.YYYYMMDDHHMMSS);
    	appendRound(rs,rs.toString());
    	return rs.toString();
    }
    /**添加ip及流水号*/
    private void appendRound(StringBuilder rs,String datetime){
    	int rnd = getIncrement(datetime);
    	String rnds = Tools.number.formatNumber(rnd,4);
    	rs.append('X').append(lastIp).append(rnds);
    }
    
    /**
     * 以时间YYYYMMDDHHMMSS作为id + 3位ip + 4流水号(已增加了ip，支持分布式服务)<br/>
     * 时间压缩(年份:2位、月份:1位、天:1位、小时:1位)，IP是16进制加1位分隔符 <br/>
     * 总长度2+1+1+1+2+2+3+4=16
     */
    public String getTimeShortId() {
    	Calendar c = Calendar.getInstance() ;
    	String y = String.valueOf(c.get(Calendar.YEAR)).substring(2);
        int m = c.get(Calendar.MONTH) + 1;
        int d = c.get(Calendar.DATE);
        int h = c.get(Calendar.HOUR_OF_DAY);
        char mc = covertChar(m),dc = covertChar(d), hc =covertChar(h);
        
        String mi = Tools.number.formatNumber(c.get(Calendar.MINUTE), 2);
        String s = Tools.number.formatNumber(c.get(Calendar.SECOND), 2);
        
        StringBuilder rs =new StringBuilder();
        rs.append(y).append(mc).append(dc).append(hc).append(mi).append(s);
        
        String dates = Tools.date.formatDate(c,FORMAT.YYYYMMDDHHMMSS).toString();
        appendRound(rs,dates);
    	return rs.toString();
    }
    
    private char covertChar(int n){
    	char rs ;
    	if(n<10){
    		rs = (char)('0' + n);
    	}
    	else{
    		rs = (char)('A' + n-10);
    	}
    	return rs;
    }
    private int covertInt(char n){
    	int rs ;
    	if(n>='0' && n<='9'){
    		rs = (int)(n - '0');
    	}
    	else if(n>='A' && n<='Z'){
    		rs = (int)('A' - n + 10);
    	}
    	else{
    		throw new YMException(n + "-不能正确还原数值");
    	}
    	return rs;
    }
    /**
     * 对 {@link #getTimeShortId()} 解压
     * */
    public Calendar unTimerShort(String timeId){
    	if(timeId == null || timeId.length()!=17 || timeId.charAt(10)!='X'){
    		log.error(timeId + "格式不匹配");
    		return null;
    	}
    	String y = "20" + timeId.substring(0,2);
    	char mc = timeId.charAt(2);
    	char dc = timeId.charAt(3);
    	char hc = timeId.charAt(4);
    	String mi = timeId.substring(4,4+2);
    	String s = timeId.substring(6,6+2);
    	
    	int year = Integer.parseInt(y);
    	int m = covertInt(mc);
    	int d = covertInt(dc);
    	int h = covertInt(hc);
    	int mi2 = Integer.parseInt(mi);
    	int s2 = Integer.parseInt(s);
    	
    	Calendar c = Calendar.getInstance();
    	c.set(Calendar.YEAR, year);
    	c.set(Calendar.MONTH, m);
    	c.set(Calendar.DATE, d);
    	c.set(Calendar.HOUR_OF_DAY, h);
    	c.set(Calendar.MINUTE, mi2);
    	c.set(Calendar.SECOND, s2);
    	return c;
    }
}
