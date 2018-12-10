package com.mimieye.netty.common;

import com.mimieye.netty.server.HeartbeatServerHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.AttributeKey;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

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
        p.addLast("idle", new IdleStateHandler(0, 0, 10, TimeUnit.SECONDS));
        p.addLast("decoder",new LengthFieldBasedFrameDecoder(1024 * 1024, 0, 8, 0, 8));
        p.addLast("encoder0",new LengthFieldPrepender(8, false));
        p.addLast("encoder1",new StringEncoder(Charset.forName("UTF-8")));
        p.addLast("heartbeat", new HeartbeatServerHandler());
        p.addLast(t);


/*        p.names().stream().forEach((msg)->{
            System.out.println(msg);
        });*/
    }
}
