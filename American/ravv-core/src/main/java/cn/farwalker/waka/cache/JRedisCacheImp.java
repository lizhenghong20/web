package cn.farwalker.waka.cache;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
import cn.farwalker.waka.util.DateUtil.FORMAT;
import cn.farwalker.waka.util.Tools; 
import cn.farwalker.waka.cache.CacheFactory.CacheType;
import cn.farwalker.waka.util.YMException;

/**
 * JRedisCacheImp 不是原生的redis，它是负责调用原生的redis，中间还会做一些业务处理。<br/>
 * 相当biz的处理<br/>
 * 分布缓存：指所有系统(不同系统版本之间共享:2.0系统与3.0系统),这缓存不清楚的。<br/>
 * 同步缓存：相同版本系统之间缓存是同步清除,只是同步清除及加载，数据不共享的。<br/>
 * 
 * 本类使用了 redis 实现分布缓存, EhCache实现 同步缓存：<br/>
 * <span style='color:red'>2017-6-23</span>改为2级缓存，本地为一级，redis为二级 ; <br/>
 * 一级与二级同步设计概念说明：get()、put()本地方法，只有第一次才会与redis同步，其他情况不同步  <br/>
 * 如果要多应用服务器同步更新，就要用getShare()、putShare(),它们是实时从redis获取 
 * @author juno 
 */
class JRedisCacheImp implements CacheManager {


    private static final Logger log = LoggerFactory.getLogger(JRedisCacheImp.class);

    /** 缓存分区标记(不同项目的区分-相同项目共用)*/
    private String cacheBranch;
    /** 共享缓存标记,整个redis共用-不同的项目都共用*/
    private String cacheShare;

    /** 共享版本号*/
    private String cacheVersion;
    /** 共享版本号 最后读取缓存时间 */
    private long cacheVersionLastTime ;
    /** 共享版本号过期时间:毫秒*/
    private static final long cacheVersionTimeOut=1024 * 2;
    private static JRedisClientDao redis;

    private static final CacheManager localCache1 = new EhCacheImp();
    private static final String star="*",D_CACHEVERSION="x_cache_x_version_x",D_KEY_REMOTE="_X_KEY_REMOTE_X_";

    public static void main(String[] args) {

    }

    /** 取本地缓存(一级缓存)*/
    private static CacheManager getLocalCache(){
        return localCache1;
    }
    /**
     * 
     * @param cacheBranch 分支标志-(相同项目共用相同缓存，不同的项目不通用)
     * @param cacheShare 共享缓存标记,整个redis共用-不同的项目都可以共用
     */
    public JRedisCacheImp(String cacheBranch,String cacheShare){
        this.cacheBranch="";
        this.cacheShare="";
        if(cacheBranch!=null){
            cacheBranch = cacheBranch.trim();
            int lb =  cacheBranch.length() ;
            if(lb>0 && cacheBranch.charAt(lb-1)!='_'){
                cacheBranch = cacheBranch+"_";
            }
            this.cacheBranch=cacheBranch;
        }
        if(cacheShare!=null){
            cacheShare = cacheShare.trim();
            int lb2 =  cacheShare.length() ;
            if(lb2>0 && cacheShare.charAt(lb2-1)!='_'){
                cacheShare = cacheShare+"_";
            }
            this.cacheShare=cacheShare;
        } 
        
        CacheRefresh.getInstance().register(this);
        redis = JRedisClientDao.getInstance();
    }

    @Override
    public void put(String key, String value) {
        //put(key, value, false);
        String k = getKey(key);
        if(value == null){
            this.remove(k);
        }
        else{
            getLocalCache().put(k, value);
            //优点是减少对资源读的次数，增加缓存命中(如果有10个服务，初始化时就要对资源读10次,而使用redis就读一次就好了)
            //但如果其他的b服务本地已初始了这个key，而A服务更新了缓存，则b服务是同步不了这值，这场景就要用getShare(),putShare()
            redis.put(k, value);
        }
    }

    @Override
    public void putShare(String key, String value) {
        String k =getShareKey(key);
        if(value == null){
            this.removeShare(k);
        }
        else{
            redis.put(k, value);
        }
    }


    @Override
    public String get(String key) { 
        boolean chk = this.cacheVersionCheck();
        if(!chk){
            return null;
        }
        /////////////////////////////////////
        String k = getKey(key);
        String rs = getLocalCache().get(k);
         
        if(rs != null){
            return rs;
        }
        //一级找不到，就找二级
        rs = redis.get(k);
        if(rs!=null){//保存到一级
            if(checkObjectSerializable(rs)){ 
                throw new YMException(key + "取得的值格式不正确,要求的是字符，但返回的是对象格式");
            }
            
            getLocalCache().put(k, rs);
        } 
    
        return rs;
    }
    @SuppressWarnings("unchecked")
    @Override
	public <T> T getObject(String key) {
		boolean chk = this.cacheVersionCheck();
		if (!chk) {
			return null;
		}

		String k = getKey(key);
		T rs = getLocalCache().getObject(k);
		if (rs == null) {// 一级找不到，就找二级
			byte[] bys = redis.getByte(k);
			if (bys != null) {
				rs = (T) parseByteToJava(bys);
				getLocalCache().putObject(k, (Serializable) rs);
			}
		}
		return rs;
	}
    /**  java的序列化流的前6位标志码,整数，字符比较时使用*/
    private static final int[] PXCODE_SERIALIZABLE = {172,237,0,5,115,114,0};
    /**字符串序列化的标志是 {-84, -19, 0, 5, 116, 0, 5}*/
    private static final int[] PXCODE_SERIALIZABLEs = {172,237,0,5,116, 0, 5};
    /** java的序列化流的前6位标志码,二进制(-84,-19,0,5,115,114,0) byte比较时使用  */
    private static final byte[] PXCODE_SERIALIZABLE_BYTE = {(byte)0xAC,(byte)0xED,(byte)0x0,(byte)0x5,(byte)0x73,(byte)0x72,(byte)0x0};
    /**字符串序列化的标志是 {-84, -19, 0, 5, 116, 0, 5}*/
    private static final byte[] PXCODE_SERIALIZABLE_BYTEs = {(byte)0xAC,(byte)0xED,(byte)0x0,(byte)0x5,(byte)0x74,(byte)0x0,(byte)0x5};
    
    /** 检查是否java的序列化对象*/
    private boolean checkObjectSerializable(String s){
        int size = (s == null ?0:s.length()) ,pl = PXCODE_SERIALIZABLE.length ;
        if(size<= pl){
            return false;
        }
        boolean rs = true;
        for(int i =0 ;i < pl ;i++){
            int c = (int)s.charAt(i);
            if(c != PXCODE_SERIALIZABLE[i] && c!= PXCODE_SERIALIZABLEs[i]){
                rs = false;
                break;
            }
        }
        return rs;
    }
    /** 检查是否java的序列化对象*/
    private boolean checkObjectSerializable(byte[] s){
        int size = (s == null ?0:s.length ),pl=PXCODE_SERIALIZABLE_BYTE.length;
        if(size<=pl){
            return false;
        }
        boolean rs = true;
        for(int i =0 ;i < pl;i++){ 
            if(s[i] != PXCODE_SERIALIZABLE_BYTE[i] && s[i] != PXCODE_SERIALIZABLE_BYTEs[i]){
                rs = false;
                break;
            }
        }
        return rs;
    }
    
    /** 
     * 取得检查版本号,如果与其他服务器不相同，则自动清除本地所有缓存(每0.5秒检查一次，避免频繁读取网络)
     * @return 返回false表示与其他服务器不相同，直接返回null表调用者
     */
    private boolean cacheVersionCheck(){
        long time = System.currentTimeMillis() ;
        if((time- cacheVersionLastTime) < cacheVersionTimeOut){  //判断超时
            //log.debug("**********" + cacheVersionLastTime);
            return true;
        }
        
        //超时
        cacheVersionLastTime = time;
        String version = cacheVersionLoad();
        boolean rs ;
        if(this.cacheVersion == null){//二级缓存的情况下，直接使用第2级的内容
            this.cacheVersion = version;
            rs = true;
        }
        else if(Tools.string.isEmpty(version)){//第2级的内容还没有初始化
            this.cacheVersion ="0"; 
            
            String k = getKey(D_CACHEVERSION);
            redis.put(k, this.cacheVersion);
            rs = false;
        }
        else if(!version.equals(this.cacheVersion)){
            getLocalCache().removeAll(null);//清除本地所有
            
            log.info("根据网络其他服务的远程缓存版本号，清除本地配置配置(这个日志不要删除，为了方便日后重复调用时检查代码)");
            //Tools.config.refresh();//清除本地配置
            this.cacheVersion = version;
            rs = false;
        }
        else{
            rs = true;
        }
        return rs;
    }
    
    /** 取得版本号(这个方法不要加延时)*/
    private String cacheVersionLoad(){ 
        String k = getKey(D_CACHEVERSION);
        String version = redis.get(k);
        return version;
    }
    
    @Override
    public String getShare(String key) {
        String k = getShareKey(key);
        return redis.get(k);
    }

    /** 测试了，没发现json比byte快*/
    private void testJavaToString(Serializable value ){
        Class clazz = value.getClass();
        String className = "";
        String menuTreeClass="com.yomnn.components.sysmenu.model.MenuTreeVo";
        
        int listSize= -1,mapSize =-1 ;
        if(Collection.class.isAssignableFrom(clazz)){
            Collection c = (Collection)value;
            listSize = c.size();
            if(listSize>0){
                className = c.iterator().next().getClass().getName();
            }
        }
        
        else if(Map.class.isAssignableFrom(clazz)){
            Map m =(Map)value;
            mapSize = m.size();
            if(mapSize>0){
                Map.Entry e =(Map.Entry) m.entrySet().iterator().next();
                className = e.getKey().getClass().getName() +","+ e.getValue().getClass().getName();
            }
        }
        else if(clazz.getName().equals(menuTreeClass)){
            listSize =0;
            mapSize =0;
            className =menuTreeClass;
        }
        
        if(listSize>=0|| mapSize>=0){
            String byteString,jsonString; 
            final Charset CHARGET =Charset.forName("ISO-8859-1");
            
            long byteTime, byteSize, stringSize, jsonTime, jsonSize;
            
            {/////// 序列化
                
                long t1 = System.currentTimeMillis();
                byte[] bys = parseJavaToByte(value); 
                byteString = new String(bys,CHARGET);
                byteSize = bys.length;
                long t2 = System.currentTimeMillis()  ;
                stringSize = byteString.length();
                byteTime =t2 - t1  ;//byte序列化使用的时间
                
                StringBuilder json = parseJavaToJson(value);
                jsonTime = System.currentTimeMillis()  - t2;//json序列化使用的时间
                jsonSize = json.length();  
                
                jsonString = json.toString();
            }
            
            long unbyteTime, unjsonTime;
            {///////反序列化
                long t1 = System.currentTimeMillis();
                byte[] bys = byteString.getBytes(CHARGET);
                Object obj = parseByteToJava(bys);
                long t2 = System.currentTimeMillis();
                unbyteTime = t2 - t1  ;
                
                Object obj2 = parseJsonToJava(jsonString);
                unjsonTime = System.currentTimeMillis() - t2; 
            } 
            
            try {
                StringBuilder sql = new StringBuilder("INSERT INTO _ser (clazz, byteTime, byteSize, stringSize, "
                        + "jsonTime, jsonSize, unbyteTime, unjsonTime,listSize,mapSize) VALUES (");
                sql.append('\'').append(className).append("',");
                sql.append(byteTime).append(',').append(byteSize).append(',').append(stringSize).append(',')
                    .append(jsonTime).append(',').append(jsonSize).append(',')
                    .append(unbyteTime).append(',') .append(unjsonTime).append(',')
                    .append(listSize).append(',') .append(mapSize).append(");\r\n");
                int r = (int)(Math.random() * 1000);
                StringBuilder fn = Tools.date.formatDate(FORMAT.YYYYMMDDHHMMSS).append('X').append(r).append(".sql"); 
                //Tools.file.fileWriteText("d:/temp/" + fn, sql.toString()) ;
            }
            catch (Exception e) {
                // TODO 自动生成的 catch 块
                e.printStackTrace();
            }
        }
    } 
    @Override
    public void putObject(String key, Serializable value) {
        String k = getKey(key);
        if(value == null){
            this.remove(k);
            return ;
        } 
        //testJavaToString(value);
        getLocalCache().putObject(k, value);
        //如果其他的b服务本地已初始了这个key，则b服务是同步不了这值
        byte[] bys = parseJavaToByte(value);
        
        //临时调试，正式要注释掉
        if(value instanceof String){
            throw new YMException("缓存值是字符，请使用put()");
        }
        /*if(!checkObjectSerializable(bys)){
            log.debug("xxxxxxxxxxx");
        };*/
        redis.put(k, bys);
        //this.putObjectShare(key, value);
    }
    
    /**
    @Override
    public void putObject(String key, Serializable value, boolean share) {
        String k = getKey(key);
        if(value == null){
            this.remove(k);
        }
        else if (share) {
            byte[] bytes = objectToByte(value);
            redis.put(getKey(key), bytes);
            localCache.put(key, D_KEY_REMOTE);
        }
        else {
            localCache.putObject(k, value);
        }
    }*/

    @Override
    public void putObjectShare(String key, Serializable value) {
        String k = getShareKey(key);
        if(value == null){
            this.removeShare(k);
        }
        else{
            //byte[] bytes = objectToByte(value);
            StringBuilder json = parseJavaToJson(value);
            redis.put(k, json.toString());
        }
    } 


    private byte[] parseJavaToByte(Serializable value) {
        if (value == null) {
            return null;
        }
        ByteArrayOutputStream os2 = new ByteArrayOutputStream();
        try (ObjectOutputStream os = new ObjectOutputStream(os2)) {
            os.writeObject(value);
            return os2.toByteArray();

        } catch (IOException e) {
            throw new YMException(value.getClass().getName() + "对象转二进制出错:" + e.getMessage());
        }
    }
    
   
    
    /*
    <!-- ================================================= -->
   <!-- json-smart 号称最快的json处理包 -->
   <!-- ================================================= -->
   <!-- dependency>
       <groupId>net.minidev</groupId>
       <artifactId>json-smart</artifactId>
       <version>2.3</version>
   </dependency -->
    */
    /**
     * 转换为json字符，并且把类名也加入json中<br/>
     * 递归把所有的元素遍历转换为json,如果是Collection/map,每个元素还要带类型
     * @param value 不能为空对象
     * @return
     * 
     */
    private StringBuilder parseJavaToJson(Object value){
        return Tools.json.toJsonClass(value);
    	//return Tools.bean.toJsonClass(value);
    }
    
    /** 由json反序列化{@link JRedisCacheImp#objectToJson(Serializable)}
     * json反序列化对于泛型的集合并不是十分适用
     */ 
    private Object parseJsonToJava(String json) { 
        return Tools.json.toClassObject(json);
    	//return Tools.bean.toClassObject(json);
    }
     
    /** 由二进制反序列化*/
    private Object parseByteToJava(byte[] bys) {
        if (bys == null) {
            return null;
        } 

        //byte[] bys = redis.getByte(key);
        try (ObjectInputStream is = new ObjectInputStream(new ByteArrayInputStream(bys))) {
            Object rs = is.readObject();
            return rs;
        } catch (Exception e) {
            throw new YMException( "对象转二进制出错:" + e.getMessage());
        }
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <T> T  getObjectShare(String key) {
        String k = getShareKey(key);
        String json = redis.get(k);
        Object rs = parseJsonToJava(json);
        return (T)rs;
    }
    
 
    @Override
    public boolean remove(String key) {
        String pk = getKey(key);
        boolean rs = redis.remove(pk);
        boolean rs2 = getLocalCache().remove(pk);
        return rs && rs2;
    }

    @Override
    public boolean removeShare(String key) {
        String pk = getShareKey(key);
        return redis.remove(pk);
    }

    /** 清除所有,不包含共享部分 
    @Override
    public boolean removeAll() {
        String keep_cacheVersion = cacheVersionLoad();
        try{
            String pkey = getKey(star);
            redis.removeAll(pkey);
            boolean rs = localCache.removeAll();
            return rs;
        }
        finally{
            cacheVersionChange(keep_cacheVersion,true);
        }
    }*/
    
    /**
     * 版本号增加，这里只改变redis的远程版本号，本地的变量及缓存由{@link #cacheVersionCheck()}方法统一处理
     * @param oldVersion
     * @param add
     */
    private void cacheVersionChange(String oldVersion,boolean add){
        if(add || (this.cacheVersion==null || !this.cacheVersion.equals(oldVersion))){//加1：版本号不相同 或才强制加1
            int version = Tools.number.nullIf(oldVersion, 0)+1;
            String vers = String.valueOf(version);
            String k = getKey(D_CACHEVERSION);
            //不要直接赋值，由cacheVersionCheck()方法统一处理
            //this.cacheVersion = String.valueOf(version);//本服务实例直接赋值，因为当前服务器已全部清除了
            redis.put(k, vers);
        }
    }

    /** 清除指定前缀*/
    @Override 
    public boolean removeAll(String preKey) {
        String keep_cacheVersion = cacheVersionLoad();
        try {
            String pkey = getKey(preKey==null ? star:preKey + star);
            boolean rs = redis.removeAll(pkey);
            getLocalCache().removeAll(pkey);
            return rs;
        }
        finally {
            cacheVersionChange(keep_cacheVersion, true);
        }
    }

    /**
    @Override
    public boolean removeShareAll() {
        String pk = getShareKey(star);
        boolean rs = redis.removeAll(pk);
        return rs;
    }*/
    
    @Override
    public boolean removeShareAll(String preKey) {
        String key = getShareKey(preKey==null ? star:preKey+star);
        boolean rs = redis.removeAll(key);
        return rs;
    }

    @Override
    public void close() {
        getLocalCache().removeAll(null);
        getLocalCache().close();
        //throw new YMException("ehcache key 超时未实现");
    }

    @Override
    public CacheType getCacheType() {
        return CacheType.reids;
    }

    @Override
    public void refresh() {
        this.removeAll(null);
    }

    /** 取得共享分区的key(不同项目的key)*/
    private String getKey(String key){
        int l = (key==null?0:key.length());
        if(l==0){
            return cacheBranch;
        }
        StringBuilder sb=new StringBuilder(cacheBranch.length() + l);
        sb.append(cacheBranch).append(key);
        return sb.toString();
    }
    
    private String getShareKey(String key){
        int l = (key==null?0:key.length());
        if(l==0){
            return cacheShare;
        }
        
        StringBuilder sb=new StringBuilder(cacheShare.length() + l);
        sb.append(cacheShare).append(key);
        return sb.toString();
    }
}
/**
class AdapterCache implements CacheManager {
    private  JRedisClientDao dao;
    public AdapterCache(JRedisClientDao client){
        dao = client;
    }
    @Override
    public void refresh() {
        this.removeAll(); 
    }
    
    @Override
    public boolean removeShareAll(String preKey) { 
        return this.removeAll(preKey);
    }
    
    @Override
    public boolean removeShareAll() { 
        return this.removeAll();
    }
    
    @Override
    public boolean removeShare(String key) { 
        return dao.remove(key);
    }
    
    @Override
    public boolean removeAll(String preKey) { 
        return dao.remove(preKey);
    }
    
    @Override
    public boolean removeAll() { 
        return dao.removeAll();
    }
    
    @Override
    public boolean remove(String key) { 
        return dao.remove(key);
    }
    
    @Override
    public void putShare(String key, String value) {
        this.put(key, value); 
    }
    
    @Override
    public void putObjectShare(String key, Serializable value) {
        this.putObject(key, value); 
    }
    
    @Override
    public void putObject(String key, Serializable value, boolean share) { 
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            ObjectOutputStream ois = new ObjectOutputStream(os);
            ois.writeObject(value);
            byte[] bys = os.toByteArray();
            dao.put(key, bys);
        }
        catch (IOException e) {
            throw new YMException(e);
        }
    }
    
    @Override
    public void putObject(String key, Serializable value) {
        this.putObject(key, value, false); 
    }
    
    @Override
    public void put(String key, String value, boolean share) {
        dao.put(key, value); 
    }
    
    @Override
    public void put(String key, String value) {
        this.put(key, value, true); 
    }
    
    @Override
    public String getShare(String key) { 
        return this.get(key);
    }
    
    @Override
    public <T> T getObjectShare(String key) { 
        return this.getObject(key);
    }
    
    @Override
    public <T> T getObject(String key) { 
        byte[] bys = dao.getByte(key); 
        try (ObjectInputStream ois= new ObjectInputStream( new ByteArrayInputStream(bys))){
              Object rs =  ois.readObject();
            return (T)rs;
        }
        catch (ClassNotFoundException | IOException e  ) {
            throw new YMException(e);
        }
    }
    
    @Override
    public CacheType getCacheType() { 
        return CacheType.reids;
    }
    
    @Override
    public String get(String key) { 
        return dao.get(key);
    }
    
    @Override
    public void close() {
        
    }
}*/