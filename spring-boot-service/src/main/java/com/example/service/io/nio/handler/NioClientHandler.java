package com.example.service.io.nio.handler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by fangrui on 2018/8/8.
 */
public class NioClientHandler implements Runnable {

    private Selector selector;
    private SocketChannel sc;
    private static volatile boolean started;
    private static String host;
    private static int port;

    public NioClientHandler(String host, int port) throws IOException {
        selector = Selector.open();
        sc = SocketChannel.open();
        sc.configureBlocking(false);
//        sc.register(selector, SelectionKey.OP_CONNECT);
        started = true;
        this.host = host;
        this.port = port;
    }

    public void stop() {
        started = false;
    }


    @Override
    public void run() {

        try {
            if (sc.connect(new InetSocketAddress(host, port)));
            else sc.register(selector, SelectionKey.OP_CONNECT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("客户端已启动...");

        while (started) {
            try {
                selector.select(1000);
                Set<SelectionKey> keys = selector.keys();
                Iterator<SelectionKey> it = keys.iterator();
                SelectionKey key = null;

                while(it.hasNext()) {
                    key = it.next();
                    handle(key, it);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (null != selector) {
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handle(SelectionKey key, Iterator<SelectionKey> it) throws IOException {

        if (key.isValid()) {

            if (key.isConnectable()) {

                if (sc.finishConnect());
                else System.exit(1);
            }

            if (key.isReadable()) {
                ByteBuffer bb = ByteBuffer.allocate(1024);
                int readBytes = sc.read(bb);

                if (readBytes > 0) {
                    bb.flip();
                    byte[] bytes = new byte[bb.remaining()];
                    bb.get(bytes);
                    String msg = new String(bytes, "UTF-8");
                    System.out.println("客户端收到消息 : " + msg);
                } else if (readBytes < 0) {
                    key.cancel();
                    sc.close();
                    it.remove();
                }
            }
        }
    }

    public void sendMsg(String msg) throws IOException {
        sc.register(selector, SelectionKey.OP_READ);
        byte[] bytes = msg.getBytes();
        ByteBuffer bb = ByteBuffer.allocate(bytes.length);
        bb.put(bytes);
        bb.flip();
        sc.write(bb);
    }
}
