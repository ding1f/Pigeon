package com.pigeon.service.menu;

import com.pigeon.entity.menu.po.MenuPO;
import com.pigeon.entity.menu.vo.MenuQueryVO;
import com.pigeon.entity.menu.vo.MenuResultVO;
import com.pigeon.entity.menu.vo.MenuVO;
import com.pigeon.service.BaseService;

import java.util.List;

/**
 * 菜单模块实现类
 *
 * @author Idenn
 * @date 3/26/2024 8:85 PM
 */
public interface MenuService extends BaseService<MenuPO> {
    List<MenuResultVO> getMenuByPage(MenuQueryVO menuQueryVO);

    String create(MenuVO menuVO);

    String update(MenuVO menuVO);

    String deleteById(String id);

    String trueDeleteById(MenuVO menuVO);
}
