package com.qianfeng.activiti.test;

import org.activiti.engine.*;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

public class OATest {

    /**
     * 部署流程：就是将画的图解析成代码和数据插入到数据库中保存
     */
    @Test
    public void testCase1(){
        //activiti是工作流引擎。所以activiti的核心API都是通过ProcessEngine类来进行操作。
        //整个项目中建议ProcessEngine对象是唯一的。
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //repositoryService仓库服务用来部署流程
        RepositoryService repositoryService = processEngine.getRepositoryService();
        //部署流程的操作者（流程部署的操作工）
        DeploymentBuilder deployment = repositoryService.createDeployment();
        deployment.addClasspathResource("bpmn/buqian.bpmn");//流程图
        deployment.addClasspathResource("bpmn/buqian.png");//图片
        deployment.name("补签");
        deployment.key("buqian");
        deployment.category("A");
        deployment.deploy();
    }

    /**
     * 创建用户
     */
    @Test
    public void testCase0(){

        IdentityService identityService = ProcessEngines.getDefaultProcessEngine()
                .getIdentityService();
        User lisi = identityService.newUser("lisi");
        lisi.setPassword("123456");
        identityService.saveUser(lisi);

    }


    /**
     * 启动流程：比如忘记打卡，那就在OA后台填写补签申请。（填写补签申请之后提交就是启动了补签流程）
     */
    @Test
    public void testCase2(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        //RuntimeService是用来启动流程的服务。
        RuntimeService runtimeService = processEngine.getRuntimeService();
        //启动流程
        //创建Map用来存放流程所需要的变量
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("user","lisi"); //李四需要填写补签卡
        //参数1：流程的key
        //参数2：流程所需的变量
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("buqian_process1",map);
        String id = processInstance.getId();
        String businessKey = processInstance.getBusinessKey();
        String name = processInstance.getName();
        System.out.println(id+"/"+businessKey+"/"+name);
    }

    /**
     * 查询当前用户名下的流程节点
     */
    @Test
    public void testCase3(){
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        //TaskService是一个用来操作流程节点的服务（任务服务），比如流程启动之后的所有流程操作，都是TaskService完成
        TaskService taskService = defaultProcessEngine.getTaskService();
        List<Task> lisi = taskService.createTaskQuery()
                .taskAssignee("lisi")
                .list();
        for (Task task:lisi) {
            System.out.println(task.getId());
            System.out.println(task.getName());
        }
//        taskService.complete();
    }

    /**
     * 填写补签内容，并且将补签单提交给主管审批
     */
    @Test
    public void testCase4(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("user","zhangsan");//指定受理人（提交给哪位主管）
        map.put("time","2018.03.22 9:00");//指定补签时间
        map.put("applyuser","lisi");
        map.put("department","研发部");
        //参数1：任务的ID
        //参数2：当前任务需要携带的数据（补签数据）
        taskService.complete("27506",map);//提交补签申请
    }

    /**
     * 主管审批
     */
    @Test
    public void testCase5(){
        //1、查询名下的申请任务
        TaskService taskService = ProcessEngines.getDefaultProcessEngine().getTaskService();
        List<Task> list = taskService.createTaskQuery()
                .taskAssignee("zhangsan")
                .list();
        for (Task task:list) {
            String taskId = task.getId();
            System.out.println(taskId + "/" + task.getName() + "/" + task.getAssignee());
            //获取当前任务保存的变量
            //参数1：任务ID
            //参数2：变量的名称
            Object time = taskService.getVariable(taskId, "time");
            Object applyuser = taskService.getVariable(taskId, "applyuser");
            Object department = taskService.getVariable(taskId, "department");
            System.out.println("补签时间："+time + "/申请人：" + applyuser +"/部门：" + department);

            //2、查询到当前名下的任务之后，进行审批
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("user","cuihua");
            map.put("time","2018.03.22 9:00");//指定补签时间
            map.put("applyuser","lisi");
            map.put("department","研发部");
            taskService.complete(taskId,map);
        }
    }

    /**
     * 人事确认
     */
    @Test
    public void testCase6(){
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = defaultProcessEngine.getTaskService();
        List<Task> tasks = taskService.createTaskQuery()
                .taskAssignee("cuihua")
                .list();

        for (Task task:tasks) {
            String id = task.getId();
            System.out.println(task.getName());
            Object time = taskService.getVariable(id, "time");
            Object applyuser = taskService.getVariable(id, "applyuser");
            Object department = taskService.getVariable(id, "department");
            System.out.println("补签时间："+time + "/申请人：" + applyuser +"/部门：" + department);

            //2、人事确认以上信息之后，进行批复
            taskService.complete(id);
        }
    }
}
