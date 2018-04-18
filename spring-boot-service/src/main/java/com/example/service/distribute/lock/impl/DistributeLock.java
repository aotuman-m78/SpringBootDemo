package com.example.service.distribute.lock.impl;

import com.alibaba.fastjson.JSON;
import com.example.service.util.SpringContextHolder;
import com.example.service.zookeeper.ZooKeeperService;
import com.example.service.zookeeper.impl.ZooKeeperServiceImpl;
import com.google.common.collect.Maps;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.context.ContextLoader;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * Created by fangrui on 2018/2/8.
 */
public class DistributeLock implements Lock {

    private Logger logger = LoggerFactory.getLogger(DistributeLock.class);

    private CountDownLatch latch;

    private ZooKeeper zk = null;

    private static String ROOT = "/root";

    private ZooKeeperService zooKeeperService;

    private static String LOCK_ROOT = "/lock";

    private static String SEPARATOR = "_";
    //路径
    private static String PATH_SEPARATOR = "/";

    private static String LOCK_KEYWORD = "lock";

    private String lockResource;

    private String lockPrefix;

    private String lockWholePath;

    private Map<Thread, String> lockMap = Maps.newConcurrentMap();

    public DistributeLock(String lockResource) throws IOException, InterruptedException {
        zk = ZooKeeperServiceImpl.getClient();
        zooKeeperService = SpringContextHolder.getBean(ZooKeeperService.class);
        this.lockResource = lockResource;

        if (! lockResource.startsWith(PATH_SEPARATOR)) {
            lockWholePath = ROOT + LOCK_ROOT + PATH_SEPARATOR + this.lockResource;
        } else {
            lockWholePath = ROOT + LOCK_ROOT + this.lockResource;
        }
        lockPrefix = lockWholePath + PATH_SEPARATOR + LOCK_KEYWORD + SEPARATOR;
    }

    @Override
    public void lock() {
        logger.info("thread" + Thread.currentThread().getName() + " try acquire lock...");
        if (tryLock()) {
            logger.info("thread" + Thread.currentThread().getName() + " acquire lock success...");
        } else {
            try {
                waitForLock();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    private void waitForLock() throws Exception {
        String currentPath = lockMap.get(Thread.currentThread());

        List<String> children = zooKeeperService.getChildren(zk, lockWholePath, null);

        if (children == null) {
            logger.error("zookeeper acquire children failure, please check the connection.");
            zooKeeperService.removeNode(zk, currentPath);
            return;
        }

        if (children.size() == 1) {
            return;
        }
        //默认不会为0
        //升序排序
        Collections.sort(children);

        String beforePath = null;
        logger.info("children : {}", JSON.toJSONString(children));

        for (String var : children) {
            String tmpVar = lockWholePath+PATH_SEPARATOR+var;

            if (tmpVar.equals(currentPath)) {
                break;
            }
            beforePath = tmpVar;
        }
        logger.info("currentPath : {}, beforePath : {}", currentPath, beforePath);

        if (beforePath == null) {
            return;
        }

        final CountDownLatch latch = new CountDownLatch(1);

        zooKeeperService.exists(zk, beforePath, new Watcher() {
            @Override
            public void process(WatchedEvent event) {

                if (event.getType() == Event.EventType.NodeDeleted) {
                    latch.countDown();
                }
            }
        });
        lockMap.put(Thread.currentThread(), currentPath);
        latch.await();
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {

        try {
            String currentPath = zooKeeperService.createNode(zk, lockPrefix, new byte[0], CreateMode.EPHEMERAL_SEQUENTIAL);
            lockMap.put(Thread.currentThread(), currentPath);
            List<String> children = zooKeeperService.getChildren(zk, lockWholePath, null);

            if (children == null) {
                logger.error("zookeeper acquire children failure, please check the connection.");
                zooKeeperService.removeNode(zk, currentPath);
                return false;
            }

            if (children.size() == 1) {
                return true;
            }
            Collections.sort(children);
            String tmpVar = lockWholePath+PATH_SEPARATOR+children.get(0);

            if (tmpVar.equals(currentPath)) {
                return true;
            }

            return false;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void unlock() {
        try {
            String currentPath = lockMap.get(Thread.currentThread());

            if (! StringUtils.isEmpty(currentPath)) {
                zooKeeperService.removeNode(zk, lockMap.get(Thread.currentThread()));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public Condition newCondition() {
        return null;
    }
}
