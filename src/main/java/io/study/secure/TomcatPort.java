package io.study.secure;

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
import javax.management.MBeanServerFactory;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.Query;
import javax.management.ReflectionException;

public class TomcatPort {
	/**
	 * ����Э���scheme��ȡ����˿ں�
	 * 
	 * @return �˿ں�
	 * @throws ReflectionException
	 * @throws MBeanException
	 * @throws InstanceNotFoundException
	 * @throws AttributeNotFoundException
	 */
	private static String getHttpPort(String protocol, String scheme)
			throws AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException {
		MBeanServer mBeanServer = null;
		if (MBeanServerFactory.findMBeanServer(null).size() > 0) {
			mBeanServer = (MBeanServer) MBeanServerFactory.findMBeanServer(null).get(0);
		}

		Set names = null;
		try {
			names = mBeanServer.queryNames(new ObjectName("Catalina:type=Connector,*"), null);
		} catch (Exception e) {
			return "";
		}

		Iterator it = names.iterator();
		ObjectName oname = null;
		while (it.hasNext()) {
			oname = (ObjectName) it.next();
			String pvalue = (String) mBeanServer.getAttribute(oname, "protocol");
			String svalue = (String) mBeanServer.getAttribute(oname, "scheme");
			if (protocol.equals(pvalue) && scheme.equals(svalue)) {
				return ((Integer) mBeanServer.getAttribute(oname, "port")).toString();
			}
		}

		return "";
	}

  static	List<String>  getEndPoints() throws MalformedObjectNameException, NullPointerException, AttributeNotFoundException,
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
	public static void main(String[] args) throws MalformedObjectNameException, AttributeNotFoundException, InstanceNotFoundException, NullPointerException, MBeanException, ReflectionException, UnknownHostException{
		List<String> result =  getEndPoints();
		System.out.println(result);
		String port = getHttpPort("HTTP/1.1","http");
				System.out.println(port);
	}
}
