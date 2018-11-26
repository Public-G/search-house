package com.github.modules.base.service;

import com.github.common.utils.PageUtils;
import com.github.modules.base.form.PageForm;

/**
 * 通用Service接口
 *
 * @author ZEALER
 * @date 2018-11-23
 */
public interface BaseService<T> {

    /**
     * 实体列表
     * @param pageForm
     * @return
     */
    PageUtils findPage(PageForm pageForm);

    /**
     * 根据ID查询实体
     * @param id
     * @return
     */
    T findById(Long id);

    /**
     * 根据名称查询实体
     * @param name
     * @return
     */
    T findByName(String name);

    /**
     * 保存实体
     * @param entity
     */
    void save(T entity);

    /**
     * 修改实体
     * @param entity
     */
    void update(T entity);

    /**
     * 删除实体
     * @param ids
     */
    void deleteBatch(Long[] ids);

}
