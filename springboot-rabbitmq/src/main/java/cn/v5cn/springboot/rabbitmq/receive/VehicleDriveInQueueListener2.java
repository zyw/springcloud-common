package cn.v5cn.springboot.rabbitmq.receive;

import cn.v5cn.springboot.rabbitmq.util.Constants;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author ZYW
 * @version 1.0
 * @date 2018-12-26 21:19
 */
@Component
@RabbitListener(queues = Constants.VEHICLE_DRIVE_IN_QUEUE + "num02")
public class VehicleDriveInQueueListener2 {

    @RabbitHandler
    public void process(String message) {
        System.out.println("VehicleDriveInQueueListener2: " + message);
    }
}
