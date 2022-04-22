package cn.v5cn.drools;

import cn.v5cn.drools.entity.Order;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

public class TestDrools {

    @Test
    public void droolsTest1() {
        //1. 获得KieServices
        KieServices kieServices = KieServices.Factory.get();
        //2. 获得KieContainer
        KieContainer container = kieServices.getKieClasspathContainer();
        //3. 获得KieSession
        KieSession session = container.newKieSession();

        //事实对象
        Order order = new Order();
        order.setAmout(234); // 订单金额
        //4. 插入规则运算对象
        session.insert(order);

        //5. 执行规则引擎，触发规则
        session.fireAllRules();
        //6. 关闭session
        session.dispose();

        //7. 打印规则结果积分
        System.out.println("执行规则之后，积分：" + order.getScore());
    }
}
