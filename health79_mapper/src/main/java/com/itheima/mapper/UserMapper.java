package com.itheima.mapper;

import com.itheima.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    User findByUserName(@Param("username") String username);
}
