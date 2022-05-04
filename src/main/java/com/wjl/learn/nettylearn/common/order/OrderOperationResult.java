package com.wjl.learn.nettylearn.common.order;

import com.wjl.learn.nettylearn.common.OperationResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderOperationResult extends OperationResult {

    private int tableId;
    private String dish;
    private boolean result;

}
