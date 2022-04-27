package com.bistu.edu.cs.textproofreading.result;


import lombok.Builder;
import lombok.Data;

import java.util.HashMap;

/**
 * <p>
 * 此类用于构造返回值对象,构造器私有，不允许通过new的方式新建对象
 * 在本系统中,所有的放回值都必须进行包装，构造成具有返回码与返回信息的json数据
 * 返回值均json对象
 * </p>
 * <pre>
 *     {
 *         "code" : "00000",
 *         "msg" : "一切OK",
 *         "data" : {
 *             "name" : "tom"
 *         }
 *     }
 * </pre>
 *
 * @author lak
 */
@Data
@Builder
public class Result<T> {
    
    /**
     * 返回码code
     */
    private String code;
    
    /**
     * 返回内容
     */
    private String msg;
    
    /**
     * 返回数据
     */
    private T data;

    
    /**
     * <p>
     * 方法正常执行，不存在任何异常情况，构建返回值
     * 返回一个Result对象，在springboot中是@ResponseBody标记的方法
     * 会将该对象序列化成json字符串，并返回方法调用
     * </p>
     * <pre>
     *     eg:
     *     Result result = Result.ok(T data);
     * </pre>
     *
     * @param data 需要返回的数据
     * @return Result 统一返回值
     * @author lak
     */
    public static <T> Result<T> ok(T data) {
        return new ResultBuilder<T>()
                .code(ResultEnum.SUCCESS.getCode())
                .msg(ResultEnum.SUCCESS.getMessage())
                .data(data)
                .build();
    }
    
    /**
     * <p>
     * 方法正常执行，不存在任何异常情况，构建返回值
     * 返回一个Result对象，在springboot中是@ResponseBody标记的方法
     * 会将该对象序列化成json字符串，并返回方法调用
     * 不携带任何成功的数据
     * </p>
     * <pre>
     *     eg:
     *     Result result = Result.ok(T data);
     * </pre>
     *
     * @return Result 统一返回值
     * @author lak
     */
    public static <T> Result<T> ok() {
        return new ResultBuilder<T>()
                .code(ResultEnum.SUCCESS.getCode())
                .msg(ResultEnum.SUCCESS.getMessage())
                .build();
    }

    /**
     * 出现异常或者错误后，通过异常捕获，返回不带数据的错误json
     * 一般出现在services服务中,错误通过枚举获取，构建成result并返回
     *
     * @param resultEnum 返回状态码枚举
     * @return Result 统一返回值
     * @author lak
     */
    public static <T> Result<T> error(ResultEnum resultEnum) {
        return new ResultBuilder<T>()
                .code(resultEnum.getCode())
                .msg(resultEnum.getMessage())
                .build();
    }

    /**
     * 出现异常或者错误后，通过异常捕获，返回不带数据的错误json
     * 一般出现在Controller中,错误通过枚举获取，数据存在错误说明，构建成result并返回
     * <p>
     * 在检验客户端传参时，参数出现在错误后，需要在data中需要指明错误的问题，是什么参数传递有误，参数的检验条件等等说明
     * </p>
     *
     * @param resultEnum 返回状态码枚举
     * @param data 返回数据
     * @return Result 统一返回值
     * @author lak
     */
    public static <T> Result<T> error(ResultEnum resultEnum, T data) {
        return new ResultBuilder<T>()
                .code(resultEnum.getCode())
                .msg(resultEnum.getMessage())
                .data(data)
                .build();
    }
}
