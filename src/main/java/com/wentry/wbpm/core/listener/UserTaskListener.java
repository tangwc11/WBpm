package com.wentry.wbpm.core.listener;

import com.wentry.wbpm.api.ProcessInstanceContext;
import com.wentry.wbpm.model.task.UserTaskNode;

/**
 * @Description:
 * @Author: tangwc
 */
public interface UserTaskListener {


    void onFlowInto(UserTaskNode userTaskNode, ProcessInstanceContext context);

    void beforeFlowOut(UserTaskNode userTaskNode, ProcessInstanceContext context);

    void afterFlowOut(UserTaskNode userTaskNode, ProcessInstanceContext context);



}
