package cn.v5cn.rcl2cache.service;

import cn.v5cn.cache.base.entity.Order;

/**
 * @author ZYW
 * @version 1.0
 * @date 2022/4/10 10:34 下午
 */
public interface OrderService {
    Order getOrderById(Long id);

    void updateOrder(Order order);

    void deleteOrder(Long id);
}
