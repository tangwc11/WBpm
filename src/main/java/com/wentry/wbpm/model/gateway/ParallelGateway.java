package com.wentry.wbpm.model.gateway;

import com.wentry.wbpm.core.base.AutoFinishedWBpmNode;
import com.wentry.wbpm.core.filter.Filters;
import com.wentry.wbpm.core.base.WBpmNode;
import com.wentry.wbpm.api.ProcessInstanceContext;
import com.wentry.wbpm.model.task.UserTaskNode;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

/**
 * @Description:
 * @Author: tangwc
 */

public class ParallelGateway extends AutoFinishedWBpmNode {

    //这里需要放到context里面，node不能带状态
    private AtomicInteger inDegree = new AtomicInteger();

    //出口
    private List<GatewayOutTo> outTos = new ArrayList<>();

    public ParallelGateway(String name) {
        super(name,Filters.DEFAULT_FILTERS);
    }

    public UserTaskNode outTo(Supplier<Boolean> condition, UserTaskNode userTaskNode) {
        outTos.add(new GatewayOutTo().setCondition(condition).setUserTaskNode(userTaskNode));
        return userTaskNode;
    }

    public void incInDegree() {
        this.inDegree.incrementAndGet();
    }

    @Override
    public void flowOut0(ProcessInstanceContext context) {
        if (inDegree.decrementAndGet() > 0) {
            return;
        }
        for (GatewayOutTo to : outTos) {
            //全都执行，不判断条件
            to.getUserTaskNode().flowInto(context, this);
        }
    }

    @Override
    public void ticketSpread(WBpmNode node) {
        //只传递
        for (GatewayOutTo outTo : outTos) {
            outTo.getUserTaskNode().ticketSpread(this);
        }
    }

    public AtomicInteger getInDegree() {
        return inDegree;
    }

    public ParallelGateway setInDegree(AtomicInteger inDegree) {
        this.inDegree = inDegree;
        return this;
    }

    public List<GatewayOutTo> getOutTos() {
        return outTos;
    }

    public ParallelGateway setOutTos(List<GatewayOutTo> outTos) {
        this.outTos = outTos;
        return this;
    }
}
