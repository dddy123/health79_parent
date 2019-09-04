package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.service.MemberService;
import com.itheima.service.OrderService;
import com.itheima.service.ReportService;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("report")
public class ReportController {

    @Reference
    private MemberService memberService;

    @Reference
    private OrderService orderService;

    @Reference
    private ReportService reportService;

    /**
     * 获取会员数量统计数据
     * @return
     */
    @RequestMapping("getMemberReport")
    public Result getMemberReport(){

        try {
            Map map = memberService.getMemberReport();
            return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
        }

    }

    /**
     * 获取套餐占比数据
     */
    @RequestMapping("getSetmealReport")
    public Result getSetmealReport(){

        try {
            Map map = orderService.getSetmealReport();
            return new Result(true,MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);
        }

    }

    /**
     * 查询运营数据
     */
    @RequestMapping("getBusinessReport")
    public Result getBusinessReport(){

        try {
            Map map = reportService.getBusinessReport();
            return new Result(true,MessageConstant.GET_BUSINESS_REPORT_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }

    }

    /**
     * 下载excel
     */
    @RequestMapping("exportBusinessReport")
    public void exportBusinessReport(HttpSession session, HttpServletResponse response){


        try {
            //获取运营数据
            Map result = reportService.getBusinessReport();
            //取出返回结果数据，准备将报表数据写入到Excel文件中
            String reportDate = (String) result.get("reportDate");
            Integer todayNewMember = (Integer) result.get("todayNewMember");
            Integer totalMember = (Integer) result.get("totalMember");
            Integer thisWeekNewMember = (Integer) result.get("thisWeekNewMember");
            Integer thisMonthNewMember = (Integer) result.get("thisMonthNewMember");
            Integer todayOrderNumber = (Integer) result.get("todayOrderNumber");
            Integer thisWeekOrderNumber = (Integer) result.get("thisWeekOrderNumber");
            Integer thisMonthOrderNumber = (Integer) result.get("thisMonthOrderNumber");
            Integer todayVisitsNumber = (Integer) result.get("todayVisitsNumber");
            Integer thisWeekVisitsNumber = (Integer) result.get("thisWeekVisitsNumber");
            Integer thisMonthVisitsNumber = (Integer) result.get("thisMonthVisitsNumber");
            List<Map> hotSetmeal = (List<Map>) result.get("hotSetmeal");

            //获取Excel模板的绝对路径
            String templatePath = session.getServletContext().getRealPath("template") + File.separator + "report_template.xlsx";

            //读取模板获取Excel对象
            XSSFWorkbook workbook = new XSSFWorkbook(templatePath);

            //获取工作表
            XSSFSheet sheet = workbook.getSheetAt(0);
            //获取第二行
            XSSFRow row = sheet.getRow(2);
            row.getCell(5).setCellValue(reportDate);//日期

            row = sheet.getRow(4);
            row.getCell(5).setCellValue(todayNewMember);//新增会员数（本日）
            row.getCell(7).setCellValue(totalMember);//总会员数

            row = sheet.getRow(5);
            row.getCell(5).setCellValue(thisWeekNewMember);//本周新增会员数
            row.getCell(7).setCellValue(thisMonthNewMember);//本月新增会员数

            row = sheet.getRow(7);
            row.getCell(5).setCellValue(todayOrderNumber);//今日预约数
            row.getCell(7).setCellValue(todayVisitsNumber);//今日到诊数

            row = sheet.getRow(8);
            row.getCell(5).setCellValue(thisWeekOrderNumber);//本周预约数
            row.getCell(7).setCellValue(thisWeekVisitsNumber);//本周到诊数

            row = sheet.getRow(9);
            row.getCell(5).setCellValue(thisMonthOrderNumber);//本月预约数
            row.getCell(7).setCellValue(thisMonthVisitsNumber);//本月到诊数

            int rowNumb = 12;
            for (Map map : hotSetmeal) {
                String name = (String) map.get("name");//套餐名称
                Long setmeal_count = (Long) map.get("setmeal_count");//预约数量
                BigDecimal proportion = (BigDecimal) map.get("proportion");//占比
                row= sheet.getRow(rowNumb);
                row.getCell(4).setCellValue(name);
                row.getCell(5).setCellValue(setmeal_count);
                row.getCell(6).setCellValue(proportion.toString());

                rowNumb++;
            }


            //设置响应的文件格式
            response.setContentType("application/vnd.ms-excel");
            //设置下载方式
            response.setHeader("content-Disposition","attachment;filename=report.xlsx");
            //获取输出流
            ServletOutputStream out = response.getOutputStream();
            workbook.write(out);//将excel响应出去
            out.flush();//清空缓冲区
            out.close();
            workbook.close();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
