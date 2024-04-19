package com.pigeon.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pigeon.context.UserHolder;
import com.pigeon.entity.po.TransMessagePO;
import com.pigeon.entity.pojo.MessagePOJO;
import com.pigeon.service.RabbitMQService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;

import javax.annotation.Resource;

/**
 * rabbitmq监听器
 * 在需要监听的业务模块中继承这个类，重写的receiveMessage()方法即为真正接收消息时执行的方法
 *
 * @author Idenn
 * @date 4/2/2024 9:34 AM
 */

@Slf4j
public abstract class MessageListener implements ChannelAwareMessageListener {

    @Resource
    RabbitMQService rabbitMQService;

    public abstract void receiveMessage(Object object);

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        MessageProperties messageProperties = message.getMessageProperties();
        long deliveryTag = messageProperties.getDeliveryTag();
        TransMessagePO transMessagePO = rabbitMQService.messageReceiveReady(
                messageProperties.getMessageId(),
                messageProperties.getReceivedExchange(),
                messageProperties.getReceivedRoutingKey(),
                messageProperties.getConsumerQueue(),
                new String(message.getBody())
        );
        log.info("收到消息{}, 消费次数{}", transMessagePO.getId(), transMessagePO.getSequence());

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            MessagePOJO userInfoPOJO = objectMapper.readValue(message.getBody(), MessagePOJO.class);
            UserHolder.setUser(userInfoPOJO.getUserPOJO());
            receiveMessage(userInfoPOJO.getBody());
            channel.basicAck(deliveryTag, false);
            rabbitMQService.messageReceiveSuccess(transMessagePO.getId());
        } catch (Exception e) {
            if (transMessagePO.getSequence() >= 10) {
                channel.basicReject(deliveryTag, false);
            } else {
                Thread.sleep((long) Math.pow(2, transMessagePO.getSequence()) * 1000);
                channel.basicNack(deliveryTag, false, true);
            }
        } finally {
            UserHolder.clear();
        }
    }
}
