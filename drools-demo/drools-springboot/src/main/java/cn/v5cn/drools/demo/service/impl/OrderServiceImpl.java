package cn.v5cn.drools.demo.service.impl;

import cn.v5cn.drools.demo.entity.Order;
import cn.v5cn.drools.demo.service.OrderService;
import org.kie.api.KieBase;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private KieBase kieBase;

    @Override
    public Order calcScore(Order order) {
        KieSession kieSession = kieBase.newKieSession();
        kieSession.insert(order);
        kieSession.fireAllRules();
        kieSession.dispose();
        return order;
    }
}
