package com.pigeon.feign;

import com.pigeon.config.CustomFeignConfig;
import com.pigeon.response.CommonResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 通过feign调用payment模块
 *
 * @author Idenn
 * @date 4/11/2024 3:42 AM
 */
@FeignClient(contextId = "paymentClient", name = "pigeon-payment-service", configuration = CustomFeignConfig.class)
public interface PaymentClient {

    @PostMapping("/payment/createPaymentByOrder")
    CommonResponse createPaymentByOrder(@RequestParam("orderId") String orderId);
}
