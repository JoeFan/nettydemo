package com.fan.netty.demo.chapter7;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.handler.codec.http.websocketx.*;

import java.util.List;

@ChannelHandler.Sharable
public class WebSocketConvertHandler extends MessageToMessageCodec<io.netty.handler.codec.http.websocketx.WebSocketFrame,
        WebSocketFrame> {

    public static final WebSocketConvertHandler INSTANCE =
            new WebSocketConvertHandler();


    @Override
    protected void encode(ChannelHandlerContext ctx,
                          WebSocketFrame msg, List<Object> out) throws Exception {
        switch (msg.getType()){
            case BINARY:
                out.add(new BinaryWebSocketFrame(msg.getData()));
                return;
            case TEXT:
                out.add(new TextWebSocketFrame(msg.getData()));
                return;
            case CLOSE:
                out.add(new CloseWebSocketFrame(true, 0, msg.getData()));
            case CONTINUATION:
                out.add(new ContinuationWebSocketFrame(msg.getData()));
                return;
            case PONG:
                out.add(new PongWebSocketFrame(msg.getData()));
                return;
            case PING:
                out.add(new PingWebSocketFrame(msg.getData()));
                return;
            default:
                throw new IllegalStateException("Unsupported websocket msg " + msg);
        }

    }

    @Override
    protected void decode(ChannelHandlerContext ctx, io.netty.handler.codec.http.websocketx.WebSocketFrame msg,
                          List<Object> out) throws Exception {
        if(msg instanceof BinaryWebSocketFrame){
            out.add(new WebSocketFrame(WebSocketFrame.FrameType.BINARY, msg.content().copy()));
            return;
        }
        if(msg instanceof CloseWebSocketFrame){
            out.add(new WebSocketFrame(WebSocketFrame.FrameType.CLOSE, msg.content().copy()));
            return;
        }
        if(msg instanceof PingWebSocketFrame){
            out.add(new WebSocketFrame(WebSocketFrame.FrameType.PING, msg.content().copy()));
            return;
        }
        if(msg instanceof PongWebSocketFrame){
            out.add(new WebSocketFrame(WebSocketFrame.FrameType.PONG, msg.content().copy()));
            return;
        }
        if(msg instanceof TextWebSocketFrame){
            out.add(new WebSocketFrame(WebSocketFrame.FrameType.TEXT, msg.content().copy()));
            return;
        }
        if(msg instanceof ContinuationWebSocketFrame){
            out.add(new WebSocketFrame(WebSocketFrame.FrameType.CONTINUATION,msg.content().copy()));
        }

        throw new IllegalStateException("Unsupported websocket msg "+ msg);
    }
}
