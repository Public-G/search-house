package com.github.modules.data.pojo;

/**
 * 爬虫集群信息
 *
 * @author ZEALER
 * @date 2018-12-01
 */
public class SpiderCluster{

    private int status;

    private int node;

    private String ip;

    public SpiderCluster() {
    }

    public SpiderCluster(int status, int node, String ip) {
        this.status = status;
        this.node = node;
        this.ip = ip;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getNode() {
        return node;
    }

    public void setNode(int node) {
        this.node = node;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
