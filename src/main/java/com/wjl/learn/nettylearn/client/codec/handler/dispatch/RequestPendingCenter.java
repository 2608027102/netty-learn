package com.wjl.learn.nettylearn.client.codec.handler.dispatch;

import com.wjl.learn.nettylearn.common.OperationResult;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RequestPendingCenter {

    private Map<Long, OperationResultFuture> map = new ConcurrentHashMap<>();


    public void add(long streamId, OperationResultFuture operationResultFuture) {
        map.put(streamId, operationResultFuture);
    }

    public void set(long streamId, OperationResult operationResult) {
        OperationResultFuture operationResultFuture = map.get(streamId);
        if (operationResultFuture != null) {
            operationResultFuture.setSuccess(operationResult);
            map.remove(streamId);
        }
    }


}
