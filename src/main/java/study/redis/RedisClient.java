package study.redis;

import java.util.ArrayList;
import java.util.List;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;



public class RedisClient {

    private Jedis jedis;//����Ƭ��ͻ�������
    private JedisPool jedisPool;//����Ƭ���ӳ�
    private ShardedJedis shardedJedis;//��Ƭ��ͻ�������
    private ShardedJedisPool shardedJedisPool;//��Ƭ���ӳ�
    
    public RedisClient() 
    { 
        initialPool(); 
        initialShardedPool(); 
        shardedJedis = shardedJedisPool.getResource(); 
        jedis = jedisPool.getResource(); 
        
        
    } 
 
    /**
     * ��ʼ������Ƭ��
     */
    private void initialPool() 
    { 
        // �ػ������� 
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(5); 
        config.setMaxWaitMillis(1000L); 
        config.setTestOnBorrow(false); 
        
        jedisPool = new JedisPool(config,"127.0.0.1",6379);
    }
    
    /** 
     * ��ʼ����Ƭ�� 
     */ 
    private void initialShardedPool() 
    { 
        // �ػ������� 
        JedisPoolConfig config = new JedisPoolConfig(); 
        config.setMaxIdle(5); 
        config.setMaxWaitMillis(1000L);
        config.setTestOnBorrow(false); 
        // slave���� 
        List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>(); 
        shards.add(new JedisShardInfo("127.0.0.1", 6379, "master")); 

        // ����� 
        shardedJedisPool = new ShardedJedisPool(config, shards); 
    } 

    public void show() {     
        KeyOperate(); 
        StringOperate(); 
        ListOperate(); 
        SetOperate();
        SortedSetOperate();
        HashOperate(); 
        jedis = jedisPool.getResource();
        shardedJedis = shardedJedisPool.getResource();
    } 

      private void KeyOperate() {
      }

      private void StringOperate() {
      }

      private void ListOperate() {
      }

      private void SetOperate() {
      }

      private void SortedSetOperate() {
      }
    
      private void HashOperate() {
      }
}