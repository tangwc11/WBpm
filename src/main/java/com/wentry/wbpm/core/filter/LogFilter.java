package com.wentry.wbpm.core.filter;

import com.wentry.wbpm.api.ProcessInstanceContext;
import com.wentry.wbpm.core.base.WBpmNode;
import com.wentry.wbpm.model.event.EndNode;
import com.wentry.wbpm.utils.JsonUtils;

/**
 * @Description:
 * @Author: tangwc
 */

public class LogFilter extends FilterAdapter {

    public static final LogFilter instance = new LogFilter();

    @Override
    public void beforeFlowInto(ProcessInstanceContext context, WBpmNode pre, WBpmNode intoNode) {
        context.addLog(">>>>>>>>>流程进入：" +  JsonUtils.toJson(intoNode)
                + ", 上一个节点：" +  JsonUtils.toJson(pre));
        if (intoNode instanceof EndNode) {
            context.addLog(">>>>>>>>>流程即将结束：" +  JsonUtils.toJson(context));
        }
    }

    @Override
    public void beforeFlowOut(ProcessInstanceContext context, WBpmNode nodeOut) {
        context.addLog(">>>>>>>>>流程流出：" +  JsonUtils.toJson(nodeOut));
    }
}
