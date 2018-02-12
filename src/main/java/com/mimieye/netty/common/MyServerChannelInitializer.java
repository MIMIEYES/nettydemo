package com.mimieye.netty.common;

import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringEncoder;

import java.nio.charset.Charset;

/**
 * Created by Pierreluo on 2017/12/6.
 */
public class MyServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    private Class<? extends ChannelInboundHandlerAdapter> t;
    public MyServerChannelInitializer(Class<? extends ChannelInboundHandlerAdapter> t){
        this.t = t;
    }

    public MyServerChannelInitializer(){

    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelInboundHandlerAdapter channelInboundHandlerAdapter = t.newInstance();
        //System.out.println("mychannel info - socketChannel:" + socketChannel.hashCode() + " mychannel:" + this.hashCode() + " t:" + channelInboundHandlerAdapter.hashCode());
        ChannelPipeline p = socketChannel.pipeline();
        p.addLast("decoder",new LengthFieldBasedFrameDecoder(1024 * 1024, 0, 8, 0, 8));
        p.addLast("encoder0",new LengthFieldPrepender(8, false));
        p.addLast("encoder1",new StringEncoder(Charset.forName("UTF-8")));
        p.addLast(channelInboundHandlerAdapter);

/*        p.names().stream().forEach((msg)->{
            System.out.println(msg);
        });*/
    }
}
