package io.study.gateway.util;

import io.study.gateway.registry.RegistryException;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.ArrayList;
import java.util.List;

public class CuratorHelper
{

    public static RuntimeException wrapException(Exception e) {
        if (e instanceof org.apache.zookeeper.KeeperException.NodeExistsException)
            return (RuntimeException)new RegistryException("registry.err_node_exists", e);
        if (e instanceof org.apache.zookeeper.KeeperException.NoNodeException)
            return (RuntimeException)new RegistryException("registry.err_no_node", e);
        if (e instanceof org.apache.zookeeper.KeeperException.ConnectionLossException)
            return (RuntimeException)new RegistryException("registry.err_connection_loss", e);
        throw new RegistryException("registry.err_fail", e);
    }
    public static List<String> getChildNames(List<ChildData> data) {
        List<String> ret = new ArrayList<String>(data.size());
        for (ChildData childData : data) {
            String path = childData.getPath();
            int pos = path.lastIndexOf('/');
            String name = path.substring(pos + 1);
            ret.add(name);
        }
        return ret;
    }
}
