package com.pigeon.config;

import com.pigeon.service.RabbitMQService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * rabbitmq的核心配置类
 *
 * @author Idenn
 * @date 4/2/2024 9:05 AM
 */
@Data
@Slf4j
@Configuration
@ConditionalOnProperty(value = "pigeon.middleware.rabbitmq.enabled", matchIfMissing = false)
public class RabbitMQConfig {

    @Resource
    RabbitMQService rabbitMQService;

    /**
     * 构建连接工厂
     *
     * @param propertiesConfig
     * @return org.springframework.amqp.rabbit.connection.ConnectionFactory
     * @author Idenn
     * @date 4/2/2024 9:08 AM
     */
    @Bean
    public ConnectionFactory connectionFactory(RabbitMQPropertiesConfig propertiesConfig) {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(propertiesConfig.getHost());
        connectionFactory.setPort(propertiesConfig.getPort());
        connectionFactory.setUsername(propertiesConfig.getUserName());
        connectionFactory.setPassword(propertiesConfig.getPassWord());
        connectionFactory.setVirtualHost(propertiesConfig.getVHost());
        connectionFactory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.CORRELATED);
        connectionFactory.setPublisherReturns(true);
        connectionFactory.createConnection();
        return connectionFactory;
    }

    /**
     * 构建rabbitMQ管理工具
     *
     * @param connectionFactory
     * @return org.springframework.amqp.rabbit.core.RabbitAdmin
     * @author Idenn
     * @date 4/2/2024 9:08 AM
     */
    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.setAutoStartup(true);
        return rabbitAdmin;
    }

    /**
     * 构建监听容器工厂
     *
     * @param connectionFactory
     * @param propertiesConfig
     * @return org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory
     * @author Idenn
     * @date 4/2/2024 9:08 AM
     */
    @Bean
    public RabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory, RabbitMQPropertiesConfig propertiesConfig) {
        SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory = new SimpleRabbitListenerContainerFactory();
        simpleRabbitListenerContainerFactory.setConnectionFactory(connectionFactory);
        simpleRabbitListenerContainerFactory.setConcurrentConsumers(propertiesConfig.getConcurrentConsumers());
        simpleRabbitListenerContainerFactory.setMaxConcurrentConsumers(propertiesConfig.getMaxConcurrentConsumers());
        simpleRabbitListenerContainerFactory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        return simpleRabbitListenerContainerFactory;
    }

    /**
     * 构建rabbitMQ操作基础工具
     *
     * @param connectionFactory
     * @return org.springframework.amqp.rabbit.core.RabbitTemplate
     * @author Idenn
     * @date 4/2/2024 9:08 AM
     */
    @Bean
    public RabbitTemplate customRabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMandatory(true);
        // 是否成功投递到exchange
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (ack && null != correlationData) {
                String id = correlationData.getId();
                rabbitMQService.messageSendSuccess(id);
            } else {
                log.error("消息投递失败！{}", cause);
            }
        });

        // 当消息无法路由时的回调
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
            String correlationId = message.getMessageProperties().getCorrelationId();
            log.error("消息：{} 发送失败, 应答码：{} 原因：{} 交换器：{} 路由键：{}", correlationId, replyCode, replyText, exchange, routingKey);
        });

        return rabbitTemplate;
    }


}
