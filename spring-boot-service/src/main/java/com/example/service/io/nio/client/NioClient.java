package com.example.service.io.nio.client;

import com.example.service.io.nio.handler.NioClientHandler;

import java.io.IOException;

/**
 * Created by fangrui on 2018/8/8.
 */
public class NioClient {
    private static NioClientHandler handler;

    public static void start() throws IOException {

        if (handler != null) {
            handler.stop();
        }

        handler = new NioClientHandler("127.0.0.1", 12345);
        new Thread(handler, "Client").start();
    }

    public static boolean sendMsg(String msg) throws IOException {
        if(msg.equals("q")) {
            System.out.println("客户端退出");
            return false;
        }
        handler.sendMsg(msg);
        return true;
    }
}
