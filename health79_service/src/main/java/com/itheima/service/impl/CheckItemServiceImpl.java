package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.mapper.CheckItemMapper;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service(interfaceClass = CheckItemService.class)
public class CheckItemServiceImpl implements CheckItemService {

    @Autowired
    private CheckItemMapper checkItemMapper;

    @Override
    public void add(CheckItem checkItem) {

        checkItemMapper.add(checkItem);
    }

    /**
     * 分页查询
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
   /* @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {

        //定义从第几条记录开始查询，limit x,y中的x值
        int firstResult = (currentPage-1)*pageSize;

        //查询总记录数
        Long count = checkItemMapper.findCountByCondition(queryString);

        //查询分页数据
        List<CheckItem> checkItemList = checkItemMapper.findByCondition(firstResult,pageSize,queryString);

        //将数据封装到PageResult
        PageResult pageResult = new PageResult(count,checkItemList);

        return pageResult;
    }*/

    /**
     * 分页查询
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {

        PageHelper.startPage(currentPage,pageSize);

        //条件查询分页数据
        List<CheckItem> checkItems=  checkItemMapper.findByCondition(queryString);

        //给PageInfo包装
        PageInfo<CheckItem> pageInfo = new PageInfo<>(checkItems);

        PageResult pageResult = new PageResult(pageInfo.getTotal(), pageInfo.getList());

        return pageResult;
    }

    /**
     * 通过id删除
     * @param id
     */
    @Override
    public void deleteById(Integer id) {

        //查询当前检查项的id有没有被检查组引用
        Integer count = checkItemMapper.findCountByCheckItemId(id);
        if (count>0){
            //如果大于0表示被某个检查组引用了，此时抛出异常，提示用户不能删除
            throw new RuntimeException(MessageConstant.CHECKITEM_IS_QUOTED);
        }

        //删除检查项
        checkItemMapper.deleteById(id);

    }

    /**
     * 通过id查询检查项
     */
    @Override
    public CheckItem findById(Integer id) {
        return checkItemMapper.findById(id);
    }

    /**
     * 编辑更新检查项
     * @param checkItem
     */
    @Override
    public void edit(CheckItem checkItem) {

        checkItemMapper.edit(checkItem);

    }

    /**
     * 查询所有检查项
     * @return
     */
    @Override
    public List<CheckItem> findAll() {
        return checkItemMapper.findAll();
    }

    /**
     * 通过检查组的id检查项的id
     */
    @Override
    public List<Integer> findCheckItemIdsByCheckGroupId(Integer id) {
        return checkItemMapper.findCheckItemIdsByCheckGroupId(id);
    }
}
