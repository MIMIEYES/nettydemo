package com.mimieye.netty.client;

import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringEncoder;

import java.nio.charset.Charset;

/**
 * Created by Pierreluo on 2017/12/6.
 */
public class MyChannelInitializer<T extends ChannelInboundHandlerAdapter> extends ChannelInitializer<SocketChannel> {
    private T t;

    public MyChannelInitializer(T t){
        this.t = t;
    }

    public MyChannelInitializer(){

    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline p = socketChannel.pipeline();
        p.addLast("decoder",new LengthFieldBasedFrameDecoder(1024 * 1024, 0, 8, 0, 8));
        p.addLast("encoder0",new LengthFieldPrepender(8, false));
        p.addLast("encoder1",new StringEncoder(Charset.forName("UTF-8")));
        p.addLast(t);
    }
}
