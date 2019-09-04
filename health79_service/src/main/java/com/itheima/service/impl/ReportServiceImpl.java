package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.mapper.MemberMapper;
import com.itheima.mapper.OrderMapper;
import com.itheima.service.ReportService;
import com.itheima.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service(interfaceClass = ReportService.class)
public class ReportServiceImpl implements ReportService {

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private OrderMapper orderMapper;

   /*
    reportDate:null,//报告日期
    todayNewMember :0,//今日新增会员数
    totalMember :0,//总会员数
    thisWeekNewMember :0,//本周新增会员数
    thisMonthNewMember :0,//本月新增会员数
    todayOrderNumber :0,//今日预约数
    todayVisitsNumber :0,//今日到诊数
    thisWeekOrderNumber :0,//本周预约数
    thisWeekVisitsNumber :0,//本周到诊数
    thisMonthOrderNumber :0,//本月预约数
    thisMonthVisitsNumber :0,//本月到诊数
    hotSetmeal :[//显示4个热门套餐
        {name:"粉红珍爱(女)升级TM12项筛查体检套餐",setmeal_count:5,proportion:0.4545},
        {name:"阳光爸妈升级肿瘤12项筛查体检套餐",setmeal_count:2,proportion:0.1818},
        {name:"珍爱高端升级肿瘤12项筛查",setmeal_count:2,proportion:0.1818},
        {name:"孕前检查套餐",setmeal_count:1,proportion:0.0909}
    ]
                    */

    @Override
    public Map getBusinessReport() throws Exception {

        //获取今日日期
        String today =  DateUtils.parseDate2String(DateUtils.getToday());
        //获取本周一
        String thisWeekMonday = DateUtils.parseDate2String(DateUtils.getThisWeekMonday());
        //获取本月第一天
        String firstDay4ThisMonth = DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth());

        //今日新增会员数
        Integer todayNewMember = memberMapper.findTodayNewMember(today);

        //总会员数
        Integer totalMember =  memberMapper.findTotalMember();

        //本周新增会员数
        Integer thisWeekNewMember = memberMapper.findNewMemberCountAfterDate(thisWeekMonday);

        //本月新增会员数
        Integer thisMonthNewMember = memberMapper.findNewMemberCountAfterDate(firstDay4ThisMonth);

        //今日预约数
        Integer todayOrderNumber = orderMapper.findTodayOrderNumber(today);

        //本周预约数
        Integer thisWeekOrderNumber = orderMapper.findOrderNumberAfterDate(thisWeekMonday);

        //本月预约数
        Integer thisMonthOrderNumber = orderMapper.findOrderNumberAfterDate(firstDay4ThisMonth);

        //今日到诊数
        Integer todayVisitsNumber = orderMapper.findTodayVisitsNumber(today);

        //本周到诊数
        Integer thisWeekVisitsNumber = orderMapper.findVisitsNumberAfterDate(thisWeekMonday);

        //本月到诊数
        Integer thisMonthVisitsNumber = orderMapper.findVisitsNumberAfterDate(firstDay4ThisMonth);

        //热门套餐4个
        List<Map<String,Object>> hotSetmeal  = orderMapper.findHotSetmeal();

        //定义map封装页面所需数据
        Map<String,Object> result = new HashMap<>();

        result.put("reportDate",today);
        result.put("todayNewMember",todayNewMember);
        result.put("totalMember",totalMember);
        result.put("thisWeekNewMember",thisWeekNewMember);
        result.put("thisMonthNewMember",thisMonthNewMember);
        result.put("todayOrderNumber",todayOrderNumber);
        result.put("thisWeekOrderNumber",thisWeekOrderNumber);
        result.put("thisMonthOrderNumber",thisMonthOrderNumber);
        result.put("todayVisitsNumber",todayVisitsNumber);
        result.put("thisWeekVisitsNumber",thisWeekVisitsNumber);
        result.put("thisMonthVisitsNumber",thisMonthVisitsNumber);
        result.put("hotSetmeal",hotSetmeal);

        return result;
    }
}
