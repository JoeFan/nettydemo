package com.fan.netty.demo.chapter1.io;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class PlainNio2EchoServer {

    public static void main(String[] args) {
        PlainNio2EchoServer server = new PlainNio2EchoServer();
        try {
            server.serve(6666);
        } catch (IOException e) {
            log.error("io exception");
        }
    }

    public void serve(int port) throws IOException {
        System.out.println("Listening for connection on port "+port);

        final AsynchronousServerSocketChannel serverSocketChannel = AsynchronousServerSocketChannel.open();

        InetSocketAddress address = new InetSocketAddress(port);
        //1. bind server to port
        serverSocketChannel.bind(address);

        final CountDownLatch latch = new CountDownLatch(1);

        //2. start to accept new cient connections
        serverSocketChannel.accept(null, new CompletionHandler<AsynchronousSocketChannel, Object>() {

            @Override
            public void completed(final AsynchronousSocketChannel channel, Object attachment) {
                //3. again accept new client connections
                serverSocketChannel.accept(null, this);
                ByteBuffer buffer = ByteBuffer.allocate(100);
                //4. trigger a read operation on the channel, the given completionHandler will be notified once something was read
                channel.read(buffer, buffer, new EchoCompletionHandler(channel));

            }

            @Override
            public void failed(Throwable exc, Object attachment) {
                try {
                    //5. close the socket on error
                    serverSocketChannel.close();
                } catch (IOException e) {

                } finally {
                    latch.countDown();
                }
            }
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().isInterrupted();
        }

    }
}
