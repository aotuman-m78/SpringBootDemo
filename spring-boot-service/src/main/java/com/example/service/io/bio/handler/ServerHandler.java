package com.example.service.io.bio.handler;

import java.io.*;
import java.net.Socket;

/**
 * Created by fangrui on 2018/8/7.
 */
public class ServerHandler implements Runnable {

    private Socket socket;

    public ServerHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        BufferedReader reader = null;
        PrintWriter writer = null;

        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

            String msg = null;

            while(true) {

                if ((msg = reader.readLine()) == null) {
                    break;
                }

                System.out.println("服务端收到的消息的是" + msg);
                writer.println("收到 \"" + msg + "\"消息了");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                reader = null;
            }

            if (writer != null) {
                writer.close();
            }

            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                socket = null;
            }
        }
    }
}
