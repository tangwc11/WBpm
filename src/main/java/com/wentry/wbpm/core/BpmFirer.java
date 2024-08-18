package com.wentry.wbpm.core;

import com.wentry.wbpm.api.ProcessInstanceContext;
import com.wentry.wbpm.core.base.WBpmNode;
import com.wentry.wbpm.model.gateway.ExclusiveGateway;
import com.wentry.wbpm.model.gateway.InclusiveGateway;
import com.wentry.wbpm.model.gateway.ParallelGateway;
import com.wentry.wbpm.model.task.UserTaskNode;

/**
 * @Description:
 * @Author: tangwc
 */
public class BpmFirer {

    public static void flowInto(Object next, ProcessInstanceContext context, WBpmNode triggerNode) {
        if (next instanceof UserTaskNode) {
            ((UserTaskNode) next).flowInto(context, triggerNode);
        } else if (next instanceof ExclusiveGateway) {
            ((ExclusiveGateway) next).flowInto(context,triggerNode);
        } else if (next instanceof InclusiveGateway) {
            ((InclusiveGateway) next).flowInto(context,triggerNode);
        } else if (next instanceof ParallelGateway) {
            ((ParallelGateway) next).flowInto(context, triggerNode);
        }
    }
}
