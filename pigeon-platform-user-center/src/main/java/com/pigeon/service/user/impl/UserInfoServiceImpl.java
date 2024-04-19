package com.pigeon.service.user.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pigeon.dao.permission.PermissionMapper;
import com.pigeon.dao.user.UserMapper;
import com.pigeon.entity.dto.UserDTO;
import com.pigeon.entity.permission.po.PermissionPO;
import com.pigeon.entity.pojo.SecurityContextPOJO;
import com.pigeon.entity.user.po.UserPO;
import com.pigeon.exception.BaseException;
import com.pigeon.manager.RedisManager;
import com.pigeon.service.user.UserInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 提供API给其他模块通过feign获取用户信息
 *
 * @author Idenn
 * @date 3/30/2024 1:15 AM
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Resource
    private PermissionMapper permissionMapper;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Resource
    private UserMapper userMapper;

    @Resource
    private SecurityContextPOJO securityContextPOJO;

    @Resource
    private RedisManager redisManager;

    /**
     * 将userInfo存入缓存中
     *
     * @param userDTO 包含全部User信息
     * @return void
     * @author Idenn
     * @date 3/29/2024 7:34 PM
     */
    @Override
    public void setUserInfo2Cache(UserDTO userDTO) throws JsonProcessingException {
        UserPO userPO = new UserPO();
        BeanUtils.copyProperties(userDTO, userPO);
        doSetUserInfo2Cache(userPO);
    }

    /**
     * 本来是void方法，后来改成了返回权限列表，服务于下面的方法
     *
     * @param userPO 包含全部User信息
     * @return java.util.List<java.lang.String>
     * @author Idenn
     * @date 3/29/2024 11:47 PM
     */
    private List<String> doSetUserInfo2Cache(UserPO userPO) throws JsonProcessingException {
        List<String> permissions = new ArrayList<>();
        // 密码脱敏
        userPO.setPassword("");
        String userObjectId = userPO.getObjectId();

        // 获取userInfo的ttl
        Long ttl = redisManager.getExpire(securityContextPOJO.getUserInfoPrefix() + userObjectId);
        // 判断是否存在，或者超过了最大ttl的一半
        if (ttl == -2 || ttl <= securityContextPOJO.getUserInfoValiditySeconds() / 2) {

            // 根据用户的id查询用户的权限
            String objectId = userPO.getObjectId();
            List<PermissionPO> list = permissionMapper.getPermissionsByUserId(objectId);
            permissions = list.stream().map(PermissionPO::getName).collect(Collectors.toList());

            Map<String, Object> jsonMap = new HashMap<>();
            jsonMap.put("objectId", objectId);
            jsonMap.put("isDeleted", userPO.getIsDeleted());
            jsonMap.put("revision", userPO.getRevision());
            jsonMap.put("createdBy", userPO.getCreatedBy());
            jsonMap.put("createdTime", userPO.getCreatedTime());
            jsonMap.put("updatedBy", userPO.getUpdatedBy());
            jsonMap.put("updatedTime", userPO.getUpdatedTime());
            jsonMap.put("username", userPO.getUsername());
            jsonMap.put("password", userPO.getPassword());
            jsonMap.put("telephone", userPO.getTelephone());
            jsonMap.put("email", userPO.getEmail());
            jsonMap.put("location", userPO.getLocation());
            jsonMap.put("permissions", permissions);
            String userInfoJsonStr = objectMapper.writeValueAsString(jsonMap);

            redisManager.setExpire(securityContextPOJO.getUserInfoPrefix() + objectId, userInfoJsonStr, securityContextPOJO.getUserInfoValiditySeconds());
        }

        return permissions;
    }

    /**
     * 从cache中获取userInfo
     *
     * @param objectId userId
     * @return com.pigeon.entity.dto.UserDTO
     * @author Idenn
     * @date 3/29/2024 7:46 PM
     */
    @Override
    public UserDTO getUserInfoFromCache(String objectId) throws JsonProcessingException {
        String redisKey = securityContextPOJO.getUserInfoPrefix() + objectId;
        String userInfoStr = redisManager.getString(redisKey);
        UserDTO userDTO = new UserDTO();
        if (StringUtils.isEmpty(userInfoStr)) {
            // 从数据库取
            UserPO userPO = userMapper.getUserById(objectId);
            BeanUtils.copyProperties(userPO, userDTO);

            // 存入缓存中
            doSetUserInfo2Cache(userPO);

        } else {
            // 从缓存中取
            Map<String, Object> jsonMap = objectMapper.readValue(userInfoStr, new TypeReference<Map<String, Object>>(){});

            userDTO.setObjectId((String) jsonMap.get("objectId"));
            userDTO.setIsDeleted((String) jsonMap.get("isDeleted"));
            userDTO.setRevision((Integer) jsonMap.get("revision"));
            userDTO.setCreatedBy((String) jsonMap.get("createdBy"));
            userDTO.setCreatedTime(new Date((Long) jsonMap.get("createdTime")));
            userDTO.setUpdatedBy((String) jsonMap.get("updatedBy"));
            userDTO.setUpdatedTime(new Date((Long) jsonMap.get("updatedTime")));
            userDTO.setUsername((String) jsonMap.get("username"));
            userDTO.setPassword((String) jsonMap.get("password"));
            userDTO.setEmail((String) jsonMap.get("email"));
            userDTO.setTelephone((String) jsonMap.get("telephone"));
            userDTO.setLocation((String) jsonMap.get("location"));

        }

        return userDTO;
    }

    /**
     * 从cache中获取权限列表
     *
     * @param objectId userId
     * @return java.util.List<java.lang.String>
     * @author Idenn
     * @date 3/29/2024 7:46 PM
     */
    @Override
    public List<String> getPermissionsFromCache(String objectId) throws JsonProcessingException {
        String redisKey = securityContextPOJO.getUserInfoPrefix() + objectId;
        String userInfoStr = redisManager.getString(redisKey);
        List<String> authorities;
        if (StringUtils.isEmpty(userInfoStr)) {
            // 从数据库取
            UserPO userPO = userMapper.getUserById(objectId);

            // 存入缓存中
            authorities = doSetUserInfo2Cache(userPO);

        } else {
            // 从缓存中取
            Map<String, Object> jsonMap = objectMapper.readValue(userInfoStr, new TypeReference<Map<String, Object>>(){});
            authorities = (List<String>) jsonMap.get("authorities");

        }

        return authorities;
    }

    /*
     * 根据ID删除缓存中的用户信息
     */
    public void deleteUserInfo(String objectId) {
        if (!redisManager.delete(securityContextPOJO.getUserInfoPrefix() + objectId)) {
            throw new BaseException(objectId + "的缓存信息删除失败");
        }
    }

}
