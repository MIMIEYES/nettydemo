package com.mimieye.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;

import java.io.UnsupportedEncodingException;

public class ClientChannelHandler extends ChannelInboundHandlerAdapter {

    private AttributeKey<String> key;
    public ClientChannelHandler(AttributeKey<String> key) {
        this.key = key;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String result = ctx.channel().attr(key).get();
        System.out.println("===============key: "+ result);
        String result0 = ctx.attr(key).get();
        System.out.println("===============Context-key: "+ result0);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("1111111111");
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("2222222222");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("33333333333");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws UnsupportedEncodingException {

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.fireChannelReadComplete();
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        String uuid = ctx.channel().id().asLongText();
        String ip = ctx.channel().remoteAddress().toString();
        //TODO 是否关闭连接

    }

}
