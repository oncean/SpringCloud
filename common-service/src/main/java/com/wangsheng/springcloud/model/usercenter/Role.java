package com.wangsheng.springcloud.model.usercenter;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Role {

    private Integer id;

    private String name;

    private String perms;

    private Integer sort;

    private Integer status;

    private Integer deleted;

    private String remark;

    private List<Integer> menuIds;

    private List<Integer> resourceIds;
}
