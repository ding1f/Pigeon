package com.pigeon.service.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pigeon.entity.dto.UserDTO;

import java.util.List;

/**
 * 提供API给其他模块通过feign获取用户信息
 *
 * @author Idenn
 * @date 3/13/2024 6:01 PM
 */
public interface UserInfoService {

    void setUserInfo2Cache(UserDTO userDTO) throws JsonProcessingException;

    UserDTO getUserInfoFromCache(String objectId) throws JsonProcessingException;

    List<String> getPermissionsFromCache(String objectId) throws JsonProcessingException;
}
