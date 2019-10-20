package com.fan.netty.demo.chapter4;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

public class PlainOioServer {

    public void serve(int port) throws IOException {
        final ServerSocket socket = new ServerSocket(port);
        try{
            while(true){
                //accept connection
                final Socket clientSocket = socket.accept();
                System.out.println("Accepted connection from clientSocket");

                //create new thread to handle connection
                new Thread(new Runnable(){

                    @Override
                    public void run() {
                        OutputStream out;
                        try{
                            //write message to connected client
                            out = clientSocket.getOutputStream();
                            out.write("Hi!\r\n".getBytes(Charset.forName("UTF-8")));
                            out.flush();
                            //close connection once message written and flushed
                            clientSocket.close();
                        } catch (IOException e) {
                            try {
                                clientSocket.close();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            e.printStackTrace();
                        } finally {

                        }

                    }
                }).start(); //start thread to begin handling
            }
        }finally {

        }
    }

    public static void main(String[] args) throws IOException {
        PlainOioServer server = new PlainOioServer();
        server.serve(6666);
    }
}
