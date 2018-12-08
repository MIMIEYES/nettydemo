package com.mimieye.netty.client;

import com.mimieye.netty.common.CommonUtil;
import com.mimieye.netty.common.ThreadPool;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;

import static com.mimieye.netty.common.CommonUtil.closeClient;

/**
 * Created by Pierreluo on 2017/12/6.
 */
public class RequestMsgClientHandler extends SimpleChannelInboundHandler {
    private static Logger logger = LoggerFactory.getLogger(NettyClientTest.class);

    public void channelRegistered(io.netty.channel.ChannelHandlerContext ctx) throws java.lang.Exception {
        super.channelRegistered(ctx);
        System.out.println("---------------client channelRegistered");
        System.out.println("--------------------------------isReg:"+ ctx.channel().isRegistered());
        System.out.println("--------------------------------isOpen:"+ ctx.channel().isOpen());
        System.out.println("--------------------------------isActive:"+ ctx.channel().isActive());
        System.out.println("--------------------------------isWritable:"+ ctx.channel().isWritable());
        SocketChannel socketChannel = (SocketChannel)ctx.channel();
        System.out.println("--------------------------------isShutdown:"+ socketChannel.isShutdown());
    }

    public void channelActive(io.netty.channel.ChannelHandlerContext ctx) throws java.lang.Exception {
        super.channelActive(ctx);
        System.out.println("---------------client channelActive");
        System.out.println("--------------------------------isReg:"+ ctx.channel().isRegistered());
        System.out.println("--------------------------------isOpen:"+ ctx.channel().isOpen());
        System.out.println("--------------------------------isActive:"+ ctx.channel().isActive());
        System.out.println("--------------------------------isWritable:"+ ctx.channel().isWritable());
        SocketChannel socketChannel = (SocketChannel)ctx.channel();
        System.out.println("--------------------------------isShutdown:"+ socketChannel.isShutdown());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

        System.out.println("---------------client channelInactive");
        System.out.println("--------------------------------isReg:"+ ctx.channel().isRegistered());
        System.out.println("--------------------------------isOpen:"+ ctx.channel().isOpen());
        System.out.println("--------------------------------isActive:"+ ctx.channel().isActive());
        System.out.println("--------------------------------isWritable:"+ ctx.channel().isWritable());
        SocketChannel socketChannel = (SocketChannel)ctx.channel();
        System.out.println("--------------------------------isShutdown:"+ socketChannel.isShutdown());
        closeClient();
    }

    public void channelUnregistered(io.netty.channel.ChannelHandlerContext ctx) throws java.lang.Exception {
        System.out.println("---------------client channelUnregistered");
        System.out.println("--------------------------------isReg:"+ ctx.channel().isRegistered());
        System.out.println("--------------------------------isOpen:"+ ctx.channel().isOpen());
        System.out.println("--------------------------------isActive:"+ ctx.channel().isActive());
        System.out.println("--------------------------------isWritable:"+ ctx.channel().isWritable());
        SocketChannel socketChannel = (SocketChannel)ctx.channel();
        System.out.println("--------------------------------isShutdown:"+ socketChannel.isShutdown());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws UnsupportedEncodingException {
        // 收到server回复的消息
        ByteBuf buf = (ByteBuf) msg;
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        String resp = new String(bytes, "UTF-8");
        //buf.release();
        ReferenceCountUtil.release(msg);

        // 异步处理服务端消息
        ThreadPool.addTask(CommonUtil.socketChannel, resp);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {

    }


    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        String uuid = ctx.channel().id().asLongText();
        String ip = ctx.channel().remoteAddress().toString();
    }
}

