package com.pigeon.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pigeon.enums.ResultCode;
import com.pigeon.response.CommonResponse;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * gateway的全局异常处理
 * 全局异常捕获不到gateway的异常，因为Spring WebFlux（Spring Cloud Gateway基于WebFlux
 * 使用的是反应式编程模型，这与传统的Servlet模型不同。
 * 在反应式编程模型中，数据流是异步处理的，这意味着异常可能不会按照传统的方式进行传播。
 *
 * @author Idenn
 * @date 3/25/2024 2:28 PM
 */
@Configuration
public class GatewayExceptionHandler {

    @Bean
    @Order(-2) // 优先级高于默认的错误处理
    public ErrorWebExceptionHandler globalErrorWebExceptionHandler() {
        return new GlobalErrorWebExceptionHandler();
    }

    static class GlobalErrorWebExceptionHandler implements ErrorWebExceptionHandler {

        private final ObjectMapper objectMapper;

        public GlobalErrorWebExceptionHandler() {
            this.objectMapper = new ObjectMapper();
        }

        @Override
        public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
            int code = ResultCode.INTERNAL_SERVER_ERROR.getCode();
            if (ex instanceof BaseException) {
                code = ((BaseException) ex).getCode();
            }
            exchange.getResponse().setStatusCode(HttpStatus.valueOf(code));
            exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
            CommonResponse commonResponse = new CommonResponse(code, ex.getMessage());
            return writeErrorResponse(exchange, commonResponse);
        }

        private Mono<Void> writeErrorResponse(ServerWebExchange exchange, CommonResponse errorResponse) {
            try {
                String body = objectMapper.writeValueAsString(errorResponse);
                DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(body.getBytes());
                return exchange.getResponse().writeWith(Mono.just(buffer));
            } catch (JsonProcessingException e) {
                return Mono.error(e);
            }
        }
    }
}
