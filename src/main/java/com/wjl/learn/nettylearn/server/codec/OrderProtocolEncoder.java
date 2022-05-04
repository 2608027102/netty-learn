package com.wjl.learn.nettylearn.server.codec;

import com.wjl.learn.nettylearn.common.ResponseMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class OrderProtocolEncoder extends MessageToMessageEncoder<ResponseMessage> {

    /**
     * Create a new instance which will try to detect the types to match out of the type parameter of the class.
     */
    public OrderProtocolEncoder() {
        log.info("OrderProtocolEncoder initialized");
    }

    /**
     * Create a new instance
     *
     * @param outboundMessageType The type of messages to match and so encode
     */
    public OrderProtocolEncoder(Class<? extends ResponseMessage> outboundMessageType) {
        super(outboundMessageType);
        log.info("OrderProtocolEncoder initialized");
    }

    /**
     * Encode from one message to an other. This method will be called for each written message that can be handled
     * by this encoder.
     *
     * @param ctx the {@link ChannelHandlerContext} which this {@link MessageToMessageEncoder} belongs to
     * @param msg the message to encode to an other one
     * @param out the {@link List} into which the encoded msg should be added
     *            needs to do some kind of aggregation
     * @throws Exception is thrown if an error occurs
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, ResponseMessage msg, List<Object> out) throws Exception {
        ByteBuf buffer = ctx.alloc().buffer();
        msg.encode(buffer);

        out.add(buffer);
    }
}
