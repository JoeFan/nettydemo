package com.fan.netty.demo.chapter1.io;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

@Slf4j
public class PlainNioEchoServer {

    public static void main(String[] args) {
        PlainNioEchoServer server = new PlainNioEchoServer();
        server.serve(6666);
    }

    public void serve(int port) {
        System.out.println("Listening for connections on port " + port);
        Selector selector = null;
        ServerSocketChannel serverChannel = null;
        try {
            serverChannel = ServerSocketChannel.open();
            ServerSocket ss = serverChannel.socket();

            //#1. bind server to the port
            InetSocketAddress address = new InetSocketAddress(port);
            ss.bind(address);

            serverChannel.configureBlocking(false);
            selector = Selector.open();
            //#2 register the channel with the selector to be interested in new client connections
            //that get accepted
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);

            while (true) {
                try {
                    //#3. block until something is selected
                    selector.select();

                } catch (IOException e) {
                    log.error("exception {0}", e.getLocalizedMessage());
                    break;
                }

                //#4 get all SelectedKey instances
                Set readKeys = selector.selectedKeys();
                Iterator iterator = readKeys.iterator();

                while (iterator.hasNext()) {
                    //#5. remove the SelectedKey from the iterator
                    SelectionKey key = (SelectionKey) iterator.next();
                    iterator.remove();

                    try {
                        if (key.isAcceptable()) {
                            //#6 accept the client connection
                            ServerSocketChannel server = (ServerSocketChannel) key.channel();
                            SocketChannel client = server.accept();
                            System.out.println("Accepted connection from " + client);
                            client.configureBlocking(false);

                            //#7 register conneciton to selector and set ByteBuffer
                            client.register(selector, SelectionKey.OP_WRITE | SelectionKey.OP_READ, ByteBuffer.allocate(100));
                        }

                        //#8 check for selectedKey for read
                        if (key.isReadable()) {
                            SocketChannel client = (SocketChannel) key.channel();
                            ByteBuffer output = (ByteBuffer) key.attachment();
                            //#9 read data to ByteBuffer
                            client.read(output);
                            System.out.println(new String(output.array()));
                        }

                        //#10 check for selectedkey for write
                        if (key.isWritable()) {
                            SocketChannel client = (SocketChannel) key.channel();
                            ByteBuffer output = (ByteBuffer) key.attachment();
                            output.flip();
                            //#11 write data from ByteBuffer to channel
                            client.write(output);
                            output.compact();

                        }

                    } catch (IOException e) {
                        log.error("excetion {0}", e);
                    } finally {
                        key.cancel();
                        try {

                            key.channel().close();
                        } catch (IOException e) {
                            log.error("exception {0}", e);
                        }
                    }
                }

            }

        } catch (IOException e) {

            log.error("io exception {0}", e.getLocalizedMessage());
        }

    }
}
