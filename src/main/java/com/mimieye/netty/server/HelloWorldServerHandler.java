package com.mimieye.netty.server;

import com.mimieye.netty.client.NettyClientTest;
import com.mimieye.netty.common.GatewayService;
import com.mimieye.netty.common.ThreadPool;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;

/**
 * Created by Pierreluo on 2017/12/6.
 */
public class HelloWorldServerHandler extends ChannelInboundHandlerAdapter {

    private static Logger logger = LoggerFactory.getLogger(NettyClientTest.class);

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        String uuid = ctx.channel().id().asLongText();
        logger.debug("服务端检测到客户端连接关闭.ID: " + uuid);
        GatewayService.removeGatewayChannel(uuid);
        logger.debug("当前容量：" + GatewayService.getChannels().size());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws UnsupportedEncodingException {
        logger.debug("receive client msg:");
        ByteBuf buf = (ByteBuf) msg;
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        String strMsg = new String(bytes, "UTF-8");
        logger.debug(strMsg);
        buf.release();

        // 异步处理客户端消息
        String uuid = ctx.channel().id().asLongText();
        SocketChannel socketChannel = GatewayService.getGatewayChannel(uuid);

        ThreadPool.addTask(socketChannel, strMsg);

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String uuid = ctx.channel().id().asLongText();
        GatewayService.addGatewayChannel(uuid, (SocketChannel)ctx.channel());
        logger.debug("a new connect come in: " + uuid);
    }

}


