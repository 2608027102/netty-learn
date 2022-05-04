package com.wjl.learn.nettylearn.common;

public class ResponseMessage extends Message<OperationResult> {
    @Override
    public Class<? extends OperationResult> getMessageBodyDecodeClass(int opcode) {
        return OperationType.fromOpCode(opcode).getOperationResultClass();
    }
}
