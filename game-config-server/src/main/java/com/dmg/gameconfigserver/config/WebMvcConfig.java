package com.dmg.gameconfigserver.config;

import javax.annotation.Resource;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.dmg.gameconfigserver.interceptor.CorsInterceptor;
import com.dmg.gameconfigserver.interceptor.LoginInterceptor;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 11:35 2019/10/29
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Resource
    private LoginInterceptor loginInterceptor;

    @Resource
    private CorsInterceptor corsInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.corsInterceptor).addPathPatterns("/**");
        registry.addInterceptor(this.loginInterceptor).addPathPatterns("/**");
    }
}
