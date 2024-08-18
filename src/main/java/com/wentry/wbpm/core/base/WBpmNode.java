package com.wentry.wbpm.core.base;

import com.wentry.wbpm.api.ProcessInstanceContext;

/**
 * @Description:
 * @Author: tangwc
 */
public interface WBpmNode {


    void flowInto(ProcessInstanceContext context, WBpmNode pre);

    void flowOut(ProcessInstanceContext context);

    void ticketSpread(WBpmNode node);
}
