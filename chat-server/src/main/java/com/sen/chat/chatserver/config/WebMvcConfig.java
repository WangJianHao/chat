package com.sen.chat.chatserver.config;

import com.sen.chat.common.interceptor.CollectorInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @description:
 * @author: sensen
 * @date: 2024/9/1 19:46
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Resource
    private CollectorInterceptor collectorInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(collectorInterceptor).addPathPatterns("/**");
    }
}
