package com.example.service.zookeeper;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.util.List;

/**
 * Created by fangrui on 2018/2/7.
 */
public interface ZooKeeperService {

    String createNode(ZooKeeper zk, String node, String data, CreateMode mode) throws Exception;

    String createNode(ZooKeeper zk, String node, byte[] data, CreateMode mode) throws Exception;

    void removeNode(ZooKeeper zk, String node) throws Exception;

    List<String> getChildren(ZooKeeper zk, String path, Watcher watcher) throws Exception;

    Stat exists(ZooKeeper zk, String path, Watcher watcher) throws Exception;
}
