package com.mimieye.netty.common;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.SocketChannel;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;

public class CommonUtil {
    //public static final Object SEND = new Object();
    //public static final Object RECEIVE = new Object();
    public static volatile Boolean RUNNING = true;
    public static final ArrayBlockingQueue<String> SEND_QUEUE = new ArrayBlockingQueue<>(10);
    public static volatile SocketChannel socketChannel;
    public static volatile EventLoopGroup worker;
    public static volatile EventLoopGroup boss;
    public static final CountDownLatch waitStartUp = new CountDownLatch(1);


    public static void addMsg(String msg) throws InterruptedException {
        synchronized (SEND_QUEUE) {
            SEND_QUEUE.put(msg);
            SEND_QUEUE.notifyAll();
        }

    }

    public static void sendServerMsg(String msg) {
        Map<String, SocketChannel> channels = GatewayService.getChannels();
        Set<Map.Entry<String, SocketChannel>> entries = channels.entrySet();
        Iterator<Map.Entry<String, SocketChannel>> iterator = entries.iterator();
        Map.Entry<String, SocketChannel> entry = null;
        SocketChannel socketChannel = null;
        while (iterator.hasNext()) {
            entry = iterator.next();
            socketChannel = entry.getValue();
            socketChannel.writeAndFlush(msg);
        }
    }

    public static void closeClient() {
        RUNNING = false;
        synchronized (SEND_QUEUE) {
            SEND_QUEUE.notifyAll();
        }
        //socketChannel.close();
    }

    public static void closeServer() {
        RUNNING = false;
        synchronized (SEND_QUEUE) {
            SEND_QUEUE.notifyAll();
        }
        boss.shutdownGracefully();
        worker.shutdownGracefully();

    }
}
