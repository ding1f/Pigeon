package com.pigeon.service.permission;

import com.pigeon.entity.permission.po.PermissionPO;
import com.pigeon.entity.permission.vo.PermissionQueryVO;
import com.pigeon.entity.permission.vo.PermissionResultVO;
import com.pigeon.entity.permission.vo.PermissionVO;
import com.pigeon.service.BaseService;

import java.util.List;

/**
 * 权限模块实现类
 *
 * @author Idenn
 * @date 3/26/2024 8:85 PM
 */
public interface PermissionService extends BaseService<PermissionPO> {
    List<PermissionResultVO> getPermissionByPage(PermissionQueryVO permissionQueryVO);

    String create(PermissionVO permissionVO);

    String update(PermissionVO permissionVO);

    String deleteById(String id);

    String trueDeleteById(PermissionVO permissionVO);
}
