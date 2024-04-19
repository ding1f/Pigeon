package com.pigeon.controller;

import com.pigeon.entity.permission.vo.PermissionQueryVO;
import com.pigeon.entity.permission.vo.PermissionResultVO;
import com.pigeon.entity.permission.vo.PermissionVO;
import com.pigeon.service.permission.PermissionService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 权限controller
 *
 * @author Idenn
 * @date 3/26/2024 8:50 PM
 */
@RestController
@RequestMapping("/permission")
public class PermissionController {

    @Resource
    private PermissionService permissionService;

    /**
     * 分页查询
     *
     * @param permissionQueryVO
     * @return java.util.List<com.pigeon.entity.permission.vo.PermissionResultVO>
     * @author Idenn
     * @date 3/26/2024 10:05 PM
     */
    @GetMapping("/getPermissionByPage")
    public List<PermissionResultVO> getPermissionByPage(@RequestBody PermissionQueryVO permissionQueryVO) {
        return permissionService.getPermissionByPage(permissionQueryVO);
    }

    /**
     * 新增权限
     *
     * @param permissionVO
     * @return java.lang.String
     * @author Idenn
     * @date 3/26/2024 10:19 PM
     */
    @PostMapping("/create")
    public String create(@RequestBody PermissionVO permissionVO) {
        return permissionService.create(permissionVO);
    }

    /**
     * 更新权限
     *
     * @param permissionVO
     * @return java.lang.String
     * @author Idenn
     * @date 3/26/2024 10:20 PM
     */
    @PostMapping("/update")
    public String update(@RequestBody PermissionVO permissionVO) {
        return permissionService.update(permissionVO);
    }

    /**
     * 删除权限
     *
     * @param permissionVO
     * @return java.lang.String
     * @author Idenn
     * @date 3/26/2024 10:23 PM
     */
    @DeleteMapping("/deleteById")
    public String deleteById(@RequestBody PermissionVO permissionVO) {
        String id = permissionVO.getObjectId();
        return permissionService.deleteById(id);
    }

    /**
     * 真实删除
     *
     * @param permissionVO
     * @return java.lang.String
     * @author Idenn
     * @date 3/26/2024 10:23 PM
     */
    @DeleteMapping("/trueDeleteById")
    public String trueDeleteById(@RequestBody PermissionVO permissionVO) {
        return permissionService.trueDeleteById(permissionVO);
    }

}
