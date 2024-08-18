package com.wentry.wbpm.core.filter;

import com.wentry.wbpm.api.ProcessInstanceContext;
import com.wentry.wbpm.core.base.WBpmNode;


/**
 * @Description:
 * @Author: tangwc
 */

public class ActiveAwareFilter extends FilterAdapter {

    public static final ActiveAwareFilter instance = new ActiveAwareFilter();

    @Override
    public void beforeFlowInto(ProcessInstanceContext context, WBpmNode pre, WBpmNode intoNode) {
        context.addActive(intoNode);
    }

    @Override
    public void beforeFlowOut(ProcessInstanceContext context, WBpmNode nodeOut) {
        context.getActive().remove(nodeOut);
    }

}
