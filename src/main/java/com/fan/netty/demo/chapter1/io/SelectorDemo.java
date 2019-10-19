package com.fan.netty.demo.chapter1.io;

/**
 * To use selector, typically complete the following steps
 * 1. create on or more selectors to which opened channel can be registered.
 * 2. when a channel is registered, you specify which events you're interested in listening in. The four
 *    available events are:
 *    OP_ACCEPT: Operation-set bit for socket-accept operation
 *    OP_CONNECT: Operation-set bit set for socket-connection operation
 *    OP_READ: Operation-set bit for read operation
 *    OP_WRITE: Operation-set bit for write operation
 * 3. when channels are registered, call Selector.select() method to block util the interested events happens
 * 4. when the method unblocks, can obtain all the SelectionKeys
 */

public class SelectorDemo {
}
