package cn.farwalker.waka.cache;

import java.io.Serializable;

import cn.farwalker.waka.cache.CacheFactory.CacheType;
import cn.farwalker.waka.cache.CacheRefresh.ICacheRegister;
 
 
/**
 * 不要在biz里使用cache，因为每次进入spring管理的方法，都会请求一个jdbc的connection，很容易就把connection pools用完
 * 如果这个biz不启用事务则没问题
 * 取消key的失效时间原因：
 * public void putObject(String key,Serializable value,int seconds);
 * 由于缓存器本身就支持超过，另外，如果业务需要失效判断时，再存多一个最后更新时间，就可以达到超时的效果
 * 
 * 缓存已实现清除控制(共享缓存清除时，本地也一起清除)
 * @author juno
 *
 */
public interface CacheManager extends ICacheRegister{
    public CacheManager cache = CacheFactory.createCache();// new JRedisCache();
    
    /**
     * 只保存在本地，只是当前的应用使用,不上传到redis服务器,(虽然数据不在服务，清除缓存时，但同步清除的)
     * @param key 键
     * @param value 可以为null,为null则变成remove方法
     */
    public void put(String key,String value);
    
    /**
     * 只保存在本地，只是当前的应用使用,(虽然数据不在服务，清除缓存时，但同步清除的)
     * @param key 键
     * @param value 可以为null,为null则变成remove方法
     * @param share 对象保存本地或者保存到redis服务器
     */
    //public void put(String key,String value,boolean share);

    
    /**
     * 保存到共享区，只要共享的key前缀相同，所有的服务器都可以获取
     * <br/>能用本 地缓存就尽量用本地，因为网络的缓存配置及对像还原都很耗时间
     * @param key键
     * @param value 可以为null,为null则变成remove方法
     */
    public void putShare(String key,String value);
 
    /**
     * 按键取本地值
     * @param key 键
     * @return 对应的值
     */
    public String get(String key);
    /**
     * 按键取共享值
     * @param key 键
     * @return 对应的值
     */
    public String getShare(String key);
    
    /**清除具体key的值 ,不能为空*/
    public boolean remove(String key);
    /**清除具体key的值, 不能为空*/
    public boolean removeShare(String key);
    
    
    //public boolean removeAll();
    /**
     * 清除指定前缀的本地缓存,如果为空，则全清<br/>
     * 共享缓存清除时，本地也一起清除
     * @param preKey 可以为空
     * @return
     */
    public boolean removeAll(String preKey);
    //public boolean removeShareAll();
    /**
     * 清除指定前缀的分布缓存,如果为空，则全清
     * @param preKey 可以为空
     * @return
     */
    public boolean removeShareAll(String preKey);
    
    /**
     * 只保存在本地，只是当前的应用使用,不上传到redis服务器,(虽然数据不在服务，清除缓存时，但同步清除的)<br/>
     * 如果需要对象实例共享，则使用putObject(String key,Serializable value,boolean share);<br/>
     * <br/>
     * 关于序列化的问题：<br/>
     * 
     * 序列化对象，如果是明确的对象:(写入,读取都明确具体类的情况)，最好用json序列化<br/>
     * 因为java序列化时对对象进行图遍历，并且要保存主类及各属性的class信息,<br/>
     * 这过程很耗性能,也是java序列化比json-smart效率低的原因,但json没法准确还原List,Map等泛型对象<br/>
     * 所以如果对象类型不明确的时候，就不能使用json序列化<br/>
     * java序列化前6位是：int(172,237,0,5,116,0)或byte(-84,-19,0,5,116,0)
     * @param key
     * @param value 可以为null,为null则变成remove方法
     */
    public void putObject(String key,Serializable value);
    
    /**
     * 只保存在本地，只是当前的应用使用,不上传到redis服务器,(虽然数据不在服务，清除缓存时，但同步清除的)
     * 有些对象本来不应该共享访问的，但是想同步清除。
     * @param key
     * @param value 可以为null,为null则变成remove方法
     * @param share 对象保存本地或者保存到redis服务器,建议不要共享对象，反序列很耗时
     */
    //public void putObject(String key,Serializable value,boolean share);
    
    /**
     * 对象是序列化保存到服务器，所以效率很差，对像是全网共用的。
     * <br/>能用本地缓存就尽量用本地，因为网络的缓存配置及对像还原都很耗时间
     * <br/>json反序列化对于泛型的集合并不是十分适用，java自身的序列化又比较沉重比较慢，也不是很理想
     * <br/>权衡后，本方法使用json序列化，对于 泛型的集合 的对象，要<span style='color:red'>注意使用</span>
     * <br/>简单的Bo，泛型的集合都已经精确还原了(可以放心使用List,Map)
     * <br/>复杂的类型：AddressSupportTreeVo ，json就不能返序列化,因为children没有set方法
     * @param key
     * @param value 可以为null,为null则变成remove方法
     */
    public void putObjectShare(String key,Serializable value);
    

    /** 本地缓存对象*/
    public <T> T  getObject(String key);
    /** 远程缓存对象*/
    public <T> T getObjectShare(String key);
    
    /** 关闭缓存*/
    public void close();
    public CacheType getCacheType();

}
/** 这个类型，json就不能返序列化,因为children没有set方法
public static class AddressSupportTreeVo extends AddressBo{
    private static final long serialVersionUID = 4543140414352530438L;
    private final Map<String,AddressSupportTreeVo> children ; 
    public AddressSupportTreeVo(){ 
        children = new TreeMap<>();
    }
    public Collection<AddressSupportTreeVo> getChildren() {
        return children.values();
    }
    
    public boolean add(AddressSupportTreeVo vo){
        String id = AddressCache.getParentId(vo.getId());
        if(Tools.string.isAllEmpty(id,this.getId()) || id.equalsIgnoreCase(this.getId())
                 || (AddressCache.checkNoArea(id))){
            children.put(vo.getId(), vo);
            return true;
        }
        else{
            return false;
        }
    }

 
    public AddressSupportTreeVo findChildren(List<String> path){
        int size = path.size();
        if(size ==0){
            return null;
        }
        String curpath = path.get(0) ;
        AddressSupportTreeVo curVo = children.get(curpath);  
        if(size == 1 || curVo == null){//末级或找不到
            return curVo ;
        }
        else {
            List<String> subpath = path.subList(1, size);
            AddressSupportTreeVo subs = curVo.findChildren(subpath);
            return subs;
        }
    }
    @Override
    public String toString(){
        return getId() +"-" + getName();
    }
}*/


