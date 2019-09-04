package com.itheima.controller;

import com.aliyuncs.exceptions.ClientException;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.utils.SMSUtils;
import com.itheima.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

@RestController
@RequestMapping("validteCode")
public class ValidateCodeController {

    @Autowired
    private JedisPool jedisPool;

    @RequestMapping("send2telephone")
    public Result send2telephone(@RequestParam("telephone")String telephone){

        try {
            //生成验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            //发送验证码到手机
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,code);

            String key = telephone+ RedisMessageConstant.SENDTYPE_ORDER;

            //将发送出去的验证码保存到redis中
            jedisPool.getResource().setex(key,60*10,code);

            return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);

        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }

    }

    @RequestMapping("send2Login")
    public Result send2Login(@RequestParam("telephone")String telephone){

        try {

            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            //发送登陆验证码给用户
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,code);

            //设置存入登陆验证码的key
            String key = telephone+RedisMessageConstant.SENDTYPE_LOGIN;
            //将验证码存入到redis中
            jedisPool.getResource().setex(key,60*10,code);

            return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
        } catch (ClientException e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.SEND_VALIDATECODE_FAIL);
        }


    }
}
