package com.itheima.mapper;

import com.itheima.pojo.CheckItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CheckItemMapper {
    void add(CheckItem checkItem);

    List<CheckItem> findByCondition(@Param("queryString")String queryString);

    Integer findCountByCheckItemId(@Param("id")Integer id);

    void deleteById(@Param("id")Integer id);

    CheckItem findById(@Param("id")Integer id);

    void edit(CheckItem checkItem);

    List<CheckItem> findAll();

    List<Integer> findCheckItemIdsByCheckGroupId(@Param("id")Integer id);

//    Long findCountByCondition(@Param("queryString") String queryString);

    /**
     * 参数时Map或者pojo，除此以外都使用@Param注解
     *
     * @param firstResult
     * @param pageSize
     * @param queryString
     * @return
     */
//    List<CheckItem> findByCondition(@Param("firstResult")int firstResult, @Param("pageSize")Integer pageSize,@Param("queryString") String queryString);


}
