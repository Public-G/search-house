package com.github.modules.sys.controller;

import com.github.common.annotation.SysLog;
import com.github.common.exception.SHException;
import com.github.common.utils.ApiResponse;
import com.github.common.utils.PageUtils;
import com.github.common.validator.ValidatorUtils;
import com.github.common.validator.group.AddGroup;
import com.github.common.validator.group.UpdateGroup;
import com.github.modules.base.form.PageForm;
import com.github.modules.data.entity.RuleEntity;
import com.github.modules.data.entity.SpiderEntity;
import com.github.modules.sys.entity.SysMenuEntity;
import com.github.modules.sys.service.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统菜单
 *
 * @author ZEALER
 * @date 2018-10-22
 */
@Controller
@RequestMapping("/sys/menu")
public class SysMenuController {

    @Autowired
    private SysMenuService sysMenuService;

    /**
     * 列表/添加/更新页面的跳转
     *
     * @param forwardType 跳转类型
     */
    @GetMapping("/forward/{forwardType:\\bList\\b|\\bAdd\\b}")
    public String forward(@PathVariable String forwardType, Model model) {
        if ("Add".equals(forwardType)) {
            List<SysMenuEntity> menuEntityList = sysMenuService.findIsNotBtn();
            model.addAttribute("menus", menuEntityList);
        }

        return "admin/sys/menu/menu" + forwardType;
    }

    /**
     * 所有菜单列表
     */
    @GetMapping("/list")
    @ResponseBody
    public ApiResponse list() {
        List<SysMenuEntity> allMenu = sysMenuService.findAll();
        return ApiResponse.ofSuccess().put("data", allMenu);
    }

    /**
     * 菜单信息
     */
    @GetMapping("/info/{menuId:[0-9]+}")
    public String info(@PathVariable Long menuId, Model model) {
        SysMenuEntity       menuEntity     = sysMenuService.findById(menuId);
        List<SysMenuEntity> menuEntityList = sysMenuService.findIsNotBtn();
        model.addAttribute("menu", menuEntity);
        model.addAttribute("menus", menuEntityList);

        return "admin/sys/menu/menuInfo";
    }

    /**
     * 保存菜单
     */
    @SysLog("保存菜单")
    @PostMapping("/save")
    @ResponseBody
    public ApiResponse save(SysMenuEntity sysMenuEntity) {
        ValidatorUtils.validateEntity(sysMenuEntity);

        sysMenuService.saveOrUpdate(sysMenuEntity);

        return ApiResponse.ofSuccess();
    }

    /**
     * 修改菜单
     */
    @SysLog("修改菜单")
    @PutMapping("/update")
    @ResponseBody
    public ApiResponse update(@ModelAttribute("menu") SysMenuEntity sysMenuEntity) {
        // @ModelAttribute 拿不到值可能该条记录已经被删除, 请求无效
        if (sysMenuEntity == null) {
            return ApiResponse.ofStatus(ApiResponse.ResponseStatus.NOT_FOUND);
        }

        ValidatorUtils.validateEntity(sysMenuEntity);

        sysMenuService.saveOrUpdate(sysMenuEntity);

        return ApiResponse.ofSuccess();
    }

    /**
     * 删除菜单
     */
    @SysLog("删除菜单")
    @DeleteMapping("/delete")
    @ResponseBody
    public ApiResponse delete(@RequestParam("selectIds") Long[] menuIds) {
        sysMenuService.deleteBatch(menuIds);

        return ApiResponse.ofSuccess();
    }

    @ModelAttribute
    private void customModelAttribute(@RequestParam(value = "menuId", required = false) Long menuId, Model model) {
        if (menuId != null) {
            model.addAttribute("rule", sysMenuService.findById(menuId));
        }
    }

}
