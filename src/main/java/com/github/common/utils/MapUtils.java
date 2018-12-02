package com.github.common.utils;

import java.util.HashMap;

/**
 * Map工具类
 *
 * @author ZEALER
 * @date 2018-11-29
 */
public class MapUtils extends HashMap<String, Object>{

    @Override
    public MapUtils put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}
