package com.example.service.zookeeper.impl;

import com.example.service.zookeeper.CuratorFrameworkService;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.transaction.CuratorTransactionResult;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * Created by fangrui on 2018/3/6.
 */
@Service
public class CuratorFrameworkServiceImpl implements CuratorFrameworkService {

    private static final String connectionInfo = "127.0.0.1";

    public static CuratorFramework getClient() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client =
                CuratorFrameworkFactory.builder()
                        .connectString(connectionInfo)
                        .sessionTimeoutMs(5000)
                        .connectionTimeoutMs(5000)
                        .retryPolicy(retryPolicy)
                        .build();
        client.start();
        return client;
    }

    @Override
    public String create(String path) throws Exception {
        return getClient().create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(path);
    }

    @Override
    public void delete(String path, Integer version) throws Exception {
        getClient().delete().deletingChildrenIfNeeded().withVersion(version).forPath(path);
    }

    @Override
    public String get(String path) throws Exception {
        Stat stat = new Stat();
        return new String(getClient().getData().storingStatIn(stat).forPath(path));
    }

    @Override
    public Stat updateDate(String path, String data, int version) throws Exception {
        return getClient().setData().withVersion(version).forPath(path, data.getBytes());
    }

    @Override
    public Stat checkExists(String path) throws Exception {
        return getClient().checkExists().forPath(path);
    }

    @Override
    public List<String> getChildren(String path) throws Exception {
        return getClient().getChildren().forPath(path);
    }

    @Override
    public Collection<CuratorTransactionResult> createWithTransaction(String path, Integer version, String data) throws Exception {
        return getClient().inTransaction().check().forPath(path)
                .and().create().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(path, data.getBytes())
                .and().setData().withVersion(version).forPath(path, data.getBytes())
                .and().commit();
    }
}
