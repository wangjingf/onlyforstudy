package io.study.gateway.base;

import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.study.gateway.balance.BalancePolicy;
import io.study.gateway.config.ApiConfig;
import io.study.gateway.config.HostConfig;
import io.study.gateway.config.ProxyConfig;
import io.study.gateway.interceptor.IFilter;
import io.study.gateway.proxy.ProxyContext;
import io.study.gateway.proxy.ProxyProtocol;
import io.study.gateway.proxy.ProxyServer;
import io.study.gateway.proxy.ProxyService;
import io.study.gateway.registry.LocalRegistry;
import io.study.gateway.server.ProxyResponseMap;
import io.study.gateway.server.SimpleHttpServer;
import junit.framework.TestCase;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class BaseTestCase extends TestCase {
    SimpleHttpServer server1 = null;
    SimpleHttpServer server2 = null;
    ProxyServer proxyServer = null;
    LocalRegistry registry = null;
    private OkHttpClient okHttpClient = new OkHttpClient();
    private ApiConfig newApiConfig(){
        ApiConfig apiConfig = new ApiConfig();

        apiConfig.setSrcUri("/json");
        apiConfig.setDestUri("/json");
        HostConfig hostConfig = new HostConfig(new InetSocketAddress("127.0.0.1",8070));
        HostConfig hostConfig1 = new HostConfig(new InetSocketAddress("127.0.0.1",8060));
        List<HostConfig> hosts = new ArrayList<>();
        hosts.add(hostConfig);
        hosts.add(hostConfig1);
        apiConfig.setActiveAddresses(hosts);
        return apiConfig;
    }
    @Override
    public void setUp() throws Exception {
        registry = new LocalRegistry();
        ProxyConfig proxyConfig = new ProxyConfig();
        proxyConfig.setPolicy(BalancePolicy.LeastActive);
        proxyConfig.setProxyProtocol(ProxyProtocol.Http1_1);
        proxyConfig.setApiConfig("/json",newApiConfig());
        registry.register("com.wjf.proxy",proxyConfig);

        server1 = new SimpleHttpServer(8070, ProxyResponseMap.responseMap);
        server2 = new SimpleHttpServer(8060,ProxyResponseMap.responseMap1);

        ProxyService proxyService = new ProxyService(registry);

        proxyServer = new ProxyServer(proxyService,5121);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    proxyServer.start();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    server1.start();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    server2.start();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }).start();
        super.setUp();
    }
    final String PROXY_JSON_URL =  "http://localhost:5121/com.wjf.proxy/json";
    public void testTrue(){
        assertEquals(1,1);
    }
    public void testProxy(){
        String response = newReq(PROXY_JSON_URL);
        assertEquals("{\"a\":0}",response);

    }
    String newReq(String url){
        Request request = new Request.Builder().url(url).build();

        try {
            Response response = okHttpClient.newCall(request).execute();
            String content = response.body().string();
            return content;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    public void testLeastLoadBalance(){
        for (int i = 0; i < 20; i++) {
            String content = newReq(PROXY_JSON_URL);
            if(i% 2 == 0){
                assertEquals("{\"a\":0}",content);
            }else{
                assertEquals("{\"a\":1}",content);
            }
        }
    }
    public void testReq(){
        Request request = new Request.Builder().url("http://localhost:5121/json").build();

        try {
            Response response = okHttpClient.newCall(request).execute();
            String content = response.body().string();
            System.out.println(content);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    public static void main(String[] args) throws InterruptedException {
        SimpleHttpServer server = new SimpleHttpServer(8070,ProxyResponseMap.responseMap);
        server.start();
    }
}
