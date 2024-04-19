package com.pigeon.entity.pojo;

import lombok.Data;

/**
 * 消息中携带的用户信息
 *
 * @author Idenn
 * @date 4/2/2024 9:26 AM
 */
@Data
public class MessagePOJO {
    private UserPOJO userPOJO;
    private Object body;
}
