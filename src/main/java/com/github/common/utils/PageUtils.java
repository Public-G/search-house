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

//    //当前页数
//    private  Integer curr;

//    //每页显示的条数
//    private Integer limit;

    //总记录数
    private Long count;

    //列表数据
    private List<?> data;

    public PageUtils() {
    }


    public PageUtils(Page<?> page) {
//        this.curr = page.getNumber() + 1;
//        this.limit = page.getNumberOfElements();
        this.count = page.getTotalElements();
        this.data = page.getContent();
    }

    public PageUtils(Page<?> page, List<?> data) {
//        this.curr = page.getNumber() + 1;
//        this.limit = page.getNumberOfElements();
        this.count = page.getTotalElements();
        this.data = data;
    }

    public PageUtils(Long count, List<?> data) {
//        this.curr = curr;
//        this.limit = limit;
        this.count = count;
        this.data = data;
    }

//    public Integer getCurr() {
//        return curr;
//    }
//
//    public void setCurr(Integer curr) {
//        this.curr = curr;
//    }
//
//    public Integer getLimit() {
//        return limit;
//    }
//
//    public void setLimit(Integer limit) {
//        this.limit = limit;
//    }

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
