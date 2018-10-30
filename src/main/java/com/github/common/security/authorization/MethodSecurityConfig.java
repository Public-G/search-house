package com.github.common.security.authorization;

import com.github.common.security.authorization.CustomPermissionEvaluator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

/**
 * Spring Security默认处理Web、方法的表达式处理器分别为
 * DefaultWebSecurityExpressionHandler和 DefaultMethodSecurityExpressionHandler，
 * 它们都继承自AbstractSecurityExpressionHandler
 *
 * 指定 DefaultMethodSecurityExpressionHandler的 PermissionEvaluator 为自己实现的 PermissionEvaluator
 *
 * 需要注意的是，项目中不能有多个EnableGlobalMethodSecurity注解，
 * 不然会根据顺序加载不同导致未能正确加载自定义的config
 *
 * @author ZEALER
 * @date 2018-10-29
 */
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {

    @Autowired
    private CustomPermissionEvaluator customPermissionEvaluator;

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setPermissionEvaluator(customPermissionEvaluator);
        return expressionHandler;
    }

}
