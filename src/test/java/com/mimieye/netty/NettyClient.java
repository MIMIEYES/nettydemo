package com.mimieye.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;


public class NettyClient {

    public static EventLoopGroup worker = new NioEventLoopGroup();
    Bootstrap boot;

    private SocketChannel socketChannel;

    private String host;

    private int port;

    public NettyClient(String host, int port) {
        this.host = host;
        this.port = port;
        boot = new Bootstrap();
        AttributeKey<String> key = AttributeKey.valueOf("NODE_KEY");
        boot.group(worker)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new NulsChannelInitializer<>(new ClientChannelHandler(key)))
                .attr(key, "yyyyyyyyyyyyyyyyyyy");
    }

    public void start() {

        try {
            ChannelFuture future = boot.connect(host, port).sync();
            if (future.isSuccess()) {
                socketChannel = (SocketChannel) future.channel();

                String msg = "send message";
                for (int i = 0; i < 10; i++) {
                    msg += i;
                    socketChannel.writeAndFlush(msg);
                }
               // socketChannel.close();
            }
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // Shut down the event loop to terminate all threads.
            worker.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 2; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    NettyClient client = new NettyClient("localhost", 8003);
                    client.start();
                }
            }).start();
        }
    }

}
