package cn.farwalker.waka.cache;
import cn.farwalker.waka.util.Tools;

/**
 * Created by Administrator on 2016/4/21.
 */
class  CacheFactory {


    public static CacheManager createCache(){
    	CacheProperties pro = Tools.springContext.getBean(CacheProperties.class);
        CacheManager rs = null;
        if(pro.isNetCache()){
            String cacheBranch = pro.getBranchKey();
            String cacheShare= pro.getShareKey();
            rs = new JRedisCacheImp(cacheBranch,cacheShare);
        }else{// 或者 ehcache.equals(value)
            //默认Ehcache
            rs= new EhCacheImp();
        }
        return rs;
    }
    
    enum CacheType { 
        reids("redis缓存"),
        ehcache("ehcache缓存");

        private String text;

        private CacheType(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }

    }

}
