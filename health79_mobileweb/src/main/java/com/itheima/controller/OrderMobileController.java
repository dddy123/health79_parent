package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.aliyuncs.exceptions.ClientException;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Order;
import com.itheima.service.OrderService;
import com.itheima.utils.SMSUtils;
import com.itheima.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

@RestController
@RequestMapping("order")
public class OrderMobileController {

    @Reference
    private OrderService orderService;

    @Autowired
    private JedisPool jedisPool;

    @RequestMapping("add")
    public Result add(@RequestBody Map map){

        try {
            String telephone = (String) map.get("telephone");
            String key = telephone+ RedisMessageConstant.SENDTYPE_ORDER;
            String code = jedisPool.getResource().get(key);

            String validateCode = (String) map.get("validateCode");

            //判断验证码是否通过
            if (code ==null || !code.equals(validateCode)){
                return new Result(false, MessageConstant.VALIDATECODE_ERROR);
            }else{
                Order order = orderService.add(map);
                //发送短信通知用户
                SMSUtils.sendShortMessage(SMSUtils.ORDER_NOTICE,telephone, ValidateCodeUtils.generateValidateCode(4).toString());
                return new Result(true,MessageConstant.ADD_ORDER_SUCCESS,order);
            }

        } catch (RuntimeException e) {
            e.printStackTrace();
            return new Result(false,e.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.ADD_ORDER_FAIL);
        }

    }

    /**
     * 通过id查询订单信息
     */
    @RequestMapping("findById")
    public Result findById(@RequestParam("id")Integer id){

        try {
            Map map = orderService.findById(id);
            return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_ORDER_FAIL);
        }


    };
}
