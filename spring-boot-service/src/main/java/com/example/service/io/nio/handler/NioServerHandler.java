package com.example.service.io.nio.handler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by fangrui on 2018/8/8.
 */
public class NioServerHandler implements Runnable {

    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    private volatile boolean started = false;

    public NioServerHandler(int port) throws IOException {
        selector = Selector.open();
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(port), 1024);
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        started = true;
        System.out.println("服务器已启动...");
    }

    public void stop() {
        started = false;
    }


    @Override
    public void run() {

        try {
            while (! selector.isOpen()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            while (started) {

                selector.select(1000);
                Set<SelectionKey> keys = selector.keys();
                Iterator<SelectionKey> it = keys.iterator();
                SelectionKey key = null;

                while (it.hasNext()) {
                    key = it.next();

                    try {
                        handleInput(key, it);
                    } catch (Exception e) {

                        if (key != null) {
                            key.cancel();

                            if (key.channel() != null) {
                                key.channel().close();
                            }
                        }
                    }
                }
            }
            if (selector != null) {
                selector.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleInput(SelectionKey key, Iterator<SelectionKey> it) throws IOException {

        if (key.isValid()) {

            if (key.isAcceptable()) {
                ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                //完成该操作意味着完成TCP三次握手，TCP物理链路正式建立
                SocketChannel sc = ssc.accept();
                sc.configureBlocking(false);
                sc.register(selector, SelectionKey.OP_READ);
            }

            if (key.isReadable()) {
                SocketChannel sc = (SocketChannel) key.channel();
                ByteBuffer bb = ByteBuffer.allocate(1024);
                int readBytes = sc.read(bb);

                if (readBytes > 0) {
                    bb.flip();
                    byte[] bytes = new byte[bb.remaining()];
                    bb.get(bytes);
                    String msg = new String(bytes, "UTF-8");
                    System.out.println("收到消息 : " + msg);
                    String response = "ojbk";
                    bytes = response.getBytes();
                    bb = ByteBuffer.allocate(bytes.length);
                    bb.put(bytes);
                    bb.flip();
                    sc.write(bb);
                } else if (readBytes < 0) {
                    key.cancel();
                    sc.close();
                    it.remove();
                }
            }
        }

    }
}
