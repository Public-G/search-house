package com.github.modules.sys.service.impl;

import com.github.common.constant.SysConstant;
import com.github.modules.sys.dto.SysMenuDTO;
import com.github.modules.sys.entity.SysMenuEntity;
import com.github.modules.sys.repository.SysMenuRepository;
import com.github.modules.sys.service.SysMenuService;
import com.github.modules.sys.service.SysUserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.*;

/**
 * 系统菜单
 *
 * @author ZEALER
 * @date 2018-10-27
 */
@Service("sysMenuService")
public class SysMenuServiceImpl implements SysMenuService {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysMenuRepository sysMenuRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<SysMenuDTO> getUserMenuList(Long userId) {
        Sort.Order          orderNum    = new Sort.Order(Sort.Direction.DESC, "orderNum");
        Sort                sort        = new Sort(orderNum);
        List<SysMenuEntity> allMenuList = sysMenuRepository.findAll(sort);

        Assert.notNull(allMenuList, "没有查询到菜单，请检查");

        //超级管理员，拥有最高权限
        if (Objects.equals(userId, SysConstant.SUPER_ADMIN)) {
            return menuListWrapper(allMenuList);
        }

        //根据菜单ID筛选出属于该用户的菜单
        List<Long> menuIdList = sysUserService.findAllMenuId(userId);
        for (SysMenuEntity sysMenuEntity : allMenuList) {
            if (!menuIdList.contains(sysMenuEntity.getMenuId())) {
                allMenuList.remove(sysMenuEntity);
            }
        }
        return menuListWrapper(allMenuList);
    }

    /**
     * 整理菜单列表
     */
    private List<SysMenuDTO> menuListWrapper(List<SysMenuEntity> allMenuList) {
        List<SysMenuDTO> allMenuListWrapper = new ArrayList<>();

        List<SysMenuDTO> allMenuDTOList = new ArrayList<>();
        for (SysMenuEntity sysMenuEntity : allMenuList) {
            allMenuDTOList.add(modelMapper.map(sysMenuEntity, SysMenuDTO.class));
        }

        // 保存所有菜单，便于根据父id查询到父菜单
        Map<Long, SysMenuDTO> allMenuMap = new HashMap<>();
        for (SysMenuDTO sysMenuDTO : allMenuDTOList) {
            allMenuMap.put(sysMenuDTO.getMenuId(), sysMenuDTO);
        }

        for (SysMenuDTO sysMenuDTO : allMenuDTOList) {
            // 保存顶级菜单，后续操作均引用顶级菜单为其设置后代菜单
            if (sysMenuDTO.getType() == SysConstant.MenuType.Top.getValue()) {
                allMenuListWrapper.add(sysMenuDTO);
            } else {
                // 不保存按钮
                if (sysMenuDTO.getType() != SysConstant.MenuType.BUTTON.getValue()) {
                    // 获取父菜单
                    Long       parentId   = sysMenuDTO.getParentId();
                    SysMenuDTO parentMenu = allMenuMap.get(parentId);

                    // 获取当前父菜单的子菜单，第一次为空
                    List<SysMenuDTO> menuChilds = parentMenu.getChild();
                    if (menuChilds != null) {
                        menuChilds.add(sysMenuDTO);
                    } else {
                        menuChilds = new ArrayList<>();
                        menuChilds.add(sysMenuDTO);

                        // 设置当前父菜单的子菜单
                        parentMenu.setChild(menuChilds);
                    }
                }
            }
        }
        return Collections.unmodifiableList(allMenuListWrapper);
    }

    @Override
    public List<SysMenuEntity> findMenuByParentId(Long parentId, List<Long> menuIdList) {
        return null;
    }
}
