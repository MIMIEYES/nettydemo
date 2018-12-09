package com.mimieye.netty.client;

import com.mimieye.netty.common.CommonUtil;
import com.mimieye.netty.common.GatewayService;
import com.mimieye.netty.common.MyChannelInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

import static com.mimieye.netty.common.CommonUtil.*;

/**
 * Created by Pierreluo on 2017/5/18.
 */
public class NettyClientTest {
    private static Logger logger = LoggerFactory.getLogger(NettyClientTest.class);

    public static void main(String[] args) {
        boolean b = new NettyClient().start();

        System.out.println("---------------------" + b);
    }
//    public static void main(String[] args) {
//        new NettyClient().start();
//        new NettyClient().start();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        }).start();
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        }).start();
//    }
//    public static void main(String[] args) throws InterruptedException {
//        logger.debug("启动客户端.");
//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                NettyClientTest client = new NettyClientTest();
//                client.startUp();
//                //SocketChannel socketChannel = client.startUp();
//                //CommonUtil.socketChannel = socketChannel;
//                //GatewayService.clientMap.put(socketChannel.id().asLongText(), socketChannel);
//            }
//        };
//        Thread thread = new Thread(runnable, "client-pierre");
//        thread.start();
//        waitStartUp.await();
//
//        logger.debug("监听消息发送队列并发送消息.");
//        Runnable listener = new Runnable() {
//            @Override
//            public void run() {
//                while (RUNNING) {
//                    String taskStr = null;
//                    String name = Thread.currentThread().getName();
//                    logger.debug(name + "-线程[消息发送队列]准备读取消息.");
//                    synchronized (SEND_QUEUE) {
//                        while(RUNNING && SEND_QUEUE.isEmpty()) {
//                            try {
//                                logger.debug( name + "-线程[消息发送队列]等待消息.");
//                                SEND_QUEUE.wait();
//                            } catch (Exception e) {
//                                Thread.currentThread().interrupt();
//                                return;
//                            }
//                        }
//                        taskStr = SEND_QUEUE.poll();
//                    }
//                    if(StringUtils.isNotBlank(taskStr)) {
//                        logger.debug(name + "-线程[消息发送队列]发送数据.");
//                        socketChannel.writeAndFlush(taskStr);
//                        ByteBuf buf = Unpooled.copiedBuffer("asd", CharsetUtil.UTF_8);
//                    }
//
//                }
//                logger.debug("客户端停止监听发送消息.");
//
//            }
//        };
//        Thread sendThread = new Thread(listener, "client_listen_and_send_msg");
//        sendThread.start();
//
//        logger.debug("生产消息.");
//        Scanner scanner = new Scanner(System.in);
//        String input = null;
//        while (scanner.hasNextLine()) {
//            input = scanner.nextLine();
//            if(!"exit".equals(input)) {
//                addMsg(input);
//            } else {
//                logger.debug("准备关闭客户端.");
//                closeClient();
//                break;
//            }
//
//        }
//
//    }
//
//    public void startUp(){
//        EventLoopGroup group = null;
//        //SocketChannel socketChannel = null;
//        try {
//            Bootstrap boot = new Bootstrap();
//            group = new NioEventLoopGroup();
//            boot.group(group)
//                    .channel(NioSocketChannel.class)
//                    .option(ChannelOption.TCP_NODELAY,true)
//                    //  保持长连接
//                    .option(ChannelOption.SO_KEEPALIVE, true);
//            //boot.handler(new LoggingHandler(LogLevel.DEBUG));
//            boot.handler(new MyChannelInitializer<>(new RequestMsgClientHandler()));
//
//            // Start the client
//            ChannelFuture f = boot.connect("localhost",11111).sync();
//
//            if(f.isSuccess()) {
//                socketChannel = (SocketChannel) f.channel();
//                logger.debug("clientId: " + socketChannel.id().asLongText() + " - 连接成功.");
//                waitStartUp.countDown();
//            } else {
//                System.out.println("&&&&&&&&&&&&&&&&&&&&&&");
//            }
//            //EventLoop eventLoop = socketChannel.eventLoop();
//
//            //socketChannel.eventLoop().parent().
//
//            // Wait until the connection is closed.
//            f.channel().closeFuture().sync();
//            logger.debug("***************** client close. ***************** ");
//        } catch (Exception e){
//            e.printStackTrace();
//            logger.error("================================================================",e);
//        } finally {
//            // Shut down the event loop to terminate all threads.
//            group.shutdownGracefully();
//        }
//    }
}


