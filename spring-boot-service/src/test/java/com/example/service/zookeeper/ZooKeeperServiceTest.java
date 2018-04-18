package com.example.service.zookeeper;

import com.example.service.base.AbstractServiceTest;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by fangrui on 2018/2/7.
 */
public class ZooKeeperServiceTest extends AbstractServiceTest {

    @Autowired
    private ZooKeeperService zooKeeperService;

    @Test
    public void testZooKeeper() throws Exception {
//        zooKeeperService.createNode("/test_node", CreateMode.PERSISTENT);

    }
}
