package com.qianfeng.activiti.test;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:activiti.cfg.xml")
public class SpringActivitiTest {

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private HistoryService historyService;

    @Test
    public void testCase1(){
        List<Task> taskList = taskService.createTaskQuery()
                .taskAssignee("zhangsan")
                .list();
        for (Task task : taskList) {
            System.out.println(task.getName());
        }
    }

    @Test
    public void testCase2(){
        HistoricTaskInstanceQuery taskInstanceQuery = historyService.createHistoricTaskInstanceQuery();
        List<HistoricTaskInstance> list = taskInstanceQuery
                .taskAssignee("zhangsan")//查询具体审批人的数据
                .taskCompletedBefore(new Date())//查询（现在为结点的以前的）历史任务
                .orderByHistoricTaskInstanceStartTime() //以任务的开始时间进行排序
                .desc() //倒序
                .list();
        //显示历史数据
        for (HistoricTaskInstance taskInstance:list) {
            System.out.println(taskInstance.getName());
            String parentTaskId = taskInstance.getParentTaskId();
            System.out.println(parentTaskId);
            System.out.println(taskInstance.getId());
            System.out.println(taskInstance.getAssignee());


        }
    }

    /**
     * 部署请假流程
     */
    @Test
    public void testCase3(){
        DeploymentBuilder deployment = repositoryService.createDeployment();
         deployment.key("qingjia")
         .name("请假")
         .category("A")
         .addClasspathResource("bpmn/qingjia.bpmn")
         .addClasspathResource("bpmn/qingjia.png")
         .deploy();

    }

    /**
     * 启动流程（开始请假）
     */
    @Test
    public void testCase4(){
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("user","zhangsan");
        runtimeService.startProcessInstanceByKey("qingjia_process2",map);
    }

    /**
     * 查询张三名下的任务
     */
    @Test
    public void testCase5(){
        List<Task> taskList = taskService.createTaskQuery()
                .taskAssignee("zhangsan")
                .list();
        for (Task task:taskList) {
            System.out.println(task.getName() + "/" + task.getCategory());

            //张三填写请假单
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("user","lisi"); //审批人是李四
            map.put("days",3); //请假3天
            taskService.complete(task.getId(),map);
        }


    }

    @Test
    public void testCase6(){
        List<Task> taskList = taskService.createTaskQuery()
                .taskAssignee("lisi")
                .list();
        for (Task task:taskList) {
            System.out.println(task.getName());
        }
    }
}
