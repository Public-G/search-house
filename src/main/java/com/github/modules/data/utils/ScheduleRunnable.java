package com.github.modules.data.utils;

import com.github.common.exception.SHException;
import com.github.common.utils.SpringContextUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

/**
 * 执行定时任务
 *
 * @author ZEALER
 * @date 2018-11-08
 */
public class ScheduleRunnable implements Runnable {

    private Object target;
    private Method method;
    private String params;

    /**
     * 初始化成员变量
     *
     * @param beanName   SpringBean名称
     * @param methodName 要执行的方法名
     * @param params     方法参数
     * @throws NoSuchMethodException
     * @throws SecurityException
     */
    public ScheduleRunnable(String beanName, String methodName, String params) throws NoSuchMethodException {
        this.target = SpringContextUtils.getBean(beanName);
        this.params = params;

        if (StringUtils.isNotBlank(params)) {
            this.method = target.getClass().getDeclaredMethod(methodName, String.class);

        } else {
            this.method = target.getClass().getDeclaredMethod(methodName);
        }
    }

    /**
     * 反射执行目标方法(定时任务只能接受一个参数，如果有多个参数，使用json数据即可)
     */
    @Override
    public void run() {
        try {
            ReflectionUtils.makeAccessible(method);
            if (StringUtils.isNotBlank(params)) {
                method.invoke(target, params);
            } else {
                method.invoke(target);
            }
        } catch (Exception e) {
            throw new SHException("执行定时任务失败", e);
        }
    }
}
