package com.wjl.learn.nettylearn.server.codec.handler;

import com.wjl.learn.nettylearn.common.Operation;
import com.wjl.learn.nettylearn.common.OperationResult;
import com.wjl.learn.nettylearn.common.RequestMessage;
import com.wjl.learn.nettylearn.common.ResponseMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OrderServerProcessHandler extends SimpleChannelInboundHandler<RequestMessage> {

    public OrderServerProcessHandler() {
        log.info("OrderProtocolDecoder initialized");
    }

    public OrderServerProcessHandler(boolean autoRelease) {
        super(autoRelease);
        log.info("OrderProtocolDecoder initialized");
    }

    public OrderServerProcessHandler(Class<? extends RequestMessage> inboundMessageType) {
        super(inboundMessageType);
        log.info("OrderProtocolDecoder initialized");
    }

    public OrderServerProcessHandler(Class<? extends RequestMessage> inboundMessageType, boolean autoRelease) {
        super(inboundMessageType, autoRelease);
        log.info("OrderProtocolDecoder initialized");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RequestMessage requestMessage) throws Exception {
        Operation operation = requestMessage.getMessageBody();
        OperationResult operationResult = operation.execute();

        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setMessageHeader(requestMessage.getMessageHeader());
        responseMessage.setMessageBody(operationResult);

        if (channelHandlerContext.channel().isActive() && channelHandlerContext.channel().isWritable()) {
            channelHandlerContext.writeAndFlush(responseMessage);
        } else {
            log.error("message dropped");
        }

    }
}
