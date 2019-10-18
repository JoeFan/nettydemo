package com.fan.netty.demo.chapter1.io;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

@Slf4j
public class PlainEchoServer {

    public static void main(String[] args) {
        PlainEchoServer plainEchoServer = new PlainEchoServer();
        try {
            plainEchoServer.server(6666);
        } catch (IOException e) {
            log.error("server start failed");
        }
    }

    public void server(int port) throws IOException {
        final ServerSocket socket = new ServerSocket(port);
        try {
            while (true) {
                final Socket clientSocket = socket.accept();
                System.err.println("Accept connection from " + clientSocket);

                new Thread(() -> {
                    try {
                        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                        writer.write("hi, i am server");
                        writer.flush();
                    } catch (IOException e) {
                        log.error("error {0}", e.getLocalizedMessage());
                    } finally {
                        try {
                            clientSocket.close();
                        } catch (IOException e) {
                            log.error("exception {0}", e.getLocalizedMessage());
                        }
                    }
                }).start();
            }

        } finally {
            socket.close();
        }
    }
}
