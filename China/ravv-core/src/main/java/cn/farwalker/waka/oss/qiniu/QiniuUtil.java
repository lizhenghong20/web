package cn.farwalker.waka.oss.qiniu;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import cn.farwalker.waka.oss.OssException;
import cn.farwalker.waka.util.Tools;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.storage.persistent.FileRecorder;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;

public class QiniuUtil {
    
	// private static final String Default_Bucket_XIANGIN="ym-xiangin";
	private static final Logger log = LoggerFactory.getLogger(QiniuUtil.class);

	/** 比较少使用，所以使用懒汉式 */
	private static QiniuUtil qiniu;


	/** 七牛客户端对象
    private final Client client;*/
	
	/** 七牛桶管理对象*/
    private final BucketManager bucketManager;
    
    private final Auth qiniuAuth;
    
    /**
     * 华南机房相关域名
     */
    private final static Zone zoneHuanan = Zone.zone2();
    /**北美*/
    private final static Zone zoneNa0 = Zone.zoneNa0();
    private QiniuProperties qiniuConfig;
    
    
    public static class UploadParamVo{
    	private String uptoken;
    	 
    	private String sourcetable;
    	private String sourcefield;
    	private String sourceid; 
        private QiniuProperties config;
    	
    	public UploadParamVo(QiniuProperties config,String sourceTable, String sourceField, String sourceId){
    		this.sourcetable = sourceTable;
    		this.sourcefield = sourceField;
    		this.sourceid = sourceId;
    		this.config = config;
    	}
    	/** 每次上传都需要七牛后台生产*/
		public String getUptoken() {
			return uptoken;
		}
		/** 每次上传都需要七牛后台生产*/
		public void setUptoken(String uptoken) {
			this.uptoken = uptoken;
		}
		
		public String getDomain() {
			return config.getDomain();
		}
		
		public String getAccesskey() {
			return config.getAccessKey();
		}
		public String getSourcetable() {
			return sourcetable;
		}
		public void setSourcetable(String sourcetable) {
			this.sourcetable = sourcetable;
		}
		public String getSourcefield() {
			return sourcefield;
		}
		public void setSourcefield(String sourcefield) {
			this.sourcefield = sourcefield;
		}
		public String getSourceid() {
			return sourceid;
		}
		public void setSourceid(String sourceid) {
			this.sourceid = sourceid;
		}
		/**资源的保存路径(根据3个source的属性生产)*/
		public String getUploadpath() {
			if (Tools.string.isEmpty(sourcetable)) {
				throw new OssException("取上传路径的sourceTable不能为空");
			}
			StringBuilder rs = new StringBuilder(sourcetable);
			if (!Tools.string.isEmpty(this.sourcefield)) {
				rs.append('/').append(this.sourcefield);
			}
			if (Tools.string.isEmpty(this.sourceid)) {
				rs.append("/t").append(Tools.uuid());
			} else {
				rs.append('/').append(this.sourceid);
			}
			rs.append('/');
			return rs.toString();
		}
    }

	public static QiniuUtil getInstance() {
		if (qiniu == null) {
			QiniuProperties pro = Tools.springContext.getBean(QiniuProperties.class);
			qiniu = new QiniuUtil(pro);

	    	//只初始化一次
	    	Tools.env.getRootPath(true, QiniuUtil.class);
		}
		return qiniu;
	}
	
    /**交spring管理，不要直接创建*/
	private QiniuUtil(QiniuProperties pro) {
		qiniuConfig = pro;
		String accessKey = qiniuConfig.getAccessKey();
		String secetKey = qiniuConfig.getSecretKey();
		//client = new Client();
		qiniuAuth = Auth.create(accessKey,secetKey);//(ACCESS_KEY, SECRET_KEY);
		
		Configuration cfg = new Configuration(zoneNa0);
		bucketManager = new BucketManager(qiniuAuth,cfg);
	}

	/**
	 * 取得上传令牌,设置指定上传策略(3600*4)<br>
	 * http://developer.qiniu.com/docs/v6/sdk/java-sdk.html#make-uptoken
	 * @param
	 *            存储空间(请确保该bucket已经存在)
	 * @return
	 */ 
	private String getUptoken(){
	    StringMap pars = new StringMap();
	  //fileType 文件存储类型。0 为普通存储（默认），1 为低频存储。 https://developer.qiniu.com/kodo/manual/1206/put-policy
	    //pars.put("fileType", Integer.valueOf(1));
	    pars.put("fileType", Integer.valueOf(0));//0 为标准存储
	    String bucket  = qiniuConfig.getBucket();
	    String rs = qiniuAuth.uploadToken(bucket, null, 3600*4, pars);
	    return rs;
	}
	/** qiniu 6.19api
	private String getUptoken() {
		try {
			Mac mac = new Mac(this.accessKey, this.secretKey);
			// 请确保该bucket已经存在
			// String bucketName = "Your bucket name";
			PutPolicy p = new PutPolicy(this.bucketName);
			p.returnBody = QiniuOSSVo.UPTOKEN_RETURNBODY;
			String uptoken = p.token(mac);
			return uptoken;
		} catch (Exception e) {
			log.error("取得七牛上传令牌", e);
			throw new OssException("取得七牛上传令牌:" + e.getMessage());
		}
	}*/

	/**
	 * 前端上传前，先取得上传参数，才能上传文件。
	 * 取得上传参数:{uptoken:令牌,domain:七牛域名,uploadpath:保存位置,accesskey:oss的accesskey}
	 * sourceTable、sourceField、sourceId用于生成uploadpath
	 * 
	 * @param sourceTable 来源表名(必填)
	 * @param sourceField 来源字段名(可以为空)
	 * @param sourceId 来源记录id(可以为空)
	 * @return
	 */
	public UploadParamVo getUploadParam(String sourceTable, String sourceField, String sourceId) {
		if(Tools.string.isEmpty(sourceTable)){
			throw new OssException("取上传路径的sourceTable不能为空");
		}
		sourceField = sourceField == null ? "" : sourceField.trim();
		sourceId = sourceId == null ? "" : sourceId.trim();
		if(sourceId.equalsIgnoreCase("uuid")){
			sourceId = Tools.timerKey.getTimeShortId();
		}

		String uptoken = getUptoken();
		UploadParamVo pv = new UploadParamVo(
				qiniuConfig, sourceTable, sourceField, sourceId);
		pv.setUptoken(uptoken);
		return pv;
		/*StringBuilder path = getUploadpath(sourceTable, sourceField, sourceId);
		Map<String, String> rs = new HashMap<String, String>(3);
		rs.put("uptoken", uptoken);
		rs.put("domain", qiniuConfig.getDomain());
		rs.put("uploadpath", path.toString());
		rs.put("accesskey", qiniuConfig.getAccessKey());
		rs.put("sourcetable", sourceTable);
		rs.put("sourcefield", sourceField);
		rs.put("sourceid", sourceId);
		return rs;*/
	}

	

	/** 把七牛返回值转为对象 */
	public QiniuOSSVo parse(String json) {
		if (Tools.string.isEmpty(json)) {
			return null;
		}

		JSONObject obj = JSONObject.parseObject(json);
		QiniuOSSVo vo = new QiniuOSSVo();
		List<String> fds = Tools.bean.getFields(QiniuOSSVo.class);
		for (String fn : fds) {
			String n = fn.toLowerCase();
			Object value = obj.get(n);
			if (value != null) {
				Tools.bean.setPropertis(vo, n, value);
			}
		}
		return vo;
	}

	/**
	 * 上传文件
	 * 
	 * @param bucketName
	 *            存储空间(请确保该bucket已经存在)
	 * @param key
	 *            位置
	 * @param localFile
	 * @param extra
	 * @return
	 *  qiniu 6.19api
	public PutRet uploadFile(String key, File localFile, PutExtra extra) {
		try {
			String uptoken = this.getUptoken();
			QiniuInputStream is = new QiniuInputStream(new FileInputStream(localFile));
			PutRet rs = IoApi.Put(uptoken, key, is, extra);
			if (rs.ok()) {
				return rs;
			} else {
				log.error("上传文件出错", rs.exception);
				throw new OssException("上传文件出错", rs.exception);
			}
		} catch (IOException e) {
			log.error("上传文件出错", e);
			throw new OssException("上传文件出错:" + e.getMessage());
		}
	}
     */
 
 
    /**
     * 上传文件
     * @param file 上传的文件对象
     * @param key 保存位置的全路径(路径+文件名，例如/mytest/lombok.jar)
     * @param mime 类型
     * @return 返回文件存放路径
     */
    public String uploadFile(File file, String key ){
    	if(file == null || !file.isFile()){
    		throw new OssException(file == null ?"上传文件不能为空": "文件不存在:" + file.getAbsolutePath());
    	}
    	try {
	    	//构造一个带指定Zone对象的配置类
	    	Configuration cfg = new Configuration(zoneNa0);
	    	//...其他参数参考类注释
	    	//Auth auth = Auth.create(qiniuConfig.getAccessKey(), qiniuConfig.getSecretKey());
	    	String uptoken = getUptoken();// auth.uploadToken(bucket);
	
	    	FileRecorder fileRecorder;{//获得不分操作系统的临时目录
	    		String bucket = qiniuConfig.getBucket();
		    	String tempDir=System.getProperty("java.io.tmpdir");
		    	String localTempDir = Paths.get(tempDir, bucket).toString();
	    	    //设置断点续传文件进度保存目录
	    	    fileRecorder = new FileRecorder(localTempDir);
	    	}
    	    UploadManager uploadManager = new UploadManager(cfg, fileRecorder);
   
	        Response response = uploadManager.put(file, key, uptoken);
	        //解析上传成功的结果
	        String json = response.bodyString();
	        Gson gson = new Gson();
	        DefaultPutRet putRet = gson.fromJson(json, DefaultPutRet.class);
	        return putRet.key;
	        /*
	        System.out.println();
	        System.out.println(putRet.hash);
	        */
	    } catch (QiniuException ex) {
	        Response r = ex.response;
	        throw new OssException(r.toString());
	        /*
	        System.err.println(r.toString());
	        try {
	            System.err.println(r.bodyString());
	        } catch (QiniuException ex2) {
	            //ignore
	        }*/
	    }catch (IOException ex) {
	    	throw new OssException("上传文件出错",ex);
    	}
    }
	

	/** 删除oss文件
	public CallRet deleteFileURL(String url) {
		Mac mac = new Mac(this.accessKey, this.secretKey);
		RSClient client = new RSClient(mac);
		CallRet cr = client.delete(this.bucketName, url);
		if (cr.statusCode == 200 || cr.statusCode == 612) {
			return cr;
		} else {
			throw new OssException("删除资源出错:" + cr.response);
		}
	}
	 */
	
	public void deleteFileURL(String url){ 
	    try{
	    	String bucket = qiniuConfig.getBucket();
	        bucketManager.delete(bucket, url);
	    }
	    catch(QiniuException e){
	        String s ="删除七牛文件("  + url +")出错:" + e.getMessage();
	        log.error(s);
	        throw new OssException(s);
	    }
	} 
	

    /**
     * 过滤用户自定义参数，只有参数名以<code>x:</code>开头的参数才会被使用
     *
     * @param params 待过滤的用户自定义参数
     * @return 过滤后的用户自定义参数
     */
    private static StringMap filterParam(StringMap params) {
        final StringMap ret = new StringMap();
        if (params == null) {
            return ret;
        }

        params.forEach(new StringMap.Consumer() {
            @Override
            public void accept(String key, Object value) {
                if (value == null) {
                    return;
                }
                String val = value.toString();
                if (key.startsWith("x:") && !val.equals("")) {
                    ret.put(key, val);
                }
            }
        });

        return ret;
    } 
    
    private static void checkArgs(final String key,   File qis, String token) {
        String message = null;
        if (qis == null || !qis.isFile()) {
            message = "no input data";
        } else if (token == null || token.equals("")) {
            message = "no token";
        }
        if (message != null) {
            throw new IllegalArgumentException(message);
        }
    }
    /**
     * 拼成可以访问的全路径(带http)
     * @param relative
     * @return
     */
    public static String getFullPath(String relative){
    	if(Tools.string.isEmpty(relative) || relative.startsWith("http://")){
    		return relative;
    	}
    	QiniuUtil util = getInstance();
    	String rs=util.qiniuConfig.getDomain();
    	if(relative.charAt(0)=='/'){
    		rs = rs + relative;	
    	}
    	else{
    		rs = rs + "/" + relative;
    	}
    	return rs;
    }
    /**
     * 根据全路径，取的相对路径
     * @param relative
     * @return
     */
    public static String getRelativePath(final String fullpath){
    	String relative = fullpath;
    	if(Tools.string.isNotEmpty(relative) && (relative.startsWith("http://") || relative.startsWith("www."))){
    		QiniuUtil util = getInstance();
    		String domain=util.qiniuConfig.getDomain().substring(7);//去掉"http://"
    		int idx = relative.indexOf(domain);
    		if(idx>=0){
    			idx = domain.length() + idx;
    			relative = relative.substring(idx +1);
    		}
    	}
		return relative;
    }

	/**
	 * 如果是相对路径，则转为绝对路径,如果是绝对路径直接返回
	 * @param url
	 * @return
     */
    public static String covertToRealPath(String url){
		QiniuUtil util = getInstance();
		if(url != null)
			if(url instanceof String)
				if(!url.contains(util.qiniuConfig.getDomain())){
					return getFullPath(url);
				}
		return url;
	}
    
	public static void main(String[] args) {
		QiniuProperties pro = new QiniuProperties();
		//七牛用户：chenjs@farwalker.cn
		pro.setAccessKey("jnIntrKoJKDbQX0wxya1VTNUTYE6DOx70SGwy_Bz");
		pro.setSecretKey("vJUjlYuOHe8CUkh1322Ag-ggN7ugAZq9fu2I_lgY");
		pro.setBucket("ledian");
		pro.setDomain("pgiyer92n.bkt.clouddn.com");
		QiniuUtil qiniu = new QiniuUtil(pro);
		File localFile = new File("D:/works/lombok.jar");
		String path = qiniu.uploadFile(localFile,"/mytest/" + localFile.getName());
		System.out.println(pro.getDomain() + path);
		//http://pgiyer92n.bkt.clouddn.com//mytest/lombok.jar
	}
}

class QiniuInputStream extends InputStream {

	private final InputStream is;

	public QiniuInputStream(InputStream is) {
		this.is = is;
	}

	@Override
	public int read() throws IOException {
		return is.read();
	}

	@Override
	public int read(byte[] b) throws IOException {
		return is.read(b);
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		return is.read(b, off, len);
	}

	@Override
	public long skip(long n) throws IOException {
		return is.skip(n);
	}

	@Override
	public int available() throws IOException {
		return is.available();
	}

	@Override
	public void close() throws IOException {
		is.close();
	}

	@Override
	public synchronized void mark(int readlimit) {
		is.mark(readlimit);
	}

	@Override
	public synchronized void reset() throws IOException {
		is.reset();
	}

	@Override
	public boolean markSupported() {
		return is.markSupported();
	}

}
