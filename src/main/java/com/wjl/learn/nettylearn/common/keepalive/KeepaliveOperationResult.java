package com.wjl.learn.nettylearn.common.keepalive;

import com.wjl.learn.nettylearn.common.OperationResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KeepaliveOperationResult extends OperationResult {

    private long time;

}
