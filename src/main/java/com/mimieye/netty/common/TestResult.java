package com.mimieye.netty.common;

import com.mimieye.netty.client.NettyClientTest;
import io.netty.channel.socket.SocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

public class TestResult implements Callable {
    private static Logger logger = LoggerFactory.getLogger(NettyClientTest.class);

    private SocketChannel socketChannel;
    private String result;

    public TestResult(SocketChannel socketChannel, String result) {
        this.socketChannel = socketChannel;
        this.result = result;
    }

    @Override
    public Object call() throws Exception {
        String name = Thread.currentThread().getName();
        logger.info(name + "-线程[结果队列]返回处理结果.");
        if(socketChannel == null) {
            logger.info("异常, 无socketChannel.");
            return null;
        }
        String channelId = socketChannel.id().asLongText();
        String resp = "channelId: " + channelId + ", msg: " + result;
        logger.info(name + "-线程[结果队列]返回的处理结果是: " + resp);
        //ByteBuf byteBuf = socketChannel.alloc().buffer(resp.getBytes("UTF-8").length);
        //byteBuf.writeBytes(resp.getBytes("UTF-8"));
        //socketChannel.writeAndFlush(byteBuf);
        socketChannel.writeAndFlush(result);
        return null;
    }
}
