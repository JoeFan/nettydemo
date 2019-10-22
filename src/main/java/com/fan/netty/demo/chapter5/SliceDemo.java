package com.fan.netty.demo.chapter5;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;

public class SliceDemo {

    public static void main(String[] args) {
        Charset utf8= Charset.forName("UTF-8");
        //create ByteBuf which holds bytes for given string
        ByteBuf byteBuf = Unpooled.copiedBuffer("Netty in action rocks!", CharsetUtil.UTF_8);

        //create new slice of ByteBuf which starts at index 0 and ends at index 14
        ByteBuf sliced = byteBuf.slice(0, 15);
        //contains netty in action
        System.out.println(sliced.toString(utf8));

        //update byte on index 0
        byteBuf.setByte(0, (byte)'J');

        System.out.println((char)sliced.getByte(0));
        //won't fail as both ByteBuf share the same content and so modifications to
        //one of them are visible on the other too
        assert byteBuf.getByte(0) == sliced.getByte(0);
    }
}
