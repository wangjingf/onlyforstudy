package io.study.gateway.gateway;

import io.study.gateway.balance.BalancePolicy;
import io.study.gateway.client.ConnectionPoolConfig;
import io.study.gateway.config.ApiConfig;
import io.study.gateway.config.GatewaySetting;
import io.study.gateway.config.INode;
import io.study.gateway.config.ServerNode;
import io.study.gateway.interceptor.FilterLoader;
import io.study.gateway.interceptor.impl.StatFilter;
import io.study.gateway.proxy.ProxyFilter;
import io.study.gateway.proxy.ProxyProtocol;
import io.study.gateway.proxy.ProxyServer;
import io.study.gateway.registry.IRegistry;
import io.study.gateway.registry.LocalRegistry;
import io.study.gateway.stat.MetricStreamChannelStats;
import io.study.gateway.stat.RequestStatFilter;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class GatewayStarter {
   static GatewaySetting gatewaySetting(){
        GatewaySetting setting = new GatewaySetting();
        setting.setPort(80);
        return setting;
    }
    static ApiConfig newApi(String uri, ProxyProtocol proxyProtocol){
        ApiConfig config = new ApiConfig();
        config.setSrcUri(uri);
        config.setDestUri(uri);
        config.setPolicy(BalancePolicy.LeastActive);
        config.setProxyProtocol(proxyProtocol);
        config.setTimeout(1000);
        INode server1 = new ServerNode(new InetSocketAddress("10.169.198.225",80));
        //INode server1 = new ServerNode(new InetSocketAddress("localhost",8080));
        INode server2 = new ServerNode(new InetSocketAddress("11.104.5.158",80));
        List<INode> hosts = new ArrayList<>();
        hosts.add(server1);
        hosts.add(server2);
        config.setServers(hosts);
        return config;
    }
    public static void main(String[] args) throws InterruptedException {
        LocalRegistry registry = new LocalRegistry();



        registry.register("com.wjf.proxy",newApi("/test", ProxyProtocol.Http1_1));


        FilterLoader filterLoader = new FilterLoader();
        ConnectionPoolConfig poolConfig = new ConnectionPoolConfig();
        GatewaySetting gatewaySetting = gatewaySetting();

        ProxyFilter proxyFilter = new ProxyFilter(gatewaySetting,poolConfig,registry);
        StatFilter statFilter = new StatFilter(registry);
        RequestStatFilter requestStatFilter = new RequestStatFilter();
        filterLoader.addLast(requestStatFilter);
        filterLoader.addLast(statFilter);
        filterLoader.addLast(proxyFilter);
        Gateway gateway = new Gateway();
        gateway.setSetting(gatewaySetting);
        gateway.setFilterLoader(filterLoader);
        gateway.setRegistry(registry);
        gateway.setServerStats(new MetricStreamChannelStats("stat.serve===============================================================r.",registry));
        ProxyServer proxyServer =  new ProxyServer(gateway);
        proxyServer.start();

        registry.start();
    }
}
