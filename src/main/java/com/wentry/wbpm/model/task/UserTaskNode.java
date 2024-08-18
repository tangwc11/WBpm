package com.wentry.wbpm.model.task;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wentry.wbpm.core.base.BaseWBpmNode;
import com.wentry.wbpm.core.BpmFirer;
import com.wentry.wbpm.core.filter.Filters;
import com.wentry.wbpm.core.base.WBpmNode;
import com.wentry.wbpm.api.ProcessInstanceContext;
import com.wentry.wbpm.core.listener.UserTaskListener;
import com.wentry.wbpm.model.gateway.ParallelGateway;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: tangwc
 */

public class UserTaskNode extends BaseWBpmNode {

    private String assignee;
    private WBpmNode next;
    @JsonIgnore
    private List<UserTaskListener> userTaskListeners = new ArrayList<>();

    public UserTaskNode(String name) {
        super(name, Filters.DEFAULT_FILTERS);
    }

    public <T extends WBpmNode> UserTaskNode next(T next) {
        this.next = next;
        if (next instanceof ParallelGateway) {
            //记录入度
            ((ParallelGateway) next).incInDegree();
        }
        return this;
    }

    public String getAssignee() {
        return assignee;
    }

    public UserTaskNode setAssignee(String assignee) {
        this.assignee = assignee;
        return this;
    }

    public void complete(String user, ProcessInstanceContext context) {
        if (!this.isComplete() && StringUtils.equals(this.assignee, user)) {
            flowOut(context);
        }
    }

    public UserTaskNode add(UserTaskListener userTaskListener) {
        this.userTaskListeners.add(userTaskListener);
        return this;
    }

    @Override
    public void flowInto0(ProcessInstanceContext context, WBpmNode pre) {
        if (!CollectionUtils.isEmpty(userTaskListeners)) {
            userTaskListeners.forEach(x -> x.onFlowInto(this, context));
        }
    }

    @Override
    public void flowOut0(ProcessInstanceContext context) {
        if (!CollectionUtils.isEmpty(userTaskListeners)) {
            userTaskListeners.forEach(x -> x.beforeFlowOut(this, context));
        }
        BpmFirer.flowInto(next, context, this);
        if (!CollectionUtils.isEmpty(userTaskListeners)) {
            userTaskListeners.forEach(x -> x.afterFlowOut(this, context));
        }
    }

    @Override
    public void ticketSpread(WBpmNode node) {
        next.ticketSpread(this);
    }


    public WBpmNode getNext() {
        return next;
    }

    public UserTaskNode setNext(WBpmNode next) {
        this.next = next;
        return this;
    }

    public List<UserTaskListener> getUserTaskListeners() {
        return userTaskListeners;
    }

    public UserTaskNode setUserTaskListeners(List<UserTaskListener> userTaskListeners) {
        this.userTaskListeners = userTaskListeners;
        return this;
    }
}
