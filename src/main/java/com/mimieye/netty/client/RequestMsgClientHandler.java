package com.mimieye.netty.client;

import com.mimieye.netty.common.CommonUtil;
import com.mimieye.netty.common.ThreadPool;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.UnsupportedEncodingException;

import static com.mimieye.netty.common.CommonUtil.closeClient;

/**
 * Created by Pierreluo on 2017/12/6.
 */
public class RequestMsgClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端检测到服务端连接关闭.");
        closeClient();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws UnsupportedEncodingException {
        // 收到server回复的消息
        System.out.print("receive server msg: ");
        ByteBuf buf = (ByteBuf) msg;
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        String resp = new String(bytes, "UTF-8");
        System.out.println(resp);
        buf.release();

        // 异步处理服务端消息
        ThreadPool.addTask(CommonUtil.socketChannel, resp);
    }


    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}

