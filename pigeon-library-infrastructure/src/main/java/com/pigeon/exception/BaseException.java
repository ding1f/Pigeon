package com.pigeon.exception;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 自定义异常类型
 *
 * @author Idenn
 * @date 3/7/2024 7:12 PM
 */
@Data
@NoArgsConstructor
public class BaseException extends RuntimeException {

    private int code = 500;

    private String businessCode = "0";

    public BaseException(String message) {
        super(message);
    }

    public BaseException(int code, String message) {
        super(message);
        this.code = code;
    }
}