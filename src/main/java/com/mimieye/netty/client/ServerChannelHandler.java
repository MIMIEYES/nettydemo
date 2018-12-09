package com.mimieye.netty.client;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;

@ChannelHandler.Sharable
public class ServerChannelHandler extends SimpleChannelInboundHandler {

    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        System.out.println("---------------server channelRegistered");
        System.out.println("--------------------------------isReg:"+ ctx.channel().isRegistered());
        System.out.println("--------------------------------isOpen:"+ ctx.channel().isOpen());
        System.out.println("--------------------------------isActive:"+ ctx.channel().isActive());
        System.out.println("--------------------------------isWritable:"+ ctx.channel().isWritable());
        SocketChannel socketChannel = (SocketChannel)ctx.channel();
        System.out.println("--------------------------------isShutdown:"+ socketChannel.isShutdown());
        System.out.println(socketChannel.remoteAddress().getAddress());
        System.out.println(socketChannel.remoteAddress().getPort());
    }

    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        System.out.println("---------------server channelActive");
        System.out.println("--------------------------------isReg:"+ ctx.channel().isRegistered());
        System.out.println("--------------------------------isOpen:"+ ctx.channel().isOpen());
        System.out.println("--------------------------------isActive:"+ ctx.channel().isActive());
        System.out.println("--------------------------------isWritable:"+ ctx.channel().isWritable());
        SocketChannel socketChannel = (SocketChannel)ctx.channel();
        System.out.println("--------------------------------isShutdown:"+ socketChannel.isShutdown());

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

        System.out.println("---------------server channelInactive");
        System.out.println("--------------------------------isReg:"+ ctx.channel().isRegistered());
        System.out.println("--------------------------------isOpen:"+ ctx.channel().isOpen());
        System.out.println("--------------------------------isActive:"+ ctx.channel().isActive());
        System.out.println("--------------------------------isWritable:"+ ctx.channel().isWritable());
        SocketChannel socketChannel = (SocketChannel)ctx.channel();
        System.out.println("--------------------------------isShutdown:"+ socketChannel.isShutdown());
    }

    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("---------------server channelUnregistered");
        System.out.println("--------------------------------isReg:"+ ctx.channel().isRegistered());
        System.out.println("--------------------------------isOpen:"+ ctx.channel().isOpen());
        System.out.println("--------------------------------isActive:"+ ctx.channel().isActive());
        System.out.println("--------------------------------isWritable:"+ ctx.channel().isWritable());
        SocketChannel socketChannel = (SocketChannel)ctx.channel();
        System.out.println("--------------------------------isShutdown:"+ socketChannel.isShutdown());
        ctx.close();
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("---------------server exceptionCaught");
        System.out.println("--------------------------------isReg:"+ ctx.channel().isRegistered());
        System.out.println("--------------------------------isOpen:"+ ctx.channel().isOpen());
        System.out.println("--------------------------------isActive:"+ ctx.channel().isActive());
        System.out.println("--------------------------------isWritable:"+ ctx.channel().isWritable());
        SocketChannel socketChannel = (SocketChannel)ctx.channel();
        System.out.println("--------------------------------isShutdown:"+ socketChannel.isShutdown());
        cause.printStackTrace();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {

    }
}
