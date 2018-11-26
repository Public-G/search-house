package com.github.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * WebMvc配置
 *
 * @author ZEALER
 * @date 2018-10-20
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/admin/login").setViewName("admin/login");
        registry.addViewController("/admin/index").setViewName("admin/index");
        registry.addViewController("/admin/main").setViewName("admin/main");
    }

    @Bean
    public AntPathMatcher pathMatcher() {
        return new AntPathMatcher();
    }

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
}
