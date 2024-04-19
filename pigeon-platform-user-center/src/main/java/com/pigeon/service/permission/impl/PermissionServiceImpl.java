package com.pigeon.service.permission.impl;

import com.pigeon.dao.permission.PermissionMapper;
import com.pigeon.entity.permission.po.PermissionPO;
import com.pigeon.entity.permission.vo.PermissionQueryVO;
import com.pigeon.entity.permission.vo.PermissionResultVO;
import com.pigeon.entity.permission.vo.PermissionVO;
import com.pigeon.exception.BaseException;
import com.pigeon.service.impl.BaseServiceImpl;
import com.pigeon.service.permission.PermissionService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 权限模块实现类
 *
 * @author Idenn
 * @date 3/26/2024 8:52 PM
 */
@Service
public class PermissionServiceImpl extends BaseServiceImpl<PermissionPO> implements PermissionService {

    @Resource
    private PermissionMapper permissionMapper;

    /**
     * 分页查询
     *
     * @param permissionQueryVO 分页查询参数
     * @return java.util.List<com.pigeon.entity.permission.vo.PermissionResultVO>
     * @author Idenn
     * @date 3/26/2024 10:05 PM
     */
    @Override
    public List<PermissionResultVO> getPermissionByPage(PermissionQueryVO permissionQueryVO) {
        return permissionMapper.getPermissionByPage(permissionQueryVO);
    }

    /**
     * 新增权限
     *
     * @param permissionVO 权限参数
     * @return java.lang.String
     * @author Idenn
     * @date 3/26/2024 10:19 PM
     */
    @Override
    public String create(PermissionVO permissionVO) {
        String msg = "插入成功！";
        PermissionPO permissionPO = new PermissionPO();
        BeanUtils.copyProperties(permissionVO, permissionPO);
        if (permissionMapper.insert(permissionPO) == 1) {
            return msg;
        } else {
            throw new BaseException("权限模块插入失败！");
        }
    }

    /**
     * 更新权限
     *
     * @param permissionVO 更新权限的参数
     * @return java.lang.String
     * @author Idenn
     * @date 3/26/2024 10:20 PM
     */
    @Override
    public String update(PermissionVO permissionVO) {
        String msg = "更新成功!";
        PermissionPO permissionPO = new PermissionPO();
        BeanUtils.copyProperties(permissionVO, permissionPO);
        if (permissionMapper.updateById(permissionPO) > 0) {
            return msg;
        } else {
            throw new BaseException("权限模块更新失败！");
        }
    }

    /**
     * 删除权限
     *
     * @param id
     * @return java.lang.String
     * @author Idenn
     * @date 3/26/2024 10:23 PM
     */
    @Override
    public String deleteById(String id) {
        String msg = "删除成功！";
        if (permissionMapper.deleteById(id) > 0) {
            return msg;
        } else {
            throw new BaseException("权限模块删除失败！");
        }
    }

    /**
     * 真实删除
     *
     * @param permissionVO
     * @return java.lang.String
     * @author Idenn
     * @date 3/26/2024 10:23 PM
     */
    @Override
    public String trueDeleteById(PermissionVO permissionVO) {
        String msg = "真实删除成功！";
        PermissionPO permissionPO = new PermissionPO();
        BeanUtils.copyProperties(permissionVO, permissionPO);
        if (trueDelete(permissionPO) > 0) {
            return msg;
        } else {
            throw new BaseException("权限模块真实删除失败！");
        }
    }
}
