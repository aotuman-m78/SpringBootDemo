package com.example.service.io.bio.server;

import com.example.service.io.bio.handler.ServerHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by fangrui on 2018/8/7.
 */
public class BioServer {

    private static final int PORT = 12345;

    private static ServerSocket server;

    public static void start() throws IOException {
        start(PORT);
    }

    private synchronized static void start(int port) throws IOException {

        try {
            server = new ServerSocket(port);
            System.out.println("服务端已启动...");

            while (true) {
                Socket socket = server.accept();
                //之后用fork join处理线程
                new Thread(new ServerHandler(socket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            if (server != null) {
                server.close();
            }
            server = null;
        }
    }

}
