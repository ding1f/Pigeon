package com.pigeon.entity.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * Jwt中只保存最低限度的用户信息
 *
 * @author Idenn
 * @date 3/29/2024 3:37 PM
 */
@Data
public class JwtUserPOJO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * base-ID;主键
     */
    private String objectId;

    /**
     * 用户名;用户名
     */
    private String username;
}
