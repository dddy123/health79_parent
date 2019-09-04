package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.mapper.MemberMapper;
import com.itheima.pojo.Member;
import com.itheima.service.MemberService;
import com.itheima.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.*;

@Service(interfaceClass = MemberService.class)
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberMapper memberMapper;

    @Override
    public Member findByTelephone(String telephone) {
        return memberMapper.findByTelephone(telephone);
    }

    @Override
    public void add(Member member) {

        if (member.getPassword()!=null){
            member.setPassword(MD5Utils.md5(member.getPassword()));
        }

        memberMapper.add(member);
    }

    /**
     * 获取过去12个月，每月的会员统计数量
     * @return
     */
    @Override
    public Map getMemberReport() {

        //初始化日历对象
        Calendar calendar = Calendar.getInstance();
        //将日历对象往前推12个月
        calendar.add(Calendar.MONTH,-12);

        //将过去12个月的月份存入集合中
        List<String> months = new ArrayList<>();

        //将过去12个月每个月注册的会员数量存入一个集合中
        List<Integer> memberCounts = new ArrayList<>();

        for (int i = 0; i < 12; i++) {

            //获取日历对象的当前日期
            Date time = calendar.getTime();
            //将日期装换为固定的格式：2018-09
            String date = new SimpleDateFormat("yyyy-MM").format(time);
            //将过去12个月的月份存入集合中
            months.add(date);

            //定义每个月的第一天
            String dateBegin=date + "-01";
            //定义每个月的最后一天
            String dateEnd=date + "-31";

            //查询每个月的会员数量
            Integer count = memberMapper.findMemberCountsByDate(dateBegin,dateEnd);
            memberCounts.add(count);
            //将日历对象加上一个月，-12月，-11月，-10月
            calendar.add(Calendar.MONTH,1);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("months",months);
        map.put("memberCounts",memberCounts);

        return map;
    }

    public static void main(String[] args) {

        //初始化日历对象
        Calendar calendar = Calendar.getInstance();
        //将日历对象往前推12个月
        calendar.add(Calendar.MONTH,-12);

        Date time = calendar.getTime();

        String date = new SimpleDateFormat("yyyy-MM").format(time);

        System.out.println(date);

    }
}
