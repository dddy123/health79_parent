package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderSettingService;
import com.itheima.utils.DateUtils;
import com.itheima.utils.POIUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("ordersetting")
public class OrderSettingController {

    @Reference
    private OrderSettingService orderSettingService;

    /**
     * 批量导入可预约数据
     * @param file
     * @return
     */
    @RequestMapping("importOrderSettings")
    public Result importOrderSettings(@RequestParam("excelFile")MultipartFile file){

        //读取excel中的数据
        try {
            //获取excel中的所有的数据，每一个String[]就是excel中的一行
            List<String[]> strings = POIUtils.readExcel(file);

            //定义一个list集合用来存放所有的OrderSetting
            List<OrderSetting> orderSettings = new ArrayList<>();
            if (strings!=null){
                for (String[] string : strings) {
                    OrderSetting orderSetting = new OrderSetting();
                    orderSetting.setOrderDate(DateUtils.parseString2Date(string[0],"yyyy/MM/dd"));
                    orderSetting.setNumber(Integer.parseInt(string[1]));
                    orderSettings.add(orderSetting);
                }
            }
            orderSettingService.importOrderSettings(orderSettings);
            return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }

    }

    /**
     * 查询指定月份数据
     */
    @RequestMapping("findOrderSettingByMonth")
    public Result findOrderSettingByMonth(@RequestParam("date")String date){

        try {
            List<Map> list = orderSettingService.findOrderSettingByMonth(date);
            return new Result(true,MessageConstant.QUERY_ORDERSETTING_SUCCESS,list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_ORDERSETTING_FAIL);

        }

    }


    @RequestMapping("updateNumberByOrderDate")
    public Result updateNumberByOrderDate(@RequestBody OrderSetting orderSetting){

        try {
            orderSettingService.updateNumberByOrderDate(orderSetting);
            return new Result(true,MessageConstant.EDIT_ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_ORDERSETTING_FAIL);
        }

    }
}
