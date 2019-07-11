package thread;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import com.ctc.wstx.util.WordSet;

public class TestConcurrentHashMap {

	public static void main(String[] args) throws InterruptedException {
		Thread[] threads = new Thread[100];
		for (int i = 0; i < 100; i++) {
			threads[i] = new Thread(new Runnable() {
				
				@Override
				public void run() {
					for(int j=0;j<500;j++){
						increase("hello");
						increase1("hello");
					}
				}
			});
			threads[i].start();
		}
		for (int i = 0; i < threads.length; i++) {
			threads[i].join();
		}
		System.out.println(wordCounts.get("hello"));
		System.out.println(wordCounts1.get("hello"));
		System.out.println(wordCounts2.get("hello"));
	}
    private static final ConcurrentMap<String, Long> wordCounts = new ConcurrentHashMap<>();
    private static final ConcurrentMap<String, AtomicLong> wordCounts1 = new ConcurrentHashMap<>();
    private static final ConcurrentMap<String, Long> wordCounts2 = new ConcurrentHashMap<>();  
    /**
     * 之间会相互覆盖
     * @param word
     * @return
     */
    public static long increase(String word) {  
    	Long newValue = null;
        while(true) {  
        	Long   oldValue = wordCounts.get(word);  
            if(oldValue == null) {  
                // Add the word firstly, initial the value as 1  
            		 oldValue = wordCounts.get(word);  
            	 newValue = 1L;  
                if(wordCounts.putIfAbsent(word, newValue) == null) {//等于空说明当前操作已经被别人完成了呗，继续下一次操作  
                    break;  
                }  
            }else{  
            	  newValue = oldValue + 1;  
                if(wordCounts.replace(word, oldValue, newValue)) {  //可能读取过后已经被别人获取到了呢
                    break;  
                }  
            }  
        }  
        return newValue;  
    } 
    public static long increase1(String word) {  
        AtomicLong number = wordCounts1.get(word);  
        if(number == null) {  //多线程同时添加一个不存在的对象时可能会创建多个newNumber对象
            AtomicLong newNumber = new AtomicLong(0);  
            number = wordCounts1.putIfAbsent(word, newNumber);  
            if(number == null) {  
                number = newNumber;  
            }  
        }  
        return number.incrementAndGet();  
    }  
}
