package com.wentry.wbpm.model.gateway;

import com.wentry.wbpm.core.base.WBpmNode;


import java.util.function.Supplier;


public class GatewayOutTo {
    private Supplier<Boolean> condition;
    private WBpmNode userTaskNode;

    public Supplier<Boolean> getCondition() {
        return condition;
    }

    public GatewayOutTo setCondition(Supplier<Boolean> condition) {
        this.condition = condition;
        return this;
    }

    public WBpmNode getUserTaskNode() {
        return userTaskNode;
    }

    public GatewayOutTo setUserTaskNode(WBpmNode userTaskNode) {
        this.userTaskNode = userTaskNode;
        return this;
    }
}