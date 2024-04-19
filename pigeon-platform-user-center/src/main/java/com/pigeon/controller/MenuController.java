package com.pigeon.controller;

import com.pigeon.entity.menu.vo.MenuQueryVO;
import com.pigeon.entity.menu.vo.MenuResultVO;
import com.pigeon.entity.menu.vo.MenuVO;
import com.pigeon.service.menu.MenuService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 菜单controller
 *
 * @author Idenn
 * @date 3/26/2024 8:50 PM
 */
@RestController
@RequestMapping("/menu")
public class  MenuController {

    @Resource
    private MenuService menuService;

    /**
     * 分页查询
     *
     * @param menuQueryVO
     * @return java.util.List<com.pigeon.entity.menu.vo.MenuResultVO>
     * @author Idenn
     * @date 3/26/2024 10:05 PM
     */
    @GetMapping("/getMenuByPage")
    public List<MenuResultVO> getMenuByPage(@RequestBody MenuQueryVO menuQueryVO) {
        return menuService.getMenuByPage(menuQueryVO);
    }

    /**
     * 新增菜单
     *
     * @param menuVO
     * @return java.lang.String
     * @author Idenn
     * @date 3/26/2024 10:19 PM
     */
    @PostMapping("/create")
    public String create(@RequestBody MenuVO menuVO) {
        return menuService.create(menuVO);
    }

    /**
     * 更新菜单
     *
     * @param menuVO
     * @return java.lang.String
     * @author Idenn
     * @date 3/26/2024 10:20 PM
     */
    @PostMapping("/update")
    public String update(@RequestBody MenuVO menuVO) {
        return menuService.update(menuVO);
    }

    /**
     * 删除菜单
     *
     * @param menuVO
     * @return java.lang.String
     * @author Idenn
     * @date 3/26/2024 10:23 PM
     */
    @DeleteMapping("/deleteById")
    public String deleteById(@RequestBody MenuVO menuVO) {
        String id = menuVO.getObjectId();
        return menuService.deleteById(id);
    }

    /**
     * 真实删除
     *
     * @param menuVO
     * @return java.lang.String
     * @author Idenn
     * @date 3/26/2024 10:23 PM
     */
    @DeleteMapping("/trueDeleteById")
    public String trueDeleteById(@RequestBody MenuVO menuVO) {
        return menuService.trueDeleteById(menuVO);
    }

}
