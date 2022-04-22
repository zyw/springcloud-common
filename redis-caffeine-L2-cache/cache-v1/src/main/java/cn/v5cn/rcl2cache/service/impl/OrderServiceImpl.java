package cn.v5cn.rcl2cache.service.impl;

import cn.v5cn.cache.base.constant.CacheConstant;
import cn.v5cn.cache.base.entity.Order;
import cn.v5cn.cache.base.mapper.OrderMapper;
import cn.v5cn.rcl2cache.service.OrderService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author ZYW
 * @version 1.0
 * @date 2022/4/10 10:38 下午
 */
@Slf4j
@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;
    private final Cache cache;
    private final RedisTemplate redisTemplate;

    @Override
    public Order getOrderById(Long id) {
        String key = CacheConstant.ORDER + id;
        Order order = (Order) cache.get(key, k -> {
            // 先查询Redis
            Object obj = redisTemplate.opsForValue().get(key);
            if (Objects.nonNull(obj)) {
                log.info("get data from redis");
                return obj;
            }

            // Redis没有则查询 DB
            log.info("get data from database");
            Order myOrder = orderMapper.selectOne(new LambdaQueryWrapper<Order>().eq(Order::getId, id));
            redisTemplate.opsForValue().set(k, myOrder, 120, TimeUnit.SECONDS);

            return myOrder;
        });

        return order;
    }

    @Override
    public void updateOrder(Order order) {
        log.info("update order data");
        String key = CacheConstant.ORDER + order.getId();
        orderMapper.updateById(order);
        // 修改Redis
        redisTemplate.opsForValue().set(key, order, 120, TimeUnit.SECONDS);
        // 修改本地缓存
        cache.put(key, order);
    }

    @Override
    public void deleteOrder(Long id) {
        log.info("delete order");
        orderMapper.deleteById(id);
        String key = CacheConstant.ORDER + id;
        // Redis中删除
        redisTemplate.delete(key);
        // 从本地缓存删除
        cache.invalidate(key);
    }
}
