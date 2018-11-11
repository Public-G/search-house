package com.github.common.utils;

import com.github.common.exception.SHException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Spring Context 工具类
 *
 * @author ZEALER
 * @date 2018-11-08
 */
@Component
public class SpringContextUtils implements ApplicationContextAware {

    public static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextUtils.applicationContext = applicationContext;
    }

    /**
     * 根据name获取对象
     *
     * @param name
     * @return
     */
    public static Object getBean(String name) {
        return applicationContext.getBean(name);
    }

    /**
     * 获取类型为requiredType的对象
     *
     * @param name
     * @param requiredType
     * @param <T>
     * @return
     * @throws BeansException   如果bean不能被类型转换，相应的异常将会被抛出
     */
    public static <T> T getBean(String name, Class<T> requiredType) throws BeansException{
        return applicationContext.getBean(name, requiredType);
    }

    /**
     * 如果BeanFactory包含一个与所给名称匹配的bean定义，则返回true
     *
     * @param name
     * @return
     */
    public static boolean containsBean(String name) {
        return applicationContext.containsBean(name);
    }


    /**
     * 判断以给定名字注册的bean定义是一个singleton还是一个prototype
     *
     * @param name
     * @return
     * @throws NoSuchBeanDefinitionException    如果与给定名字相应的bean定义没有被找到，将会抛出一个异常
     */
    public static boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
        return applicationContext.isSingleton(name);
    }

    /**
     * 获取name的bean的类型
     *
     * @param name
     * @return
     * @throws NoSuchBeanDefinitionException
     */
    public static Class<?> getType(String name) throws NoSuchBeanDefinitionException {
        return applicationContext.getType(name);
    }
}
