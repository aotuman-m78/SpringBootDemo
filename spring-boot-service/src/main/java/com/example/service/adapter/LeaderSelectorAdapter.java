package com.example.service.adapter;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by fangrui on 2018/3/28.
 */
public class LeaderSelectorAdapter extends LeaderSelectorListenerAdapter implements Closeable {

    private final LeaderSelector leaderSelector;
    private final String name;

    public LeaderSelectorAdapter(CuratorFramework client, String path, String name) {
        this.leaderSelector = new LeaderSelector(client, path, this);
        leaderSelector.autoRequeue();
        this.name = name;
    }

    public void start() {
        leaderSelector.start();
    }

    @Override
    public void takeLeadership(CuratorFramework client) throws Exception {
        System.out.println(name + " 身为领导想做点什么");
        Thread.sleep(1000);
    }

    @Override
    public void close() throws IOException {
        leaderSelector.close();
    }
}
