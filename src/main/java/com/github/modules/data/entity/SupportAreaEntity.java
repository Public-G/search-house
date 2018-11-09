package com.github.modules.data.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * 支持区域
 *
 * @author ZEALER
 * @date 2018-10-31
 */
@Table(name = "tb_support_area")
@Entity
public class SupportAreaEntity implements Serializable {
    private static final long serialVersionUID = 1233333L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "parent_id")
    private Long parentId;

    /**
     * 区域中文名
     */
    @Column(name = "cn_name")
    private String cnName;

    /**
     * 行政级别 区域拼音首字母-0 市-1 区/县-2
     */
    private int level;

    @Transient
    private List<SupportAreaEntity> childCity;

    public SupportAreaEntity() {
    }

    public SupportAreaEntity(Long parentId, String cnName, int level, List<SupportAreaEntity> childCity) {
        this.parentId = parentId;
        this.cnName = cnName;
        this.level = level;
        this.childCity = childCity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getCnName() {
        return cnName;
    }

    public void setCnName(String cnName) {
        this.cnName = cnName;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public List<SupportAreaEntity> getChildCity() {
        return childCity;
    }

    public void setChildCity(List<SupportAreaEntity> childCity) {
        this.childCity = childCity;
    }
}
