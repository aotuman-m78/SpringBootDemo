package com.example.service.io.bio.client;

import java.io.*;
import java.net.Socket;

/**
 * Created by fangrui on 2018/8/7.
 */
public class BioClient {

    private static Socket socket;

    private static String IP = "127.0.0.1";

    private static int PORT = 12345;

    public static void sendMsg(String msg) throws IOException {
        BufferedReader in = null;
        PrintWriter out = null;

        try {
            socket = new Socket(IP, PORT);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

            out.println(msg);
            System.out.println("收到回复是 " + in.readLine());
        } finally {

            if (in != null) {
                in.close();
            }
            in = null;

            if (out != null) {
                out.close();
            }
            out = null;

            if (socket != null) {
                socket.close();
            }
            socket = null;
        }
    }
}
