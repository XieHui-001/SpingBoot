package com.spingboot.demo.spingbootdemo.response;

import com.spingboot.demo.spingbootdemo.mark.Mark;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.concurrent.CompletableFuture;

public class ResponseUtils {

    /**
     *  同步请求成功返回体
     * @param msg 成功消息
     * @param data 返回数据
     * @return
     * @param 
     */
    public static  ResponseEntity<BaseResponse> responseSuccess(String msg,Object data){
        BaseResponse response = new BaseResponse();
        response.setData(data);
        response.setMsg(msg);
        response.setCode(Mark.API_SUCCESS);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    /**
     *  同步请求失败返回体
     * @param msg 失败消息
     * @param data 返回数据
     * @return
     * @param 
     */

    public static  ResponseEntity<BaseResponse> responseError(String msg, Object data, Integer code) {
        BaseResponse response = new BaseResponse();
        response.setData(data);
        response.setMsg(msg);
        response.setCode(code);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    /**
     * 异步请求成功返回体
     * @param msg
     * @param data
     * @return
     */
    public static CompletableFuture<ResponseEntity<BaseResponse>> asyncResponseSuccess(String msg,Object data){
        BaseResponse response = new BaseResponse();
        response.setData(data);
        response.setMsg(msg);
        response.setCode(Mark.API_SUCCESS);
        return CompletableFuture.completedFuture(new ResponseEntity<>(response, HttpStatus.OK));
    }

    /**
     *  异步请求失败返回体
     * @param msg 失败消息
     * @param data 返回数据
     * @return
     * @param
     */
    public static CompletableFuture<ResponseEntity<BaseResponse>> asyncResponseError(String msg, Object data, Integer code) {
        BaseResponse response = new BaseResponse();
        response.setData(data);
        response.setMsg(msg);
        response.setCode(code);
        return CompletableFuture.completedFuture(new ResponseEntity<>(response, HttpStatus.OK));
    }
}
