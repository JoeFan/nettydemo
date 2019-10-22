package com.fan.netty.demo.chapter7;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

public class IntegerToStringDecoder extends MessageToMessageDecoder<Integer> {

    @Override
    protected void decode(ChannelHandlerContext ctx, Integer integer,
                          List<Object> out) throws Exception {
        out.add(String.valueOf(integer));

    }
}
