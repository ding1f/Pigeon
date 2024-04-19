package com.pigeon.controller.advice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageInfo;
import com.pigeon.interfaces.annotation.NoControllerResponseAdvice;
import com.pigeon.enums.ResultCode;
import com.pigeon.exception.BaseException;
import com.pigeon.response.CommonResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.ArrayList;
import java.util.List;

/**
 * 统一拦截controller返回结果，包装为CommonResponse
 *
 * @author Idenn
 * @date 3/8/2024 4:09 PM
 */
@RestControllerAdvice(basePackages = {"com.pigeon"})
public class ControllerResponseAdvice<T> implements ResponseBodyAdvice<Object> {

    /**
     * 如果返回的是CommonResponse类型的对象则不进行包装
     *
     * @param methodParameter
     * @param aClass
     * @return boolean
     * @author Idenn
     * @date 3/8/2024 4:11 PM
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        return !methodParameter.getParameterType().equals(CommonResponse.class)
                && !methodParameter.hasMethodAnnotation(NoControllerResponseAdvice.class);
    }

    /**
     * String对象默认被包装为message
     * 如果想要返回给前台String类型的Data，自己在Controller中包装返回
     *
     * @param o
     * @param methodParameter
     * @param mediaType
     * @param aClass
     * @param serverHttpRequest
     * @param serverHttpResponse
     * @return java.lang.Object
     * @author Idenn
     * @date 3/8/2024 4:11 PM
     */
    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        if (o instanceof CommonResponse) {
            return o;
        } else if (o instanceof String) {
            // 如果返回对象为String类型，不能直接返回。
            // 在Spring中，如果你的控制器方法的返回类型是String，Spring会认为这是一个视图的名称，而不是一个要返回给客户端的字符串。
            // 所以，当你的切面把String类型的返回值封装成CommonResponse后，Spring仍然会尝试把它当作视图名称来处理，导致了类型转换错误。
            CommonResponse commonResponse = new CommonResponse<>(ResultCode.SUCCESS.getCode(), (String) o);
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                // 指定response类型为json
                serverHttpResponse.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                return objectMapper.writeValueAsString(commonResponse);
            } catch (JsonProcessingException e) {
                return new BaseException(ResultCode.INTERNAL_SERVER_ERROR.getCode(), e.getMessage());
            }
        } else if (o instanceof ArrayList) {
            // List类型则包装为PageInfo返回
            PageInfo<T> pageInfo = new PageInfo<>((List<T>) o);
            return new CommonResponse<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), pageInfo);
        }else {
            return new CommonResponse<>(o);
        }
    }
}
