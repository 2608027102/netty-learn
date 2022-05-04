package com.wjl.learn.nettylearn.util;

import lombok.experimental.UtilityClass;

import java.util.concurrent.atomic.AtomicLong;

@UtilityClass
public class IdUtil {

    private static final AtomicLong IDX = new AtomicLong();

    public static long nextId() {
        return IDX.incrementAndGet();
    }

}
