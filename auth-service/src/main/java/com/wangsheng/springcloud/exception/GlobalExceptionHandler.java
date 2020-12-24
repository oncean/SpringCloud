package com.wangsheng.springcloud.exception;

import com.wangsheng.springcloud.common.model.result.Result;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UnAuthorisedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result unAuthorised(UnAuthorisedException e){
        return Result.failed(e.getDetail());
    }

}
