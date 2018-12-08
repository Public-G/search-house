package com.github.modules.search.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 房源收藏
 *
 * @author ZEALER
 * @date 2018-12-06
 */
@Table(name = "tb_collect")
@Entity
public class CollectEntity implements Serializable{

    private static final long serialVersionUID = 8882111534L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "collect_id")
    private Long collectId;

    /**
     * 普通用户ID，逻辑外键
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 房源ID
     */
    @Column(name = "house_id")
    private String houseId;

    public CollectEntity() {
    }

    public CollectEntity(Long userId, String houseId) {
        this.userId = userId;
        this.houseId = houseId;
    }

    public Long getCollectId() {
        return collectId;
    }

    public void setCollectId(Long collectId) {
        this.collectId = collectId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getHouseId() {
        return houseId;
    }

    public void setHouseId(String houseId) {
        this.houseId = houseId;
    }
}

