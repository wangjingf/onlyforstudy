package io.study.gateway.proxy;

import com.jd.vd.common.exception.BizException;
import com.jd.vd.common.lang.Guard;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Promise;
import io.study.gateway.channel.ChannelManager;
import io.study.gateway.client.ConnectionPoolConfig;
import io.study.gateway.client.PooledConnection;
import io.study.gateway.common.GatewayConstant;
import io.study.gateway.config.ApiConfig;
import io.study.gateway.config.GatewaySetting;
import io.study.gateway.config.INode;
import io.study.gateway.interceptor.IFilter;
import io.study.gateway.interceptor.IFilterChain;
import io.study.gateway.message.http.HttpMessageInfo;
import io.study.gateway.message.http.HttpResponseInfo;
import io.study.gateway.registry.IRegistry;
import io.study.gateway.registry.IServerList;
import io.study.gateway.registry.ZookeeperServerList;
import io.study.gateway.transport.ClientResponseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ProxyFilter implements IFilter {
    IRegistry registry = null;
    Map<ApiConfig,ChannelManager> apiChannel = new ConcurrentHashMap<>();
    ChannelManager channelManager = null;
    ConnectionPoolConfig poolConfig;
    GatewaySetting gatewaySetting;
    static final Logger logger = LoggerFactory.getLogger(ProxyFilter.class);

    public ProxyFilter(GatewaySetting gatewaySetting,ConnectionPoolConfig poolConfig, IRegistry registry){
        this.gatewaySetting = gatewaySetting;
        this.poolConfig = poolConfig;
        this.registry = registry;
    }
    @Override
    public Promise<FullHttpResponse> filter(StreamContext context, IFilterChain filterChain) {
        String uri = context.getRequest().uri();
        ApiConfig config = registry.getConfig(uri);
        logger.info("proxyfilter.receive_request:uri={}",uri);
        if(config == null){
            throw new BizException("proxy.err_found_invalid_uri:uri="+uri);
        }
        Promise result = context.fromChannel.newPromise();
        channelManager = getOrCreateChannelManager(config);
        transformRequest(context,config);
        logger.info("proxyfilter.transform_request:destUri={}",context.getRequest().uri());
        ProxyEndpoint endpoint = new ProxyEndpoint(context,result);
        Promise<PooledConnection> connectionPromise = channelManager.acquire(context);
        if(connectionPromise.isDone() ){
            Guard.assertTrue(connectionPromise.isSuccess(),"proxyfilter.err_found_invalid_connection");
            logger.info("proxyfilter.get_exist_channel_handle");
            try {
                operateConnectSuccess(endpoint,context,connectionPromise.get());
            } catch (Exception e) {
                logger.error("proxyfilter.found_invalid_connection",e);
            }
        }else{
            connectionPromise.addListener(new GenericFutureListener<Future<? super PooledConnection>>() {
                @Override
                public void operationComplete(Future<? super PooledConnection> future) throws Exception {
                    if(future.isSuccess()){
                        PooledConnection connection = (PooledConnection) future.getNow();
                        logger.info("proxy.connection_target_success:targetAddress={}",connection.getChannel().localAddress());
                        connection.getChannel().pipeline().addBefore("connectPoolHandler","clientResponseHandler",new ClientResponseHandler());
                        operateConnectSuccess(endpoint,context,connectionPromise.get());
                    }else{
                        throw new BizException("proxy.err_get_connection",future.cause());
                    }
                }
            });
        }
        return result;
    }

    private void operateConnectSuccess(ProxyEndpoint endpoint,StreamContext context,PooledConnection connection) {
        context.setTargetAddress(connection.getChannel().localAddress());
        context.setToChannel(connection);
        connection.getChannel().attr(GatewayConstant.KEY_PROXY_ENDPOINT).set(endpoint);
        endpoint.proxy();
    }

    private void transformRequest(StreamContext context,ApiConfig config) {
        FullHttpRequest request = context.getRequest();
        String destUri = config.getDestUri();
        if(!destUri.startsWith("/")){
            destUri =  "/"+destUri;
        }
        request.setUri(destUri);
    }

    private ChannelManager getOrCreateChannelManager(ApiConfig config) {

        ChannelManager ret = apiChannel.computeIfAbsent(config,(key)->{
            List<INode> servers = config.getServers();
                IServerList serverList = new ZookeeperServerList(servers);
                ChannelManager manager = new ChannelManager(gatewaySetting,poolConfig,serverList,config);
                return manager;
        });
        return ret;
    }


}
