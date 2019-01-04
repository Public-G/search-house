package com.github.modules.search.dto;

/**
 * 首页chart展示
 *
 * @author ZEALER
 * @date 2018-12-02
 */
public class IndexChartDTO {

    private String[] xAxis;

    private Long[] data;

    public IndexChartDTO() {
    }

    public IndexChartDTO(String[] xAxis, Long[] data) {
        this.xAxis = xAxis;
        this.data = data;
    }

    public String[] getxAxis() {
        return xAxis;
    }

    public void setxAxis(String[] xAxis) {
        this.xAxis = xAxis;
    }

    public Long[] getData() {
        return data;
    }

    public void setData(Long[] data) {
        this.data = data;
    }
}
