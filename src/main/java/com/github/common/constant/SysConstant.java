package com.github.common.constant;

import org.springframework.security.access.method.P;

/**
 * 系统常量
 *
 * @author ZEALER
 * @date 2018-10-24
 */
public class SysConstant {

    /** 超级管理员ID */
    public static final Long SUPER_ADMIN = 1L;

    /** session中用户菜单的KEY */
    public static final String SESSION_KEY_USER_MENU = "userMenu";

    /** 分页对象参数名 */
    public static final String PAGE_BEAN_PARAM = "pageBean";

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
