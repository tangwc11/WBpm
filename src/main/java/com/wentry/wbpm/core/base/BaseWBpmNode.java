package com.wentry.wbpm.core.base;

import com.wentry.wbpm.api.ProcessInstanceContext;
import com.wentry.wbpm.core.filter.NodeFilter;
import com.wentry.wbpm.utils.IdGenerator;


import java.util.List;

/**
 * @Description: 基本实现，里面增加了filter机制
 * @Author: tangwc
 */

public abstract class BaseWBpmNode implements WBpmNode {

    private String id = IdGenerator.globalIncNodeId();
    private final String name;
    private boolean complete;
    private final List<NodeFilter> filters;

    public boolean isComplete() {
        return complete;
    }

    public BaseWBpmNode setComplete(boolean complete) {
        this.complete = complete;
        return this;
    }

    public BaseWBpmNode(String name, List<NodeFilter> filters) {
        this.name = name;
        this.filters = filters;
    }

    public String getId() {
        return id;
    }

    public BaseWBpmNode setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public BaseWBpmNode addFilter(NodeFilter filter) {
        filters.add(filter);
        return this;
    }


    public abstract void flowInto0(ProcessInstanceContext context, WBpmNode pre);

    @Override
    public void flowInto(ProcessInstanceContext context, WBpmNode pre) {
        for (NodeFilter filter : filters) {
            filter.beforeFlowInto(context, pre, this);
        }
        flowInto0(context, pre);
        for (NodeFilter filter : filters) {
            filter.afterFlowInto(context, pre, this);
        }
    }

    @Override
    public void flowOut(ProcessInstanceContext context) {
        setComplete(true);
        for (NodeFilter filter : filters) {
            filter.beforeFlowOut(context, this);
        }
        flowOut0(context);
        for (NodeFilter filter : filters) {
            filter.afterFlowOut(context, this);
        }
    }

    public abstract void flowOut0(ProcessInstanceContext context);


}
