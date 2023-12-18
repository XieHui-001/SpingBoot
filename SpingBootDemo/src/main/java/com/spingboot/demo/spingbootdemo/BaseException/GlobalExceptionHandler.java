package com.spingboot.demo.spingbootdemo.BaseException;

import com.spingboot.demo.spingbootdemo.mark.Mark;
import com.spingboot.demo.spingbootdemo.response.BaseResponse;
import com.spingboot.demo.spingbootdemo.response.ResponseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.ResponseEntity;


@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse> handleGlobalException(Exception e) {
        // 处理全局异常
        logger.error("GlobalException occurred: {"+e.getMessage()+"}");
        if (e.getMessage().contains("JWT expired")){
            return ResponseUtils.responseError("Token已失效",null, Mark.ERROR_TOKEN_EXPIRES);
        }
        return ResponseUtils.responseError(e.getMessage(),null, Mark.ERROR_BASE);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<BaseResponse> handleCustomException(CustomException e) {
        // 处理自定义异常
        logger.error("CustomException occurred: {"+e.getMessage()+"}");
        return  ResponseUtils.responseError(e.getMessage(),null,Mark.ERROR_BASE);
    }
}
