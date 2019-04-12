package cn.farwalker.waka.oss.qiniu;

/**
 * 取得上传令牌,并且定义返回值
 * http://developer.qiniu.com/docs/v6/api/overview/up/response/vars.html
 */
public class QiniuOSSVo  {

	/**
	 * 取得上传令牌,并且定义返回值(http://developer.qiniu.com/docs/v6/api/overview/up/
	 * response/vars.html)
	 */
	public static final String UPTOKEN_RETURNBODY = "{\"bucket\":$(bucket),\"key\":$(key), \"etag\": $(etag),\"mimeType\": $(mimeType)"
			+ ",\"fname\":$(fname),\"fsize\":$(fsize)}";

	 private String bucket;
	 /**资源连接(资源名。)*/
	 private String resurl;
	 private String etag;
	 private String mimetype;
	 private String fname;
	 private Long fsize;
 
	public String getBucket() {
		return bucket;
	}
 
	public void setBucket(String bucket) {
		 this.bucket = bucket;
	}

	public String getEtag() {
		return etag;
	}

	public void setEtag(String etag) {
		 this.etag = etag;
	}

	public String getMimetype() {
		return mimetype;
	}

	public void setMimetype(String mimetype) {
		this.mimetype = mimetype;
	}

	public String getFname() {
		 return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public Long getFsize() {
		return fsize;
	}

	public void setFsize(Long fsize) {
		this.fsize = fsize;
	} 
	/**资源连接*/
	public String getResurl() {
		return resurl;
	}
	/**资源连接*/
	public void setResurl(String resurl) {
		this.resurl = resurl;
	}
}
