package com.github.modules.search.form;

import com.google.common.collect.Sets;
import org.springframework.data.domain.Sort;

import java.util.Set;

/**
 * 排序生成器
 *
 * @author ZEALER
 * @date 2018-11-4
 */
public class HouseSort {
    // 默认按照相关性排序
    private static final String DEFAULT_SORT_KEY = "_score";

    private static final Set<String> SORT_KEYS = Sets.newHashSet("releaseTime", "price");

//    public static Sort generateSort(String key, String directionKey) {
//        key = getSortKey(key);
//
//        Sort.Direction direction = Sort.Direction.fromStringOrNull(directionKey);
//        if (direction == null) {
//            direction = Sort.Direction.DESC;
//        }
//
//        return new Sort(direction, key);
//    }

    public static String getSortKey(String key) {
        if (!SORT_KEYS.contains(key)) {
            key = DEFAULT_SORT_KEY;
        }

        return key;
    }
}
