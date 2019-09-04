package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.constant.RedisConstant;
import com.itheima.entity.PageResult;
import com.itheima.mapper.SetmealMapper;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.List;

@Service(interfaceClass = SetmealService.class)
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private JedisPool jedisPool;

    @Override
    public void add(Integer[] checkGroupIds, Setmeal setmeal) {

        //新增套餐
        setmealMapper.add(setmeal);

        //设置套餐和检查组中间表关系
        setSetmealAndCheckGroup(checkGroupIds,setmeal.getId());

        if (setmeal.getImg()!=null){
            //将将要保存到数据库中的图片保存到redis
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,setmeal.getImg());
        }

    }

    //设置套餐和检查组中间表关系
    private void setSetmealAndCheckGroup(Integer[] checkGroupIds, Integer id) {

        if (checkGroupIds!=null && checkGroupIds.length>0){
            setmealMapper.setSetmealAndCheckGroup(checkGroupIds,id);
        }

    }

    /**
     * 查询分页数据
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {

        //定义当前页的页码和页面大小
        PageHelper.startPage(currentPage,pageSize);
        //条件查询套餐数据
        List<Setmeal> setmealList = setmealMapper.findByCondition(queryString);
        //包装数据
        PageInfo<Setmeal> info = new PageInfo<>(setmealList);

        return new PageResult(info.getTotal(),info.getList());
    }

    /**
     * 查询所有套餐
     * @return
     */
    @Override
    public List<Setmeal> findAll() {
        return setmealMapper.findAll();
    }

    /**
     * 通过id查询套餐和检查组以及检查项
     * @param id
     * @return
     */
    @Override
    public Setmeal findById(Integer id) {

        Setmeal setmeal = setmealMapper.findById(id);
        return setmeal;
    }
}
