package com.itheima.mapper;

import com.itheima.pojo.Setmeal;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SetmealMapper {
    void add(Setmeal setmeal);

    void setSetmealAndCheckGroup(@Param("checkGroupIds") Integer[] checkGroupIds,@Param("id")  Integer id);

    List<Setmeal> findByCondition(@Param("queryString")String queryString);

    List<Setmeal> findAll();

    Setmeal findById(@Param("id") Integer id);
}
