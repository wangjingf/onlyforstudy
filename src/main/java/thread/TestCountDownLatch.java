package thread;

import java.util.concurrent.CountDownLatch;

public class TestCountDownLatch {
	public  void startThread(){
		for(int i = 0;i<3;i++){
			Thread t = new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					latch.countDown();
				}
			});
			t.start();
			
		}
	}
	public CountDownLatch getLatch(){
		return latch;
	}
	private static final CountDownLatch latch = new CountDownLatch(3);
	public static void main(String[] args) throws InterruptedException {
		long now = System.currentTimeMillis();
		new TestCountDownLatch().startThread();
		latch.await();//阻塞直到count为0,count is 0 the status is fixed
		System.out.println(System.currentTimeMillis() - now);
	}

}
