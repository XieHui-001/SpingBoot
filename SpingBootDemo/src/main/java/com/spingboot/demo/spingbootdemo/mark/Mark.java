package com.spingboot.demo.spingbootdemo.mark;

public class Mark {
    public static final Integer API_SUCCESS = 200;
    public static final Integer ERROR_USER_INFO = 1001;
    public static final Integer ERROR_NOT_USER = 1002;
    public static final Integer ERROR_USER_REGISTER = 1003;
    /**
     * token 错误或者过期通用错误码
     */
    public static final Integer ERROR_TOKEN_EXPIRES = 1004;
    public static final Integer ERROR_DEFAULT = 1005;
    public static final Integer ERROR_BASE = 1006; // 基础请求错误返回码

    public static final String ERROR_USER_INFO_MSG = "用户信息错误！";
    public static final String ERROR_USER_PARAMETER = "参数错误";
    public static final String ERROR_USER_LOGIN_CHECK = "用户名或密码错误！";
    public static final String ERROR_CHECK_TOKEN = "token 验证失败！";
    public static final String LOGIN_USER_CHECK_MARK = "HHHHHDDDDDLLLLLLSSS";

    /**
     * 用于Redis 用户数据缓存Key
     */
    public static final String ALL_USER_DATA_KEY = "ALL_USER_KEY";
}
