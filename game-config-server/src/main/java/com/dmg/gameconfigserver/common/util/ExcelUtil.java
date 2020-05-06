package com.dmg.gameconfigserver.common.util;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.metadata.Font;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.metadata.TableStyle;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.dmg.gameconfigserver.common.ExcelWriterFactory;
import org.apache.poi.ss.usermodel.IndexedColors;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.List;

/**
 * @Author liubo
 * @Description //TODO
 * @Date 15:58 2020/1/6
 */
public class ExcelUtil {

    /**
     * 导出 Excel ：一个 sheet，带表头
     *
     * @param response  HttpServletResponse
     * @param list      数据 list，每个元素为一个 BaseRowModel
     * @param fileName  导出的文件名
     * @param sheetName 导入文件的 sheet 名
     * @param clazz    映射实体类，Excel 模型
     */
    public static void writeExcel(HttpServletResponse response, List<? extends BaseRowModel> list, String fileName,
                                  String sheetName, Class<? extends BaseRowModel> clazz) throws Exception {
        ExcelWriter writer = new ExcelWriter(getOutputStream(fileName, response), ExcelTypeEnum.XLSX);
        Sheet sheet = new Sheet(1, 0, clazz);
        sheet.setSheetName(sheetName);

        TableStyle tableStyle = new TableStyle();
        tableStyle.setTableContentBackGroundColor(IndexedColors.WHITE);
        Font font = new Font();
        font.setFontHeightInPoints((short) 9);
        tableStyle.setTableHeadFont(font);
        tableStyle.setTableContentFont(font);
        sheet.setTableStyle(tableStyle);

        writer.write(list, sheet);
        writer.finish();
    }

    /**
     * 导出 Excel ：多个 sheet，带表头
     *
     * @param response  HttpServletResponse
     * @param list      数据 list，每个元素为一个 BaseRowModel
     * @param fileName  导出的文件名
     * @param sheetName 导入文件的 sheet 名
     * @param clazz    映射实体类，Excel 模型
     */
    public static ExcelWriterFactory writeExcelWithSheets(HttpServletResponse response,
                                                          List<? extends BaseRowModel> list, String fileName,
                                                          String sheetName, Class<? extends BaseRowModel> clazz) throws Exception {
        ExcelWriterFactory writer = new ExcelWriterFactory(getOutputStream(fileName, response), ExcelTypeEnum.XLSX);
        Sheet sheet = new Sheet(1, 0, clazz);
        sheet.setSheetName(sheetName);
        sheet.setTableStyle(getTableStyle());
        writer.write(list, sheet);

        return writer;
    }

    /**
     * 导出文件时为Writer生成OutputStream
     */
    private static OutputStream getOutputStream(String fileName, HttpServletResponse response) throws Exception {
        //创建本地文件
        fileName = fileName + ".xlsx";

        fileName = new String(fileName.getBytes(), "ISO-8859-1");
        response.addHeader("Content-Disposition", "filename=" + fileName);

        return response.getOutputStream();
    }

    /**
     * 设置全局样式
     *
     * @return
     */
    private static TableStyle getTableStyle() {
        TableStyle tableStyle = new TableStyle();

        tableStyle.setTableContentBackGroundColor(IndexedColors.WHITE);
        Font font = new Font();
        font.setBold(true);
        font.setFontHeightInPoints((short) 9);
        tableStyle.setTableHeadFont(font);
        Font fontContent = new Font();
        fontContent.setFontHeightInPoints((short) 9);
        tableStyle.setTableContentFont(fontContent);
        return tableStyle;
    }
}
