package com.wentry.wbpm.core.filter;

import com.wentry.wbpm.api.ProcessInstanceContext;
import com.wentry.wbpm.core.base.WBpmNode;
import com.wentry.wbpm.model.event.EndNode;
import com.wentry.wbpm.utils.DateUtils;
import com.wentry.wbpm.utils.json.JsonUtils;

import java.util.Date;

/**
 * @Description:
 * @Author: tangwc
 */

public class LogFilter extends FilterAdapter {

    public static final LogFilter INSTANCE = new LogFilter();

    static final String IN_TEMPLATE = ">>>>>>>>>流程进入：%s , 上一个节点：%s , 当前时间： %s";
    static final String OUT_TEMPLATE = ">>>>>>>>>流程流出：%s , 当前时间： %s";
    static final String ENDING_TEMPLATE = ">>>>>>>>>流程即将结束：%s , 当前时间： %s";

    @Override
    public void beforeFlowInto(ProcessInstanceContext context, WBpmNode pre, WBpmNode intoNode) {
        context.addLog(String.format(IN_TEMPLATE,
                JsonUtils.toJson(intoNode, "next", "outTos"),
                JsonUtils.toJson(pre, "next", "outTos"),
                DateUtils.format(new Date())
        ));
        if (intoNode instanceof EndNode) {
            context.addLog(String.format(ENDING_TEMPLATE,
                    JsonUtils.toJson(context),
                    DateUtils.format(new Date())
            ));
        }
    }

    @Override
    public void beforeFlowOut(ProcessInstanceContext context, WBpmNode nodeOut) {
        context.addLog(String.format(OUT_TEMPLATE,
                JsonUtils.toJson(nodeOut, "next", "outTos"),
                DateUtils.format(new Date())
        ));
    }
}
