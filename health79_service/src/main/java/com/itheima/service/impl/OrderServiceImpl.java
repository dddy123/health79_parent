package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.constant.MessageConstant;
import com.itheima.mapper.MemberMapper;
import com.itheima.mapper.OrderMapper;
import com.itheima.mapper.OrderSettingMapper;
import com.itheima.pojo.Member;
import com.itheima.pojo.Order;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderService;
import com.itheima.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@Service(interfaceClass = OrderService.class)
public class OrderServiceImpl implements OrderService {


    @Autowired
    private OrderSettingMapper orderSettingMapper;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private OrderMapper orderMapper;

   /*
    1、检查用户所选择的预约日期是否已经提供了预约设置，如果没有设置则无法进行预约
    2、检查用户所选择的预约日期是否已经约满，如果已经约满则无法预约
    3、通过手机号检查当前用户是否为会员
        不是会员：自动完成注册，
        是会员：检查用户是否重复预约（通过member_id、orderDate、setmeal_id，查询同一个会员在当天是不是预约了同一个套餐），如果是重复预约则无法完成再次预约
    4、添加预约
        需要手动设置预约状态、预约类型、会员编号、预约日期、套餐编号
    5、预约成功，更新当日的已预约人数*/


    @Override
    public Order add(Map map) throws Exception {

        String telephone = (String) map.get("telephone");

        String orderDate = (String) map.get("orderDate");
        //将日期装换成日期类型
        Date date = DateUtils.parseString2Date(orderDate);
        //通过日期查询是否提供了预约体检的服务
       OrderSetting orderSetting =  orderSettingMapper.findByOrderDate(date);

       //判断orderSetting是否提供了服务
        if (orderSetting==null){
            //抛出异常提示用户
            throw new RuntimeException(MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }
        //判断预约是否已满
        if (orderSetting.getReservations()>=orderSetting.getNumber()){
            //抛出异常提示预约已满
            throw new RuntimeException(MessageConstant.ORDER_FULL);
        }

        //通过手机号查询用户是否是会员
        Member member =memberMapper.findByTelephone(telephone);

        if (member==null){
            //如果不是会员，就注册为会员
            member = new Member();
            member.setName((String)map.get("name"));
            member.setSex((String)map.get("sex"));
            member.setIdCard((String)map.get("idCard"));
            member.setPhoneNumber(telephone);
            member.setRegTime(date);
            //注册会员
            memberMapper.add(member);
        }else{

            Order order = new Order();
            order.setMemberId(member.getId());
            order.setOrderDate(date);
            order.setSetmealId(Integer.parseInt((String)map.get("setmealId")));
            //如果是会员，查询该会员是否已经预约过这个体检
            Order queryOrder = orderMapper.findByCondition(order);
            if (queryOrder!=null){
                //提示已经预约过，不能重复预约
                throw new RuntimeException(MessageConstant.HAS_ORDERED);
            }
        }

        Order order = new Order();
        order.setMemberId(member.getId());
        order.setSetmealId(Integer.parseInt((String)map.get("setmealId")));
        order.setOrderDate(date);
        order.setOrderType((String)map.get("orderType"));
        order.setOrderStatus(Order.ORDERSTATUS_NO);
        //添加预约订单
        orderMapper.add(order);
        //更新数量
        orderSettingMapper.updateReservationsByOrderDate(date);

        return order;
    }

    /**
     * 通过id查询订单信息
     * @param id
     * @return
     */
    @Override
    public Map findById(Integer id) throws Exception {

        Map map = orderMapper.findById(id);
        Date orderDate = (Date) map.get("orderDate");
        String date = DateUtils.parseDate2String(orderDate);
        map.put("orderDate",date);

        return map;
    }

    /**
     * 查询套餐占比
     * @return
     */
    @Override
    public Map getSetmealReport() {

        List<Map> list = orderMapper.getSetmealReport();

        //定义一个集合，用来存放所有的name
        List<String> names = new ArrayList<>();
        for (Map map : list) {
            String name = (String) map.get("name");
            names.add(name);
        }

        //就是前台需要解析的数据
        Map<String, Object> map = new HashMap<>();

        map.put("setmealNames",names);
        map.put("setmealCounts",list);

        return map;
    }
}
