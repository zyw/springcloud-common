package cn.v5cn.activiti.demo;

import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricActivityInstanceQuery;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.List;

public class ActivitiDemo {

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
                .addClasspathResource("bpmn/Leave.bpmn") // 添加bpmn资源
                // png资源命名是有规范的。Leave.Process_1.png|jpg|gif|svg 或者Leave.png|jpg|gif|svg
                // 其中Process_1是<bpmn2:process id="Process_1" name="1" isExecutable="true"> 中的id
                .addClasspathResource("bpmn/Leave.svg")
                .name("请假申请流程")
                .deploy();
        // 4输出部署信息
        System.out.println("流程部署id: " + deployment.getId());
        System.out.println("流程部署名称： " + deployment.getName());
    }

    /**
     * 启动流程实例
     */
    @Test
    public void testStartProcess() {
        // 1. 创建ProcessEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2.获取RuntimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();
        // 3. 根据流程定义ID启动流程
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("Process_1");
        // 输出内容
        System.out.println("流程定义Id: " + processInstance.getProcessDefinitionId());
        System.out.println("流程实例Id: " + processInstance.getId());
        System.out.println("流程活动Id: " + processInstance.getActivityId());
    }

    /**
     * 查询当前个人待执行的任务
     */
    @Test
    public void testFindPersonalTaskList() {
        // 1. 任务负责人
        String assignee = "1";
        // 2. 创建ProcessEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 3. 获得TaskService
        TaskService taskService = processEngine.getTaskService();
        // 4. 根据流程Key和任务负责人查询任务
        List<Task> taskList = taskService.createTaskQuery()
                .processDefinitionKey("Process_1")
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
        String assignee = "1";
        // 2. 创建ProcessEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 3. 获得TaskService
        TaskService taskService = processEngine.getTaskService();
        // 4. 根据流程Key和任务负责人查询任务
        // 返回一个任务对象
        Task task = taskService.createTaskQuery()
                .processDefinitionKey("Process_1")
                .taskAssignee(assignee)
                .singleResult();

        // 5. 完成任务，参数：任务id
        taskService.complete(task.getId());
    }

    /**
     * 查询流程定义
     */
    @Test
    public void queryProcessDefinition() {
        // 1. 创建ProcessEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2. 得到RepositoryService实例
        RepositoryService repositoryService = processEngine.getRepositoryService();
        // 3. 得到ProcessDefinitionQuery对象
        ProcessDefinitionQuery query = repositoryService.createProcessDefinitionQuery();
        // 查询出当前所有的流程定义
        //      条件：processDefinitionKey=Process_1
        //      orderByProcessDefinitionVersion 按照版本排序
        // desc倒序
        // list 返回集合
        List<ProcessDefinition> list = query.processDefinitionKey("Process_1")
                .orderByProcessDefinitionVersion()
                .desc()
                .list();
        list.forEach((item) -> {
            System.out.println("流程定义 id=" + item.getId());
            System.out.println("流程定义 name=" + item.getName());
            System.out.println("流程定义 key=" + item.getKey());
            System.out.println("流程定义 version=" + item.getVersion());
            System.out.println("流程部署 id=" + item.getDeploymentId());
            System.out.println("--------------------------------------------------------");
        });
    }

    /**
     * 查询流程实例
     */
    @Test
    public void queryProcessInstance() {
        // 1. 流程定义key
        String processDefinitionKey = "Process_1";
        // 2. 创建ProcessEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 3. 获得RuntimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();
        List<ProcessInstance> instanceList = runtimeService.createProcessInstanceQuery()
                .processDefinitionKey(processDefinitionKey)
                .list();

        instanceList.forEach(item -> {
            System.out.println("============================================");
            System.out.println("流程实例id: " + item.getProcessInstanceId());
            System.out.println("所属流程定义id: " + item.getProcessDefinitionId());
            System.out.println("是否执行完成: " + item.isEnded());
            System.out.println("是否暂停: " + item.isSuspended());
            System.out.println("当前活动标识: " + item.getActivityId());
            System.out.println("业务关键字: " + item.getBusinessKey());
        });
    }

    /**
     * 删除部署流程
     */
    @Test
    public void deleteDeployment() {
        // 1. 部署流程ID
        String deploymentId = "2501";
        // 2. 创建ProcessEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 3. 得到RepositoryService实例
        RepositoryService repositoryService = processEngine.getRepositoryService();
        // 4. 删除流程定义，如果该流程定义已经有流程实例启动则删除时出错
        repositoryService.deleteDeployment(deploymentId);
        // 4. 设置为true，级联删除流程定义，即使该流程有流程实例启动也可以删除，设置为false非级联删除方式，如果流程
        //repositoryService.deleteDeployment(deploymentId, true);
    }

    /**
     * 查看历史信息
     */
    @Test
    public void findHistoryInfo() {
        // 1. 创建ProcessEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2. 获取HistoryService
        HistoryService historyService = processEngine.getHistoryService();
        // 3. 获取actinst表的查询对象
        HistoricActivityInstanceQuery query = historyService.createHistoricActivityInstanceQuery();
        // 4. 条件：根据InstanceId: 查询：查询一个流程的所有历史信息
        query.processInstanceId("5001");
        // 条件：根据DefinitionId 查询一种流程的所有历史信息
//        query.processDefinitionId("Process_1:2:2504");
        // 增加排序操作，orderByHistoricActivityInstanceStartTime 根据开始时间排序 asc升序
        query.orderByHistoricActivityInstanceStartTime().asc();
        // 查询所有内容
        List<HistoricActivityInstance> list = query.list();
        list.forEach(item->{
            System.out.println(item.getActivityId());
            System.out.println(item.getActivityName());
            System.out.println(item.getProcessDefinitionId());
            System.out.println(item.getProcessInstanceId());
            System.out.println("=================================");
        });
    }

}
