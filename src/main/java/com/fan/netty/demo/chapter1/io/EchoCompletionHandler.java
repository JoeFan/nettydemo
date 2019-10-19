package com.fan.netty.demo.chapter1.io;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

@Slf4j
public final class EchoCompletionHandler implements CompletionHandler<Integer, ByteBuffer> {

    private final AsynchronousSocketChannel channel;

    public EchoCompletionHandler(AsynchronousSocketChannel socketChannel) {
        this.channel = socketChannel;
    }

    @Override
    public void completed(Integer result, ByteBuffer buffer) {
        buffer.flip();

        channel.write(buffer, buffer, new CompletionHandler<Integer, ByteBuffer>() {
            //6. trigger a write operation on the cahnnel, the given completionHandler will be notified
            //once something was written
            @Override
            public void completed(Integer result, ByteBuffer attachment) {
                if(attachment.hasRemaining()){
                    //7. trigger again a write operation if something is left in the bytebuffer
                    channel.write(attachment, attachment, this);
                }else{
                    System.out.println(new String(attachment.array()));
                    attachment.compact();
                    //8. trigger a read operation on the channel, the given completionHandler will be notified
                    // once something was read
                    channel.read(attachment, attachment, EchoCompletionHandler.this);
                }
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                try {
                    channel.close();
                } catch (IOException e) {
                    log.error("io exception {0}", e.getLocalizedMessage());
                }
            }
        });
    }

    @Override
    public void failed(Throwable exc, ByteBuffer buffer) {
        try {
            channel.close();
        } catch (IOException e) {
            log.error("io exception {0}", e.getLocalizedMessage());
        }
    }
}
