package cn.v5cn.activiti.demo;

import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestVariables {
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
                .addClasspathResource("bpmn/BusinessTravel.bpmn") // 添加bpmn资源
                // png资源命名是有规范的。Leave.Process_1.png|jpg|gif|svg 或者Leave.png|jpg|gif|svg
                // 其中Process_1是<bpmn2:process id="Process_1" name="1" isExecutable="true"> 中的id
                .addClasspathResource("bpmn/BusinessTravel.svg")
                .name("出差申请流程-variables")
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
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("businessTravel",veriables);
    }

    /**
     * 查询当前个人待执行的任务
     */
    @Test
    public void testFindPersonalTaskList() {
        // 1. 任务负责人
        String assignee = "4";
        // 2. 创建ProcessEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 3. 获得TaskService
        TaskService taskService = processEngine.getTaskService();
        // 4. 根据流程Key和任务负责人查询任务
        List<Task> taskList = taskService.createTaskQuery()
                .processDefinitionKey("businessTravel")
                .taskAssignee(assignee)
                .list();
        taskList.forEach((item) -> {
            System.out.println("流程实例Id: " + item.getProcessInstanceId());
            System.out.println("任务Id: " + item.getId());
            System.out.println("任务负责人: " + item.getAssignee());
            System.out.println("任务名称: " + item.getName());
        });
    }

    /**
     * 完成任务
     */
    @Test
    public void completeTask() {
        // 1. 任务负责人
        String assignee = "4";
        // 2. 创建ProcessEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 3. 获得TaskService
        TaskService taskService = processEngine.getTaskService();
        // 4. 根据流程Key和任务负责人查询任务
        // 返回一个任务对象
        Task task = taskService.createTaskQuery()
                .processDefinitionKey("businessTravel")
                .taskAssignee(assignee)
                .singleResult();

        // 5. 完成任务，参数：任务id
        taskService.complete(task.getId());
    }
}
