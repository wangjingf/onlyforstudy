package filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class CORSFilter implements Filter {

	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub

	}
	private static final Logger logger = Logger.getLogger(CORSFilter.class);
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
	
		HttpServletRequest req  = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		logger.info(" Origin:"+req.getHeader("Origin"));
		System.out.println("==========begin dump===========");
		logger.info("requstURI is ::"+ req.getRequestURI());
		if(req.getHeader("Origin")!=null){
			logger.info("find Origin:"+req.getHeader("Origin"));
			resp.addHeader("Access-Control-Allow-Origin", "http://localhost:8080");//*
			resp.addHeader("Access-Control-Allow-Credentials", "true");
			resp.addHeader("Access-Control-Allow-Headers", "Authorization");
		}
		chain.doFilter(request, response);
	}

	public void destroy() {
		// TODO Auto-generated method stub

	}
	private String accessControlAllowOrigins;
}	
