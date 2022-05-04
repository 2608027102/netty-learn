package com.wjl.learn.nettylearn.util;

import com.wjl.learn.nettylearn.client.codec.handler.dispatch.OperationResultFuture;
import com.wjl.learn.nettylearn.client.codec.handler.dispatch.RequestPendingCenter;
import com.wjl.learn.nettylearn.common.RequestMessage;
import io.netty.channel.Channel;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestUtil {

    private static RequestUtil INSTANCE;
    private final RequestPendingCenter REQUEST_PENDING_CENTER;

    public static RequestUtil getInstance(RequestPendingCenter requestPendingCenter) {
        if (INSTANCE == null) {
            synchronized (RequestUtil.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RequestUtil(requestPendingCenter);
                }
            }
        }
        return RequestUtil.INSTANCE;
    }

    public OperationResultFuture send(Channel channel, RequestMessage requestMessage) {
        OperationResultFuture operationResultFuture = new OperationResultFuture();
        REQUEST_PENDING_CENTER.add(requestMessage.getMessageHeader().getStreamId(), operationResultFuture);

        channel.writeAndFlush(requestMessage);

        return operationResultFuture;
    }
}
