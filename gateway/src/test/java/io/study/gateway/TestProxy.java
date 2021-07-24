package io.study.gateway;

import io.study.gateway.proxy.ProxyServer;
import io.study.gateway.server.ProxyResponseMap;
import io.study.gateway.server.SimpleHttpServer;
import junit.framework.TestCase;

public class TestProxy extends TestCase {
    SimpleHttpServer server = null;
    ProxyServer proxyServer = null;
    @Override
    public void setUp() throws Exception {
        server = new SimpleHttpServer(5121,ProxyResponseMap.responseMap);
        proxyServer = new ProxyServer(8080);
        server.start();
        proxyServer.start();
        super.setUp();
    }
    public void testTrue(){
        assertEquals(1,1);
    }
    public void testProxy(){

    }
    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }
}
