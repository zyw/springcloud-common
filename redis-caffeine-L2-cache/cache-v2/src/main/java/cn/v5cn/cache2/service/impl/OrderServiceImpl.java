package cn.v5cn.cache2.service.impl;

import cn.v5cn.cache.base.constant.CacheConstant;
import cn.v5cn.cache.base.entity.Order;
import cn.v5cn.cache.base.mapper.OrderMapper;
import cn.v5cn.cache2.service.OrderService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @Cacheable：根据键从缓存中取值，如果缓存存在，那么获取缓存成功之后，直接返回这个缓存的结果。如果缓存不存在，那么执行方法，并将结果放入缓存中。
 * @CachePut：不管之前的键对应的缓存是否存在，都执行方法，并将结果强制放入缓存
 * @CacheEvict：执行完方法后，会移除掉缓存中的数据。
 *
 * @author ZYW
 * @version 1.0
 * @date 2022/4/11 9:13 下午
 */
@Slf4j
@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;
    private RedisTemplate redisTemplate;

    @Override
    @Cacheable(value = "order", key = "#id")
    public Order getOrderById(Long id) {
        String key= CacheConstant.ORDER + id;
        //先查询 Redis
        Object obj = redisTemplate.opsForValue().get(key);
        if (Objects.nonNull(obj)){
            log.info("get data from redis");
            return (Order) obj;
        }
        // Redis没有则查询 DB
        log.info("get data from database");
        Order myOrder = orderMapper.selectOne(new LambdaQueryWrapper<Order>()
                .eq(Order::getId, id));
        redisTemplate.opsForValue().set(key,myOrder,120, TimeUnit.SECONDS);
        return myOrder;
    }

    @Override
    @CachePut(cacheNames = "order", key = "#order.id")
    public Order updateOrder(Order order) {
        log.info("update order data");
        orderMapper.updateById(order);
        //修改 Redis
        redisTemplate.opsForValue().set(CacheConstant.ORDER + order.getId(),
                order, 120, TimeUnit.SECONDS);
        return order;
    }

    @Override
    @CacheEvict(cacheNames = "order", key = "#id")
    public void deleteOrder(Long id) {
        log.info("delete order");
        orderMapper.deleteById(id);
        redisTemplate.delete(CacheConstant.ORDER + id);
    }
}
