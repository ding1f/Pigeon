package com.pigeon.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pigeon.entity.po.TransMessagePO;

import java.util.List;

/**
 * RabbitMQ接口声明
 *
 * @author Idenn
 * @date 4/2/2024 9:58 AM
 */
public interface RabbitMQService {

    /**
     * 发送前暂存消息
     *
     * @param exchange
     * @param routingKey
     * @param body
     * @return com.pigeon.entity.po.TransMessagePO
     * @author Idenn
     * @date 4/2/2024 10:15 AM
     */
    TransMessagePO messageSendReady(String exchange, String routingKey, String body);

    /**
     * 设置消息发送成功
     *
     * @param id
     * @return void
     * @author Idenn
     * @date 4/2/2024 10:15 AM
     */
    void messageSendSuccess(String id);

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
    TransMessagePO messageSendReturn(String id, String exchange, String routingKey, String body);

    /**
     * 查询未发送的消息
     *
     * @return java.util.List<com.pigeon.entity.po.TransMessagePO>
     * @author Idenn
     * @date 4/2/2024 10:16 AM
     */
    List<TransMessagePO> listReadyMessage();

    /**
     * 记录消息重新发送的次数
     *
     * @param id
     * @return void
     * @author Idenn
     * @date 4/2/2024 10:16 AM
     */
    void MessageResend(String id);

    /**
     * 设置记录消息重发多次失败后放弃发送
     *
     * @param id
     * @return void
     * @author Idenn
     * @date 4/2/2024 10:16 AM
     */
    void messageDead(String id);

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
    TransMessagePO messageReceiveReady(String id, String exchange, String routingKey, String queue, String body) throws JsonProcessingException;


    /**
     * 消息消费成功
     *
     * @param id
     * @return void
     * @author Idenn
     * @date 4/2/2024 10:17 AM
     */
    void messageReceiveSuccess(String id);
}
