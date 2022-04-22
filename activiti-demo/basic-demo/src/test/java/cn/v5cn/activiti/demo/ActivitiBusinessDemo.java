package cn.v5cn.activiti.demo;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

public class ActivitiBusinessDemo {

    /**
     * 添加业务key, 到Activiti的表
     */
    @Test
    public void addBusinessKey() {
        // 1. 创建ProcessEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2.获取RuntimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();
        // 3. 根据流程定义ID启动流程，添加BusinessKey
        //    第一个参数：流程定义的key
        //    第二个参数：businessKey, 存出差申请单的id，就是1002
        ProcessInstance processInstance = runtimeService
                .startProcessInstanceByKey("Process_1","1002");
        // 4. 输出
        System.out.println("busionessKey=" + processInstance.getBusinessKey());
    }

    /**
     * 全部流程实例的挂起和激活
     * suspend 暂停
     */
    @Test
    public void suspendAllProcessDefinition() {
        // 1. 创建ProcessEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2. 得到RepositoryService实例
        RepositoryService repositoryService = processEngine.getRepositoryService();
        // 3. 查询流程定义，获取流程定义的查询对象
        ProcessDefinition definition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey("Process_1")
                .singleResult();
        // 4. 获取当前流程定义的实例是否都是挂起状态
        boolean suspended = definition.isSuspended();
        // 5. 获取流程定义的id
        String id = definition.getId();
        // 6. 如果是挂起状态就激活，如果是激活就挂起
        if (suspended) {
            // 如果是挂起，可以执行激活的操作，参数1：流程定义id, 参数2：是否激活，参数3：激活时间
            repositoryService.activateProcessDefinitionById(id, true, null);
            System.out.println("流程定义Id: " + id + "，已激活");
        } else {
            // 如果是激活状态，改为挂起状态，参数1：流程定义id，参数2：是否暂停 参数3：暂停时间
            repositoryService.suspendProcessDefinitionById(id, true, null);
            System.out.println("流程定义id: " + id + ", 已挂起");
        }
    }

    /**
     * 挂起和激活单个流程实例
     * suspend 暂停
     */
    @Test
    public void suspendSingleProcessInstance() {
        // 1. 创建ProcessEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2. 得到RepositoryService实例
        RuntimeService runtimeService = processEngine.getRuntimeService();
        // 3. 查询流程定义，获取流程定义的查询对象
        ProcessInstance instance = runtimeService.createProcessInstanceQuery()
                .processInstanceId("Process_1")
                .singleResult();
        // 4. 获取当前流程定义的实例是否都是挂起状态
        boolean suspended = instance.isSuspended();
        // 5. 获取流程定义的id
        String id = instance.getId();
        // 6. 如果是挂起状态就激活，如果是激活就挂起
        if (suspended) {
            // 如果是挂起，可以执行激活的操作，参数1：流程定义id, 参数2：是否激活，参数3：激活时间
            runtimeService.activateProcessInstanceById(id);
            System.out.println("流程实例Id: " + id + "，已激活");
        } else {
            // 如果是激活状态，改为挂起状态，参数1：流程定义id，参数2：是否暂停 参数3：暂停时间
            runtimeService.suspendProcessInstanceById(id);
            System.out.println("流程实例id: " + id + ", 已挂起");
        }
    }
}
