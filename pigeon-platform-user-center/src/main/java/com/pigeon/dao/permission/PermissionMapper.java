package com.pigeon.dao.permission;

import com.pigeon.dao.CustomMapper;
import com.pigeon.entity.permission.po.PermissionPO;
import com.pigeon.entity.permission.vo.PermissionQueryVO;
import com.pigeon.entity.permission.vo.PermissionResultVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PermissionMapper extends CustomMapper<PermissionPO> {

    /*
     * 通过用户id获取它全部的角色
     */
    List<PermissionPO> getPermissionsByUserId(String id);

    /*
     * 分页查询权限
     */
    List<PermissionResultVO> getPermissionByPage(PermissionQueryVO permissionQueryVO);
}
