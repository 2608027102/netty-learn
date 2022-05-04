package com.wjl.learn.nettylearn.client.codec;

import com.wjl.learn.nettylearn.common.ResponseMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class OrderProtocolDecoder extends MessageToMessageDecoder<ByteBuf> {

    /**
     * Create a new instance which will try to detect the types to match out of the type parameter of the class.
     */
    public OrderProtocolDecoder() {
        log.info("OrderProtocolDecoder initialized");
    }

    /**
     * Create a new instance
     *
     * @param inboundMessageType The type of messages to match and so decode
     */
    public OrderProtocolDecoder(Class<? extends ByteBuf> inboundMessageType) {
        super(inboundMessageType);
        log.info("OrderProtocolDecoder initialized");
    }

    /**
     * Decode from one message to an other. This method will be called for each written message that can be handled
     * by this decoder.
     *
     * @param ctx the {@link ChannelHandlerContext} which this {@link MessageToMessageDecoder} belongs to
     * @param msg the message to decode to an other one
     * @param out the {@link List} to which decoded messages should be added
     * @throws Exception is thrown if an error occurs
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        ResponseMessage message = new ResponseMessage();
        message.decode(msg);

        out.add(message);
    }
}
