package com.github.modules.data.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * RabbitMQ 监听器，切面处理类
 *
 * @author ZEALER
 * @date 2018-11-29
 */
@Aspect
@Component
public class ListenerAspect {

//    @AfterReturning("execution (void com.github.modules.data.controller.HouseController.listener(..))")
//    public void afterReturning(JoinPoint joinPoint) {
//        System.out.println("-----------返回通知--------" + joinPoint);
//    }
}

