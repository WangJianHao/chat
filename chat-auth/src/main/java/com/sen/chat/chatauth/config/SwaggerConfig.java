package com.sen.chat.chatauth.config;

import com.sen.chat.common.config.BaseSwaggerConfig;
import com.sen.chat.common.domain.SwaggerProperties;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @description:
 * @author: sensen
 * @date: 2023/8/26 13:09
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig extends BaseSwaggerConfig {

    @Override
    public SwaggerProperties swaggerProperties() {
        return SwaggerProperties.builder()
                .apiBasePackage("com.sen.chat.auth.controller")
                .title("sen认证中心")
                .description("sen认证中心相关接口文档")
                .contactName("sen")
                .version("1.0")
                .enableSecurity(true)
                .build();
    }

    @Bean
    public BeanPostProcessor springfoxHandlerProviderBeanPostProcessor() {
        return generateBeanPostProcessor();
    }
}
