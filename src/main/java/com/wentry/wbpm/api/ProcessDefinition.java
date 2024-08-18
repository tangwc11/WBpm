package com.wentry.wbpm.api;

import com.wentry.wbpm.model.event.StartNode;


import java.util.Map;

/**
 * @Description:
 * @Author: tangwc
 */
public class ProcessDefinition {

    private String name;
    private StartNode start;

    public String getName() {
        return name;
    }

    public ProcessDefinition setName(String name) {
        this.name = name;
        return this;
    }

    public StartNode getStart() {
        return start;
    }

    public ProcessDefinition setStart(StartNode start) {
        this.start = start;
        return this;
    }

    public ProcessInstance startProcess(Map<String, Object> variables) {
        ProcessInstance instance = new ProcessInstance();

        ProcessInstanceContext context = new ProcessInstanceContext()
                .setProcessInstance(instance)
                .setVariables(variables);

        //第一个节点触发
        start.flowInto(context, null);

        return instance
                .setProcessDefinition(this)
                .setProcessInstanceContext(context)
                ;
    }

    @Override
    public String toString() {
        return "ProcessDefinition{" +
                "name='" + name + '\'' +
                ", start=" + start +
                '}';
    }
}
