package com.fan.netty.demo.chapter4;

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

public class PlainNIOServer {

    public static void main(String[] args) throws IOException {
        PlainNIOServer server = new PlainNIOServer();
        server.serve(6666);
    }

    public void serve(int port) throws IOException {
        System.out.println("listening for connections on port " + port);
        ServerSocketChannel socketChannel = null;
        Selector selector = null;


        //bind server to port
        socketChannel = ServerSocketChannel.open();
        socketChannel.configureBlocking(false);
        socketChannel.bind(new InetSocketAddress(port));

        //open selector to handle channel
        selector = Selector.open();
        //Register Server socket to selector and specify that it is interested in new  accepted clients
        socketChannel.register(selector, SelectionKey.OP_ACCEPT);
        final ByteBuffer msg = ByteBuffer.wrap("Hi!\r\n".getBytes());

        while(true){
            try{
                //wait for new evens that are ready for process. this will block until something happen
                selector.select();
            }catch (IOException e){
                break;
            }

            //obtain all selectionkey instances that received events
            Set<SelectionKey> readyKeys = selector.selectedKeys();
            Iterator<SelectionKey>  keyIterator = readyKeys.iterator();
            while(keyIterator.hasNext()){
                SelectionKey selectionKey = keyIterator.next();
                keyIterator.remove();

                try{
                    if(selectionKey.isAcceptable()){//check if event was new client ready to get accepted
                        ServerSocketChannel serverChannel = (ServerSocketChannel) selectionKey.channel();

                        //accept client and register it to selector
                        SocketChannel client  = serverChannel.accept();
                        System.out.println("accepted connection from " + client);

                        client.configureBlocking(false);
                        client.register(selector, SelectionKey.OP_WRITE|SelectionKey.OP_READ,
                                msg.duplicate()); //

                    }
                    if(selectionKey.isWritable()){//check if event was ready to write data
                        SocketChannel channel = (SocketChannel) selectionKey.channel();
                        ByteBuffer buffer = (ByteBuffer) selectionKey.attachment();
                        while(buffer.hasRemaining()){
                            //write data to connected client. this may not write all the data if the network
                            //is saturated. if so it will pick up the not-written data and write it once the network is
                            //writable again
                            if(channel.write(buffer) == 0){
                                break;
                            }
                        }
                        channel.close(); //close connection


                    }
                }catch (IOException e){
                    selectionKey.cancel();
                    selectionKey.channel().close();
                }finally {

                }
            }
        }

    }
}
