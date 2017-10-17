import java.util.List;
import java.util.Map;

import junit.framework.TestCase;
import redis.clients.jedis.Jedis;

public class TestRedis  extends TestCase {
	private static final Jedis jedis = new Jedis("localhost");;
	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		super.setUp();
	}
	public static void main(String[] args) {
		  //连接本地的 Redis 服务
	      Jedis jedis = new Jedis("localhost");
	      System.out.println("Server is running: "+jedis.ping());

	      jedis.set("wjf", "123");
	      System.out.println("Connection to server sucessfully");
	      System.out.println(jedis.get("wjf"));
	}
	public void testSetGet(){
		jedis.set("wjf", "123");
		System.out.println(jedis.get("123"));
	}
	public void testJedisList(){
		jedis.lpush("list01", "redis1");
		jedis.lpush("list01", "redis2");
		jedis.rpush("list01", "redis3");
		List<String> list = jedis.lrange("list01", 0, 5);
		for(String item : list){
			System.out.println(item);
		}
	}
	public void testReadRedisSet(){
		Map<String,String> result = jedis.hgetAll("spring:session:sessions:ff982a4d-7b47-4eb3-b067-4fd9c0402fa4");
		System.out.println("result is :" + result);
	}
	public void testSet(){
		jedis.sadd("workgroup", "workmember1","workmember2","workmember3");
		jedis.sadd("target", "target1");
		jedis.smembers("workgroup");//列出所有成员
		jedis.srem("workgroup", "workmember1");//移除一些成员
		jedis.scard("workgroup");//返回元素的size
		jedis.srandmember("workgroup",2);//随机返回1个或多个数字
		jedis.spop("workgroup");//随机的移除一些元素
		jedis.smove("workgroup", "target", "workmember1");//从src移动元素到target
		jedis.sdiff("workgroup","target");//返回存在于第一个group而不存在其他group的元素
		jedis.sdiffstore("savedData","workgroup","target");//将workgroup与target的差集保存到savedData里面去
		jedis.sinter("workgroup","target");//求交集
		jedis.sinter("saved","workgroup","target");//求并集
		jedis.sunion("workgroup","target");//求并集
		jedis.sunionstore("saved","workgroup","target");//求并集
	}
	public void testMap(){
		jedis.hset("map01", "myName","01");//设值
		jedis.hget("map01", "myName");//取值
		jedis.hdel("map01", "myNmae");//删除
		jedis.hlen("map01");//长度
		jedis.hexists("map01", "myNmae");
		jedis.hkeys("map01");//所有的key
		jedis.hvals("map01");//所有的值
		
		
	}
}
