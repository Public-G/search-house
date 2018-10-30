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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public List<SysMenuEntity> getUserMenuList(Long userId) {
//        Sort.Order          order       = new Sort.Order(Sort.Direction.DESC, "orderNum");
        List<SysMenuEntity> allMenuList = sysMenuRepository.findAllMenuList();

        //系统管理员，拥有最高权限
        if (userId == SysConstant.SUPER_ADMIN) {
            return menuListWrapper(allMenuList);
        }

        //根据菜单ID筛选出属于该用户的菜单
        List<Long> menuIdList = sysUserService.findAllMenuId(userId);
        if (menuIdList != null && menuIdList.size() > 0) {
            for (SysMenuEntity sysMenuEntity : allMenuList) {
                if (!menuIdList.contains(sysMenuEntity.getMenuId())) {
                    allMenuList.remove(sysMenuEntity);
                }
            }
            return menuListWrapper(allMenuList);
        }
        return null;
    }

    /**
     * 整理菜单列表
     */
    private List<SysMenuEntity> menuListWrapper(List<SysMenuEntity> allMenuList) {
        // 保存整理后的菜单
        List<SysMenuEntity> allMenuListWrapper = new ArrayList<>();

        // 保存所有菜单，便于根据父id查询到父菜单
        Map<Long, SysMenuEntity> allMenuMap = new HashMap<>();

        // 将所有菜单放入Map，MenuId作为key
        for (SysMenuEntity sysMenuEntity : allMenuList) {
            allMenuMap.put(sysMenuEntity.getMenuId(), sysMenuEntity);
        }

        for (SysMenuEntity sysMenuEntity : allMenuList) {
            // 保存顶级菜单，后续操作均引用顶级菜单为其设置后代菜单
            if (sysMenuEntity.getType() == SysConstant.MenuType.Top.getValue()) {
                allMenuListWrapper.add(sysMenuEntity);
            } else {
                // 不保存按钮
                if (sysMenuEntity.getType() != SysConstant.MenuType.BUTTON.getValue()) {
                    // 获取父菜单
                    Long parentId = sysMenuEntity.getParentId();
                    SysMenuEntity parentMenu = allMenuMap.get(parentId);

                    // 获取当前父菜单的子菜单，第一次为空
                    List<SysMenuEntity> menuChilds = parentMenu.getChild();
                    if (menuChilds != null) {
                        menuChilds.add(sysMenuEntity);
                    } else {
                        menuChilds = new ArrayList<>();
                        menuChilds.add(sysMenuEntity);

                        // 设置当前父菜单的子菜单
                        parentMenu.setChild(menuChilds);
                    }
                }

            }
        }

        // 转换成DTO
//        List<SysMenuDTO> allMenuDTOListWrapper = new ArrayList<>();
//        for (SysMenuEntity sysMenuEntity : allMenuListWrapper) {
//            SysMenuDTO sysMenuDTO = modelMapper.map(sysMenuEntity, SysMenuDTO.class);
//            allMenuDTOListWrapper.add(sysMenuDTO);
//        }

        return allMenuListWrapper;
    }

    @Override
    public List<SysMenuEntity> findMenuByParentId(Long parentId, List<Long> menuIdList) {
        return null;
    }
}
