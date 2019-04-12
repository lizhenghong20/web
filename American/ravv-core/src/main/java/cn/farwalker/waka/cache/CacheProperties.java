package cn.farwalker.waka.cache;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author: EarthChen
 * @date: 2018/03/13
 */
@Component
@ConfigurationProperties(prefix = CacheProperties.CONFIG_PREFIX)
public class CacheProperties {
	public static final String CONFIG_PREFIX = "cache";
    private String branchKey;
    private String shareKey;
    /**
     * 格式 192.168.8.90:7000,192.168.8.90:7001,192.168.8.90:7002,192.168.8.90:7003,192.168.8.90:7004,192.168.8.90:7005
     * 多个表示使用集群 
     */
    private String redisServer;
    private String redisPassword;
    
    /**本地缓存(true)还是网络缓存(false)*/
   /* public boolean isNetCache(){
    	return (redisServer!=null && redisServer.length()>0);
    }
*/
	public boolean isNetCache(){
		return false;
	}
	/**分支标志-(一般是项目名,相同项目共用相同缓存，不同的项目不通用)*/
	public String getBranchKey() {
		return (branchKey == null ? "": branchKey);
	}

    /**共享缓存标记,整个redis共用-不同的项目都可以共用*/
	public String getShareKey() {
		return (shareKey == null ? "": shareKey);
	}
	/**
	 * 格式 192.168.8.90:7000,192.168.8.90:7001,192.168.8.90:7002,192.168.8.90:7003,192.168.8.90:7004,192.168.8.90:7005
     * 多个表示使用集群 
     */
	public String getRedisServer() {
		return redisServer;
	}

	public String getRedisPassword() {
		return redisPassword;
	}

	public void setBranchKey(String branchKey) {
		this.branchKey = branchKey;
	}

	public void setShareKey(String shareKey) {
		this.shareKey = shareKey;
	}

	public void setRedisServer(String redisServer) {
		this.redisServer = redisServer;
	}

	public void setRedisPassword(String redisPassword) {
		this.redisPassword = redisPassword;
	}
}