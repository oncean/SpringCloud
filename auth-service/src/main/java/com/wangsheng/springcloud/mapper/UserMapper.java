package com.wangsheng.springcloud.mapper;

import com.wangsheng.springcloud.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    List<User> findByPage();
}
