package com.github.modules.base.form;

import java.io.Serializable;

/**
 * 分页请求参数结构体
 *
 * @author ZEALER
 * @date 2018-11-4
 */
public class PageForm implements Serializable {
    private static final long serialVersionUID = 213452134559L;

    /**
     * 当前页数
     */
    private int curr = 1;

    /**
     * 每页显示的条数
     */
    private int limit = 12;

    /**
     * 搜索关键词
     */
    private String keyword;

    public PageForm() {
    }

    public PageForm(int curr, int limit) {
        this.curr = curr;
        this.limit = limit;
    }

    public PageForm(int curr, int limit, String keyword) {
        this.curr = curr;
        this.limit = limit;
        this.keyword = keyword;
    }

    public int getCurr() {
        return curr;
    }

    public void setCurr(int curr) {
        this.curr = curr;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
