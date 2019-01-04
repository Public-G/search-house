package com.github.modules.search.service;

import com.github.common.utils.PageUtils;
import com.github.modules.search.dto.HouseDTO;
import com.github.modules.search.dto.HouseListDTO;
import com.github.modules.search.form.HouseForm;

import java.util.List;
import java.util.Set;

/**
 * 房源搜索
 *
 * @author ZEALER
 * @date 2018-11-4
 */
public interface SearchService {

    /**
     * 获取补全建议关键词
     */
    Set<String> suggest(String prefix);

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

    /**
     * 根据用户ID查询收藏的房源
     * @param userId
     * @return
     */
    List<HouseListDTO> queryById(Long userId);

}
