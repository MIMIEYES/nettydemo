package com.mimieye.netty.common;

import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.AttributeKey;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * Created by Pierreluo on 2017/12/6.
 */
public class MyChannelInitializer<T extends ChannelInboundHandlerAdapter> extends ChannelInitializer<SocketChannel> {
    private T t;

    public MyChannelInitializer(T t){
        this.t = t;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        System.out.println("mychannel info - socketChannel:" + socketChannel.hashCode() + " mychannel:" + this.hashCode() + " t:" + t.hashCode());
        ChannelPipeline p = socketChannel.pipeline();
        //p.addLast("decoder0",new MsgDecoder());
        p.addLast("decoder1",new LengthFieldBasedFrameDecoder(1024 * 1024, 4, 4, 6, 0));
        //p.addLast("decoder1",new LengthFieldBasedFrameDecoder(1024 * 1024, 0, 8, 0, 8));
        p.addLast("encoder0",new LengthFieldPrepender(8, false));
        //p.addLast("encoder0",new MsgEncoder());
        //p.addLast("encoder1",new StringEncoder(Charset.forName("UTF-8")));
        p.addLast(t);

/*        p.names().stream().forEach((msg)->{
            System.out.println(msg);
        });*/
    }
}
