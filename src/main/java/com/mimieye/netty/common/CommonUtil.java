package com.mimieye.netty.common;

import com.mimieye.netty.client.NettyClientTest;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static com.mimieye.netty.common.ThreadPool.*;
import static io.netty.util.CharsetUtil.UTF_8;

public class CommonUtil {
    private static Logger logger = LoggerFactory.getLogger(NettyClientTest.class);

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

    public static void sendServerMsg(ByteBuf buf) {
        Iterator<Map.Entry<String, SocketChannel>> iterator = getIterator();
        Map.Entry<String, SocketChannel> entry = null;
        SocketChannel socketChannel = null;
        while (iterator.hasNext()) {
            entry = iterator.next();
            socketChannel = entry.getValue();
            socketChannel.writeAndFlush(buf);
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

        logger.info("TASK_POOL - " + getTaskStatus());
        logger.info("RESULT_POOL - " + getResultStatus());

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


        logger.info("TASK_POOL - " + getTaskStatus());
        logger.info("RESULT_POOL - " + getResultStatus());
        logger.info("boss - " + boss.isTerminated());
        logger.info("worker - " + worker.isTerminated());

        //ObjectCleaner.
    }

    public static ByteBuf wapperByteBuf(String msg) {
        if(msg == null) {
            return Unpooled.wrappedBuffer(new byte[]{});
        }
        byte[] msgBytes = msg.getBytes(UTF_8);
        int length = msgBytes.length;
        // magicNumber(int) + bodyLength(int) + xor(byte) + arithmetic(byte) + moduleId(short) + msgType(short)
        ByteBuf byteBuf = Unpooled.buffer(4 + 4 + 1 + 1 + 2 + 2 +length);
        byteBuf.writeInt(20180738);
        byteBuf.writeInt(length);
        byteBuf.writeByte((byte) 0);
        byteBuf.writeByte((byte) 1);
        byteBuf.writeShort((short) 4);
        byteBuf.writeShort((short) 7);
        byteBuf.writeBytes(msgBytes);
        byte[] bytes = byteBuf.array();
        System.out.println(Arrays.toString(bytes));
        return byteBuf;
    }
}
