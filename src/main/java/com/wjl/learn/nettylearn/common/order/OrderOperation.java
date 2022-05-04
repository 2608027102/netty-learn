package com.wjl.learn.nettylearn.common.order;

import com.wjl.learn.nettylearn.common.Operation;
import com.wjl.learn.nettylearn.common.OperationResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;

@Data
@Log
@AllArgsConstructor
@NoArgsConstructor
public class OrderOperation extends Operation {

    private int tableId;
    private String dish;

    @Override
    public OperationResult execute() {
        log.info("order`s executing startup with orderRequest: " + toString());
        // execute ordre logic
        log.info("order`s executing complete");

        return new OrderOperationResult(tableId, dish, true);
    }
}
