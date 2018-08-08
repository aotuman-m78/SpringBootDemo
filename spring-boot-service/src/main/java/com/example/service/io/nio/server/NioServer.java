package com.example.service.io.nio.server;

import com.example.service.io.nio.handler.NioServerHandler;

import java.io.IOException;

/**
 * Created by fangrui on 2018/8/8.
 */
public class NioServer {

    private static NioServerHandler handler;

    public static void start(int port) throws IOException {

        if (handler != null) {
            handler.stop();
        }

        handler = new NioServerHandler(port);
        new Thread(handler, "Server").start();
    }
}
