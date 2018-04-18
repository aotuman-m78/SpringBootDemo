package com.example.service.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.transaction.CuratorTransactionResult;
import org.apache.zookeeper.data.Stat;

import java.util.Collection;
import java.util.List;

/**
 * Created by fangrui on 2018/3/6.
 */
public interface CuratorFrameworkService {

    String create(String path) throws Exception;

    void delete(String path, Integer version) throws Exception;

    String get(String path) throws Exception;

    Stat updateDate(String path, String data, int version) throws Exception;

    Stat checkExists(String path) throws Exception;

    List<String> getChildren(String path) throws Exception;

    Collection<CuratorTransactionResult> createWithTransaction(String path, Integer version, String data) throws Exception;
}
