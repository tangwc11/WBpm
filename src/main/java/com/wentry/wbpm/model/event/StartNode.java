package com.wentry.wbpm.model.event;

import com.wentry.wbpm.api.ProcessInstanceContext;
import com.wentry.wbpm.core.BpmFirer;
import com.wentry.wbpm.core.base.AutoFinishedWBpmNode;
import com.wentry.wbpm.core.base.WBpmNode;
import com.wentry.wbpm.core.filter.Filters;


/**
 * @Description:
 * @Author: tangwc
 */
public class StartNode extends AutoFinishedWBpmNode {

    private WBpmNode next;

    public StartNode(String name) {
        super(name,Filters.DEFAULT_FILTERS);
    }

    public <T extends WBpmNode> StartNode next(T userTaskNode) {
        next = userTaskNode;
        return this;
    }

    @Override
    public void flowOut0(ProcessInstanceContext context) {
        BpmFirer.flowInto(next, context,this);
    }


    @Override
    public void ticketSpread(WBpmNode node) {
        next.ticketSpread(this);
    }

    public WBpmNode getNext() {
        return next;
    }

    public StartNode setNext(WBpmNode next) {
        this.next = next;
        return this;
    }
}
