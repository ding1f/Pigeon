package com.pigeon.repo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pigeon.config.RabbitMQPropertiesConfig;
import com.pigeon.entity.po.TransMessagePO;
import com.pigeon.enums.MessageType;
import com.pigeon.manager.RedisManager;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * rabbitmq的数据访问层的实现类
 *
 * @author Idenn
 * @date 4/2/2024 9:54 AM
 */
@Repository
public class RabbitMQRepositoryImpl implements RabbitMQRepository {

    @Resource
    private RedisManager redisManager;

    @Resource
    RabbitMQPropertiesConfig rabbitMQPropertiesConfig;

    /**
     * 存入消息
     *
     * @param transMessagePO
     * @return void
     * @author Idenn
     * @date 4/2/2024 9:51 AM
     */
    @Override
    public void insert(TransMessagePO transMessagePO) {
        redisManager.setPojo(getKey(transMessagePO.getId(), transMessagePO.getType()), transMessagePO);
    }

    /**
     * 修改消息
     *
     * @param transMessagePO
     * @return void
     * @author Idenn
     * @date 4/2/2024 9:52 AM
     */
    @Override
    public void update(TransMessagePO transMessagePO) {
        redisManager.setPojo(getKey(transMessagePO.getId(), transMessagePO.getType()), transMessagePO);
    }

    /**
     * 根据ID获取消息
     *
     * @param id
     * @param messageType
     * @return TransMessagePO
     * @author Idenn
     * @date 4/2/2024 9:52 AM
     */
    @Override
    public TransMessagePO select(String id, MessageType messageType) throws JsonProcessingException {
        return redisManager.getPojo(getKey(id, messageType), TransMessagePO.class);
    }

    /**
     * 根据type获取消息
     *
     * @param type
     * @return java.util.List<TransMessagePO>
     * @author Idenn
     * @date 4/2/2024 9:52 AM
     */
    @Override
    public List<TransMessagePO> selectByType(String type) {
        return null;
    }

    /**
     * 根据id删除消息
     *
     * @param id
     * @param messageType
     * @return void
     * @author Idenn
     * @date 4/2/2024 9:52 AM
     */
    @Override
    public void delete(String id, MessageType messageType) {
        // redisManager.delete(getKey(id, messageType));
        redisManager.expire(getKey(id, messageType), rabbitMQPropertiesConfig.getAccessTokenValiditySeconds());
    }

    private String getKey(String id, MessageType messageType) {
        return rabbitMQPropertiesConfig.getRedisKeyPrefix() + messageType + ":" + id;
    }
}
