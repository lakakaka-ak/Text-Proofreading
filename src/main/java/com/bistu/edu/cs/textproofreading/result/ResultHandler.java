package com.bistu.edu.cs.textproofreading.result;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * 对定义responseBody标记进行加强
 * @author lak
 */
@ControllerAdvice
public class ResultHandler implements ResponseBodyAdvice<Object> {

    /**
     * 标记名称
     */
    static final String RESPONSE_RESULT_ANN = "RESPONSE-RESULT-ANN";
    
    /**
     * 判断请求是否包含了注解请求, 没有直接返回, 不需要重写返回体
     * @param methodParameter 方法参数
     * @param converterType 消息类型
     * @return boolean
     *
     * @author lak
     */
    @Override
    public boolean supports(@NonNull MethodParameter methodParameter,
                            @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = Objects.requireNonNull(sra).getRequest();
        // 判断请求是否有包装标记
        ResponseResult responseResultAnn = (ResponseResult) request.getAttribute(RESPONSE_RESULT_ANN);
        return responseResultAnn != null;
    }

    /**
     * @param body 传入的参数
     * @param returnType  返回类型
     * @param selectedContentType 内容类型
     * @param selectedConverterType 消息转换
     * @param request 请求
     * @param response 响应
     * @return java.lang.Object
     * @author lak
     */
    @Override
    public Object beforeBodyWrite(Object body,
                                  @NonNull MethodParameter returnType,
                                  @NonNull MediaType selectedContentType,
                                  @NonNull Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  @NonNull ServerHttpRequest request,
                                  @NonNull ServerHttpResponse response) {
        // 判断body是否为Result类型
        return (body instanceof Result) ? body : Result.ok(body);
    }
}
