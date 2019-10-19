package com.fan.netty.demo.chapter1.io;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.Socket;

@Slf4j
public class ClientDemo {
    public static void main(String[] args) {
        Socket socket = null;
        BufferedWriter bufferedWriter = null;
        try {
            socket = new Socket("127.0.0.1", 6666);
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferedWriter.write("hello, i am client");
            bufferedWriter.flush();
        } catch (IOException e) {
            log.error("exception {0}", e.getLocalizedMessage());
        } finally {
            if(bufferedWriter != null){
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                    log.error("exception", e);
                }
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

    }
}
