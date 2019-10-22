package com.fan.netty.demo.chapter4;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.oio.OioServerSocketChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;

public class NettyOioServer {

    public static void main(String[] args) {
        NettyOioServer server = new NettyOioServer();
        server.server(6666);
    }

    public void server(int port){
        final ByteBuf buf = Unpooled.unreleasableBuffer(
                Unpooled.copiedBuffer("Hi!\r\n", CharsetUtil.UTF_8)
        );

        EventLoopGroup group = new OioEventLoopGroup();

        try{
            //create ServerBootstrap to allow bootstrap to server instance
            ServerBootstrap bootstrap = new ServerBootstrap();
            //user OioEventLoopGroup to allow blocking mode
            bootstrap.group(group)
                    .channel(OioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    //specify ChannelInitializer that will be called for each accepted connection
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            //add channel handler to intercept events and allow to react on them
                            socketChannel.pipeline().addLast(
                                    new ChannelInboundHandlerAdapter(){
                                        @Override
                                        public void channelActive(ChannelHandlerContext ctx){
                                            //write message to client and add ChannelFutureListener
                                            //to close connection once message return
                                            ctx.write(buf.duplicate())
                                                    .addListener(ChannelFutureListener.CLOSE);
                                            ctx.flush();
                                        }
                                    }
                            );
                        }
                    });
            //bind server to accept connections
            ChannelFuture future =bootstrap.bind().sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //release all resource
            group.shutdownGracefully();
        }

    }
}
