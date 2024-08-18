package com.wentry.wbpm.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wentry.wbpm.core.base.WBpmNode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

/**
 * @Description:
 * @Author: tangwc
 */
public class ProcessInstanceContext {

    private List<String> logs = new ArrayList<>();
    private boolean finished;
    @JsonIgnore
    private transient ProcessInstance processInstance;
    private Map<String, Object> variables;
    private Set<WBpmNode> active = new HashSet<>();
    private final List<Consumer<ProcessInstanceContext>> finishedListener = new ArrayList<>();

    public ProcessInstance getProcessInstance() {
        return processInstance;
    }

    public ProcessInstanceContext setProcessInstance(ProcessInstance processInstance) {
        this.processInstance = processInstance;
        return this;
    }

    public List<Consumer<ProcessInstanceContext>> getFinishedListener() {
        return finishedListener;
    }

    public ProcessInstanceContext addLog(String log) {
        logs.add(log);
        return this;
    }

    public void setFinished(boolean finished) {
        if (finished) {
            for (Consumer<ProcessInstanceContext> consumer : finishedListener) {
                consumer.accept(this);
            }
        }
        this.finished = finished;
    }

    public boolean getFinished() {
        return finished;
    }

    public List<String> getLogs() {
        return logs;
    }

    public ProcessInstanceContext setLogs(List<String> logs) {
        this.logs = logs;
        return this;
    }

    public boolean isFinished() {
        return finished;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }

    public ProcessInstanceContext setVariables(Map<String, Object> variables) {
        this.variables = variables;
        return this;
    }

    public void addCompleteListener(Consumer<ProcessInstanceContext> consumer) {
        finishedListener.add(consumer);
    }


    public void addActive(WBpmNode node) {
        active.add(node);
    }

    public Set<WBpmNode> getActive() {
        return active;
    }

    public ProcessInstanceContext setActive(Set<WBpmNode> active) {
        this.active = active;
        return this;
    }

    @Override
    public String toString() {
        return "ProcessInstanceContext{" +
                ", logs=" + logs +
                ", finished=" + finished +
//                ", processInstance=" + processInstance +
                ", variables=" + variables +
                ", active=" + active +
                ", finishedListener=" + finishedListener +
                '}';
    }
}
