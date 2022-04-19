package com.bistu.edu.cs.textproofreading.result;


import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 定义返回时默认拦截器
 * @author lak
 */
@Component
public class ResultInterceptor implements HandlerInterceptor {
    
    /**
     * 设置拦截器, 拦截请求
     * @param request 请求
     * @param response 响应
     * @return boolean
     *
     * @author lak
     */
    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler) {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Class<?> clazz = handlerMethod.getBeanType();
            Method method = handlerMethod.getMethod();
            // 判断是否在类上加入了注解
            if (clazz.isAnnotationPresent(ResponseResult.class)) {
                // 设置此请求返回体,需要包装,往下传递,在ResponseBodyAdvice接口进行判断
                request.setAttribute(ResultHandler.RESPONSE_RESULT_ANN, clazz.getAnnotation(ResponseResult.class));
            }
            // 判断方法上是否有注解
            if (method.isAnnotationPresent(ResponseResult.class)) {
                request.setAttribute(ResultHandler.RESPONSE_RESULT_ANN, method.getAnnotation(ResponseResult.class));
            }
        }
        return true;
    }
}
