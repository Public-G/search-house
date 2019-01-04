package com.github.modules.sys.controller;

import com.github.common.annotation.SysLog;
import com.github.common.constant.SysConstant;
import com.github.common.exception.SHException;
import com.github.common.utils.ApiResponse;
import com.github.common.utils.PageUtils;
import com.github.common.validator.ValidatorUtils;
import com.github.common.validator.group.AddGroup;
import com.github.common.validator.group.UpdateGroup;
import com.github.modules.base.form.PageForm;
import com.github.modules.data.entity.RuleEntity;
import com.github.modules.sys.entity.SysUserEntity;
import com.github.modules.sys.service.SysUserService;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
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
public class SysUserController extends AbstractController {

    @Autowired
    private SysUserService sysUserService;

    /**
     * 列表/添加/更新页面的跳转
     *
     * @param forwardType 跳转类型
     */
    @GetMapping("/forward/{forwardType:\\bList\\b|\\bAdd\\b}")
//    @PreAuthorize("hasPermission(null, 'sys:user:forward:user'+#forwardType)")
    public String forward(@PathVariable String forwardType) {
        return "admin/sys/user/user" + forwardType;
    }

    /**
     * 所有用户列表
     *
     * @param pageForm 请求参数
     */
    @GetMapping("/list")
    @ResponseBody
    public ApiResponse list(PageForm pageForm) {
        PageUtils pageBean = sysUserService.findPage(pageForm);
        return ApiResponse.ofSuccess().put("pageBean", pageBean);
    }

    /**
     * 用户名唯一性校验
     */
    @GetMapping("/verify")
    @ResponseBody
    public ApiResponse verify(@RequestParam String username) {
        SysUserEntity sysUserEntity = sysUserService.findByName(username);
        if (sysUserEntity == null) {
            return ApiResponse.ofSuccess();
        }

        return ApiResponse.ofFail("用户名已存在");

    }

    /**
     * 保存用户
     */
    @SysLog("保存用户")
    @PostMapping("/save")
    @ResponseBody
    public ApiResponse save(SysUserEntity userEntity) {
        ValidatorUtils.validateEntity(userEntity, AddGroup.class);

        userEntity.setCreateUserId(getUserId());
        sysUserService.save(userEntity);

        return ApiResponse.ofSuccess();
    }

    /**
     * 用户信息
     */
    @GetMapping("/info/{userId:[0-9]+}")
    public String info(@PathVariable Long userId, Model model){
        model.addAttribute("user", sysUserService.findById(userId));
        return "admin/sys/user/userInfo";
    }

    /**
     * 修改用户
     */
    @SysLog("修改用户")
    @PutMapping("/update")
    @ResponseBody
    public ApiResponse update(@ModelAttribute("user") SysUserEntity sysUserEntity){
        // @ModelAttribute 拿不到值可能该条记录已经被删除, 请求无效
        if (sysUserEntity == null) {
            return ApiResponse.ofStatus(ApiResponse.ResponseStatus.NOT_FOUND);
        }

        if (Objects.equals(sysUserEntity.getUserId(), SysConstant.SUPER_ADMIN)) {
            if (sysUserEntity.getStatus() == 1) {
                return ApiResponse.ofFail("超级管理员不能禁用");
            }
        }

        ValidatorUtils.validateEntity(sysUserEntity, UpdateGroup.class);

        sysUserEntity.setCreateUserId(getUserId());
        sysUserService.update(sysUserEntity);

        return ApiResponse.ofSuccess();
    }

    /**
     * 删除用户
     */
    @SysLog("删除用户")
    @DeleteMapping("/delete")
    @ResponseBody
    public ApiResponse delete(@RequestParam("selectIds") Long[] userIds) {
        if (ArrayUtils.contains(userIds, SysConstant.SUPER_ADMIN)) {
            return ApiResponse.ofFail("超级管理员不能删除");
        }

        if(ArrayUtils.contains(userIds, getUserId())){
            return ApiResponse.ofFail("当前用户不能删除");
        }

        sysUserService.deleteBatch(userIds);

        return ApiResponse.ofSuccess();
    }

    /**
     * 重置密码
     */
    @SysLog("重置密码")
    @PutMapping("/pwd/{userId:[0-9]+}")
    @ResponseBody
    public ApiResponse reset(@PathVariable Long userId) {
        sysUserService.reset(userId);
        return ApiResponse.ofSuccess();
    }

    @ModelAttribute
    private void customModelAttribute(@RequestParam(value = "userId", required = false) Long userId, Model model) {
        if (userId != null) {
            SysUserEntity userEntity = sysUserService.findById(userId);
            model.addAttribute("user", userEntity);
        }
    }

}
