package com.wjl.learn.nettylearn.common;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RequestMessage extends Message<Operation> {

    @Override
    public Class<? extends Operation> getMessageBodyDecodeClass(int opcode) {
        return OperationType.fromOpCode(opcode).getOperationClass();
    }


    public RequestMessage(long streamId, Operation operation) {
        MessageHeader header = new MessageHeader();
        header.setStreamId(streamId);
        header.setOpCode(OperationType.fromOperation(operation).getOpCode());

        this.setMessageHeader(header);
        this.setMessageBody(operation);
    }
}
