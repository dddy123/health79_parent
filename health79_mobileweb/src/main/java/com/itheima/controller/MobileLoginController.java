package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("login")
public class MobileLoginController {

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private MemberService memberService;

    @RequestMapping("check")
    public Result check(@RequestBody Map map){

        try {
            String telephone = (String) map.get("telephone");
            String validateCode = (String) map.get("validateCode");

            String key = telephone+ RedisMessageConstant.SENDTYPE_LOGIN;

            //从redis中获取发送给用户的验证码
            String code = jedisPool.getResource().get(key);

            if (code==null || !code.equals(validateCode)){
                return new Result(false, MessageConstant.VALIDATECODE_ERROR);
            }else{
                //通过电话查询用户是不是会员
               Member member =  memberService.findByTelephone(telephone);
               if (member==null){
                   //注册为会员
                   member = new Member();
                   member.setPhoneNumber(telephone);
                   member.setRegTime(new Date());
                   //执行注册
                   memberService.add(member);
               }
               return new Result(true,MessageConstant.LOGIN_SUCCESS);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.LOGIN_ERROR);
        }

    }
}
