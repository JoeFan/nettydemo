package com.fan.netty.demo.chapter1.io;

import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

@Slf4j
public class ByteBufferDemo {

    public static void main(String[] args) {
        FileChannel inChannel = null;
        ByteBuffer byteBuffer = null;
        try {
            RandomAccessFile filePath = new RandomAccessFile(ByteBufferDemo.class.getResource("/").getPath()
                    + "filechannel.txt", "rw");
            log.error(ByteBufferDemo.class.getResource("/").getPath());
            System.out.println(ByteBufferDemo.class.getResource("/"));
            inChannel = filePath.getChannel();
            byteBuffer = ByteBuffer.allocate(48);
            int byteReads  = inChannel.read(byteBuffer);
            if(byteReads != -1){
                byteBuffer.flip();
                while(byteBuffer.hasRemaining()){
                    System.out.println((char) byteBuffer.get());
                }
            }
            byteBuffer.clear();

        } catch (FileNotFoundException e) {
            log.error("file not found {0}", e.getLocalizedMessage());
        } catch (IOException e) {
            log.error("io excepiton {0}", e.getLocalizedMessage());
        } finally {

            try {
                if (inChannel != null) {
                    inChannel.close();
                    inChannel = null;
                }
            } catch (IOException e) {
                log.error("close channel has exception", e.getLocalizedMessage());
            }
        }

    }
}
