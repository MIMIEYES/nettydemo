package com.mimieye.netty.server;

import com.mimieye.netty.common.MyChannelInitializer;
import com.mimieye.netty.client.NettyClientTest;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

import static com.mimieye.netty.common.CommonUtil.*;

/**
 * Created by Pierreluo on 2017/5/18.
 */
public class NettyServerTest {
    private static Logger logger = LoggerFactory.getLogger(NettyClientTest.class);

    public static void main(String[] args) throws InterruptedException {
        logger.debug("启动服务端.");
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                NettyServerTest server = new NettyServerTest();
                server.startUp();
            }
        };
        Thread thread = new Thread(runnable, "server-pierre");
        thread.start();

        logger.debug("监听消息发送队列并发送消息.");
        Runnable listener = new Runnable() {
            @Override
            public void run() {
                while (RUNNING) {
                    String taskStr = null;
                    String name = Thread.currentThread().getName();
                    logger.debug(name + "-线程[消息发送队列]准备读取消息.");
                    synchronized (SEND_QUEUE) {
                        while(RUNNING && SEND_QUEUE.isEmpty()) {
                            try {
                                logger.debug( name + "-线程[消息发送队列]等待消息.");
                                SEND_QUEUE.wait();
                            } catch (Exception e) {
                                Thread.currentThread().interrupt();
                                return;
                            }
                        }
                        taskStr = SEND_QUEUE.poll();
                    }
                    if(StringUtils.isNotBlank(taskStr)) {
                        logger.debug(name + "-线程[消息发送队列]发送数据.");
                        sendServerMsg(taskStr);
                    }

                }
                logger.debug("服务端停止监听发送消息.");
            }
        };
        Thread sendThread = new Thread(listener, "server_listen_and_send_msg");
        sendThread.start();

        logger.debug("生产消息.");
        Scanner scanner = new Scanner(System.in);
        String input = null;
        while (scanner.hasNextLine()) {
            input = scanner.nextLine();
            if(!"exit".equals(input)) {
                addMsg(input);
            } else {
                logger.debug("准备关闭服务端.");
                closeServer();
                break;
            }

        }

    }

    public void startUp() {
        //EventLoopGroup boss = null;
        //EventLoopGroup worker = null;
        try {
            ServerBootstrap boot = new ServerBootstrap();

            // Linux ->
            // EpollEventLoopGroup
            // EpollServerSocketChannel.class
            boss = new NioEventLoopGroup();
            worker = new NioEventLoopGroup();
            boot.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    // 保持长连接
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new MyChannelInitializer<>(new HelloWorldServerHandler()));
            // Start the server.
            ChannelFuture f = boot.bind(11111).sync();


            // Wait until the server socket is closed.
            f.channel().closeFuture().sync();
        } catch (Exception e){
            e.printStackTrace();
        }finally {
            // Shut down all event loops to terminate all threads.
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }

    }
}
