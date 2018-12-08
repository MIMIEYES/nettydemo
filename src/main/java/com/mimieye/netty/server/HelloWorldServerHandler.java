package com.mimieye.netty.server;

import com.mimieye.netty.client.NettyClientTest;
import com.mimieye.netty.common.GatewayService;
import com.mimieye.netty.common.ThreadPool;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * Created by Pierreluo on 2017/12/6.
 */
public class HelloWorldServerHandler extends ChannelInboundHandlerAdapter {

    private static Logger logger = LoggerFactory.getLogger(NettyClientTest.class);

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        String uuid = ctx.channel().id().asLongText();
        GatewayService.removeGatewayChannel(uuid);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws UnsupportedEncodingException {
        ByteBuf buf = (ByteBuf) msg;
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        String strMsg = new String(bytes, "UTF-8");
        //buf.release();
        ReferenceCountUtil.release(msg);


        // 异步处理客户端消息
        String uuid = ctx.channel().id().asLongText();
        SocketChannel socketChannel = GatewayService.getGatewayChannel(uuid);

        ThreadPool.addTask(socketChannel, strMsg);

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        String uuid = ctx.channel().id().asLongText();
        String ip = ctx.channel().remoteAddress().toString();
        //TODO 是否关闭连接
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String uuid = ctx.channel().id().asLongText();
        SocketChannel socketChannel = (SocketChannel) ctx.channel();
        GatewayService.addGatewayChannel(uuid, socketChannel);
        InetSocketAddress socketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        //socketChannel.eventLoop()
    }

}


