package cn.v5cn.drools.demo.controller;

import cn.v5cn.drools.demo.entity.Order;
import cn.v5cn.drools.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/calc")
    public String calcScore() {
        Order order = new Order();
        order.setAmout(600);
        order = orderService.calcScore(order);
        System.out.println(order);
        return "success";
    }
}
