package com.pigeon.dao.menu;

import com.pigeon.dao.CustomMapper;
import com.pigeon.entity.menu.po.MenuPO;
import com.pigeon.entity.menu.vo.MenuQueryVO;
import com.pigeon.entity.menu.vo.MenuResultVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MenuMapper extends CustomMapper<MenuPO> {

    /*
     * 分页查询菜单
     */
    List<MenuResultVO> getMenuByPage(MenuQueryVO menuQueryVO);
}
