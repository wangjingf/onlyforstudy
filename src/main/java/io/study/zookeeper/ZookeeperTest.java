package io.study.zookeeper;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class ZookeeperTest implements Watcher {
    private static final CountDownLatch countDownLatch = new CountDownLatch(1);
    private static final String ADDRESS = "localhost:2181";
    private static final String PREFIX = "/wjf";
    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        ZooKeeper zooKeeper = new ZooKeeper(ADDRESS, 5000, new ZookeeperTest());
        System.out.println("state:"+zooKeeper.getState());
        countDownLatch.await();
        String path1 = zooKeeper.create(PREFIX, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println("success create znode:"+ path1);
        String path2 = zooKeeper.create(PREFIX, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println("success create znode:"+ path2);
        zooKeeper.close();
    }

    public void process(WatchedEvent event) {
        //连上了
        if(Event.KeeperState.SyncConnected == event.getState())
            countDownLatch.countDown();
    }
}
