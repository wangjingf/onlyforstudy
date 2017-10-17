package filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.session.Session;
import org.springframework.session.SessionRepository;

public class SpringSessionFilter implements Filter {
	private SessionRepository<Session> sessionRepository;
	
	public SessionRepository<Session> getSessionRepository() {
		return sessionRepository;
	}

	public void setSessionRepository(SessionRepository<Session> sessionRepository) {
		this.sessionRepository = sessionRepository;
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		chain.doFilter(request, response);
		doFilterInternal();
	}

	private void doFilterInternal() {
		
	}
	private void commitSession(){
	}
	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

}
