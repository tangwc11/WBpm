package com.wentry.wbpm.api;

import com.wentry.wbpm.core.base.BaseWBpmNode;
import com.wentry.wbpm.core.base.WBpmNode;
import com.wentry.wbpm.model.task.UserTaskNode;

import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description:
 * @Author: tangwc
 */
public class ProcessEngine {

    private static final Map<String, ProcessDefinition> processDefinitionMap = new ConcurrentHashMap<>();

    public static void load(String key, ProcessDefinition processDefinition) {
        processDefinitionMap.put(key, processDefinition);
    }

    private static final Map<String, ProcessInstance> runningInstance = new ConcurrentHashMap<>();

    public static ProcessInstance startProcess(String key, Map<String, Object> variables) {
        ProcessDefinition processDefinition = processDefinitionMap.get(key);
        if (processDefinition == null) {
            return null;
        }
        ProcessInstance processInstance = processDefinition.startProcess(variables);

        //记录运行中
        runningInstance.put(processInstance.getId(), processInstance);

        //注册回调事件，清除运行中的实例
        processInstance.getProcessInstanceContext().addCompleteListener(
                ctx -> runningInstance.remove(processInstance.getId())
        );

        //注册结束日志打印回调
        processInstance.getProcessInstanceContext().addCompleteListener(
                ctx -> {
                    System.out.println(ctx.getProcessInstance().getProcessDefinition().getName()
                            + ":" + ctx.getProcessInstance().getId() + "执行完毕，日志如下：");
                    for (String log : ctx.getLogs()) {
                        System.out.println("执行日志:" + log);
                    }
                }
        );

        return processInstance;
    }


    public static Map<String/*instanceId+:+processName*/, List<WBpmNode>> getUserTask(String userName) {

        Map<String, List<WBpmNode>> userTask = new ConcurrentHashMap<>();

        for (Map.Entry<String, ProcessInstance> ety : runningInstance.entrySet()) {
            ProcessInstance instance = ety.getValue();
            ProcessInstanceContext context = instance.getProcessInstanceContext();

            Set<WBpmNode> active = context.getActive();
            if (CollectionUtils.isEmpty(active)) {
                continue;
            }

            List<WBpmNode> node = new ArrayList<>();
            for (WBpmNode wBpmNode : active) {
                if (wBpmNode instanceof UserTaskNode) {
                    if (((UserTaskNode) wBpmNode).getAssignee().equals(userName)) {
                        node.add(wBpmNode);
                    }
                }
            }

            if (CollectionUtils.isEmpty(node)) {
                continue;
            }

            userTask.put(instance.getId() + ":" + instance.getProcessDefinition().getName(), node);
        }

        return userTask;
    }


    public static String complete(String userName, String instanceId, String nodeId) {
        Map<String, List<WBpmNode>> userTask = getUserTask(userName);

        for (Map.Entry<String, List<WBpmNode>> ety : userTask.entrySet()) {
            for (WBpmNode wBpmNode : ety.getValue()) {
                if (!ety.getKey().split(":")[0].equals(instanceId)) {
                    continue;
                }
                if (((BaseWBpmNode) wBpmNode).getId().equals(nodeId)) {
                    String instId = ety.getKey().split(":")[0];
                    ProcessInstance processInstance = runningInstance.get(instId);
                    if (processInstance == null) {
                        return "找不到process实例";
                    }
                    wBpmNode.flowOut(processInstance.getProcessInstanceContext());
                    return "执行完毕";
                }
            }
        }

        return "找不到task";
    }

    public static Map<String, ProcessDefinition> processDefs() {
        return processDefinitionMap;
    }

    public static Map<String, ProcessInstance> runningProcess() {
        return runningInstance;
    }
}
