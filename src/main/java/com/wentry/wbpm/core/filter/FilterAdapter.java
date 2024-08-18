package com.wentry.wbpm.core.filter;

import com.wentry.wbpm.api.ProcessInstanceContext;
import com.wentry.wbpm.core.base.WBpmNode;


/**
 * @Description:
 * @Author: tangwc
 */

public abstract class FilterAdapter implements NodeFilter{
    @Override
    public void beforeFlowInto(ProcessInstanceContext context, WBpmNode pre, WBpmNode intoNode) {

    }

    @Override
    public void afterFlowInto(ProcessInstanceContext context, WBpmNode pre, WBpmNode intoNode) {

    }

    @Override
    public void beforeFlowOut(ProcessInstanceContext context, WBpmNode nodeOut) {

    }

    @Override
    public void afterFlowOut(ProcessInstanceContext context, WBpmNode nodeOut) {

    }
}
