package com.example.instagram2.config.interceptor;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * <code>Interceptor</code><br>
 * 나중에 접근에 대한 통계 같은것을 내기 위해 인터셉터로 <br>
 * 컨트롤러 값들을 확인합니다.
 * @author chasw326
 */
@Component
@Log4j2
public class Interceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("Url: {}({})", request.getRequestURI(), request.getMethod());
        if(request.getUserPrincipal() != null) {
            log.info("User: {}", request.getUserPrincipal().getName());
        }
        log.info("Addr: {}", request.getRemoteAddr());
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("response status: {}", response.getStatus());
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}

