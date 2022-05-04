package com.wjl.learn.nettylearn.common;

import com.wjl.learn.nettylearn.common.auth.AuthOperation;
import com.wjl.learn.nettylearn.common.auth.AuthOperationResult;
import com.wjl.learn.nettylearn.common.keepalive.KeepaliveOperation;
import com.wjl.learn.nettylearn.common.keepalive.KeepaliveOperationResult;
import com.wjl.learn.nettylearn.common.order.OrderOperation;
import com.wjl.learn.nettylearn.common.order.OrderOperationResult;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum OperationType {

    AUTH(1, AuthOperation.class, AuthOperationResult.class),
    KEEPALIVE(2, KeepaliveOperation.class, KeepaliveOperationResult.class),
    ORDER(3, OrderOperation.class, OrderOperationResult.class),
    ;

    private final int opCode;

    private final Class<? extends Operation> operationClass;

    private final Class<? extends OperationResult> operationResultClass;

    public static OperationType fromOpCode(int opcode) {
        for (OperationType value : values()) {
            if (value.opCode == opcode) {
                return value;
            }
        }
        throw new RuntimeException("unknown opcode: " + opcode);
    }

    public static OperationType fromOperation(Operation operation) {
        for (OperationType value : values()) {
            if (value.operationClass == operation.getClass()) {
                return value;
            }
        }
        throw new RuntimeException("unknown opcode: " + operation);
    }
}
