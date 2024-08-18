package com.wentry.wbpm.example;

import com.google.common.collect.Lists;
import com.wentry.wbpm.api.ProcessDefinition;
import com.wentry.wbpm.api.ProcessEngine;
import com.wentry.wbpm.api.ProcessInstance;
import com.wentry.wbpm.api.ProcessInstanceContext;
import com.wentry.wbpm.core.base.BaseWBpmNode;
import com.wentry.wbpm.core.base.WBpmNode;
import com.wentry.wbpm.core.listener.UserTaskListenerAdapter;
import com.wentry.wbpm.model.event.EndNode;
import com.wentry.wbpm.model.event.StartNode;
import com.wentry.wbpm.model.gateway.InclusiveGateway;
import com.wentry.wbpm.model.task.UserTaskNode;
import com.wentry.wbpm.utils.json.JsonUtils;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;

/**
 * @Description:
 * @Author: tangwc
 * <p>
 * 测试类，用于访问引擎状态
 */

@RequestMapping("/wbpm")
@RestController
public class Controller implements SmartInitializingSingleton {

    private static final Logger log = LoggerFactory.getLogger(Controller.class);

    /**
     * 所有的实例定义
     */
    @RequestMapping("/process/defs")
    public String processDef() {
        return JsonUtils.toJson(ProcessEngine.processDefs());
    }


    /**
     * 获取运行中的实例
     */
    @RequestMapping("/process/running")
    public String runningProcess() {
        return JsonUtils.toJson(ProcessEngine.runningProcess());
    }

    /**
     * 获取用户待操作的任务
     */
    @RequestMapping("/getUserTask")
    public String getUserTask(String userName) {
        return JsonUtils.toJson(ProcessEngine.getUserTask(userName));
    }

    /**
     * 完成任务，流转到下一个状态
     */
    @RequestMapping("/completeUserTask")
    public String complete(String userName, String instanceId, String nodeId) {
        return ProcessEngine.complete(userName, instanceId, nodeId);
    }

    @RequestMapping("/autoTest")
    public String autoTest() throws InterruptedException {

        List<String> userList = Lists.newArrayList("tangwc", "zhangsan", "lisi", "rose");
        while (true) {

            //随机等待3-8秒
            Thread.sleep(3000 + new Random().nextInt(5000));

            Map<String, ProcessInstance> running = ProcessEngine.runningProcess();
            if (MapUtils.isEmpty(running)) {
                startProcess();
                continue;
            }

            for (String user : userList) {
                Map<String, List<WBpmNode>> userTask = ProcessEngine.getUserTask(user);
                if (MapUtils.isEmpty(userTask)) {
                    continue;
                }
                for (Map.Entry<String, List<WBpmNode>> ety : userTask.entrySet()) {
                    String instanceId = ety.getKey().split(":")[0];
                    WBpmNode wBpmNode = ety.getValue().get(0);
                    String complete = ProcessEngine.complete(user, instanceId, ((BaseWBpmNode) wBpmNode).getId());
                    log.info("autoTest>>>>>>自动完成节点:{}， 结果:{}", JsonUtils.toJson(wBpmNode), complete);
                    log.info("autoTest>>>>>>当前运行中的实例:{}", JsonUtils.toJson(ProcessEngine.runningProcess()));
                    Thread.sleep(3000 + new Random().nextInt(5000));
                }
            }
        }
    }

    @Override
    public void afterSingletonsInstantiated() {

        //最后需要中继到同一个节点，这里先定义
        InclusiveGateway inclusiveGateway = new InclusiveGateway("中继节点：等待大组长和财务审批完毕")
                .outTo(() -> true, new EndNode("结束节点"));


        //初始化，定义流程，启动流程
        ProcessEngine.load("demo1", new ProcessDefinition()
                .setName("出差申请")
                .setStart(new StartNode("起始节点")
                        .next(new UserTaskNode("提交申请")
                                .add(new UserTaskListenerAdapter() {
                                    @Override
                                    public void onFlowInto(UserTaskNode userTaskNode, ProcessInstanceContext context) {
                                        Object currUser = context.getVariables().get("currUser");
                                        if (currUser == null) {
                                            throw new RuntimeException("缺少变量");
                                        }
                                        //在这动态设置处理人
                                        userTaskNode.setAssignee((String) currUser);
                                    }
                                })
                                .next(new UserTaskNode("组长审批")
                                        .add(new UserTaskListenerAdapter() {
                                            @Override
                                            public void onFlowInto(UserTaskNode userTaskNode, ProcessInstanceContext context) {
                                                //这里可以动态获取提交人的组长，就不写了，直接写死
                                                //在这动态设置处理人
                                                userTaskNode.setAssignee("zhangsan");
                                            }
                                        })
                                        //下一个节点，需要两个人同时审批通过才能提交
                                        .next(new InclusiveGateway("中继节点：同时分发给大组长和财务审批")

                                                //大组长审批
                                                .outTo(new Supplier<Boolean>() {
                                                           @Override
                                                           public Boolean get() {
                                                               //大组长请假了，这里返回false，即不走大组长审批
                                                               return false;
                                                           }
                                                       }, new UserTaskNode("大组长审批")
                                                                .setAssignee("lisi")
                                                                .add(new UserTaskListenerAdapter() {
                                                                    @Override
                                                                    public void onFlowInto(UserTaskNode userTaskNode, ProcessInstanceContext context) {
                                                                        //这里还可以发邮件通知，或者push推送
                                                                        System.out.println("请通知：" + userTaskNode.getAssignee() + "，审批出差申请~~~~~~~");
                                                                    }
                                                                })
                                                                //到下一个协调节点，也是Inclusive网关节点，等待大组长审核完成之后，再放行下一个流程。
                                                                //注意，这里要next到同一个节点，
                                                                .next(inclusiveGateway)
                                                )
                                                //财务审批
                                                .outTo(() -> true, new UserTaskNode("财务审批")
                                                        .setAssignee("rose")
                                                        .add(new UserTaskListenerAdapter() {
                                                            @Override
                                                            public void onFlowInto(UserTaskNode userTaskNode, ProcessInstanceContext context) {
                                                                //这里还可以发邮件通知，或者push推送
                                                                System.out.println("请通知：" + userTaskNode.getAssignee() + "，审批出差申请~~~~~~~");
                                                            }
                                                        })
                                                        //到下一个协调节点，也是Inclusive网关节点，等待财务审核完成之后，再放行下一个流程。
                                                        //注意，这里要next到同一个节点，
                                                        .next(inclusiveGateway)
                                                )

                                        )
                                )

                        )
                )
        );


        ProcessInstance instance = startProcess();

        System.out.println("流程启动成功：" + JsonUtils.toJson(instance));
    }

    private ProcessInstance startProcess() {
        //设置流程变量
        HashMap<String, Object> vars = new HashMap<>();
        vars.put("currUser", "tangwc");
        //启动流程
        ProcessInstance instance = ProcessEngine.startProcess("demo1", vars);
        return instance;
    }
}
