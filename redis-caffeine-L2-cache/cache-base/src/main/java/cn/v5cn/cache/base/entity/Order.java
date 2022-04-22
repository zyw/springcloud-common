package cn.v5cn.cache.base.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author ZYW
 * @version 1.0
 * @date 2022/4/10 10:35 下午
 */
@Data
@Accessors(chain = true)
@TableName(value = "t_order")
public class Order {
    @TableId(value = "id")
    Long id;

    String orderNumber;

    Double money;

    Integer status;
}
