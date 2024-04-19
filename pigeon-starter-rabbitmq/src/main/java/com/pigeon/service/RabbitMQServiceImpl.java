package com.pigeon.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pigeon.entity.po.TransMessagePO;
import com.pigeon.enums.MessageType;
import com.pigeon.repo.RabbitMQRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * RabbitMQ接口实现类
 *
 * @author Idenn
 * @date 4/2/2024 9:59 AM
 */
@Service
public class RabbitMQServiceImpl implements RabbitMQService {

    /**
     * 消息服务存储器
     */
    @Resource
    private RabbitMQRepository rabbitMQRepository;

    @Value("${spring.application.name}")
    private String serviceName;

    /**
     * 发送前暂存消息
     * 在redis中存储发送的消息
     *
     * @param exchange
     * @param routingKey
     * @param body
     * @return com.pigeon.entity.po.TransMessagePO
     * @author Idenn
     * @date 4/2/2024 10:15 AM
     */
    @Override
    public TransMessagePO messageSendReady(String exchange, String routingKey, String body) {
        final String messageId = UUID.randomUUID().toString();
        TransMessagePO transMessagePO = new TransMessagePO();
        transMessagePO.setId(messageId);
        transMessagePO.setServiceName(serviceName);
        transMessagePO.setExchange(exchange);
        transMessagePO.setRoutingKey(routingKey);
        transMessagePO.setBody(body);
        transMessagePO.setDate(new Date());
        transMessagePO.setSequence(0);
        transMessagePO.setType(MessageType.SEND);
        rabbitMQRepository.insert(transMessagePO);
        return transMessagePO;
    }

    /**
     * 设置消息发送成功
     * 发送成功则删除成功发送的消息
     *
     * @param id
     * @return void
     * @author Idenn
     * @date 4/2/2024 10:15 AM
     */
    @Override
    public void messageSendSuccess(String id) {
        rabbitMQRepository.delete(id, MessageType.SEND);
    }

    /**
     * 设置消息返回重新持久化
     *
     * @param id
     * @param exchange
     * @param routingKey
     * @param body
     * @return com.pigeon.entity.po.TransMessagePO
     * @author Idenn
     * @date 4/2/2024 10:16 AM
     */
    @Override
    public TransMessagePO messageSendReturn(String id, String exchange, String routingKey, String body) {
        return messageSendReady(exchange, routingKey, body);
    }

    /**
     * 查询未发送的消息
     *
     * @return java.util.List<com.pigeon.entity.po.TransMessagePO>
     * @author Idenn
     * @date 4/2/2024 10:16 AM
     */
    @Override
    public List<TransMessagePO> listReadyMessage() {
        return null;
    }

    /**
     * 记录消息重新发送的次数
     *
     * @param id
     * @return void
     * @author Idenn
     * @date 4/2/2024 10:16 AM
     */
    @Override
    public void MessageResend(String id) {

    }

    /**
     * 设置记录消息重发多次失败后放弃发送
     *
     * @param id
     * @return void
     * @author Idenn
     * @date 4/2/2024 10:16 AM
     */
    @Override
    public void messageDead(String id) {

    }

    /**
     * 消息消费前保存
     *
     * @param id
     * @param exchange
     * @param routingKey
     * @param queue
     * @param body
     * @return com.pigeon.entity.po.TransMessagePO
     * @author Idenn
     * @date 4/2/2024 10:16 AM
     */
    @Override
    public TransMessagePO messageReceiveReady(String id, String exchange, String routingKey, String queue, String body) throws JsonProcessingException {
        // 首先查询判断是不是消费过
        TransMessagePO transMessagePO = rabbitMQRepository.select(id, MessageType.RECEIVE);
        if (null == transMessagePO) {
            transMessagePO = new TransMessagePO();
            transMessagePO.setId(id);
            transMessagePO.setServiceName(serviceName);
            transMessagePO.setExchange(exchange);
            transMessagePO.setRoutingKey(routingKey);
            transMessagePO.setQueue(queue);
            transMessagePO.setBody(body);
            transMessagePO.setDate(new Date());
            transMessagePO.setSequence(0);
            transMessagePO.setType(MessageType.RECEIVE);
            rabbitMQRepository.insert(transMessagePO);
        } else {
            transMessagePO.setSequence(transMessagePO.getSequence() + 1);
            rabbitMQRepository.update(transMessagePO);
        }
        return transMessagePO;
    }

    /**
     * 消息消费成功
     *
     * @param id
     * @return void
     * @author Idenn
     * @date 4/2/2024 10:17 AM
     */
    @Override
    public void messageReceiveSuccess(String id) {
        rabbitMQRepository.delete(id, MessageType.RECEIVE);
    }
}
