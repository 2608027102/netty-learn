package com.wjl.learn.nettylearn.common.keepalive;

import com.wjl.learn.nettylearn.common.Operation;
import com.wjl.learn.nettylearn.common.OperationResult;
import lombok.Data;
import lombok.extern.java.Log;

@Data
@Log
public class KeepaliveOperation extends Operation {

    private long time;

    public KeepaliveOperation() {
        this.time = System.nanoTime();
    }

    @Override
    public OperationResult execute() {
        return new KeepaliveOperationResult(time);
    }
}
