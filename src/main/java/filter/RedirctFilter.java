package filter;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.Query;
import javax.management.ReflectionException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RedirctFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		// if(req.getRequestURI().indexOf("index.jsp")!=-1)
		// resp.sendRedirect("corsTest.jsp");
		int port = req.getLocalPort();// 80端口也是可以获取到的
		List<String> results = null;
		try {
			 results  = getEndPoints();
		} catch (MalformedObjectNameException | AttributeNotFoundException | InstanceNotFoundException
				| NullPointerException | MBeanException | ReflectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		chain.doFilter(request, response);
	}

	static List<String> getEndPoints()
			throws MalformedObjectNameException, NullPointerException, AttributeNotFoundException,
			InstanceNotFoundException, MBeanException, ReflectionException, UnknownHostException {
		MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
		Set<ObjectName> objs = mbs.queryNames(new ObjectName("*:type=Connector,*"),
				Query.match(Query.attr("protocol"), Query.value("HTTP/1.1")));
		String hostname = InetAddress.getLocalHost().getHostName();
		InetAddress[] addresses = InetAddress.getAllByName(hostname);
		ArrayList<String> endPoints = new ArrayList<String>();
		for (Iterator<ObjectName> i = objs.iterator(); i.hasNext();) {
			ObjectName obj = i.next();
			String scheme = mbs.getAttribute(obj, "scheme").toString();
			String port = obj.getKeyProperty("port");
			for (InetAddress addr : addresses) {
				String host = addr.getHostAddress();
				String ep = scheme + "://" + host + ":" + port;
				endPoints.add(ep);
			}
		}
		return endPoints;
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

}
