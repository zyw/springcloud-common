package cn.v5cn.activiti;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class SpringBootActivitiDemo {
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private HistoryService historyService;


    /**
     * 启动流程实例
     */
    @Test
    public void testStartProcess() {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("myLeave");

        // 输出内容
        System.out.println("流程定义ID: " + processInstance.getProcessDefinitionId());
        System.out.println("流程实例ID: " + processInstance.getProcessInstanceId());
        System.out.println("当前活动ID: " + processInstance.getActivityId());
    }
}
