package com.pigeon.exception;

import com.pigeon.enums.ResultCode;
import com.pigeon.response.CommonResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;

/**
 * 全局异常处理
 *
 * @author Idenn
 * @date 3/4/2024 8:37 PM
 */
@RestControllerAdvice
public class CustomExceptionHandler {

    /**
     * 捕获自定义异常
     *
     * @param e
     * @return com.pigeon.response.CommonResponse
     * @author Idenn
     * @date 3/16/2024 5:41 PM
     */
    @ExceptionHandler(BaseException.class)
    public CommonResponse handleException(Exception e) {
        return new CommonResponse<>(ResultCode.INTERNAL_SERVER_ERROR.getCode(), e.getMessage());
    }



    /**
     * 捕获参数异常
     * @param exception MethodArgumentNotValidException
     * @return 错误信息
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public CommonResponse argumentException(MethodArgumentNotValidException exception) {
        String result = "";
        exception.getBindingResult();
        exception.getBindingResult().getAllErrors();
        if (exception.getBindingResult().getAllErrors().size() > 0) {
            result = exception.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        }
        return new CommonResponse<>(ResultCode.INTERNAL_SERVER_ERROR.getCode(), result);
    }

    /**
     * 全局异常捕获
     * @param e 抛出的错误
     * @return 错误结果
     */
    @ExceptionHandler(value = RuntimeException.class)
    @ResponseBody
    public CommonResponse handelException(RuntimeException e) {
        // e.printStackTrace();
        // 来自于seata
        if (e.getCause() instanceof BaseException) {
            return new CommonResponse<>(ResultCode.INTERNAL_SERVER_ERROR.getCode(), e.getCause().getMessage());

        }
        return new CommonResponse<>(ResultCode.INTERNAL_SERVER_ERROR.getCode(), e.getMessage());
    }


    /**
     * 权限认证失败时
     *
     * @param e
     * @return DefaultBean
     * @author Idenn
     * @date 3/16/2024 5:44 PM
     */
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(value = AccessDeniedException.class)
    @ResponseBody
    public CommonResponse handelAccessDeniedException(AccessDeniedException e) {
        return new CommonResponse<>(ResultCode.FORBIDDEN.getCode(), e.getMessage());
    }

}
