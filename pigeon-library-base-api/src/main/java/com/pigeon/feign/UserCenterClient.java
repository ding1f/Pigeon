package com.pigeon.feign;

import com.pigeon.entity.dto.UserDTO;
import com.pigeon.response.CommonResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 通过feign调用user-center中的接口
 *
 * @author Idenn
 * @date 3/30/2024 12:04 AM
 */
@FeignClient(contextId = "baseUserCenterClient", name = "pigeon-user-center-service")
public interface UserCenterClient {

    @PostMapping("/account/fetchUserInfoFromCache")
    CommonResponse<UserDTO> fetchUserInfoFromCache(@RequestParam("objectId") String objectId);
}
