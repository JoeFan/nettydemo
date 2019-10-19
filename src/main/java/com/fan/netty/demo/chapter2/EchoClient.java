package com.fan.netty.demo.chapter2;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

@ChannelHandler.Sharable
@Slf4j
public class EchoClient {

    private final String host;
    private final int port;

    public EchoClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start(){
        EventLoopGroup group = new NioEventLoopGroup();

        try{
            //create boot for client
            Bootstrap bootstrap = new Bootstrap();
            //specify eventloopgroup to handle client events. NioGroupLoopGroup is used
            //as the NIO-Transport should be used
            bootstrap.group(group)
                    .channel(NioSocketChannel.class) //specify channel type; use correct one for NIO-Transport
                    .remoteAddress(new InetSocketAddress(host, port)) //specify InteSocketAddress to which client connects
                    //specify channel handler, using ChannelInitializer, called once connection
                    //established and channel created
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            //add EchoClientHandler to ChannelPipeline thant belongs to channel.
                            //ChannelPipeline holds all ChannelHandlers of channel
                            socketChannel.pipeline().addLast(new EchoClientHandler());
                        }
                    });
            //connect client to remote peer; wait until sync() completes connect completes
            ChannelFuture channelFuture = bootstrap.connect().sync();

            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("exception ", e);
        } finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        if(args.length != 2){
            System.out.println("Usage: " + EchoClient.class.getSimpleName() + "<host><port>");
            return;
        }

        //parse options
        final String host = args[0];
        final int port = Integer.parseInt(args[1]);
        new EchoClient(host, port).start();
    }
}
