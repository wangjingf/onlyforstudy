package study.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import junit.framework.TestCase;
import xapp.web.entity.User;

public class SecondLevelCacheTest extends TestCase {
	private ApplicationContext context;
	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		super.setUp();
		try{
			context = new ClassPathXmlApplicationContext("context-servlet.xml");
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	public void testCache(){
		try {
			SessionFactory sessionFactory = (SessionFactory) context.getBean("sessionFactory");
			Session session = sessionFactory.openSession();
			
			System.out.println("before load 111!");
			User wjf =  (User) session.load(User.class, 1);
			System.out.println(wjf.getName());
			session.close();
			Session session2 = sessionFactory.openSession();
			System.out.println("before load!");
			User wjf1 =  (User) session2.load(User.class, 1);
			System.out.println("loaded wjf is " + wjf1.getName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	@Override
	protected void tearDown() throws Exception {
		// TODO Auto-generated method stub
		super.tearDown();
	}
	
}
