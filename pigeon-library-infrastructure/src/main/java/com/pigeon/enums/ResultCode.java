package com.pigeon.enums;

import lombok.Getter;

/**
 * 返回的状态码
 *
 * @author Idenn
 * @date 3/4/2024 8:29 PM
 */
@Getter
public enum ResultCode {
    SUCCESS(200, "操作成功"),
    BAD_REQUEST(400, "请求错误"),
    UNAUTHORIZED(401, "鉴权失败"),
    FORBIDDEN(403, "forbidden"),
    NOT_FOUND(404, "资源未找到"),
    INTERNAL_SERVER_ERROR(500, "服务器内部错误");

    private final int code;
    private final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
