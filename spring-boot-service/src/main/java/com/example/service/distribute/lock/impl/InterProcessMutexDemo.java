package com.example.service.distribute.lock.impl;

import com.example.service.util.FakeLimitedResource;
import com.example.service.zookeeper.impl.CuratorFrameworkServiceImpl;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.utils.CloseableUtils;

import java.util.concurrent.*;

/**
 * Created by fangrui on 2018/3/28.
 */
public class InterProcessMutexDemo {

    private InterProcessMutex lock;
    private final FakeLimitedResource resource;
    private final String clientName;
    private static final String PATH = "/examples/locks";

    public InterProcessMutexDemo(CuratorFramework client, FakeLimitedResource resource, String clientName, String lockPath) {
        this.lock = new InterProcessMutex(client, lockPath);
        this.resource = resource;
        this.clientName = clientName;
    }

    public void doWork(long time, TimeUnit unit) throws Exception {
        if (! lock.acquire(time, unit)) {
            throw new IllegalStateException(clientName + " could not acquire the lock.");
        }

        try {
            System.out.println(clientName + " acquire the lock");
            resource.use();
        } finally {
            System.out.println(clientName + " release the lock");
            lock.release();
        }
    }

    public static void main(String[] arg) throws Exception {
        final FakeLimitedResource resource = new FakeLimitedResource();
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        System.out.println("availableProcessors : " + availableProcessors);
        ExecutorService service = Executors.newFixedThreadPool(availableProcessors);

        for (int i = 0; i < availableProcessors; i ++) {
            final int index = i;

            Callable<Void> task = new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    CuratorFramework client = CuratorFrameworkServiceImpl.getClient();

                    try {
                        final InterProcessMutexDemo demo = new InterProcessMutexDemo(client, resource, "Client " + index, PATH);
                        demo.doWork(10, TimeUnit.SECONDS);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        CloseableUtils.closeQuietly(client);
                    }
                    return null;
                }
            };
            service.submit(task);
        }
        service.shutdown();
        service.awaitTermination(10, TimeUnit.SECONDS);
    }
}
