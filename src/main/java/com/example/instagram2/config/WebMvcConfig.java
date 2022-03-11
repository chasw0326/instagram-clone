package com.example.instagram2.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * <code>WebMvcConfig</code><br>
 * cors, interceptor 설정
 * @author chasw326
 */
@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final HandlerInterceptor interceptor;

    /**
     * Origins에 허용할 uri를 지정할수 있습니다.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry){
        final long MAX_AGE_SECS = 3600;
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("GET","POST","PUT","PATCH","DELETE","OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(MAX_AGE_SECS);
    }

    /**
     * 인터셉터를 추가했습니다.
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(interceptor)
                .addPathPatterns("/**");
    }
}
