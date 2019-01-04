package com.github.common.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.common.annotation.SysLog;
import com.github.common.utils.HttpContextUtils;
import com.github.common.utils.IPUtils;
import com.github.modules.sys.entity.SysLogEntity;
import com.github.modules.sys.entity.SysUserEntity;
import com.github.modules.sys.service.SysLogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * 系统日志，切面处理类
 *
 * @author ZEALER
 * @date 2018-10-20
 */
//@Aspect
//@Component
public class SysLogAspect {

    @Autowired
    private SysLogService sysLogService;

    @Autowired
    private ObjectMapper objectMapper;

    @Pointcut("@annotation(com.github.common.annotation.SysLog)")
    public void logPointCut() {

    }

    /**
     * 环绕通知
     * @param point
     * @return
     * @throws Throwable
     */
//    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        //执行方法
        Object result = point.proceed();
        //执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;

        //保存日志
        saveSysLog(point, time);

        return result;
    }

    private void saveSysLog(ProceedingJoinPoint joinPoint, long time) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method          method    = signature.getMethod();

        SysLogEntity sysLogEntity = new SysLogEntity();
        SysLog       sysLog = method.getAnnotation(SysLog.class);
        if(sysLog != null){
            //注解上的描述
            sysLogEntity.setOperation(sysLog.value());
        }

        //请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        sysLogEntity.setMethod(className + "." + methodName + "()");

        //请求的参数
        Object[] args = joinPoint.getArgs();
        try{
            String params = objectMapper.writeValueAsString(args);
            sysLogEntity.setParams(params);
        }catch (Exception e){

        }

        //获取request
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        //设置IP地址
        sysLogEntity.setIp(IPUtils.getIpAddr(request));

        //用户名
        SysUserEntity sysUserEntity = (SysUserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = sysUserEntity.getUsername();
        sysLogEntity.setUsername(username);

        sysLogEntity.setTime(time);
        sysLogEntity.setCreateTime(new Date());

        //保存系统日志
        sysLogService.save(sysLogEntity);
    }

}
