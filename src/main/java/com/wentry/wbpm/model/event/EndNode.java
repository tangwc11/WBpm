package com.wentry.wbpm.model.event;

import com.wentry.wbpm.api.ProcessInstanceContext;
import com.wentry.wbpm.core.base.AutoFinishedWBpmNode;
import com.wentry.wbpm.core.base.WBpmNode;
import com.wentry.wbpm.core.filter.Filters;


/**
 * @Description:
 * @Author: tangwc
 */

public class EndNode extends AutoFinishedWBpmNode {

    public EndNode(String name) {
        super(name,Filters.DEFAULT_FILTERS);
    }

    @Override
    public void flowOut0(ProcessInstanceContext context) {
        context.setFinished(true);
    }

    @Override
    public void ticketSpread(WBpmNode node) {
        //do nothing
    }

}
