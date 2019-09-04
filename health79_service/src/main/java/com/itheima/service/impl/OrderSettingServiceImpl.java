package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.mapper.OrderSettingMapper;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderSettingService.class)
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    private OrderSettingMapper orderSettingMapper;

    @Override
    public void importOrderSettings(List<OrderSetting> orderSettings) {

        for (OrderSetting orderSetting : orderSettings) {
            //通过日期查询是否数据库中已配置了指定日期的预约设置
            Integer count = orderSettingMapper.findCountByOrderDate(orderSetting.getOrderDate());
            if (count>0){
                //表示当前日期已经在数据库中设置过，就去修改数量
                orderSettingMapper.updateNumberByOrderDate(orderSetting);
            }else{
                //将数据新增
                orderSettingMapper.add(orderSetting);
            }
        }

    }

    /**
     * 查询指定月份的可预约数据
     * @param date
     * @return
     */
   /* @Override
    public List<Map> findOrderSettingByMonth(String date) {

        String dateBegin = date+"-1";
        String dateEnd  = date+"-31";

        List<OrderSetting> list = orderSettingMapper.findOrderSettingByMonth(dateBegin,dateEnd);

        List<Map> maps = new ArrayList<>();
        for (OrderSetting orderSetting : list) {
            Map<String, Object> map = new HashMap<>();
            map.put("date",orderSetting.getOrderDate().getDate());
            map.put("number",orderSetting.getNumber());
            map.put("reservations",orderSetting.getReservations());
            maps.add(map);
        }

        return maps;
    }*/

    @Override
    public List<Map> findOrderSettingByMonth(String date) {
        String dateBegin = date+"-1";
        String dateEnd  = date+"-31";

        List<Map> list = orderSettingMapper.findOrderSettingByMonth(dateBegin,dateEnd);

        return list;
    }

    /**
     * 更新可预约数量
     * @param orderSetting
     */
    @Override
    public void updateNumberByOrderDate(OrderSetting orderSetting) {

        Integer count = orderSettingMapper.findCountByOrderDate(orderSetting.getOrderDate());
        if (count > 0) {

            //执行更新
            orderSettingMapper.updateNumberByOrderDate(orderSetting);

        }else{
            //执行新增
            orderSettingMapper.add(orderSetting);
        }

    }
}
