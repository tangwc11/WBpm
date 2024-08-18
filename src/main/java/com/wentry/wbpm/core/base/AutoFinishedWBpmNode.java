package com.wentry.wbpm.core.base;

import com.wentry.wbpm.api.ProcessInstanceContext;
import com.wentry.wbpm.core.filter.NodeFilter;


import java.util.List;

/**
 * @Description:
 * @Author: tangwc
 */

public abstract class AutoFinishedWBpmNode extends BaseWBpmNode {


    public AutoFinishedWBpmNode(String name,List<NodeFilter> filters) {
        super(name,filters);
    }


    @Override
    public void flowInto0(ProcessInstanceContext context, WBpmNode pre) {
        flowOut(context);
    }

}
