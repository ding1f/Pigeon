package com.pigeon.service.menu.impl;

import com.pigeon.dao.menu.MenuMapper;
import com.pigeon.entity.menu.po.MenuPO;
import com.pigeon.entity.menu.vo.MenuQueryVO;
import com.pigeon.entity.menu.vo.MenuResultVO;
import com.pigeon.entity.menu.vo.MenuVO;
import com.pigeon.exception.BaseException;
import com.pigeon.service.impl.BaseServiceImpl;
import com.pigeon.service.menu.MenuService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 菜单模块实现类
 *
 * @author Idenn
 * @date 3/26/2024 8:52 PM
 */
@Service
public class MenuServiceImpl extends BaseServiceImpl<MenuPO> implements MenuService {

    @Resource
    private MenuMapper menuMapper;

    /**
     * 分页查询
     *
     * @param menuQueryVO 分页查询参数
     * @return java.util.List<com.pigeon.entity.menu.vo.MenuResultVO>
     * @author Idenn
     * @date 3/26/2024 10:05 PM
     */
    @Override
    public List<MenuResultVO> getMenuByPage(MenuQueryVO menuQueryVO) {
        return menuMapper.getMenuByPage(menuQueryVO);
    }

    /**
     * 新增菜单
     *
     * @param menuVO 菜单参数
     * @return java.lang.String
     * @author Idenn
     * @date 3/26/2024 10:19 PM
     */
    @Override
    public String create(MenuVO menuVO) {
        String msg = "插入成功！";
        MenuPO menuPO = new MenuPO();
        BeanUtils.copyProperties(menuVO, menuPO);
        if (menuMapper.insert(menuPO) == 1) {
            return msg;
        } else {
            throw new BaseException("菜单模块插入失败！");
        }
    }

    /**
     * 更新菜单
     *
     * @param menuVO 更新菜单的参数
     * @return java.lang.String
     * @author Idenn
     * @date 3/26/2024 10:20 PM
     */
    @Override
    public String update(MenuVO menuVO) {
        String msg = "更新成功!";
        MenuPO menuPO = new MenuPO();
        BeanUtils.copyProperties(menuVO, menuPO);
        if (menuMapper.updateById(menuPO) > 0) {
            return msg;
        } else {
            throw new BaseException("菜单模块更新失败！");
        }
    }

    /**
     * 删除菜单
     *
     * @param id
     * @return java.lang.String
     * @author Idenn
     * @date 3/26/2024 10:23 PM
     */
    @Override
    public String deleteById(String id) {
        String msg = "删除成功！";
        if (menuMapper.deleteById(id) > 0) {
            return msg;
        } else {
            throw new BaseException("菜单模块删除失败！");
        }
    }

    /**
     * 真实删除
     *
     * @param menuVO
     * @return java.lang.String
     * @author Idenn
     * @date 3/26/2024 10:23 PM
     */
    @Override
    public String trueDeleteById(MenuVO menuVO) {
        String msg = "真实删除成功！";
        MenuPO menuPO = new MenuPO();
        BeanUtils.copyProperties(menuVO, menuPO);
        if (trueDelete(menuPO) > 0) {
            return msg;
        } else {
            throw new BaseException("菜单模块真实删除失败！");
        }
    }
}
