package ssl;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.io.IOUtils;

public class TestConnectHttps {

	public static void main(String[] args) throws IOException, KeyManagementException, NoSuchAlgorithmException {
		System.out.println(sendGetRequest("https://localhost:8443/cxf/index.jsp"));;
	}
	public static String sendGetRequest(String urlString) throws NoSuchAlgorithmException, KeyManagementException, IOException{
		SSLContext sslContext = SSLContext.getInstance("SSL");
		sslContext.init(null, new TrustManager[]{myX509TrustManager}, null);
		URL url = new URL(urlString);
		HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
		connection.setSSLSocketFactory(sslContext.getSocketFactory());
		InputStream in = connection.getInputStream();
		return IOUtils.toString(in);
	}
	private static final TrustManager myX509TrustManager = new X509TrustManager() {
		
		@Override
		public X509Certificate[] getAcceptedIssuers() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
			// TODO Auto-generated method stub
			
		}
	};
}
