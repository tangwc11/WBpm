package com.wentry.wbpm.core.filter;

import com.wentry.wbpm.api.ProcessInstanceContext;
import com.wentry.wbpm.core.base.WBpmNode;

/**
 * @Description:
 * @Author: tangwc
 */
public interface NodeFilter {

    void beforeFlowInto(ProcessInstanceContext context, WBpmNode pre, WBpmNode intoNode);

    void afterFlowInto(ProcessInstanceContext context, WBpmNode pre, WBpmNode intoNode);

    void beforeFlowOut(ProcessInstanceContext context, WBpmNode nodeOut);

    void afterFlowOut(ProcessInstanceContext context, WBpmNode nodeOut);

}
