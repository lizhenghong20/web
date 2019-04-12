package cn.farwalker.waka.cache;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import redis.clients.jedis.BinaryClient.LIST_POSITION;
import redis.clients.jedis.BitPosParams;
import redis.clients.jedis.GeoCoordinate;
import redis.clients.jedis.GeoRadiusResponse;
import redis.clients.jedis.GeoUnit;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;
import redis.clients.jedis.SortingParams;
import redis.clients.jedis.Tuple;
import redis.clients.jedis.params.geo.GeoRadiusParam;
import redis.clients.jedis.params.sortedset.ZAddParams;
import redis.clients.jedis.params.sortedset.ZIncrByParams;
import cn.farwalker.waka.util.YMException;

/**
 * redis的扩展，以兼容接口JedisCommands
 * @author juno
 *
 */
class JRedisPoolCommands implements JedisCommands{
    private JedisPool pools;
    public JRedisPoolCommands(JedisPool pool){
        pools = pool;
    }
    private void close(Jedis jedis){
        if(jedis!=null){ 
            jedis.close();
        }
    }
    public void flushAll(){
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            jedis.flushAll();
        }
        finally  {
            close(jedis);
        }
    }
    @Override
    public Long append(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Long rs = jedis.append(key, value);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Long bitcount(String key) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Long rs = jedis.bitcount(key );
            return rs;
        }
        finally  {
            close(jedis);
        }
    }
    @Override
    public String set(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            String rs = jedis.set(key, value);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public String set(String key, String value, String nxxx, String expx, long time) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            String rs = jedis.set(key, value,  nxxx,  expx,  time);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public String set(String key, String value, String nxxx) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            String rs = jedis.set(key, value,  nxxx );
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public String get(String key) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            String rs = jedis.get(key );
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Boolean exists(String key) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Boolean rs = jedis.exists(key );
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Long persist(String key) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Long rs = jedis.persist(key );
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public String type(String key) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            String rs = jedis.type(key );
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Long expire(String key, int seconds) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Long rs = jedis.expire(key ,seconds);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Long pexpire(String key, long milliseconds) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Long rs = jedis.pexpire(key ,milliseconds);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Long expireAt(String key, long unixTime) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Long rs = jedis.expireAt(key ,unixTime);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Long pexpireAt(String key, long millisecondsTimestamp) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Long rs = jedis.pexpireAt(key ,millisecondsTimestamp);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Long ttl(String key) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Long rs = jedis.ttl(key  );
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Long pttl(String key) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Long rs = jedis.pttl(key  );
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Boolean setbit(String key, long offset, boolean value) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Boolean rs = jedis.setbit(key ,offset,   value );
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Boolean setbit(String key, long offset, String value) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Boolean rs = jedis.setbit(key ,offset,   value );
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Boolean getbit(String key, long offset) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Boolean rs = jedis.getbit(key ,offset );
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Long setrange(String key, long offset, String value) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Long rs = jedis.setrange(key ,offset ,value);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public String getrange(String key, long startOffset, long endOffset) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            String rs = jedis.getrange(key ,startOffset ,endOffset);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public String getSet(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            String rs = jedis.getSet(key ,value);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Long setnx(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Long rs = jedis.setnx(key ,value);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public String setex(String key, int seconds, String value) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            String rs = jedis.setex(key,seconds ,value);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public String psetex(String key, long milliseconds, String value) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            String rs = jedis.psetex(key,milliseconds ,value);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Long decrBy(String key, long integer) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Long rs = jedis.decrBy(key,integer);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Long decr(String key) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Long rs = jedis.decr(key);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Long incrBy(String key, long integer) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Long rs = jedis.incrBy(key,integer);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Double incrByFloat(String key, double value) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Double rs = jedis.incrByFloat(key,value);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Long incr(String key) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Long rs = jedis.incr(key);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Long del(String key) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Long rs = jedis.del(key);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public String substr(String key, int start, int end) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            String rs = jedis.substr(key,start,   end);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Long hset(String key, String field, String value) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Long rs = jedis.hset(key,field, value);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public String hget(String key, String field) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            String rs = jedis.hget(key,field);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Long hsetnx(String key, String field, String value) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Long rs = jedis.hsetnx(key,field,value);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public String hmset(String key, Map<String, String> hash) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            String rs = jedis.hmset(key,hash );
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public List<String> hmget(String key, String... fields) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            List<String> rs = jedis.hmget(key,fields );
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Long hincrBy(String key, String field, long value) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Long rs = jedis.hincrBy(key,field,value);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Double hincrByFloat(String key, String field, double value) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Double rs = jedis.hincrByFloat( key,  field,  value);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Boolean hexists(String key, String field) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Boolean rs = jedis.hexists( key,  field);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Long hdel(String key, String... field) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Long rs = jedis.hdel(key,field);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Long hlen(String key) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Long rs = jedis.hlen(key);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Set<String> hkeys(String key) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Set<String> rs = jedis.hkeys( key);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public List<String> hvals(String key) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            List<String> rs = jedis.hvals( key);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Map<String, String> hgetAll(String key) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Map<String, String> rs = jedis.hgetAll( key);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Long rpush(String key, String... string) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Long rs = jedis.rpush(key,string);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Long lpush(String key, String... string) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Long rs = jedis.lpush(key,string);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Long llen(String key) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Long rs = jedis.llen(key);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public List<String> lrange(String key, long start, long end) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            List< String> rs = jedis.lrange( key,start,  end);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public String ltrim(String key, long start, long end) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            String rs = jedis.ltrim( key,start,  end);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public String lindex(String key, long index) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            String rs = jedis.lindex( key,index);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public String lset(String key, long index, String value) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            String rs = jedis.lset( key,index,value);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Long lrem(String key, long count, String value) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Long rs = jedis.lrem( key,count,value);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public String lpop(String key) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            String rs = jedis.lpop( key );
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public String rpop(String key) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            String rs = jedis.rpop( key );
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Long sadd(String key, String... member) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Long rs = jedis.sadd(key,member);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Set<String> smembers(String key) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Set<String> rs = jedis.smembers(key);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Long srem(String key, String... member) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Long rs = jedis.srem(key,member);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public String spop(String key) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            String rs = jedis.spop( key );
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Set<String> spop(String key, long count) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Set<String>  rs = jedis.spop( key ,count);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Long scard(String key) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Long rs = jedis.scard(key);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Boolean sismember(String key, String member) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Boolean  rs = jedis.sismember( key ,member);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public String srandmember(String key) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            String  rs = jedis.srandmember( key);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public List<String> srandmember(String key, int count) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            List<String>  rs = jedis.srandmember( key,count);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Long strlen(String key) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Long rs = jedis.strlen(key);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Long zadd(String key, double score, String member) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Long rs = jedis.zadd(key,score,  member);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Long zadd(String key, double score, String member, ZAddParams params) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Long rs = jedis.zadd(key,score,  member,params);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Long zadd(String key, Map<String, Double> scoreMembers) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Long rs = jedis.zadd(key,scoreMembers);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Long zadd(String key, Map<String, Double> scoreMembers, ZAddParams params) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Long rs = jedis.zadd(key,scoreMembers,params);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Set<String> zrange(String key, long start, long end) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Set<String>  rs = jedis.zrange( key,start, end);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Long zrem(String key, String... member) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Long rs = jedis.zrem(key,member);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Double zincrby(String key, double score, String member) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Double rs = jedis.zincrby(key,score,member);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Double zincrby(String key, double score, String member, ZIncrByParams params) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Double rs = jedis.zincrby(key,score,member,params);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Long zrank(String key, String member) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Long rs = jedis.zrank(key,member);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Long zrevrank(String key, String member) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Long rs = jedis.zrevrank(key,member);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Set<String> zrevrange(String key, long start, long end) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Set<String> rs = jedis.zrevrange( key,  start,  end) ;
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Set<Tuple> zrangeWithScores(String key, long start, long end) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Set<Tuple> rs = jedis.zrangeWithScores( key,  start,  end);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Set<Tuple> zrevrangeWithScores(String key, long start, long end) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Set<Tuple> rs = jedis.zrevrangeWithScores(key,start, end);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Long zcard(String key) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Long rs = jedis.zcard(key);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Double zscore(String key, String member) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Double rs = jedis.zscore(key,member);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public List<String> sort(String key) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            List<String> rs = jedis.sort(key);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public List<String> sort(String key, SortingParams sortingParameters) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            List<String> rs = jedis.sort(key ,sortingParameters);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Long zcount(String key, double min, double max) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Long rs = jedis.zcount(key,min,  max);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Long zcount(String key, String min, String max) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Long rs = jedis.zcount(key,min,  max);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Set<String> zrangeByScore(String key, double min, double max) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Set<String> rs = jedis.zrangeByScore(key ,min,max);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Set<String> zrangeByScore(String key, String min, String max) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Set<String> rs = jedis.zrangeByScore(key ,min,max);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Set<String> zrevrangeByScore(String key, double max, double min) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Set<String> rs = jedis.zrevrangeByScore(key,max ,min);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Set<String> zrangeByScore(String key, double min, double max, int offset, int count) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Set<String> rs = jedis.zrangeByScore(key ,min,max,offset, count);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Set<String> zrevrangeByScore(String key, String max, String min) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Set<String> rs = jedis.zrevrangeByScore(key ,max,min);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Set<String> zrangeByScore(String key, String min, String max, int offset, int count) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Set<String> rs = jedis.zrangeByScore(key ,min,max, offset,  count);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Set<String> zrevrangeByScore(String key, double max, double min, int offset, int count) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Set<String> rs = jedis.zrevrangeByScore(key,max ,min, offset,  count);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Set<Tuple> rs = jedis.zrangeByScoreWithScores(key ,  min,max);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Set<Tuple> rs = jedis.zrevrangeByScoreWithScores(key ,max,  min);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max, int offset, int count) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Set<Tuple> rs = jedis.zrangeByScoreWithScores(key ,  min,max,  offset,  count);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Set<String> zrevrangeByScore(String key, String max, String min, int offset, int count) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Set<String> rs = jedis.zrevrangeByScore(key ,max,  min,  offset,  count);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(String key, String min, String max) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Set<Tuple> rs = jedis.zrangeByScoreWithScores(key ,min,  max);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(String key, String max, String min) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Set<Tuple> rs = jedis.zrevrangeByScoreWithScores(key,  max ,min);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(String key, String min, String max, int offset, int count) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Set<Tuple> rs = jedis.zrangeByScoreWithScores(key,  min,max ,offset, count);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min, int offset, int count) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Set<Tuple> rs = jedis.zrevrangeByScoreWithScores(key,  max ,min,offset, count);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(String key, String max, String min, int offset, int count) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Set<Tuple> rs = jedis.zrevrangeByScoreWithScores(key,  max ,min,offset, count);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Long zremrangeByRank(String key, long start, long end) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Long rs = jedis.zremrangeByRank(key,start, end);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Long zremrangeByScore(String key, double start, double end) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Long rs = jedis.zremrangeByScore(key,start, end);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Long zremrangeByScore(String key, String start, String end) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Long rs = jedis.zremrangeByScore(key,start, end);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Long zlexcount(String key, String min, String max) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Long rs = jedis.zlexcount(key,min, max);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Set<String> zrangeByLex(String key, String min, String max) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Set<String> rs = jedis.zrangeByLex(key, min, max );
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Set<String> zrangeByLex(String key, String min, String max, int offset, int count) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Set<String> rs = jedis.zrangeByLex(key, min, max ,offset, count);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Set<String> zrevrangeByLex(String key, String max, String min) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Set<String> rs = jedis.zrevrangeByLex(key, min, max );
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Set<String> zrevrangeByLex(String key, String max, String min, int offset, int count) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Set<String> rs = jedis.zrevrangeByLex(key, min, max,offset,  count);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Long zremrangeByLex(String key, String min, String max) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Long rs = jedis.zremrangeByLex(key, min, max);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Long linsert(String key, LIST_POSITION where, String pivot, String value) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Long rs = jedis.linsert(key, where,  pivot,  value);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Long lpushx(String key, String... string) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Long rs = jedis.lpushx(key, string);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Long rpushx(String key, String... string) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Long rs = jedis.rpushx(key, string);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }
    @Deprecated
    @Override
    public List<String> blpop(String key) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            List<String> rs = jedis.blpop(key);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public List<String> blpop(int timeout, String key) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            List<String> rs = jedis.blpop(timeout,key);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }
    
    @Deprecated
    @Override
    public List<String> brpop(String key) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            List<String> rs = jedis.brpop(key);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public List<String> brpop(int timeout, String key) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            List<String> rs = jedis.brpop(timeout,  key);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    

    @Override
    public String echo(String string) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            String rs = jedis.echo(string);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Long move(String key, int dbIndex) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Long rs = jedis.move(key, dbIndex);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

 
    @Override
    public Long bitcount(String key, long start, long end) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Long rs = jedis.bitcount(key, start,   end);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Long bitpos(String key, boolean value) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Long rs = jedis.bitpos(key, value);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Long bitpos(String key, boolean value, BitPosParams params) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Long rs = jedis.bitpos(key, value,params);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }
    @Deprecated
    @Override
    public ScanResult<Entry<String, String>> hscan(String key, int cursor) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            ScanResult<Entry<String, String>> rs = jedis.hscan(key,cursor);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }
    @Deprecated
    @Override
    public ScanResult<String> sscan(String key, int cursor) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            ScanResult<String> rs = jedis.sscan(key,cursor);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }
    @Deprecated
    @Override
    public ScanResult<Tuple> zscan(String key, int cursor) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            ScanResult<Tuple> rs = jedis.zscan(key,cursor);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public ScanResult<Entry<String, String>> hscan(String key, String cursor) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            ScanResult<Entry<String, String>> rs = jedis.hscan(key,cursor);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public ScanResult<Entry<String, String>> hscan(String key, String cursor, ScanParams params) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            ScanResult<Entry<String, String>> rs = jedis.hscan(key,cursor, params);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public ScanResult<String> sscan(String key, String cursor) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            ScanResult<String> rs = jedis.sscan(key,cursor);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public ScanResult<String> sscan(String key, String cursor, ScanParams params) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            ScanResult<String> rs = jedis.sscan(key,cursor, params);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public ScanResult<Tuple> zscan(String key, String cursor) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            ScanResult<Tuple> rs = jedis.zscan(key,cursor);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public ScanResult<Tuple> zscan(String key, String cursor, ScanParams params) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            ScanResult<Tuple> rs = jedis.zscan(key,cursor, params);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Long pfadd(String key, String... elements) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Long rs = jedis.pfadd(key,elements);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public long pfcount(String key) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            long rs = jedis.pfcount(key);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Long geoadd(String key, double longitude, double latitude, String member) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Long rs = jedis.geoadd(key,longitude,  latitude,  member);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Long geoadd(String key, Map<String, GeoCoordinate> memberCoordinateMap) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Long rs = jedis.geoadd(key,memberCoordinateMap);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Double geodist(String key, String member1, String member2) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Double rs = jedis.geodist(key,member1,  member2);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public Double geodist(String key, String member1, String member2, GeoUnit unit) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            Double rs = jedis.geodist(key,member1,  member2,  unit);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public List<String> geohash(String key, String... members) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            List<String> rs = jedis.geohash(key,members);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public List<GeoCoordinate> geopos(String key, String... members) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            List<GeoCoordinate> rs = jedis.geopos(key,members);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public List<GeoRadiusResponse> georadius(String key, double longitude, double latitude, double radius, GeoUnit unit) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            List<GeoRadiusResponse> rs = jedis.georadius(key,longitude,  latitude,  radius, unit);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public List<GeoRadiusResponse> georadius(String key, double longitude, double latitude, double radius,
            GeoUnit unit, GeoRadiusParam param) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            List<GeoRadiusResponse> rs = jedis.georadius(key,longitude,  latitude,  radius, unit,  param);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public List<GeoRadiusResponse> georadiusByMember(String key, String member, double radius, GeoUnit unit) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            List<GeoRadiusResponse> rs = jedis.georadiusByMember(key,member,radius,  unit);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }

    @Override
    public List<GeoRadiusResponse> georadiusByMember(String key, String member, double radius, GeoUnit unit,
            GeoRadiusParam param) {
        Jedis jedis = null;
        try {
            jedis = pools.getResource();
            List<GeoRadiusResponse> rs = jedis.georadiusByMember(key,member,radius,  unit, param);
            return rs;
        }
        finally  {
            close(jedis);
        }
    }
	@Override
	public List<Long> bitfield(String arg0, String... arg1) {
		throw new YMException("redis 2.9新增的方法，没有实现"); 
	}
    
}
