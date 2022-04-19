package com.bistu.edu.cs.textproofreading.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * 定义全局返回码规范 {@link ResultEnum}
 * <p>
 * 此错误参照阿里巴巴编码规范，共定义三种错误码类型分别为A/B/C三种,
 * 整数开头的错误都属于宏观错误码，在某些情况可以直接返回宏观码
 * </p>
 * <pre>
 * eg:
 *     A0100 为用户注册相关错误码
 *     A0101 - A0199 均为用户注册相关错误码，请自觉按照此项规则编写错误码
 * </pre>
 * <pre>
 *     A: 用户端错误 - enum中以CLIENT开头
 *     B: 服务端错误 - enum中以SERVER开头
 *     C: 第三方错误 - enum中以NON_SYSTEM开头
 * </pre>
 *
 * @author lak
 */
@Getter
@AllArgsConstructor
public enum ResultEnum {
    // 成功状态码
    SUCCESS("00000", "一切OK,万事大吉"),
    
    // 用户端错误使用A开头，后面4位数字对应错误类型码
    CLIENT_ERROR("A0001", "用户端错误"),
    CLIENT_REGISTER_ERROR("A0100", "用户注册错误"),
    CLIENT_LOGIN_ERROR("A0200", "用户登录错误"),
    CLIENT_PERMISSION_ERROR("A0300", "用户访问权限错误"),
    CLIENT_PARAM_ERROR("A0400", "用户请求参数错误"),
    CLIENT_SERVER_ERROR("A0500", "用户请求服务错误"),
    CLIENT_RESOURCE_ERROR("A0600", "用户资源错误"),
    CLIENT_UPLOAD_FILE_ERROR("A0700", "用户上传文件错误"),
    CLIENT_VERSION_ERROR("A0800", "用户当前版本错误"),
    CLIENT_PRIVACY_ERROR("A0900", "用户隐私未授权"),
    CLIENT_DEVICE_ERROR("A1000", "用户设备异常"),


    // 以下为A0101-A153的错误码，主要相关的二级宏观错误码为A0100的用户注册相关错误
    CLIENT_REGISTER_USER_IS_EXISTS_ERROR("A0111", "用户已存在"),
    
    // 以下为A0201-A299的错误码，主要相关的二级宏观错误码为A0200的用户登录相关错误
    CLIENT_LOGIN_ACCOUNT_IS_NOT_EXISTS_ERROR("A0201", "用户账户不存在"),
    CLIENT_LOGIN_ACCOUNT_IS_LOCKED_ERROR("A0202", "用户账户已锁定"),
    CLIENT_LOGIN_ACCOUNT_IS_STOP_ERROR("A0203", "用户账户已停用"),
    CLIENT_LOGIN_PASSWORD_ERROR("A0210", "用户密码错误"),
    CLIENT_LOGIN_OLD_PASSWORD_ERROR("A0211", "原密码错误"),
    CLIENT_LOGIN_TOKEN_CHECKED_ERROR("A0220", "用户身份校验失败"),
    
    // 以下为A0301-A399的错误码，主要相关的二级宏观错误码为A0300的用户访问权限错误
    CLIENT_PERMISSION_UNAUTHORIZED_ERROR("A0301", "访问未授权"),
    CLIENT_PERMISSION_UNAUTHORIZED_CHECK_ERROR("A0302", "操作未授权"),

    // 以下为A0401-A499的错误码，主要相关的二级宏观错误码为A0400的用户参数错误
    CLIENT_PARAM_ILLEGAL_ERROR("A0420", "请求参数超出允许的范围"),


    // 以下为A0501-A599的错误码，主要相关的二级宏观错误码为A0500的用户请求服务错误
    CLIENT_SERVER_METHOD_SUPPORT_ERROR("A0507", "请求方法不支持"),
    CLIENT_SERVER_MEDIA_SUPPORT_ERROR("A0508", "请求数据类型错误"),
    CLIENT_RESOURCE_NOT_EXISTS_ERROR("A0601", "用户资源不存在"),

    // 以下为A0701-A799的错误码，主要相关的二级宏观错误码为A0700的用户上传文件错误
    CLIENT_UPLOAD_IMG_FILE_FORMAT_ERROR("A0701", "图片格式不支持"),
    
    // 服务端错误使用B开头，后面4位数字对应错误类型码
    SERVER_ERROR("B0001", "系统执行错误"),
    SERVER_TIMEOUT_ERROR("B0100", "系统执行超时"),
    SERVER_DISASTER_RECOVER_ERROR("B0200", "系统容灾被触发"),
    SERVER_RESOURCE_ERROR("B0300", "系统资源异常"),
    
    // 第三方错误使用C开头，后面4位数组对应错误类型码
    NON_SYSTEM_ERROR("C0001", "调用第三方服务错误"),
    NON_SYSTEM_MIDDLEWARE_ERROR("C0100", "中间件服务错误"),
    NON_SYSTEM_TIMEOUT_ERROR("C0200", "第三方系统执行超时"),
    NON_SYSTEM_DATABASE_ERROR("C0300", "数据库服务错误"),
    NON_SYSTEM_DISASTER_RECOVER_ERROR("C0400", "第三方容灾被触发"),
    NON_SYSTEM_ADVICE_ERROR("C0500", "通知服务错误"),

    NON_SYSTEM_ADVICE_MESSAGE_ERROR("C0501", "调用短信服务发送短信失败"),
    ;
   
    /**
     * 返回值状态码
     */
    private final String code;
   
    /**
     * 返回值内容
     */
    private final String message;
}
