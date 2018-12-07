package com.mimieye.netty;

import com.mimieye.netty.client.NettyClientTest;
import com.mimieye.netty.common.GatewayService;
import io.netty.channel.socket.SocketChannel;

import java.io.IOException;

public class NettyDemoTest {

    public static void main(String[] args) throws IOException {
        System.out.println("===1");
        clientStartUp(11111111);
        System.out.println("===2");
        clientStartUp(22222222);
        System.in.read();
    }

    static void clientStartUp(final int id) {
    //    Runnable runnable = new Runnable() {
    //        @Override
    //        public void run() {
    //            NettyClientTest client = new NettyClientTest();
    //            client.startUp();
    //            String channelID = socketChannel.id().asLongText();
    //            GatewayService.clientMap.put(channelID, socketChannel);
    //            System.out.println("================" + channelID);
    //        }
    //    };
    //    Thread thread = new Thread(runnable, "client-"+id+"-pierre");
    //    thread.start();
    }
}
