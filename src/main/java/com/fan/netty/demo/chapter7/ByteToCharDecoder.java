package com.fan.netty.demo.chapter7;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class ByteToCharDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx,
                          ByteBuf in, List<Object> list) throws Exception {
        while(in.readableBytes() >= 2){
            //write char into message buf
            list.add(Character.valueOf(in.readChar()));
        }
    }
}
