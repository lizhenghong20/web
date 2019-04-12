package cn.farwalker.waka.cache;



/**
 * 
  * 类名      MemcachedUtil.java
  * 
  * add	仅当存储空间中不存在键相同的数据时才保存
  * replace	仅当存储空间中存在键相同的数据时才保存
  * set	与add和replace不同，无论何时都保存
  * 一次取得多条数据时使用get_multi。get_multi可以非同步地同时取得多个键值，其速度要比循环调用get快数十倍
  * 
  * 创建日期 2014年6月9日
  * 作者  zhangx
 */
public class MemcachedClient { 

}
