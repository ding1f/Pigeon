package com.pigeon.feign;

import com.pigeon.entity.dto.UserDTO;
import com.pigeon.response.CommonResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 通过feign远程调用Account服务
 *
 * @author Idenn
 * @date 3/18/2024 10:26 PM
 */
@FeignClient(name = "pigeon-account-service")
public interface AccountServiceClient {

    @GetMapping("/account/getUserByUsername")
    CommonResponse<UserDTO> getUserByUsername(@RequestParam("name") String name);
}
