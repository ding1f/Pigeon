package com.pigeon.entity.po;

import com.pigeon.enums.MessageType;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

/**
 * 消息体
 *
 * @author Idenn
 * @date 4/2/2024 9:22 AM
 */
@Data
public class TransMessagePO {

    /**
     * uuid
     */
    private String id;

    /**
     * 服务名
     */
    private String serviceName;

    /**
     * 消息类型
     */
    private MessageType type;

    /**
     * 交换机
     */
    private String exchange;

    /**
     * 绑定键值
     */
    private String routingKey;

    /**
     * 队列
     */
    private String queue;

    /**
     * 重试次数
     */
    private Integer sequence;

    /**
     * 消息主体内容
     */
    private String body;

    /**
     * 时间
     */
    private Date date;
}
