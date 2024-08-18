package com.wentry.wbpm.api;

import com.wentry.wbpm.utils.IdGenerator;


/**
 * @Description:
 * @Author: tangwc
 */
public class ProcessInstance {

    private String id = IdGenerator.globalIncInstId();
    private ProcessDefinition processDefinition;
    private ProcessInstanceContext processInstanceContext;

    public String getId() {
        return id;
    }

    public ProcessInstance setId(String id) {
        this.id = id;
        return this;
    }

    public ProcessDefinition getProcessDefinition() {
        return processDefinition;
    }

    public ProcessInstance setProcessDefinition(ProcessDefinition processDefinition) {
        this.processDefinition = processDefinition;
        return this;
    }

    public ProcessInstanceContext getProcessInstanceContext() {
        return processInstanceContext;
    }

    public ProcessInstance setProcessInstanceContext(ProcessInstanceContext processInstanceContext) {
        this.processInstanceContext = processInstanceContext;
        return this;
    }
}
