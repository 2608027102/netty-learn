package com.wjl.learn.nettylearn.client.codec;

import io.netty.handler.codec.LengthFieldPrepender;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OrderFrameEncoder extends LengthFieldPrepender {
    /**
     * Creates a new instance.
     *
     * @param lengthFieldLength the length of the prepended length field.
     *                          Only 1, 2, 3, 4, and 8 are allowed.
     * @throws IllegalArgumentException if {@code lengthFieldLength} is not 1, 2, 3, 4, or 8
     */
    public OrderFrameEncoder() {
        super(2);
        log.info("OrderFrameEncoder initialized");
    }


}
