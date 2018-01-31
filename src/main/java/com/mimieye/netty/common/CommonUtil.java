package com.mimieye.netty.common;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.SocketChannel;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static com.mimieye.netty.common.ThreadPool.*;

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
        Iterator<Map.Entry<String, SocketChannel>> iterator = getIterator();
        Map.Entry<String, SocketChannel> entry = null;
        SocketChannel socketChannel = null;
        while (iterator.hasNext()) {
            entry = iterator.next();
            socketChannel = entry.getValue();
            socketChannel.writeAndFlush(msg);
        }
    }

    public static Iterator<Map.Entry<String, SocketChannel>> getIterator() {
        Map<String, SocketChannel> channels = GatewayService.getChannels();
        Set<Map.Entry<String, SocketChannel>> entries = channels.entrySet();
        Iterator<Map.Entry<String, SocketChannel>> iterator = entries.iterator();
        return iterator;
    }

    public static void closeClient() {
        // 关闭消息发送队列的监听
        RUNNING = false;
        synchronized (SEND_QUEUE) {
            SEND_QUEUE.notifyAll();
        }

        // 关闭连接
        if(!socketChannel.isShutdown())
            socketChannel.close();

        // 关闭任务池和结果池
        ThreadPool.closePool();

        System.out.println("TASK_POOL - " + getTaskStatus());
        System.out.println("RESULT_POOL - " + getResultStatus());

    }

    public static void closeServer() {

        // 关闭消息发送队列的监听
        RUNNING = false;
        synchronized (SEND_QUEUE) {
            SEND_QUEUE.notifyAll();
        }

        // 与客户端断连
        Map<String, SocketChannel> channels = GatewayService.getChannels();
        Iterator<Map.Entry<String, SocketChannel>> iterator = getIterator();
        Map.Entry<String, SocketChannel> entry = null;
        SocketChannel socketChannel = null;
        while (iterator.hasNext()) {
            entry = iterator.next();
            channels.remove(entry.getKey());
            socketChannel = entry.getValue();
            socketChannel.disconnect();
        }

        // 关闭服务
        Future<?> bossFutrue = boss.shutdownGracefully();
        Future<?> workerFutrue = worker.shutdownGracefully();
        try {
            bossFutrue.get();
            workerFutrue.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        // 关闭任务池和结果池
        ThreadPool.closePool();


        System.out.println("TASK_POOL - " + getTaskStatus());
        System.out.println("RESULT_POOL - " + getResultStatus());
        System.out.println("boss - " + boss.isTerminated());
        System.out.println("worker - " + worker.isTerminated());

        //ObjectCleaner.
    }
}
