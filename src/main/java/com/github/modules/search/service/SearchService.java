package com.github.modules.search.service;

import com.github.common.utils.PageUtils;
import com.github.modules.search.dto.HouseDTO;
import com.github.modules.search.form.HouseForm;

/**
 * 房源搜索
 *
 * @author ZEALER
 * @date 2018-11-4
 */
public interface SearchService {

    /**
     * 查询房源接口
     *
     * @param houseForm
     * @return
     */
    PageUtils query(HouseForm houseForm);

    /**
     * 根据id查询房源
     *
     * @param id
     * @return
     */
    HouseDTO queryById(String id);
}
