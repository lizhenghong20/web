package cn.farwalker.waka.cache;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.Protocol;
import redis.clients.jedis.ShardedJedisPool;
import cn.farwalker.waka.util.Tools;
import cn.farwalker.waka.util.YMException;

/**
 * 本类与JRedisCacheImp区别是，这里只做相当于dao的处理，不做业务运算
 * @author juno
 *
 */
public class JRedisClientDao { 
    private static final Logger log = LoggerFactory.getLogger(JRedisClientDao.class);
    
    /**如果配置多个服务器，则自动切换为集群模式*/
    //private static final String D_REDIS_SERVER="cache.redis.server";

    /** 默认缓存时间*/
    public static final int CACHE_TIME= 60 * 60 *24;
    
    private static JRedisClientDao redis  ;
    //private Jedis            jedis;            //非切片额客户端连接
    //private JedisPool        jedisPool;        //非切片连接池
    
    //private ShardedJedis     shardedJedis;     //切片额客户端连接
    //private ShardedJedisPool shardedJedisPool; //切片连接池

    private JedisCommands jedisCommands;//客户端

    private List<JedisShardInfo> shards;//=new ArrayList<>();
    //private Set<HostAndPort> jedisClusterNodes ;//= new HashSet<HostAndPort>();
    
    /** 每个项目不相同的*/
    //private String redisKeyPx="";
    private static final Charset CHARGET ;
    static{
        CHARGET =Charset.forName("ISO-8859-1"); //ISO-8859-1
    }
    public static JRedisClientDao getInstance(){
        if(redis!=null){
            return redis;
        }

    	CacheProperties pro = Tools.springContext.getBean(CacheProperties.class);
        HostAndPort[] hps = readConfig(pro.getRedisServer());
        if(hps==null || hps.length==0){
            throw new YMException("redis配置不正确,没有配置地址");
        }
        String password = pro.getRedisPassword();
        JRedisClientDao rd = new JRedisClientDao(hps,password);
        
        if(hps.length>1){
            //shardedJedisPool = initialShardedPool();
            //shardedJedis = shardedJedisPool.getResource();
            //第二种方式
            JedisCluster jedisCluster = rd.initJedisCluster();
            rd.jedisCommands=jedisCluster;
        }
        else{
            //rd.jedisPool = rd.initialPool();
            //rd.jedis = rd.jedisPool.getResource(); 
           // Jedis jedis =rd.initialRedis();
            rd.jedisCommands=  rd.initialPool();
        }
        redis = rd;
        return rd;
    }

    private final HostAndPort[] hps ;
    private final String password;

    private JRedisClientDao(HostAndPort[] hps ) { 
         this(hps,null);
    }
    private JRedisClientDao(HostAndPort[] hps ,String passwrod) { 
        this.hps = hps;
        this.password = (passwrod == null || passwrod.length()==0? null : passwrod);
   }
    /**
     * 默认失效时间为1天
     * @param key
     * @param value 可以为null,为null则变成remove方法
     */
    public void put(String key,String value){
        put(key,value,CACHE_TIME);
    }
    /**
     * 
     * @param key
     * @param value 可以为null,为null则变成remove方法
     * @param seconds 失效时间(秒)，如果是0，则永不失效
     */
    public void put(String key,String value,int seconds){
        if(value == null){
            this.remove(key);
        }
        else{
            jedisCommands.set(key,value);
            if(seconds>0){
                jedisCommands.expire(key,seconds);
            }
            else if(seconds <0){
                throw new YMException("失效时间不能为负数");
            }
        }
    }
    
    /**
     * 由于redisCache对二进制也是转换为字符，建议业务代码自己实现转换(默认失效时间为1天)
     * @param key
     * @param value 可以为null,为null则变成remove方法
     */
    public void put(String key,byte[] value){  
        put(key,value,CACHE_TIME);  
    }
    
    /**
     * 由于redisCache对二进制也是转换为字符，建议业务代码自己实现转换
     * @param key
     * @param value 可以为null,为null则变成remove方法
     * @param seconds 超时时间(秒)
     * java序列化前6位是：int(172,237,0,5,115,114,0)或byte(-84,-19,0,5,115,114,0)
     */
    public void put(String key,byte[] value,int seconds){
        String tempValue =(value == null ? null:new String(value,CHARGET)); 
        this.put(key,tempValue,seconds);
        
/*        StringBuilder sb = new StringBuilder(200); 
        for(int i =0 ;i < value.length  && i<7;i++){
            sb.append((int)tempValue.charAt(i)).append(',');
        }
        sb.append("|");
        
        for(int i =0 ;i < value.length  && i<7;i++){
            sb.append(value[i]).append(',');
        }
        sb.append(key);
        log.debug(sb.toString());*/
    }
    public String get(String key ){
        String k = key;//getRedisKeyPx(key);
        return jedisCommands.get(k);
    }
    public byte[] getByte(String key){ 
        String s = this.get(key);
        if (s == null || s.length()==0) {
            return null;
        }
        byte[] rs = s.getBytes(CHARGET);
        
/*        StringBuilder sb = new StringBuilder(200); 
        for(int i =0 ; i<7;i++){
            sb.append((int)s.charAt(i)).append(',');
        }
        sb.append("|");
        
        for(int i =0 ; i<7;i++){
            sb.append(rs[i]).append(',');
        }
        sb.append(key);
        log.debug(sb.toString());
        */
        return rs;
    }
    
    /** 增加前缀
    private String getRedisKeyPx(String key){
        if(redisKeyPx == null || redisKeyPx.length()==0){
            return key;
        }
        StringBuilder rs = new StringBuilder(redisKeyPx);
        rs.append(key);
        return rs.toString();//redisKeyPx + key;
    }
    private void setRedisKeyPx(){
        String px = Tools.config.getValue(D_REDISKEYPX);
        redisKeyPx =(px == null ?"": px.trim());
        if(Tools.string.isEmpty(redisKeyPx)){
            throw new YMException("redisKey前缀为空");
        } 
    }*/
    
    public boolean remove(String key){
        String k = key;//getRedisKeyPx(key);
        jedisCommands.del(k);
    	return true;
    }
    public boolean removeAll(){
        if(isCluster()){
            for (int i=0,size =(hps==null?0:hps.length);i < size;i++)  {
                HostAndPort jedisShardInfo = hps[i];
                String host=jedisShardInfo.getHost();
                int port=jedisShardInfo.getPort();
                
                if(host!=null && port>0){
                    Jedis jedis = new Jedis(host,port);
                    jedis.flushAll();
                    jedis.close();
                }
            }
        }
        else if(jedisCommands instanceof JRedisPoolCommands){
            JRedisPoolCommands jd =(JRedisPoolCommands)jedisCommands;
            jd.flushAll();
        }
        else if(jedisCommands instanceof Jedis){
            Jedis jd = (Jedis)jedisCommands;
            jd.flushAll(); 
        }
        return true;
    }

    /**
     * 指定删除包含前缀的key
     * @param preKey
     * @return
     */
    public boolean removeAll(String preKey){
        if(isCluster()){
            //HostAndPort[] hps =readConfig();
            for (int i=0,size =(hps==null?0:hps.length);i < size;i++)  {
                HostAndPort jedisShardInfo = hps[i];
                String host=jedisShardInfo.getHost();
                int port=jedisShardInfo.getPort();
                try{
                    if(host!=null && port>0){
                        Jedis jedis = new Jedis(host,port);
                        //jedis.del(preKey);
                        Set<String> keys = jedis.keys(preKey);
                        if(Tools.collection.isEmpty(keys)){
                            continue;
                        }
                        for (String key : keys){
                            this.remove(key);
                        }
                        jedis.close();
                    }
                }catch (Exception e){
                    log.error("删除redis缓存错误信息："+e.getMessage());
                }

            }
            /*JedisCluster jedisCluster = (JedisCluster) jedisCommands;
            jedisCluster.del(preKey);*/
            return true;

        }else{
           // ((Jedis)jedisCommands).flushAll();
            removeAll();
        }
        //setRedisKeyPx();
        return true;
    }
    
    /**
     * 初始化非切片池
     */
    private JedisCommands initialPool() {
        //this.hps =readConfig(); 
        HostAndPort host = this.hps[0];
        
        // 池基本配置 
        JedisPoolConfig config = new JedisPoolConfig();
        //异常信息:JedisConnectionException: Could not get a resource from the pool
        //config.setMaxActive(20);//设置20时，已发生几次不够用的异常了,150也经常不够用
        //config.setMaxWait(1000l);
        config.setMaxTotal(4500);//最大连接数，新版的api maxActive  ==>  maxTotal 
        config.setMaxIdle(50);  //最大空闲连接数, 默认8个
        config.setMaxWaitMillis(1000l);//最大等待时间 新版的api maxWait ==> maxWaitMillis
        config.setTestOnBorrow(false);//192.168.1.203:6000
        
        log.info("使用单例的redis:" + host.toString());
        int timeout =Protocol.DEFAULT_TIMEOUT ;
        //JedisPool pool = new JedisPool(config, host.getHost(), host.getPort());
        JedisPool pool = new JedisPool(config, host.getHost(), host.getPort(),timeout,  password);
        JRedisPoolCommands rs = new JRedisPoolCommands(pool);
        return rs;
    }
 

    /** 
     * 初始化切片池 
     */
    private ShardedJedisPool initialShardedPool() {
        // 池基本配置 
        JedisPoolConfig config = new JedisPoolConfig();
        //config.setMaxActive(20);
        config.setMaxTotal(30);
        config.setMaxIdle(20);
        //config.setMaxWait(1000l);
        config.setMaxWaitMillis(5000l);
        config.setTestOnBorrow(true);
        // slave链接 
        
        //hps =readConfig();
        shards = new ArrayList<>(hps.length); 
        for(int i =0 ;i < hps.length;i++){
            JedisShardInfo js = new JedisShardInfo(hps[i].getHost(),hps[i].getPort(),"m_" +(7000+i));
            shards.add(js);
        } 

        // 构造池 
        ShardedJedisPool pool = new ShardedJedisPool(config, shards);
        return pool;
    }

    /**
     * 初始redis集群模式配置
     * 配置格式：
     * redis.server=192.168.8.90:7000,192.168.8.90:7001,192.168.8.90:7002,192.168.8.90:7003,192.168.8.90:7004,192.168.8.90:7005
     */
    private JedisCluster initJedisCluster() {  
        //hps =readConfig();
        Set<HostAndPort> nodes  = new HashSet<>(hps.length);
        for (HostAndPort hp : hps) {
        	nodes.add(hp);
            log.info("redis集群模式:" + hp.toString() );
        }
        
        GenericObjectPoolConfig config=new GenericObjectPoolConfig();
        config.setMaxIdle(30);
        config.setMinIdle(20);
        config.setMaxWaitMillis(50001);
        
        final int timeout =Protocol.DEFAULT_TIMEOUT ;
        final int DEFAULT_MAX_REDIRECTIONS= 5;
        final int soTimeout=timeout;
        
        JedisCluster jc = new JedisCluster(nodes, timeout,soTimeout, DEFAULT_MAX_REDIRECTIONS
                  ,password, config);
        //JedisCluster jc = new JedisCluster(nodes,config); 
        return jc;
    }
    
    /** 读取系统配置*/
    private static HostAndPort[] readConfig(String redisServer){
        //redis.server=192.168.8.90:7000,192.168.8.90:7001,192.168.8.90:7002,192.168.8.90:7003,192.168.8.90:7004,192.168.8.90:7005
        //String redisServer = Tools.config.getValue(D_REDIS_SERVER);
    	//String redisServer = pro.getRedisServer();
    	String[] rds = null;
        if(Tools.string.isNotEmpty(redisServer)){
            rds = redisServer.split(",");
        }
        if(rds == null || rds.length==0){
            redisServer="192.168.8.90:7000,192.168.8.90:7001,192.168.8.90:7002,192.168.8.90:7003,192.168.8.90:7004,192.168.8.90:7005";
            log.error("没有配置redis.server的服务，现替换为:" + redisServer);
            rds = redisServer.split(",");
        }
        HostAndPort[] hps = new HostAndPort[rds.length];
        for(int i =0 ;i < rds.length;i++){
            String[] hp = rds[i].split(":");
            if(hp.length!=2 || !Tools.number.isInteger(hp[1])){
                throw new YMException("Redis服务地址配置不正确,(正确格式是192.168.8.90:7001,192.168.8.91:7001)"  + rds[i]);
            }
            hps[i] = new HostAndPort(hp[0], Integer.parseInt(hp[1]));
        }
        return hps;
    }
    /**是否分片(多个服务器),是否集群模式*/
    public boolean isCluster() {
        return (hps != null || hps.length>0 ? true:false);
    }

    
}
