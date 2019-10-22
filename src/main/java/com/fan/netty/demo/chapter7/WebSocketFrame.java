package com.fan.netty.demo.chapter7;

import io.netty.buffer.ByteBuf;

public final class WebSocketFrame {

    public enum FrameType{
        BINARY,
        CLOSE,
        PING,
        PONG,
        TEXT,
        CONTINUATION
    }

    private final FrameType type;
    private final ByteBuf data;

    public WebSocketFrame(FrameType type, ByteBuf data){
        this.type = type;
        this.data = data;
    }

    public FrameType getType(){
        return type;
    }

    public ByteBuf getData(){
        return data;
    }
}
