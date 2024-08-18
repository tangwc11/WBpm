package com.wentry.wbpm.utils;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description:
 * @Author: tangwc
 */
public class IdGenerator {

    static AtomicInteger incNodeId = new AtomicInteger(1);
    static AtomicInteger incInstId = new AtomicInteger(1);

    public static String globalIncInstId() {
        return "inst_" + incInstId.getAndAdd(1);
    }

    public static String globalIncNodeId() {
        return "node_" + incNodeId.getAndAdd(1);
    }

}
