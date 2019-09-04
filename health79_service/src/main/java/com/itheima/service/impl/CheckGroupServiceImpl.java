package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.Result;
import com.itheima.mapper.CheckGroupMapper;
import com.itheima.pojo.CheckGroup;
import com.itheima.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service(interfaceClass = CheckGroupService.class)
public class CheckGroupServiceImpl implements CheckGroupService {

    @Autowired
    private CheckGroupMapper checkGroupMapper;

    /**
     * 新增检查项
     * @param checkItemIds
     * @param checkGroup
     */
    @Override
    public void add(Integer[] checkItemIds, CheckGroup checkGroup) {

        //新增检查组
        checkGroupMapper.add(checkGroup);
        //设置检查组和检查项之间的关系
        setCheckGroupAndCheckItem(checkItemIds,checkGroup.getId());

    }

    /*private void setCheckGroupAndCheckItem(Integer[] checkItemIds, Integer id) {

        if (checkItemIds!=null && checkItemIds.length>0){

            for (Integer checkItemId : checkItemIds) {
                //循环添加检查组和检查项的关系（向中间表插入数据）
                checkGroupMapper.setCheckGroupAndCheckItem(checkItemId,id);
            }

        }

    }*/

    /**
     * 设置检查项和检查组中间表关系
     * @param checkItemIds
     * @param id
     */
    private void setCheckGroupAndCheckItem(Integer[] checkItemIds, Integer id) {

        if (checkItemIds!=null && checkItemIds.length>0){
            checkGroupMapper.setCheckGroupAndCheckItem(checkItemIds,id);
        }

    }

    /**
     * 分页查询检查项
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {

        //定义页面大小和当前页的页面
        PageHelper.startPage(currentPage,pageSize);

        //条件查询检查组数据
        List<CheckGroup> checkGroupList = checkGroupMapper.findByCondition(queryString);

        //包装数据
        PageInfo<CheckGroup> info = new PageInfo<>(checkGroupList);

        return new PageResult(info.getTotal(),info.getList());
    }

    /**
     * 通过id查询检查组
     * @param id
     * @return
     */
    @Override
    public CheckGroup findById(Integer id) {
        return checkGroupMapper.findById(id);
    }

    /**
     * 编辑更新检查组
     */
    @Override
    public void edit(Integer[] checkItemIds, CheckGroup checkGroup) {

        //删除中间表关系
        checkGroupMapper.deleteAssociation(checkGroup.getId());
        //更新检查组
        checkGroupMapper.edit(checkGroup);
        //重新设置检查组和检查项的中间表关系
        setCheckGroupAndCheckItem(checkItemIds,checkGroup.getId());

    }

    /**
     * 通过id删除检查组
     * @param id
     */
    @Override
    public void deleteById(Integer id) {

        //查询检查组是否被套餐引用
        Integer count = checkGroupMapper.findCountById(id);
        if (count>0){
            throw new RuntimeException(MessageConstant.CHECKGROUP_IS_QUOTED);
        }

        //解除检查组和检查项之间的关系
        checkGroupMapper.deleteAssociation(id);

        //通过id检查组
        checkGroupMapper.deleteById(id);

    }

    /**
     * 查询所有检查组信息
     * @return
     */
    @Override
    public List<CheckGroup> findAll() {
        return checkGroupMapper.findAll();
    }
}
