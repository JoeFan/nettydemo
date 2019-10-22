package com.fan.netty.demo.chapter5;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;

public class ByteBufCopying {

    public static void main(String[] args) {
        Charset utf8 = Charset.forName("UTF-8");

        ByteBuf buf = Unpooled.copiedBuffer("Netty in Action rocks!", utf8);

        ByteBuf copy = buf.copy(0,15);
        System.out.println(copy.toString(utf8));

        buf.setByte(0, (byte)'J');
        //won't fail as both ByteBuf does not share the same content and so modifications to
        //one of them are not shared
        assert buf.getByte(0) != copy.getByte(0);
    }
}
