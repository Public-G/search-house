package com.github.modules.data.pojo;

import java.util.List;
import java.util.Set;

/**
 * 搜索建议
 *
 * @author ZEALER
 * @date 2018-11-15
 */
public class HouseSuggest {

    private Set<String> input;

    private int weight = 10; // 默认权重

    public HouseSuggest() {
    }

    public HouseSuggest(Set<String> input, int weight) {
        this.input = input;
        this.weight = weight;
    }

    public Set<String> getInput() {
        return input;
    }

    public void setInput(Set<String> input) {
        this.input = input;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
