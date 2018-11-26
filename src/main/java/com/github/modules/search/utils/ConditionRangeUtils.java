package com.github.modules.search.utils;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * 筛选区间工具类
 *
 * @author ZEALER
 * @date 2018-11-4
 */
public final class ConditionRangeUtils {

    private static final Logger logger = LoggerFactory.getLogger(ConditionRangeUtils.class);

    /**
     * 价格区间定义
     */
    public static final Map<String, ConditionRangeUtils> PRICE_BLOCK;

    /**
     * 面积区间定义
     */
    public static final Map<String, ConditionRangeUtils> SQUARE_BLOCK;

    /**
     * 无限制区间
     */
    public static final ConditionRangeUtils ALL = new ConditionRangeUtils("*", -1, -1);

    static {
        PRICE_BLOCK = ImmutableMap.<String, ConditionRangeUtils>builder()
                .put("*-1000", new ConditionRangeUtils("*-1000", -1, 1000))
                .put("1000-2000", new ConditionRangeUtils("1000-2000", 1000, 2000))
                .put("2000-3000", new ConditionRangeUtils("2000-3000", 2000, 3000))
                .put("3000-4000", new ConditionRangeUtils("3000-4000", 3000, 4000))
                .put("4000-5000", new ConditionRangeUtils("4000-5000", 4000, 5000))
                .put("5000-8000", new ConditionRangeUtils("5000-8000", 5000, 8000))
                .put("8000-*", new ConditionRangeUtils("8000-*", 8000, -1))
                .build();

        SQUARE_BLOCK = ImmutableMap.<String, ConditionRangeUtils>builder()
                .put("*-30", new ConditionRangeUtils("*-30", -1, 30))
                .put("30-50", new ConditionRangeUtils("30-50", 30, 50))
                .put("50-100", new ConditionRangeUtils("50-100", 50, 100))
                .put("100-*", new ConditionRangeUtils("100-*", 100, -1))
                .build();
    }

    private final String key;

    private final int min;

    private final int max;

    private ConditionRangeUtils(String key, int min, int max) {
        this.key = key;
        this.min = min;
        this.max = max;
    }

    public static ConditionRangeUtils matchPrice(String key) {
        if (StringUtils.isBlank(key)) {
            return ALL;
        }

        ConditionRangeUtils priceRange = PRICE_BLOCK.get(key);

        return wrapperCondition(priceRange, key);
    }

    public static ConditionRangeUtils matchArea(String key) {
        if (StringUtils.isBlank(key)) {
            return ALL;
        }

        ConditionRangeUtils areaRange = SQUARE_BLOCK.get(key);

        return wrapperCondition(areaRange, key);
    }

    private static ConditionRangeUtils wrapperCondition(ConditionRangeUtils conditionRange, String key) {
        if (conditionRange == null) {
            String[] condition = key.split("[-]+");

            if (condition.length != 2) {
                return ALL;
            }

            Integer min = null;
            Integer max = null;
            try {
                min = Integer.valueOf(condition[0]);
                max = Integer.valueOf(condition[1]);
            } catch (NumberFormatException e) {
                logger.warn(e.getMessage(), e);
                return ALL;
            }

            if (max > min) {
                return new ConditionRangeUtils(min + "-" + max, min, max);
            } else {
                return ALL;
            }
        }

        return conditionRange;
    }


    public String getKey() {
        return key;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }
}
