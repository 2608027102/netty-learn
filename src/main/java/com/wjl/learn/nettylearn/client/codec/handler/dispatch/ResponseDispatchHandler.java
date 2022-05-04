package com.wjl.learn.nettylearn.client.codec.handler.dispatch;

import com.wjl.learn.nettylearn.common.MessageHeader;
import com.wjl.learn.nettylearn.common.ResponseMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ResponseDispatchHandler extends SimpleChannelInboundHandler<ResponseMessage> {

    private final RequestPendingCenter requestPendingCenter;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ResponseMessage responseMessage) throws Exception {
        MessageHeader messageHeader = responseMessage.getMessageHeader();
        requestPendingCenter.set(messageHeader.getStreamId(), responseMessage.getMessageBody());
    }
}
