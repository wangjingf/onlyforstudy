package io.study.gateway.util;

import com.jd.vd.common.io.serializer.IObjectSerializer;
import com.jd.vd.common.io.serializer.impl.DefaultSerializer;
import io.study.gateway.registry.CuratorChildListener;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.curator.framework.api.GetChildrenBuilder;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class ZkClient {
    private static final String zkConn = "127.0.0.1:2181";
    static final int sessionTimeout = 3000;
    static final int connectionTimeout = 3 * 1000;
    static final Logger logger = LoggerFactory.getLogger(ZkClient.class);
    IObjectSerializer serializer = new DefaultSerializer();
    Map<String, PathChildrenCache> caches = new ConcurrentHashMap<String, PathChildrenCache>();
    ExecutorService executor;
    RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 10);
    //2 通过工厂创建连接

    //会话超 时时间
    private static final int SESSION_TIMEOUT = 30 * 1000;
    @PostConstruct
    public void start(){
        this.client = CuratorFrameworkFactory.builder()
                .connectString(CONNECT_ADDR).connectionTimeoutMs(CONNECTION_TIMEOUT)
                .sessionTimeoutMs(SESSION_TIMEOUT)
                .retryPolicy(retryPolicy)
                .build();
        this.executor = Executors.newSingleThreadExecutor();

    }
    PathChildrenCache makeChildrenCache(String path) {
        PathChildrenCache cache = this.caches.get(path);
        if (cache != null) {
            return cache;
        }
        synchronized (this.caches) {
            cache = this.caches.get(path);
            if (cache != null) {
                return cache;
            }
            cache = new PathChildrenCache(this.client, path, true, false, getExecutorForPath(path));
            try {
                cache.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);
            } catch (Exception e) {
                throw CuratorHelper.wrapException(e);
            }
            this.caches.put(path, cache);
        }
        return cache;
    }
    protected ExecutorService getExecutorForPath(String path) {
        return this.executor;
    }
    //连接超时时间
    private static final int CONNECTION_TIMEOUT = 3 * 1000;

    //ZooKeeper服务地址
    private static final String CONNECT_ADDR = "192.168.1.1:2100,192.168.1.1:2101,192.168.1.:2102";

    //创建连接实例
    private CuratorFramework client = null;

    public void registerNodeChangeEventListener(String path,Watcher watcher) throws Exception {

        client.getData().usingWatcher(watcher).forPath(path);
    }
    public List<String> addChildChangeListener(String path, PathChildrenCacheListener listener){
        PathChildrenCache cache = makeChildrenCache(path);
        cache.getListenable().addListener(listener);

        return CuratorHelper.getChildNames(cache.getCurrentData());
    }
    public CuratorFramework getCuratorClient(){
        return client;
    }
    public void createNode(String path,byte[] data) throws Exception {
        client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path,data);
    }
    public void deleteNode(String path) throws Exception {
        client.delete().forPath(path);
    }
    public boolean exist(String path) throws Exception {
        Stat stat= client.checkExists().forPath(path);
        return stat != null;
    }
    public byte[] getData(String path) throws Exception {
        return client.getData().forPath(path);
    }
    public List<String> getAllChildren(String path) throws Exception {
        List<String> children = client.getChildren().forPath(path);
        return children;
    }
}
