package com.itheima.mapper;

import com.itheima.pojo.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface OrderMapper {
    Order findByCondition(Order order);

    void add(Order order);

    Map findById(@Param("id")Integer id);

    List<Map> getSetmealReport();

    Integer findTodayOrderNumber(@Param("today")String today);

    Integer findOrderNumberAfterDate(@Param("date")String date);

    Integer findTodayVisitsNumber(@Param("today")String today);

    Integer findVisitsNumberAfterDate(@Param("date")String date);

    List<Map<String, Object>> findHotSetmeal();
}
