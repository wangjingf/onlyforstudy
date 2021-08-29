package io.study.gateway;

import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.util.concurrent.Promise;
import io.study.gateway.balance.BalancePolicy;
import io.study.gateway.base.BaseTestCase;
import io.study.gateway.client.ConnectionPoolConfig;
import io.study.gateway.config.*;
import io.study.gateway.interceptor.FilterLoader;
import io.study.gateway.interceptor.IFilter;
import io.study.gateway.interceptor.IFilterChain;
import io.study.gateway.interceptor.impl.AuthFilter;
import io.study.gateway.interceptor.impl.StatFilter;
import io.study.gateway.message.http.HttpResponseInfo;
import io.study.gateway.proxy.ProxyFilter;
import io.study.gateway.proxy.ProxyProtocol;
import io.study.gateway.proxy.ProxyServer;

import io.study.gateway.proxy.StreamContext;
import io.study.gateway.registry.LocalRegistry;
import io.study.gateway.server.ProxyResponseMap;
import io.study.gateway.server.SimpleHttpServer;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class TestProxy extends BaseTestCase {
    SimpleHttpServer server1 = null;
    SimpleHttpServer server2 = null;
    ProxyServer proxyServer = null;
    LocalRegistry registry = null;
    private OkHttpClient okHttpClient = new OkHttpClient();

    GatewaySetting gatewaySetting(){
        GatewaySetting setting = new GatewaySetting();
        setting.setPort(5121);
        return setting;
    }
    ApiConfig newApi(String uri,ProxyProtocol proxyProtocol){
        ApiConfig config = new ApiConfig();
        config.setSrcUri("/html");
        config.setDestUri("html");
        config.setPolicy(BalancePolicy.LeastActive);
        config.setProxyProtocol(proxyProtocol);
        config.setTimeout(1000);
        INode server1 = new ServerNode(new InetSocketAddress("127.0.0.1",8070));
        INode server2 = new ServerNode(new InetSocketAddress("127.0.0.1",8060));
        List<INode> hosts = new ArrayList<>();
        hosts.add(server1);
        hosts.add(server2);
        config.setServers(hosts);
        return config;
    }
    @Override
    public void setUp() throws Exception {
        registry = new LocalRegistry();



        registry.register("com.wjf.proxy",newApi("/html",ProxyProtocol.Http1_1));
        registry.register("com.wjf.proxy",newApi("/json",ProxyProtocol.Rpc));

        server1 = new SimpleHttpServer(8070,ProxyResponseMap.responseMap);
        server2 = new SimpleHttpServer(8060,ProxyResponseMap.responseMap1);
        FilterLoader filterLoader = new FilterLoader();
        ConnectionPoolConfig poolConfig = new ConnectionPoolConfig();
        GatewaySetting gatewaySetting = gatewaySetting();

        ProxyFilter proxyFilter = new ProxyFilter(gatewaySetting,poolConfig,registry);
         StatFilter statFilter = new StatFilter(registry);
        filterLoader.addLast(statFilter);
        filterLoader.addLast(proxyFilter);

        proxyServer =  new ProxyServer(registry,gatewaySetting,filterLoader);
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
        registry.start();
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
    public void testHttp(){
        while (true);
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
    private IFilter newAuthFilter(){
        return new IFilter() {

            @Override
            public Promise<FullHttpResponse> filter(StreamContext context, IFilterChain filterChain) {
                return null;
            }
        };
    }
    public void testAuthFilter(){
     /*   proxyServer.addFilter(newAuthFilter());
        String content = newReq(PROXY_JSON_URL);
        System.out.println("content::"+content);
*/
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
