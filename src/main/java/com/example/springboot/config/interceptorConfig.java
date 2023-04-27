package com.example.springboot.config;

import com.example.springboot.config.interceptor.JwtInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author xuyihao
 * @date 2023-01-17
 * @apiNote 拦截器设置
 */
@Configuration
public class interceptorConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor())
                .addPathPatterns("/**")  //拦截所有请求，通过判断token是否合法来决定是否需要登录
                .excludePathPatterns("/user/login","/user/register","/**/export","/**/import","/file/**",
                        "/**");  //为了测试方便，“/**”将token完全开放，之后再将其关闭即可
    }

    @Bean
    public JwtInterceptor jwtInterceptor(){
        return new JwtInterceptor();
    }
}
