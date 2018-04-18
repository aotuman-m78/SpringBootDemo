package com.example.service.zookeeper.impl;

import com.example.service.zookeeper.ZooKeeperService;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by fangrui on 2018/2/7.
 */
@Service
public class ZooKeeperServiceImpl implements ZooKeeperService {

    private static final String URL = "127.0.0.1";
    private static final Integer PORT = 2181;

    private static final String ROOT = "/root";

    public static ZooKeeper getClient() throws IOException, InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);

        ZooKeeper zk = new ZooKeeper(URL, 5000, new Watcher() {
            @Override
            public void process(WatchedEvent event) {

                if (event.getState() == Event.KeeperState.SyncConnected) {
                    latch.countDown();
                }
            }
        });
        latch.await();
        return zk;
    }

    @Override
    public String createNode(ZooKeeper zk, String node, String data, CreateMode mode) throws Exception {
        return createNode(zk, node, data.getBytes(), mode);
    }

    @Override
    public String createNode(ZooKeeper zk, String node, byte[] data, CreateMode mode) throws Exception {
        return zk.create(node, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, mode);
    }

    @Override
    public void removeNode(ZooKeeper zk, String node) throws Exception {
        zk.delete(node, -1);
    }

    @Override
    public List<String> getChildren(ZooKeeper zk, String path, Watcher watcher) throws Exception {
        List<String> list = zk.getChildren(path, watcher);
        return list;
    }

    @Override
    public Stat exists(ZooKeeper zk, String path, Watcher watcher) throws Exception {
        Stat stat = zk.exists(path, watcher);
        return stat;
    }
}
