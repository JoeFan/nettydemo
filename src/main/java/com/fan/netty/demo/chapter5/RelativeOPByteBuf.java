package com.fan.netty.demo.chapter5;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;

public class RelativeOPByteBuf {

    public static void main(String[] args) {
        Charset utf8 = CharsetUtil.UTF_8;
        ByteBuf buf = Unpooled.copiedBuffer("Netty in Action rocks!", utf8);

        System.out.println((char)buf.getByte(0));

        int readerIndex = buf.readerIndex();
        int writerIndex = buf.writerIndex();

        buf.setByte(0, (byte)'B');
        System.out.println((char)buf.getByte(0));

        System.out.println("readindex "+ readerIndex + " writeindex "+ writerIndex);
        assert  readerIndex == buf.readerIndex();
        assert writerIndex == buf.writerIndex();
    }
}