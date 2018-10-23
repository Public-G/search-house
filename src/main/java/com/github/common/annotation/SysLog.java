package com.github.common.annotation;

import java.lang.annotation.*;

/**
 * 系统日志注解
 *
 * @author ZEALER
 * @date 2018-10-20
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLog {

    String value() default "";
}
