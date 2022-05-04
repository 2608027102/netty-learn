package com.wjl.learn.nettylearn.common.auth;

import com.wjl.learn.nettylearn.common.OperationResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthOperationResult extends OperationResult {

    private boolean passAuth;

}
