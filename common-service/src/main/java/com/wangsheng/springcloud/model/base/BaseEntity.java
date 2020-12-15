package com.wangsheng.springcloud.model.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
@ApiModel
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "创建时间", example = "2020-01-01 00:00:00")
    private Date gmtCreate;

    @ApiModelProperty(value = "创建时间", example = "2020-01-01 00:00:00")
    private Date gmtModified;
}
