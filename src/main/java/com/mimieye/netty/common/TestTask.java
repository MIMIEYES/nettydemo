package com.mimieye.netty.common;

import com.mimieye.netty.client.NettyClientTest;
import io.netty.channel.socket.SocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

public class TestTask implements Callable {
    private static Logger logger = LoggerFactory.getLogger(NettyClientTest.class);

    private SocketChannel socketChannel;
    private String msg;

    public TestTask(SocketChannel socketChannel, String msg) {
        this.socketChannel = socketChannel;
        this.msg = msg;
    }

    @Override
    public Object call() throws Exception {
        String name = Thread.currentThread().getName();
        System.out.println(name + "-线程[任务队列]开始处理消息.");
        System.out.println(name + "-线程[任务队列]正在处理的消息 -> " + msg + "");
        //TimeUnit.SECONDS.sleep(3);
        System.out.println(name + "-线程[任务队列]任务处理结束.");
        String result = "r0r" + msg + "-> 正常处理.";

        String flag = msg.substring(0,3);
        if(!"r0r".equals(flag)) {
            // 异步回复消息
            ThreadPool.addResult(socketChannel, result);
        }
        return null;

    }
}
