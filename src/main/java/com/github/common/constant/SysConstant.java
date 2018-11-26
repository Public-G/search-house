package com.github.common.constant;

import com.github.common.utils.PageUtils;

import javax.persistence.Transient;

/**
 * 系统常量
 *
 * @author ZEALER
 * @date 2018-10-24
 */
public class SysConstant {

    /**
     * 超级管理员ID
     */
    public static final Long SUPER_ADMIN = 1L;

    /**
     * 响应类型
     */
    public static final String CONTENT_TYPE_JSON = "application/json;charset=UTF-8";

    /**
     * session中用户菜单的KEY
     */
    public static final String SESSION_KEY_USER_MENU = "userMenu";

    /**
     * 任务调度参数KEY
     */
    public static final String JOB_PARAM_KEY = "JOB_PARAM_KEY";

    /**
     * 分页对象参数名
     */
    public static final String PAGE_BEAN_PARAM = "pageBean";

    /**
     * 默认城市
     */
    public static final String DEFAULT_CITY = "北京";

    /**
     * RabbitMQ处理数据的Queue名
     */
    public static final String RABBITMQ_HOUSE_QUEUE = "house_queue";

    /**
     * RabbitMQ处理通信的Queue名
     */
    public static final String RABBITMQ_COMMAND_QUEUE = "command_queue";

    /**
     * 菜单类型
     *
     * @author ZEALER
     * @date 2018-10-24
     */
    public enum MenuType {

        /**
         * 顶级目录
         */
        Top(0),

        /**
         * 目录
         */
        CATALOG(1),

        /**
         * 菜单
         */
        MENU(2),

        /**
         * 按钮
         */
        BUTTON(3);

        private int value;

        MenuType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * 任务状态
     *
     * @author ZEALER
     * @date 2018-11-08
     */
    public enum ScheduleStatus {
        /**
         * 正常
         */
        NORMAL(0),
        /**
         * 暂停
         */
        PAUSE(1);

        private int value;

        ScheduleStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * 请求参数
     *
     * @author ZEALER
     * @date 2018-10-24
     */
    public enum RequestParam {
        /**
         * 当前页数
         */
        CURR("curr"),

        /**
         * 每页显示的条数
         */
        LIMIT("limit"),

        /**
         * 搜索关键词
         */
        KEYWORD("keyword");

        private String name;

        public String getName() {
            return name;
        }

        RequestParam(String name) {
            this.name = name;
        }
    }

    /**
     * 行政级别
     *
     * @author ZEALER
     * @date 2018-11-5
     */
    public enum AreaLevel {
        /**
         * 市名拼音首字母
         */
        CITYPREFIX(0),

        /**
         * 市
         */
        CITY(1),

        /**
         * 区/县
         */
        REGION(2);

        private int value;

        AreaLevel(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }
}
