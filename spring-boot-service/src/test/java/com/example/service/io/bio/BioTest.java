package com.example.service.io.bio;

import com.example.service.io.bio.client.BioClient;
import com.example.service.io.bio.server.BioServer;

import java.io.IOException;

/**
 * Created by fangrui on 2018/8/7.
 */
public class BioTest {

    public static void main(String[] args) throws InterruptedException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    BioServer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        Thread.sleep(1000l);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    BioClient.sendMsg("测试啦");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
