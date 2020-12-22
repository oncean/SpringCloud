package com.wangsheng.springcloud.exception;

import com.wangsheng.springcloud.common.model.result.ResultCode;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
@Data
public class UnAuthorisedException extends RuntimeException{
    private ResultCode detail;

    public UnAuthorisedException(ResultCode detail){
        this.detail = detail;
    }
}
