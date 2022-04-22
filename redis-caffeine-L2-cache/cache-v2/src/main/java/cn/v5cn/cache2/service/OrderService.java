package cn.v5cn.cache2.service;

import cn.v5cn.cache.base.entity.Order;

/**
 * @author ZYW
 * @version 1.0
 * @date 2022/4/11 8:58 下午
 */
public interface OrderService {
    Order getOrderById(Long id);

    Order updateOrder(Order order);

    void deleteOrder(Long id);
}
