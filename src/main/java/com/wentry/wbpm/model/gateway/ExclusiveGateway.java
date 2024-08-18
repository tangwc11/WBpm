package com.wentry.wbpm.model.gateway;

import com.wentry.wbpm.api.ProcessInstanceContext;
import com.wentry.wbpm.core.base.AutoFinishedWBpmNode;
import com.wentry.wbpm.core.base.WBpmNode;
import com.wentry.wbpm.core.filter.Filters;
import com.wentry.wbpm.model.task.UserTaskNode;


import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * @Description:
 * @Author: tangwc
 */

public class ExclusiveGateway extends AutoFinishedWBpmNode {

    //出口
    private List<GatewayOutTo> outTos = new ArrayList<>();

    public ExclusiveGateway(String name) {
        super(name,Filters.defaultFilters);
    }

    public UserTaskNode outTo(Supplier<Boolean> condition, UserTaskNode userTaskNode) {
        outTos.add(new GatewayOutTo().setCondition(condition).setUserTaskNode(userTaskNode));
        return userTaskNode;
    }

    @Override
    public void flowOut0(ProcessInstanceContext context) {
        for (GatewayOutTo to : outTos) {
            if (to.getCondition().get()) {
                to.getUserTaskNode().flowInto(context, this);
                //这里是排他的语义
                return;
            }
        }
        //走到这里，整个流程结束
        context.setFinished(true);
    }

    @Override
    public void ticketSpread(WBpmNode node) {
        //只传递
        for (GatewayOutTo outTo : outTos) {
            outTo.getUserTaskNode().ticketSpread(this);
        }
    }


    public List<GatewayOutTo> getOutTos() {
        return outTos;
    }

    public ExclusiveGateway setOutTos(List<GatewayOutTo> outTos) {
        this.outTos = outTos;
        return this;
    }
}
