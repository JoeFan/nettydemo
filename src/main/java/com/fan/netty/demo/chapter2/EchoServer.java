package com.fan.netty.demo.chapter2;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

@Slf4j
public class EchoServer {

    private final int port;

    public EchoServer(int port){
        this.port = port;
    }

    public void start(){
        EventLoopGroup group = new NioEventLoopGroup();
        try{
            ServerBootstrap bootstrap = new ServerBootstrap();
            //1. bootstrap the server
            bootstrap.group(group)
                    .channel(NioServerSocketChannel.class) //2. specifies nio transport, local socket address
                    .localAddress(new InetSocketAddress(port))
                    //3. add handler to channel pipleline
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new EchoServerHandler());
                        }
                    });

            ChannelFuture f = bootstrap.bind().sync();
            System.out.println(EchoServer.class.getName() + " started and listened on " + f.channel().localAddress());
            //4.binds to server waits for server to close, and release resource
            f.channel().closeFuture().sync();
        }catch (InterruptedException e) {
            log.error("exception ", e.getLocalizedMessage());
        } finally{
            try {
                group.shutdownGracefully().sync();
            } catch (InterruptedException e) {
                log.error("exception ", e.getLocalizedMessage());
            }
        }
    }

    public static void main(String[] args) {
        if(args.length != 1){
            System.out.println("Usage: " + EchoServer.class.getSimpleName()+" <port>");
        }

        int port = Integer.parseInt(args[0]);
        new EchoServer(port).start();
    }
}
