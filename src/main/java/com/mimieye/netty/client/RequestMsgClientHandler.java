package com.mimieye.netty.client;

import com.mimieye.netty.common.CommonUtil;
import com.mimieye.netty.common.ThreadPool;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import static com.mimieye.netty.common.CommonUtil.closeClient;

/**
 * Created by Pierreluo on 2017/12/6.
 */
public class RequestMsgClientHandler extends ChannelInboundHandlerAdapter {
    private static Logger logger = LoggerFactory.getLogger(NettyClientTest.class);

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("客户端检测到服务端连接关闭.");
        closeClient();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws UnsupportedEncodingException {
        // 收到server回复的消息
        logger.info("receive server msg: ");
        ByteBuf buf = (ByteBuf) msg;
        logger.info("magic number is " + buf.readInt());
        logger.info("msg length is " + buf.readInt());
        logger.info("xor is " + buf.readByte());
        logger.info("arithmetic is " + buf.readByte());
        logger.info("moduleId is " + buf.readShort());
        logger.info("msgType is " + buf.readShort());
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        String resp = new String(bytes, "UTF-8");
        logger.info(resp);
        //buf.release();
        ReferenceCountUtil.release(msg);

        // 异步处理服务端消息
        //ThreadPool.addTask(CommonUtil.socketChannel, resp);
    }


    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        String uuid = ctx.channel().id().asLongText();
        String ip = ctx.channel().remoteAddress().toString();
        logger.error("服务端-" + ip + " | " + uuid + "异常.", cause);
        //TODO 是否关闭连接

    }
}

