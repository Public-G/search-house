package com.github.modules.data.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.common.utils.HttpClientUtils;
import com.github.modules.base.pojo.BaiduMapLocation;
import com.github.modules.data.service.BaiduMapService;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.methods.HttpPost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Service("baiduMapService")
public class BaiduMapServiceImpl implements BaiduMapService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private static final String BAIDU_MAP_GEOCONV_API = "http://api.map.baidu.com/geocoder/v2/?";

    private static final String mapAk = "4qYIXrZ4iWdi4kGU655FYNGoWEdL2mzf";

    @Value("${baidu.map.lbs.table}")
    private String geotable_id;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * POI数据管理接口
     */
    private static final String LBS_QUERY_API = "http://api.map.baidu.com/geodata/v3/poi/list?";

    private static final String LBS_CREATE_API = "http://api.map.baidu.com/geodata/v3/poi/create";

    private static final String LBS_UPDATE_API = "http://api.map.baidu.com/geodata/v3/poi/update";

    private static final String LBS_DELETE_API = "http://api.map.baidu.com/geodata/v3/poi/delete";


    @Override
    public BaiduMapLocation getBaiduMapLocation(String city, String region, String address, String community) {
        String encodeAddress = city + region + address;
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

        String url = BAIDU_MAP_GEOCONV_API + "address=" + encodeAddress +
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
        params.put("geotable_id", geotable_id);
        params.put("ak", mapAk);
        params.put("sourceUrlId", sourceUrlId);
        params.put("square", square);
        params.put("price", price);

        String result;
        if (isLbsDataExists(sourceUrlId)) {
            result = HttpClientUtils.httpPost(LBS_UPDATE_API, params);
        } else {
            result = HttpClientUtils.httpPost(LBS_CREATE_API, params);
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
        params.put("sourceUrlId", sourceUrlId);
        params.put("geotable_id", geotable_id);
        params.put("ak", mapAk);

        String result = HttpClientUtils.httpPost(LBS_DELETE_API, params);
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

    private boolean isLbsDataExists(String sourceUrlId) {
        String url = LBS_QUERY_API + "geotable_id=" + geotable_id
                + "&ak=" + mapAk + "&sourceUrlId=" + sourceUrlId + "$";

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
