package com.example.service.curator;

import com.example.service.base.AbstractServiceTest;
import com.example.service.zookeeper.impl.CuratorFrameworkServiceImpl;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.*;
import org.junit.Test;

/**
 * Created by fangrui on 2018/3/26.
 */
public class CuratorServiceTest extends AbstractServiceTest {

    private static final String PATH = "/root/test";

    @Test
    public void testPathCache() throws Exception {
        CuratorFramework client = CuratorFrameworkServiceImpl.getClient();
        client.create().creatingParentsIfNeeded().forPath(PATH);
        PathChildrenCache cache = new PathChildrenCache(client , PATH, true);
        cache.start();
        PathChildrenCacheListener listener = new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                System.out.println(event.getType());

                if (event.getData() != null) {
                    System.out.println(new String(event.getData().getData()));
                }
            }
        };
        cache.getListenable().addListener(listener);
        client.create().creatingParentsIfNeeded().forPath(PATH + "/children", "01".getBytes());
        Thread.sleep(1000);
        client.create().creatingParentsIfNeeded().forPath(PATH + "/children1", "02".getBytes());
        Thread.sleep(1000);
        client.setData().forPath(PATH + "/children", "011".getBytes());
        Thread.sleep(1000);

        for (ChildData var : cache.getCurrentData()) {
            System.out.println("currentData : " + var.getPath() + ", data : " + new String(var.getData()));
        }
        Thread.sleep(1000);
        client.delete().deletingChildrenIfNeeded().forPath(PATH);
        Thread.sleep(1000 * 2);
        cache.close();
        client.close();
        System.out.println("ok!");
    }

    @Test
    public void testNodeCache() throws Exception {
        CuratorFramework client = CuratorFrameworkServiceImpl.getClient();
        String str = client.create().creatingParentsIfNeeded().forPath(PATH, "01".getBytes());
        System.out.println(str);

        final NodeCache cache = new NodeCache(client, PATH);
        NodeCacheListener listener = new NodeCacheListener() {
            @Override
            public void nodeChanged() throws Exception {
              ChildData data = cache.getCurrentData();

              if (data != null) {
                  System.out.println("节点数据：" + new String(data.getData()));
              } else {
                  System.out.println("节点被删除了");
              }
            }
        };

        cache.getListenable().addListener(listener);
        cache.start();
        client.setData().forPath(PATH, "02".getBytes());
        Thread.sleep(1000);
        client.setData().forPath(PATH, "03".getBytes());
        Thread.sleep(1000);
        client.delete().deletingChildrenIfNeeded().forPath(PATH);
        Thread.sleep(1000);
        cache.close();
        client.close();
        System.out.println("ok!");
    }
}
