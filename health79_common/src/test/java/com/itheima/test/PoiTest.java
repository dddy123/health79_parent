package com.itheima.test;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class PoiTest {
    @Test
    public void demo1() throws Exception {

        XSSFWorkbook workbook = new XSSFWorkbook("D:\\hello.xlsx");
        XSSFSheet sheet = workbook.getSheetAt(0);

        //遍历工作表中的所有行
        for (Row row : sheet) {
            //遍历行中的所有的单元格
            for (Cell cell : row) {
                System.out.println(cell.getStringCellValue());
            }

        }
        workbook.close();
    }

    @Test
    public void demo2() throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook("D:\\hello.xlsx");
        XSSFSheet sheet = workbook.getSheetAt(0);
        int lastRowNum = sheet.getLastRowNum();
        for (int i = 0; i <= lastRowNum; i++) {
            XSSFRow row = sheet.getRow(i);
            int lastCellNum = row.getLastCellNum();
            for (int j = 0; j < lastCellNum; j++) {
                XSSFCell cell = row.getCell(j);
                System.out.println(cell.getStringCellValue());
            }
        }
        workbook.close();
    }

    @Test
    public void demo3() throws Exception {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();
        XSSFRow row = sheet.createRow(0);
        row.createCell(0).setCellValue("编号");
        row.createCell(1).setCellValue("姓名");
        row.createCell(2).setCellValue("年龄");

        for (int i = 1; i < 5; i++) {
            row=sheet.createRow(i);
            row.createCell(0).setCellValue(i);
            row.createCell(1).setCellValue("admin"+i);
            row.createCell(2).setCellValue(18+i);
        }

        FileOutputStream out = new FileOutputStream("d:\\itcast.xlsx");
        workbook.write(out);
        out.flush();//清空缓冲区
        out.close();
        workbook.close();


    }
}
