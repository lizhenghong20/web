package cn.farwalker.waka.oss.qiniu;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import cn.farwalker.waka.util.Tools;

/**
 * @author: EarthChen
 * @date: 2018/03/13
 */
@Component
@ConfigurationProperties(prefix = QiniuProperties.CONFIG_PREFIX)
public class QiniuProperties {
	public static final String CONFIG_PREFIX = "qiniu";
    private String accessKey;

    private String secretKey;

    private String bucket;
    
    private String domain;

	private String cdnPrefix;
    /**
     * 域名，格式http://7vznqs.com2.z0.glb.qiniucdn.com<br/>
     * 前有"http://",后面以字母结束(没有后面的“/”)
     */
    public String getDomain() {
    	if(Tools.string.isEmpty(domain)){
    		return "";
    	}
    	if(!domain.startsWith("http://")){
    		domain = "http://" + domain;
    	}
    	int l = domain.length();
    	if(domain.charAt(l - 1)=='/'){
    		domain = domain.substring(l-1);
    	}
		return domain;
	}

	public String getAccessKey() {
		return accessKey;
	}

	public String getSecretKey() {
		return secretKey;
	}
	/** 桶名*/
	public String getBucket() {
		return bucket;
	}
	/**前缀，可省略*/
	public String getCdnPrefix() {
		if(Tools.string.isEmpty(cdnPrefix)){
			return "";
		}
		return cdnPrefix;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public void setBucket(String bucket) {
		this.bucket = bucket;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public void setCdnPrefix(String cdnPrefix) {
		this.cdnPrefix = cdnPrefix;
	}


}