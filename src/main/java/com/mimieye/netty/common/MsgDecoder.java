/**
 * MIT License
 * <p>
 * Copyright (c) 2017-2018 nuls.io
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.mimieye.netty.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.util.List;

/**
 * @desription:
 * @author: PierreLuo
 * @date: 2018/8/6
 */
public class MsgDecoder extends ByteToMessageDecoder {

    private MyLengthFieldBasedFrameDecoder oldDecoder = new MyLengthFieldBasedFrameDecoder(1024 * 1024, 0, 8, 0, 8);
    private MyLengthFieldBasedFrameDecoder newDecoder = new MyLengthFieldBasedFrameDecoder(1024 * 1024, 4, 4, 6, 0);


    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int number = in.getInt(0);
        //System.out.println("==========================================");
        //int number = in.readInt();
        //p.addLast("decoder1",new LengthFieldBasedFrameDecoder(1024 * 1024, 4, 4, 6, 0));
        //p.addLast("decoder2",new LengthFieldBasedFrameDecoder(1024 * 1024, 0, 8, 0, 8));
        if(20180738 == number) {
            // 新版本
            //try {
            //    ctx.pipeline().remove("decoder1");
            //} catch (Exception e) {
            //
            //}
            //ctx.pipeline().addAfter("decoder0", "decoder1", new LengthFieldBasedFrameDecoder(1024 * 1024, 4, 4, 6, 0));
            System.out.println("================[new decoder]==================");
            Object decoded = newDecoder.decode(ctx, in);
            if (decoded != null) {
                out.add(decoded);
            }
        } else {
            number = in.getInt(8);
            if(20180738 == number) {
                // 老版本
                //try {
                //    ctx.pipeline().remove("decoder1");
                //} catch (Exception e) {
                //
                //}
                //ctx.pipeline().addAfter("decoder0", "decoder1", new LengthFieldBasedFrameDecoder(1024 * 1024, 0, 4, 0, 4));
                System.out.println("================[old decoder]==================");
                Object decoded = oldDecoder.decode(ctx, in);
                if (decoded != null) {
                    out.add(decoded);
                }
            }
        }
    }
}
