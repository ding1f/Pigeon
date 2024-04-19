package com.pigeon.feign;

import com.pigeon.response.CommonResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 通过feign调用user-center中的接口
 *
 * @author Idenn
 * @date 3/30/2024 12:04 AM
 */
@FeignClient(contextId = "paymentUserCenterClient", name = "pigeon-user-center-service")
public interface UserCenterClient {

    @PostMapping("/account/fetchPermissionsFromCache")
    CommonResponse<List<String>> fetchPermissionsFromCache(@RequestParam("objectId") String objectId);
}

