package io.study.gateway.registry;

import com.jd.vd.common.exception.BizException;
import com.jd.vd.common.io.serializer.IObjectSerializer;
import com.jd.vd.common.io.serializer.impl.DefaultSerializer;
import io.study.gateway.common.GatewayConstant;
import io.study.gateway.config.ApiConfig;
import io.study.gateway.config.AppConfig;
import io.study.gateway.util.CuratorHelper;
import io.study.gateway.util.ZkClient;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class ZookeeperRegistry extends AbstractRegistry{
    ZkClient zkClient;
    static final Logger logger = LoggerFactory.getLogger(ZookeeperRegistry.class);
    IObjectSerializer serializer = new DefaultSerializer();
    Map<String,AppConfig> config = new ConcurrentHashMap<>();
    public ZookeeperRegistry(ZkClient zkClient){
        this.zkClient = zkClient;
    }
    @Override
    public void start() {
        try {

            // 先注册，确保所有节点均能处理
            zkClient.addChildChangeListener(GatewayConstant.REGISTRY_APPLICATION_CONFIG_PATH,  new PathChildrenCacheListener(){
                @Override
                public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent event) throws Exception {
                    ChildData data = event.getData();
                    String path = data.getPath();
                    String app = path.substring(GatewayConstant.REGISTRY_APPLICATION_CONFIG_PATH.length());
                    PathChildrenCacheEvent.Type type = event.getType();
                    if(PathChildrenCacheEvent.Type.CHILD_ADDED.equals(type) || PathChildrenCacheEvent.Type.CHILD_UPDATED.equals(type)){
                        config.put(app, (AppConfig) serializer.deserialize(data.getData()));
                    }else if(PathChildrenCacheEvent.Type.CHILD_REMOVED.equals(type)){
                        config.remove(app);
                    }

                }
            });
            initAppConfig();

        } catch (Exception e) {
            throw BizException.adapt(e);
        }
    }

    /**
     * @todo 看看是否有批量处理api
     */
    private void initAppConfig(){
        List<String> children = null;
        try {
            children = zkClient.getAllChildren(GatewayConstant.REGISTRY_APPLICATION_CONFIG_PATH);
            for (String child : children) {
                AppConfig data = (AppConfig) getData(child);
                if(data != null){
                    config.put(child.substring(GatewayConstant.REGISTRY_APPLICATION_CONFIG_PATH.length()),data);

                }
            }
        } catch (Exception e) {
            throw BizException.adapt(e);
        }

    }
    String getAppCfgPath(String appPath){
        return GatewayConstant.REGISTRY_APPLICATION_CONFIG_PATH + "/"+ appPath+"/config";
    }

    @Override
    public ApiConfig getConfig(String uri) {
        String app = getApp(uri);
        if(app == null){
            return null;
        }
        try {
            byte[] data = zkClient.getData(getAppCfgPath(app));
            return  (ApiConfig)serializer.deserialize(data);
        } catch (Exception e) {
            logger.error("registry.err_get_config",e);
            return null;
        }
    }

    @Override
    public Object getData(String key) {
        try {
            byte[] data = zkClient.getData(getAppCfgPath(getAppCfgPath(key)));
            return  serializer.deserialize(data);
        } catch (Exception e) {
            logger.error("registry.err_get_data",e);
            return null;
        }
    }

    @Override
    public void setData(String key, Object data) {
        try {
            byte[] bytes = serializer.serialize(data);
            zkClient.createNode(key,bytes);
        } catch (Exception e) {
            logger.error("registry.err_set_data",e);
        }
    }

    public void register(String app,ApiConfig apiConfig){
        try {
            byte[] bytes = serializer.serialize(apiConfig);
            zkClient.createNode(app,bytes);
        } catch (Exception e) {
            logger.error("registry.err_register_config",e);
        }
    }
}
