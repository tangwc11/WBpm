package com.wentry.wbpm.model.gateway;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wentry.wbpm.core.base.BaseWBpmNode;
import com.wentry.wbpm.core.filter.Filters;
import com.wentry.wbpm.core.base.WBpmNode;
import com.wentry.wbpm.api.ProcessInstanceContext;
import com.wentry.wbpm.model.task.UserTaskNode;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

/**
 * @Description:
 * @Author: tangwc
 */

public class InclusiveGateway extends BaseWBpmNode {

    //上游传递过来的需要等待的节点
    @JsonIgnore
    private Set<WBpmNode> ticketSet = new HashSet<>();
    //出口
    private List<GatewayOutTo> outTos = new ArrayList<>();

    public InclusiveGateway(String name) {
        super(name, Filters.defaultFilters);
    }


    public InclusiveGateway outTo(Supplier<Boolean> condition, WBpmNode userTaskNode) {
        outTos.add(new GatewayOutTo().setCondition(condition).setUserTaskNode(userTaskNode));
        return this;
    }

    @Override
    public void flowInto0(ProcessInstanceContext context, WBpmNode pre) {
        ticketSet.remove(pre);
        if (ticketSet.isEmpty()) {
            flowOut(context);
        }
    }

    @Override
    public void flowOut0(ProcessInstanceContext context) {
        boolean hit = false;
        for (GatewayOutTo to : outTos) {
            if (to.getCondition().get()) {
                to.getUserTaskNode().ticketSpread(this);
                to.getUserTaskNode().flowInto(context, this);
                hit = true;
            }
        }

        if (!hit) {
            context.setFinished(true);
        }
    }

    @Override
    public void ticketSpread(WBpmNode node) {
        ticketSet.add(node);
        for (GatewayOutTo outTo : outTos) {
            outTo.getUserTaskNode().ticketSpread(this);
        }
    }

    public Set<WBpmNode> getTicketSet() {
        return ticketSet;
    }

    public InclusiveGateway setTicketSet(Set<WBpmNode> ticketSet) {
        this.ticketSet = ticketSet;
        return this;
    }

    public List<GatewayOutTo> getOutTos() {
        return outTos;
    }

    public InclusiveGateway setOutTos(List<GatewayOutTo> outTos) {
        this.outTos = outTos;
        return this;
    }
}
