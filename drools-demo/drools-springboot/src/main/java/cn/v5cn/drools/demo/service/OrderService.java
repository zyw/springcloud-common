package cn.v5cn.drools.demo.service;

import cn.v5cn.drools.demo.entity.Order;

/**
 * @author powertime
 */
public interface OrderService {
    /**
     *
     * @param order
     * @return
     */
    Order calcScore(Order order);
}
