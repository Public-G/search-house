package com.github.modules.sys.controller;

import com.github.common.annotation.SysLog;
import com.github.common.constant.SysConstant;
import com.github.common.utils.ApiResponse;
import com.github.common.utils.PageUtils;
import com.github.common.validator.ValidatorUtils;
import com.github.common.validator.group.AddGroup;
import com.github.common.validator.group.UpdateGroup;
import com.github.modules.base.form.PageForm;
import com.github.modules.sys.entity.SysMenuEntity;
import com.github.modules.sys.entity.SysRoleEntity;
import com.github.modules.sys.service.SysMenuService;
import com.github.modules.sys.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * 系统角色
 *
 * @author ZEALER
 * @date 2018-10-22
 */
@Controller
@RequestMapping("/sys/role")
public class SysRoleController extends AbstractController {

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysMenuService sysMenuService;

    /**
     * 列表/添加/更新页面的跳转
     *
     * @param forwardType 跳转类型
     */
    @GetMapping("/forward/{forwardType:\\bList\\b|\\bAdd\\b}")
    public String forward(@PathVariable String forwardType) {
        return "admin/sys/role/role" + forwardType;
    }

    /**
     * 所有role列表
     *
     * @param pageForm 请求参数
     */
    @GetMapping("/list")
    @ResponseBody
    public ApiResponse list(PageForm pageForm) {
        PageUtils pageBean = sysRoleService.findPage(pageForm);
        return ApiResponse.ofSuccess().put("pageBean", pageBean);
    }

    /**
     * 用户名唯一性校验
     */
    @GetMapping("/verify")
    @ResponseBody
    public ApiResponse verify(@RequestParam String roleName) {
        SysRoleEntity sysRoleEntity = sysRoleService.findByName(roleName);
        if (sysRoleEntity == null) {
            return ApiResponse.ofSuccess();
        }

        return ApiResponse.ofFail("角色名已存在");
    }

    /**
     * 保存角色
     */
    @SysLog("保存角色")
    @PostMapping("/save")
    @ResponseBody
    public ApiResponse save(SysRoleEntity sysRoleEntity) {
        ValidatorUtils.validateEntity(sysRoleEntity, AddGroup.class);

        sysRoleEntity.setCreateUserId(getUserId());
        sysRoleService.save(sysRoleEntity);

        return ApiResponse.ofSuccess();
    }

    /**
     * 角色信息
     */
    @GetMapping("/info/{roleId:[0-9]+}")
    public String info(@PathVariable Long roleId, Model model){
        model.addAttribute("role", sysRoleService.findById(roleId));
        return "admin/sys/role/roleInfo";
    }

    /**
     * 修改角色
     */
    @SysLog("修改角色")
    @PutMapping("/update")
    @ResponseBody
    public ApiResponse update(@ModelAttribute("role") SysRoleEntity sysRoleEntity){
        // @ModelAttribute 拿不到值可能该条记录已经被删除, 请求无效
        if (sysRoleEntity == null) {
            return ApiResponse.ofStatus(ApiResponse.ResponseStatus.NOT_FOUND);
        }

        ValidatorUtils.validateEntity(sysRoleEntity, UpdateGroup.class);

        sysRoleEntity.setCreateUserId(getUserId());
        sysRoleService.update(sysRoleEntity);

        return ApiResponse.ofSuccess();
    }

    /**
     * 删除角色
     */
    @SysLog("删除角色")
    @DeleteMapping("/delete")
    @ResponseBody
    public ApiResponse delete(@RequestParam("selectIds") Long[] roleIds) {

        sysRoleService.deleteBatch(roleIds);

        return ApiResponse.ofSuccess();
    }

    /**
     * 菜单列表
     * @return
     */
    @GetMapping("menu")
    @ResponseBody
    public ApiResponse menuList() {
        return ApiResponse.ofSuccess().put("data", sysMenuService.findAll());
    }


    @ModelAttribute
    private void customModelAttribute(@RequestParam(value = "roleId", required = false) Long roleId, Model model) {
        if (roleId != null) {
            SysRoleEntity sysRoleEntity = sysRoleService.findById(roleId);
            model.addAttribute("role", sysRoleEntity);
        }
    }

}
