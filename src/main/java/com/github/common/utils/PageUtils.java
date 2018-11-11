package com.github.common.utils;

import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.List;

/**
 * 分页工具类
 *
 * @author ZEALER
 * @date 2018-10-28
 */
public class PageUtils implements Serializable {
    private static final long serialVersionUID = 11232456L;

    //总记录数
    private Long count;

    //列表数据
    private List<?> data;

    public PageUtils() {
    }

    public PageUtils(Page<?> page) {
        this.count = page.getTotalElements();
        this.data = page.getContent();
    }

    public PageUtils(Page<?> page, List<?> data) {
        this.count = page.getTotalElements();
        this.data = data;
    }

    public PageUtils(Long count, List<?> data) {
        this.count = count;
        this.data = data;
    }


    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public List<?> getData() {
        return data;
    }

    public void setData(List<?> data) {
        this.data = data;
    }
}
