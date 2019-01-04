package com.github.modules.data.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.common.utils.HttpClientUtils;
import com.github.modules.data.constant.BaiduServiceConstant;
import com.github.modules.data.pojo.BaiduMapLocation;
import com.github.modules.data.service.BaiduMapService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Service("baiduMapService")
public class BaiduMapServiceImpl implements BaiduMapService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${baidu.map.service.ak}")
    private String mapAk;

    @Value("${baidu.map.lbs.table}")
    private String geoTableId;

    @Autowired
    private ObjectMapper objectMapper;


    @Override
    public BaiduMapLocation getBaiduMapLocation(String city, String region, String address, String community) {
        String encodeAddress = city + region;
        if (StringUtils.isNotBlank(address)) {
            encodeAddress += address;
        }

        if (StringUtils.isNotBlank(community)) {
            encodeAddress += community;
        }

        String encodeCity;
        try {
            encodeCity = URLEncoder.encode(city, "utf-8");
            encodeAddress = URLEncoder.encode(encodeAddress, "utf-8");

        } catch (UnsupportedEncodingException e) {
            logger.error("房源地址UrlEncoded错误", e);
            return null;
        }

        String url = BaiduServiceConstant.BAIDU_MAP_GEOCONV_API + "address=" + encodeAddress +
                "&city=" + encodeCity +
                "&output=json" +
                "&ak=" + mapAk;

        String result = HttpClientUtils.httpGet(url);

        if (result == null) {
            return null;
        }

        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(result);
            int status = jsonNode.get("status").asInt();

            if (status != 0) {
                logger.error("获取百度经纬度错误，状态码 : {}", status);
                return null;

            } else {
                JsonNode jsonLocation = jsonNode.get("result").get("location");
                return new BaiduMapLocation(jsonLocation.get("lng").asDouble(), jsonLocation.get("lat").asDouble());
            }
        } catch (IOException e) {
            logger.error("解析百度经纬度出现异常", e);
            return null;
        }
    }

    @Override
    public void uploadLBS(BaiduMapLocation location, String title, String address, String sourceUrlId, Integer price, Integer square) {
        Map<String, Object> params = new HashMap<>();
        params.put("title", title);
        params.put("address", address);
        params.put("latitude", location.getLatitude());
        params.put("longitude", location.getLongitude());
        params.put("coord_type", "3"); // 百度加密经纬度坐标
        params.put("geotable_id", geoTableId);
        params.put("ak", mapAk);
        params.put("sourceUrlId", sourceUrlId);
        params.put("square", square);
        params.put("price", price);

        String result;
        if (isLbsDataExists(sourceUrlId)) {
            result = HttpClientUtils.httpPost(BaiduServiceConstant.LBS_UPDATE_API, params);
        } else {
            result = HttpClientUtils.httpPost(BaiduServiceConstant.LBS_CREATE_API, params);
        }

        try {
            JsonNode jsonNode = objectMapper.readTree(result);
            int      status   = jsonNode.get("status").asInt();
            if (status != 0) {
                String message = jsonNode.get("message").asText();
                logger.error("上传LBS数据失败，状态码: {}, 状态码描述: {}", status, message);
            }
        } catch (IOException e) {
            logger.error("上传LBS数据出现异常", e);
        }

    }

    @Override
    public void removeLBS(String sourceUrlId) {
        Map<String, Object> params = new HashMap<>();
//        params.put("ids", sourceUrlId); // 批量删除，最多1000个id
        params.put("sourceUrlId", sourceUrlId);
        params.put("geotable_id", geoTableId);
        params.put("ak", mapAk);

        String result = HttpClientUtils.httpPost(BaiduServiceConstant.LBS_DELETE_API, params);
        try {
            JsonNode jsonNode = objectMapper.readTree(result);
            int      status   = jsonNode.get("status").asInt();
            if (status != 0) {
                String message = jsonNode.get("message").asText();
                logger.error("删除LBS数据失败，状态码：{}, 状态码描述: {} ", status, message);
            }
        } catch (IOException e) {
            logger.error("删除LBS数据出现异常", e);
        }
    }

    @Override
    public JsonNode getLBSList(int page_index, int page_size) {
        String url = BaiduServiceConstant.LBS_QUERY_API + "coord_type=" + 3 +
                "&page_index=" + page_index +
                "&page_size=" + page_size + // 默认为10，上限为200
                "&geotable_id=" + geoTableId +
                "&ak=" + mapAk;

        String result = HttpClientUtils.httpGet(url);

        if (result == null) {
            return null;
        }

        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(result);
            int status = jsonNode.get("status").asInt();

            if (status != 0) {
                logger.error("获取poi数据列表失败，状态码 : {}", status);
                return null;

            } else {
                return jsonNode;
            }
        } catch (IOException e) {
            logger.error("解析poi数据列表出现异常", e);
            return null;
        }
    }

    private boolean isLbsDataExists(String sourceUrlId) {
        String url = BaiduServiceConstant.LBS_QUERY_API + "geotable_id=" + geoTableId
                + "&ak=" + mapAk + "&sourceUrlId=" + sourceUrlId + "$"; // 精确匹配在字段值末尾加$

        String result = HttpClientUtils.httpGet(url);

        try {
            JsonNode jsonNode = objectMapper.readTree(result);
            int      status   = jsonNode.get("status").asInt();
            if (status != 0) {
                logger.error("获取LBS数据失败，状态码：{}", status);
                return false;
            } else {
                long size = jsonNode.get("size").asLong();
                return size > 0;
            }
        } catch (IOException e) {
            logger.error("获取LBS数据出现异常", e);
            return false;
        }
    }

}
