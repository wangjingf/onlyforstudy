package io.study.zookeeper;



import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.slf4j.Logger;

import java.io.IOException;

public class CuratorTest {

    public static void main(String[] args) {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000,3,10000);
        CuratorFramework client = CuratorFrameworkFactory.newClient("localhost:2181",5000,3000,retryPolicy);
        client.start();
        /*final NodeCache cache = new NodeCache(client, path, false);

        cache.start(true);

        cache.getListenable().addListener(new NodeCacheListener() {

            @Override

            public void nodeChanged() throws Exception {

                System.out.println("Node data update, new data: " + new String(cache.getCurrentData().getData()));

            }

        });*/
        System.out.println("create success");
        client.close();
    }

    /**
     * "/wjf/cute/cute/cute"
     * @param client
     * @param path
     */
    public void create(CuratorFramework client,String path){
        try {
            client.create().creatingParentContainersIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path);
        } catch(KeeperException.NodeExistsException e){
            // juat ignore
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
