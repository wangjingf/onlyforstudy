package study;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TestUrlConnect {
	public static void main(String[] args) throws InterruptedException, ExecutionException{
		long curr = System.currentTimeMillis();
		Executor executor = Executors.newFixedThreadPool(3);
		List<String> urls = new ArrayList<String>();
		urls.add("http://192.168.5.11:1000/soa");
		urls.add("http://192.168.5.11:1000/soa");
		urls.add("http://192.168.5.11:1000/soa");
		urls.add("http://192.168.5.11:1000/soa");
		urls.add("http://192.168.5.11:1000/soa");
		urls.add("http://192.168.5.11:1000/soa");
		urls.add("http://www.baidu.com");
		//System.out.println(IsUrlValid(urls,10));
		System.out.println(IsUrlValidBetter(urls,1000));
		System.out.println((System.currentTimeMillis()-curr)/1000);
	}
	public static List<Boolean> IsUrlValid(List<String> urls,final int  timeoutInMillion) throws InterruptedException, ExecutionException{
		List<Boolean> ret = new ArrayList<Boolean>();
		final ExecutorService executorService = Executors.newFixedThreadPool(urls.size());
		List<Future<Boolean>> futures = new ArrayList<Future<Boolean>>();
		for(final String urlString : urls){
			Callable<Boolean> task = new Callable<Boolean>() {
				public Boolean call() {
					try {
						URL url = new URL(urlString);
						URLConnection connection = url.openConnection();
						connection.setConnectTimeout(timeoutInMillion);
						connection.connect();
					} catch (IOException e) {
						e.printStackTrace();
						return false;
					}catch (Exception e) {
						//e.printStackTrace();
						return false;
					}
					return true;
				}
			};
			Future<Boolean> future=	executorService.submit(task);
			futures.add(future);
		}
		for (int i = 0; i < urls.size(); i++) {
			Boolean result = futures.get(i).get();//get方法会一直阻塞直到任务完成
			if(result!=null){
				ret.add(result);
			}
		}
		executorService.shutdown();
		return ret;
	}
	public static List<Boolean> IsUrlValidBetter(List<String> urls,final int  timeoutInMillion) throws InterruptedException, ExecutionException{
		List<Boolean> ret = new ArrayList<Boolean>();
		final ExecutorService executorService = Executors.newFixedThreadPool(urls.size());
		ExecutorCompletionService<Boolean> completionService  =  new ExecutorCompletionService<Boolean>(executorService);;
		for(final String urlString : urls){
			Callable<Boolean> task = new Callable<Boolean>() {
				public Boolean call() {
					try {
						URL url = new URL(urlString);
						URLConnection connection = url.openConnection();
						connection.setConnectTimeout(timeoutInMillion);
						connection.connect();
					} catch (IOException e) {
						e.printStackTrace();
						return false;
					}catch (Exception e) {
						//e.printStackTrace();
						return false;
					}
					return true;
				}
			};
			
			completionService.submit(task);
		}
		for (int i = 0; i < urls.size(); i++) {
			Future<Boolean> future = completionService.take();//completionService完成的是哪一个?可通过一个对象在存储url对象不就行了？
				ret.add(future.get());
		}
		executorService.shutdown();
		return ret;
	}
	public static  boolean isUrlValid(String urlString){
		URL url = null;
		try {
			url = new URL(urlString);
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		URLConnection connection;
		
		try {
			connection = url.openConnection();
			connection.setConnectTimeout(1*1000);
			connection.connect();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}catch (Exception e) {
			//e.printStackTrace();
			return false;
		}
		return true;
	}
}
