package com.fan.netty.demo.chapter5;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class WriteHandler extends ChannelHandlerAdapter {

    private ChannelHandlerContext ctx;

    @Override
    public void handlerAdded(ChannelHandlerContext ctx){
        this.ctx = ctx; //sotre reference to channelHandlercontext for later use
    }

    public void send(String msg){
        ctx.write(msg); //send message using previously stored channelhandlercontext
    }
}
