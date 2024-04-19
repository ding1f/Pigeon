package com.pigeon.response;

import com.pigeon.enums.ResultCode;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 请求返回的javaBean
 *
 * @author Idenn
 * @date 3/4/2024 9:03 PM
 */
@Data
@NoArgsConstructor
public class CommonResponse<T> {
    private int code;
    private String message;
    private T data;

    /*
     * 定义了code和msg，主要用来返回一些操作的结果
     */
    public CommonResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    /*
     * 直接返回数据，多用作查询
     */
    public CommonResponse(T data) {
        this.code = ResultCode.SUCCESS.getCode();
        this.message = ResultCode.SUCCESS.getMessage();
        this.data = data;
    }

    /*
     * 全都包含，看你喜欢
     */
    public CommonResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

}