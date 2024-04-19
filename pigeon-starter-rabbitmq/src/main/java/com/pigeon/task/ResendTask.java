package com.pigeon.task;

import com.pigeon.config.RabbitMQPropertiesConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 定时任务，管理发送失败的消息
 *
 * @author Idenn
 * @date 4/2/2024 9:59 AM
 */
@Slf4j
// @Component
@EnableScheduling
@ConditionalOnProperty(value = "pigeon.middleware.rabbitmq.enabled", matchIfMissing = false)
public class ResendTask {

    @Value("${spring.application.name}")
    private String serviceName;

    @Resource
    RabbitMQPropertiesConfig rabbitMQPropertiesConfig;

    /**
     * 重发消息
     *
     * @return void
     * @author Idenn
     * @date 4/2/2024 10:11 AM
     */
    @Scheduled(fixedDelayString = "${pigeon.middleware.rabbitmq.resendFreq}")
    public void resendMessage() {

        log.info(serviceName);
        log.info(rabbitMQPropertiesConfig.getResendFreq());
    }
}
