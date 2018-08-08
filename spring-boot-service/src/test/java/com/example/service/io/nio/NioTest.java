package com.example.service.io.nio;

import com.example.service.io.nio.client.NioClient;
import com.example.service.io.nio.server.NioServer;

import java.io.IOException;
import java.util.Scanner;

/**
 * Created by fangrui on 2018/8/8.
 */
public class NioTest {

    public static void main(String[] args) throws IOException {
        NioServer.start(12345);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        NioClient.start();
        while (NioClient.sendMsg(new Scanner(System.in).nextLine()));
    }
}
