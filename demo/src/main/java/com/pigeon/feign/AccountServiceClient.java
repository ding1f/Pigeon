package com.pigeon.feign;

import com.pigeon.config.CustomFeignConfig;
import com.pigeon.response.CommonResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 通过feign远程调用Account服务
 *
 * @author Idenn
 * @date 3/18/2024 10:26 PM
 */
@FeignClient(contextId = "demoClient", name = "pigeon-user-center-service", configuration = CustomFeignConfig.class)
public interface AccountServiceClient {

    // @GetMapping("/account/getUserByUsername")
    // CommonResponse<UserDTO> getUserByUsername(@RequestParam("name") String name);

    @GetMapping("/account/testFeign")
    CommonResponse testFeign();
}
