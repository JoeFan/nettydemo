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
            Thread.sleep(1000);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("%%%%%%%%%%%%4"+bufferedReader.readLine() + " $$$$$4");
        } catch (IOException e) {
            log.error("exception {0}", e.getLocalizedMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
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
