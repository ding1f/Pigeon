package com.pigeon.manager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pigeon.entity.po.TransMessagePO;
import com.pigeon.entity.pojo.MessagePOJO;
import com.pigeon.entity.pojo.UserPOJO;
import com.pigeon.service.RabbitMQService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * rabbitmq管理
 *
 * @author Idenn
 * @date 4/2/2024 9:42 AM
 */
@Slf4j
@Component
public class RabbitMQManager {

    /**
     * 消息发送器
     */
    @Resource
    RabbitTemplate rabbitTemplate;

    /**
     * 消息服务
     */
    @Resource
    RabbitMQService rabbitMQService;


    /**
     * 发送消息
     *
     * @param exchange
     * @param routingKey
     * @param body
     * @param user
     * @return void
     * @author Idenn
     * @date 4/2/2024 10:14 AM
     */
    public void send(String exchange, String routingKey, Object body, UserPOJO user) {
        log.info("send(): exchange: {} routingKey: {} body: {}", exchange, routingKey, body.toString());
        try {
            // 封装协议
            MessagePOJO userInfoPOJO = new MessagePOJO();
            userInfoPOJO.setBody(body);
            userInfoPOJO.setUserPOJO(user);
            ObjectMapper objectMapper = new ObjectMapper();
            String bodyStr = objectMapper.writeValueAsString(userInfoPOJO);
            TransMessagePO transMessagePO = rabbitMQService.messageSendReady(exchange, routingKey, bodyStr);
            MessageProperties messageProperties = new MessageProperties();
            messageProperties.setContentType("application/json");
            Message message = new Message(bodyStr.getBytes(), messageProperties);
            message.getMessageProperties().setMessageId(transMessagePO.getId());
            rabbitTemplate.convertAndSend(exchange, routingKey, message,
                    new CorrelationData(transMessagePO.getId()));
            log.info("message sent, ID: {}", transMessagePO.getId());
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
    }

    public void send(String exchange, String routingKey, MessagePOJO userInfoPOJO) {
        log.info("send(): exchange: {} routingKey: {} UserInfoPOJO: {}", exchange, routingKey, userInfoPOJO.toString());
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String bodyStr = objectMapper.writeValueAsString(userInfoPOJO);
            TransMessagePO transMessagePO = rabbitMQService.messageSendReady(exchange, routingKey, bodyStr);
            MessageProperties messageProperties = new MessageProperties();
            messageProperties.setContentType("application/json");
            Message message = new Message(bodyStr.getBytes(), messageProperties);
            message.getMessageProperties().setMessageId(transMessagePO.getId());
            rabbitTemplate.convertAndSend(exchange, routingKey, message,
                    new CorrelationData(transMessagePO.getId()));
            log.info("message sent, ID: {}", transMessagePO.getId());
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
    }
}
