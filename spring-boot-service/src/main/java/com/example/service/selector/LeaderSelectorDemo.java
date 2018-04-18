package com.example.service.selector;

import com.example.service.adapter.LeaderSelectorAdapter;
import com.example.service.zookeeper.impl.CuratorFrameworkServiceImpl;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.utils.CloseableUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fangrui on 2018/3/28.
 */
public class LeaderSelectorDemo {

    private static String PATH = "/example/test";

    public static void main(String[] argv) throws Exception {
        int availableProcessorts = Runtime.getRuntime().availableProcessors();
        System.out.println(" availableProcessors : " + availableProcessorts);
        List<CuratorFramework> clients = new ArrayList<>();
        List<LeaderSelectorAdapter> adapters = new ArrayList<>();

        try {
            for (int i = 0; i < availableProcessorts; i ++) {
                CuratorFramework client = CuratorFrameworkServiceImpl.getClient();
                clients.add(client);
                LeaderSelectorAdapter adapter = new LeaderSelectorAdapter(client, PATH, "Client#" + i);
                adapters.add(adapter);
                adapter.start();
            }
            System.out.println("Press enter/return to quit\n");
            new BufferedReader(new InputStreamReader(System.in)).readLine();
        } finally {

            for (CuratorFramework var : clients) {
                CloseableUtils.closeQuietly(var);
            }
            for (LeaderSelectorAdapter var : adapters) {
                CloseableUtils.closeQuietly(var);
            }
        }
    }
}
