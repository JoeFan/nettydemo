package com.fan.netty.demo.chapter2;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;

@Slf4j
@ChannelHandler.Sharable //share between channels
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext context, Object message){
        ByteBuf byteBuf = (ByteBuf) message;
        ByteBuffer buffer = byteBuf.nioBuffer();
        while(buffer.hasRemaining()){
            System.out.println((char)buffer.get());
        }
        //write the received message back. this will not flush the message to remote peer yet
        context.write(message);
        System.out.println("Server received: " + message);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx){
        //flush all previous written messages to the remote peer, and close channel after the operation is complete
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
                .addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        log.error("exception ", cause);
        ctx.close();

    }
}
