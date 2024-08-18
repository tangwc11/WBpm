package com.wentry.wbpm.core.listener;

import com.wentry.wbpm.api.ProcessInstanceContext;
import com.wentry.wbpm.model.task.UserTaskNode;

/**
 * @Description:
 * @Author: tangwc
 */
public abstract class UserTaskListenerAdapter implements UserTaskListener {

    @Override
    public void onFlowInto(UserTaskNode userTaskNode, ProcessInstanceContext context) {

    }

    @Override
    public void beforeFlowOut(UserTaskNode userTaskNode, ProcessInstanceContext context) {

    }

    @Override
    public void afterFlowOut(UserTaskNode userTaskNode, ProcessInstanceContext context) {

    }
}
