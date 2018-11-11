package com.github.modules.sys.repository;

import com.github.modules.sys.entity.SysUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface SysUserRepository extends JpaRepository<SysUserEntity, Long>, JpaSpecificationExecutor<SysUserEntity> {

    SysUserEntity findByUsername(String username);

    @Query(value = "select distinct rm.menu_id from sys_user_role ur " +
            "LEFT JOIN sys_role_menu rm " +
            "on ur.role_id = rm.role_id " +
            "where ur.user_id = ?1", nativeQuery = true)
    List<Long> findAllMenuId(Long userId);

    void deleteByUserIdIn(Collection<Long> userIds);

    @Modifying
    @Query("UPDATE SysUserEntity user SET user.password = :resetPassword WHERE userId = :userId")
    void updatePassword(@Param("userId") Long userId, @Param("resetPassword") String resetPassword);

}
