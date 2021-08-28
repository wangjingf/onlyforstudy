package io.study.gateway;

import io.study.gateway.balance.ILoadBalance;
import io.study.gateway.balance.LoadBalanceContext;
import io.study.gateway.balance.impl.LeastActiveLoadBalance;
import io.study.gateway.invoker.ProxyInvoker;
import io.study.gateway.protocol.HttpProxyInvoker;
import junit.framework.TestCase;
import org.junit.Before;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class TestLoadBalance extends TestCase {
     List<ProxyInvoker> invokers = new ArrayList<>();
     LoadBalanceContext context = new LoadBalanceContext();
    private ProxyInvoker newInvoker(String ip,Integer port){

        return null;
    }
    @Before
    public void setUp() throws Exception {
        invokers.add(newInvoker("192.168.0.1",8080));
        invokers.add(newInvoker("192.168.0.2",8080));
        context.setDomain("com.wjf.proxy");
        context.setPath("/json");
    }
    public  void testLeastActive(){
       /* ILoadBalance balance = new LeastActiveLoadBalance();
        ProxyInvoker last = null;
        for (int i = 0; i < 100; i++) {
            ProxyInvoker invoker = balance.select(invokers, context);
            if(i%2 == 0){
                assertEquals("/192.168.0.1:8080",invoker.getUrl());
            }else{
                assertEquals("/192.168.0.2:8080",invoker.getUrl());
            }
        }*/
    }
}
