package com.github.common.constant;

/**
 * 系统常量
 *
 * @author ZEALER
 * @date 2018-10-24
 */
public class SysConstant {

    /** 超级管理员ID */
    public static final int SUPER_ADMIN = 1;

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
}
