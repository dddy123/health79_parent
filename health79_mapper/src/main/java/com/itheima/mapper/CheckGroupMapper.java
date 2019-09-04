package com.itheima.mapper;

import com.itheima.pojo.CheckGroup;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CheckGroupMapper {

    void add(CheckGroup checkGroup);

    void setCheckGroupAndCheckItem(@Param("checkItemIds") Integer[] checkItemIds,@Param("id") Integer id);

    List<CheckGroup> findByCondition(@Param("queryString")String queryString);

    CheckGroup findById(@Param("id") Integer id);

    void deleteAssociation(@Param("id") Integer id);

    /**
     * @Param 该注解什么时候使用：
     *          当方法中的参数不是pojo，或者map时，都使用
     * @param checkGroup
     */
    void edit(CheckGroup checkGroup);

    Integer findCountById(@Param("id") Integer id);

    void deleteById(@Param("id")Integer id);

    List<CheckGroup> findAll();

//    void setCheckGroupAndCheckItem(@Param("checkItemId") Integer checkItemId, @Param("id")Integer id);

}
