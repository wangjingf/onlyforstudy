package xapp.web.listener;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class UserLogoutSessionListener  implements HttpSessionListener{

	@Override
	public void sessionCreated(HttpSessionEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
		HttpSession session =  event.getSession();
		System.out.println(" the expired session is ::"  +session.getId());
		System.out.println(" the expired userName is ::"  +session.getAttribute("userName"));
	}

}
