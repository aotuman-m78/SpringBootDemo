package com.example.server;

import com.example.handler.EchoServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.internal.logging.InternalLogLevel;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.internal.logging.Slf4JLoggerFactory;

import java.net.InetSocketAddress;

/**
 * Created by fangrui on 2018/4/8.
 */
public class EchoServer {

    private final int port;

    public EchoServer (int port) {
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        InternalLoggerFactory.setDefaultFactory(new Slf4JLoggerFactory());

        if (args.length != 1) {
            System.err.println("Usage : " + EchoServer.class.getSimpleName() + "<port>");
            return;
        }

        int port = Integer.parseInt(args[0]);
        new EchoServer(port).start();
    }

    public void start() throws Exception {
        final EchoServerHandler handler = new EchoServerHandler();
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(group).channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new LoggingHandler(LogLevel.INFO)).addLast(handler);
                        }
                    });
            ChannelFuture f = b.bind().sync();
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }
    }

}
