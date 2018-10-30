package com.github.modules.sys.controller;

import com.github.common.constant.SysConstant;
import com.github.common.exception.SHException;
import com.github.common.utils.ApiResponse;
import com.github.common.utils.ApiStrategy;
import com.github.common.utils.PageUtils;
import com.github.common.validator.ValidatorUtils;
import com.github.common.validator.group.AddGroup;
import com.github.common.validator.group.UpdateGroup;
import com.github.modules.sys.entity.SysUserEntity;
import com.github.modules.sys.service.SysUserService;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 系统用户
 *
 * @author ZEALER
 * @date 2018-10-22
 */
@Controller
@RequestMapping("/sys/user")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    /**
     * 列表/添加/更新页面的跳转
     *
     * @param forwardType 跳转类型
     */
    @GetMapping("/forward/{forwardType:\\bList\\b|\\bAdd\\b}")
    @PreAuthorize("hasPermission(null, 'sys:user:forward:user'+#forwardType)")
    public String forward(@PathVariable String forwardType) {
        return "admin/sys/user/user" + forwardType;
    }

    /**
     * 所有用户列表
     *
     * @param params 请求参数
     */
    @GetMapping("/list")
    @ResponseBody
    public ApiResponse list(@RequestParam Map<String, String> params) {
        PageUtils pageBean = sysUserService.findPage(params);
        return ApiResponse.ofSuccess().put("pageBean", pageBean);
    }

    /**
     * 用户名唯一性校验
     */
    @GetMapping("/verify")
    @ResponseBody
    public ApiResponse verify(@RequestParam String username) {
        if (sysUserService.findByUsername(username) == null) {
            return null;
        }
        return ApiResponse.ofFail("用户名已存在");

    }

    /**
     * 保存用户
     */
    @PostMapping("/save")
    @ResponseBody
    public ApiResponse save(SysUserEntity userEntity) {
        ValidatorUtils.validateEntity(userEntity, AddGroup.class);

        sysUserService.userSaveOrUpdate(userEntity);

        return ApiResponse.ofSuccess();
    }

    /**
     * 用户信息
     */
    @GetMapping("/info/{userId:[1-9]+}")
    public String info(@PathVariable Long userId, Model model){
        model.addAttribute("user", sysUserService.findByUserId(userId));
        return "admin/sys/user/userInfo";
    }

    /**
     * 修改用户
     */
    @PutMapping("/update")
    @ResponseBody
    public ApiResponse update(@ModelAttribute("user") SysUserEntity sysUserEntity){
        // @ModelAttribute 一定会拿到值, 如果UserId为null, 请求无效
        if (sysUserEntity.getUserId() == null) {
            return ApiResponse.ofStatus(ApiResponse.ResponseStatus.BAD_REQUEST);
        }

        if (Objects.equals(sysUserEntity.getUserId(), SysConstant.SUPER_ADMIN)) {
            if (sysUserEntity.getStatus() == 1) {
                return ApiResponse.ofFail("超级管理员不能禁用");
            }
        }


        ValidatorUtils.validateEntity(sysUserEntity, UpdateGroup.class);

        sysUserService.userSaveOrUpdate(sysUserEntity);

        return ApiResponse.ofSuccess();
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/delete")
    @ResponseBody
    public ApiResponse delete(Long[] deleteCheck) {
        if (ArrayUtils.contains(deleteCheck, SysConstant.SUPER_ADMIN)) {
            return ApiResponse.ofFail("超级管理员不能删除");
        }

        sysUserService.deleteBatch(Arrays.asList(deleteCheck));

        return ApiResponse.ofSuccess();
    }

    /**
     * 重置密码
     */
    @PutMapping("/pwd/{userId:[1-9]+}")
    @ResponseBody
    public ApiResponse reset(@PathVariable Long userId) {
        sysUserService.reset(userId);
        return ApiResponse.ofSuccess();
    }

    @ModelAttribute
    public void customModelAttribute(@RequestParam(value = "userId", required = false) Long userId, Model model) {
        if (userId != null) {
            SysUserEntity userEntity = sysUserService.findByUserId(userId);
            model.addAttribute("user", userEntity);
        }
    }




}
