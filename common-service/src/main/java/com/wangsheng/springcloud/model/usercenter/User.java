package com.wangsheng.springcloud.model.usercenter;

import com.wangsheng.springcloud.model.base.BaseEntity;
import lombok.Builder;
import lombok.Data;

import java.util.List;


@Data
@Builder
public class User extends BaseEntity {

    private Long id;

    private String username;

    private String nickname;

    private String mobile;

    private Integer gender;

    private String avatar;

    private String password;

    private Integer status;

    private Integer deptId;

    private Integer deleted;

    private String deptName;

    private List<String> roles;

}

