package cache;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class EhcaheTest {
	public static final CacheManager cacheManager = new CacheManager();
	private static Cache cache;
	public static Cache getCache(){
		if(cache == null){
			cache = cacheManager.getCache("ehcacheName");
		}
		return cache;
	}
	public static void setCacheElement(String name,String value){
		Element element = new Element(name,value);
		getCache().put(element);
	}
	public static Object getCacheElement(String key){
		Element element = cache.get(key);
		if(element == null){
			return null;
		}
		return element.getValue();
	}
	public static void main(String[] args){
		//ApplicationContext context = new ClassPathXmlApplicationContext("context-servlet.xml");
		setCacheElement("wjf", "123");
		System.out.println(getCacheElement("wjf"));
		getCache().flush();
	}
}
