package com.pigeon.repo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pigeon.entity.po.TransMessagePO;
import com.pigeon.enums.MessageType;

import java.util.List;

/**
 * rabbitmq的消息需要保存在redis中
 *
 * @author Idenn
 * @date 4/2/2024 9:50 AM
 */
public interface RabbitMQRepository {

    /**
     * 存入消息
     *
     * @param transMessagePO
     * @return void
     * @author Idenn
     * @date 4/2/2024 9:51 AM
     */
    void insert(TransMessagePO transMessagePO);

    /**
     * 修改消息
     *
     * @param transMessagePO
     * @return void
     * @author Idenn
     * @date 4/2/2024 9:52 AM
     */
    void update(TransMessagePO transMessagePO);

    /**
     * 根据ID获取消息
     *
     * @param id
     * @param messageType
     * @return TransMessagePO
     * @author Idenn
     * @date 4/2/2024 9:52 AM
     */
    TransMessagePO select(String id, MessageType messageType) throws JsonProcessingException;

    /**
     * 根据type获取消息
     *
     * @param type
     * @return java.util.List<TransMessagePO>
     * @author Idenn
     * @date 4/2/2024 9:52 AM
     */
    List<TransMessagePO> selectByType(String type);

    /**
     * 根据id删除消息
     *
     * @param id
     * @param messageType
     * @return void
     * @author Idenn
     * @date 4/2/2024 9:52 AM
     */
    void delete(String id, MessageType messageType);
}
