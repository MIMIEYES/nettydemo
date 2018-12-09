package com.mimieye.netty.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;

public class ClientChannelHandler extends SimpleChannelInboundHandler {

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

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        System.out.println("---------------client exceptionCaught");
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
