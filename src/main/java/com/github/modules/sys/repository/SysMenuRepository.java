package com.github.modules.sys.repository;

import com.github.modules.sys.entity.SysMenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SysMenuRepository extends JpaRepository<SysMenuEntity, Long> {

    /**
     * 查询用户的所有权限
     * @param userId  用户ID
     */
    @Query(value = "SELECT m.perms FROM sys_user_role ur " +
            "LEFT JOIN sys_role_menu rm ON ur.role_id = rm.role_id " +
            "LEFT JOIN sys_menu m ON rm.menu_id = m.menu_id " +
            "WHERE ur.user_id = ?1", nativeQuery = true)
    List<String> findPermsByUserId(Long userId);

//    @Query("SELECT new SysMenuEntity(se.menuId, se.parentId, se.menuName, se.requestUrl, se.type, se.icon) " +
//            "FROM SysMenuEntity se ORDER BY se.orderNum DESC")
//    List<SysMenuEntity> findAllMenuList();

}
