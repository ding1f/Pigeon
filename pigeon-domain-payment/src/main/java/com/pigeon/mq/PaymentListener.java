package com.pigeon.mq;

import com.pigeon.context.UserHolder;
import com.pigeon.listener.MessageListener;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/**
 * 支付模块MQ监听器
 *
 * @author Idenn
 * @date 4/10/2024 3:07 AM
 */
@Service
public class PaymentListener extends MessageListener {

    /*
     * 测试rabbitmq组件
     */
    @Override
    public void receiveMessage(Object object) {
        System.out.println(object);
        System.out.println(UserHolder.getUser());
    }

    @RabbitListener(containerFactory = "rabbitListenerContainerFactory",
            admin = "rabbitAdmin",
            bindings = {
                    @QueueBinding(
                            value = @Queue(name = "pigeon.order.queue"),
                            exchange = @Exchange(name = "pigeon.order.exchange"),
                            key = "pigeon.order.orderRoutingKey"
                    )
            })
    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        super.onMessage(message, channel);
    }
}
