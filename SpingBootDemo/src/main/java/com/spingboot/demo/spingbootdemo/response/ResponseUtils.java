package com.spingboot.demo.spingbootdemo.response;

import com.spingboot.demo.spingbootdemo.mark.Mark;

public class ResponseUtils {

    /**
     *
     * @param msg 成功消息
     * @param data 返回数据
     * @return
     * @param <T>
     */
    public static <T> BaseResponse<T> responseSuccess(String msg,T data){
        BaseResponse<T> response = new BaseResponse<T>();
        response.setData(data);
        response.setMsg(msg);
        response.setCode(Mark.API_SUCCESS);
        return response;
    }


    /**
     *
     * @param msg 失败消息
     * @param data 返回数据
     * @return
     * @param <T>
     */
    public static <T> BaseResponse<T> responseError(String msg,T data,Integer code){
        BaseResponse<T> response = new BaseResponse<T>();
        response.setData(data);
        response.setMsg(msg);
        response.setCode(code);
        return response;
    }
}
