package com.github.modules.sys.service;

import com.github.common.utils.PageUtils;
import com.github.modules.base.form.PageForm;
import com.github.modules.sys.entity.SysLogEntity;

/**
 * 系统日志
 *
 * @author ZEALER
 * @date 2018-10-22
 */
public interface SysLogService {

    /**
     * 日志列表
     * @param pageForm
     * @return
     */
    PageUtils findPage(PageForm pageForm);

    void save(SysLogEntity sysLogEntity);
}
