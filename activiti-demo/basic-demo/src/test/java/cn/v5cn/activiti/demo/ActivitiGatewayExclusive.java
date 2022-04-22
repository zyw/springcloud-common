package cn.v5cn.activiti.demo;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class ActivitiGatewayExclusive {
    /**
     * 部署流程定义，文件上传方式
     */
    @Test
    public void testDeployment() {
        // 1. 创建ProcessEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2. 得到RepositoryService实例
        RepositoryService repositoryService = processEngine.getRepositoryService();
        // 3. 使用RepositoryService进行部署
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("bpmn/BtExclusion.bpmn") // 添加bpmn资源
                // png资源命名是有规范的。Leave.Process_1.png|jpg|gif|svg 或者Leave.png|jpg|gif|svg
                // 其中Process_1是<bpmn2:process id="Process_1" name="1" isExecutable="true"> 中的id
                .addClasspathResource("bpmn/BtExclusion.svg")
                .name("请假申请流程-排他网关")
                .deploy();
        // 4输出部署信息
        System.out.println("流程部署id: " + deployment.getId());
        System.out.println("流程部署名称： " + deployment.getName());
    }

    /**
     * 启动流程的时候设置流程变量
     * 设置流程变量num
     * 设置任务负责人
     */
    @Test
    public void testStartProcess() {
        // 1. 创建ProcessEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2.获取RuntimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();

        Map<String,Object> veriables = new HashMap<>();
        Map<String,Integer> bt = new HashMap<>();
        bt.put("num", 4);

        veriables.put("bt", bt);

        // 3. 根据流程定义ID启动流程
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("BtExclusion",veriables);


        System.out.println("流程实例ID：" + processInstance.getProcessInstanceId());
        System.out.println("流程定义ID：" + processInstance.getProcessDefinitionId());
        System.out.println("流程激活：" + processInstance.getActivityId());
    }
}
