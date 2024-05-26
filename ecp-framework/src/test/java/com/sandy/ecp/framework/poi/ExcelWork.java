package com.sandy.ecp.framework.poi;

import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;

public class ExcelWork {

	public ExcelWork() throws IOException {
        HSSFWorkbook book = new HSSFWorkbook();
        HSSFSheet sheet = book.createSheet("统计表");
        
        for (int i = 0; i < 10; i ++) { 
        	HSSFRow row = sheet.createRow(i);
        	
        	CellStyle style = book.createCellStyle();
        	style.setFillForegroundColor(HSSFColor.YELLOW.index);
        	style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        	
        	style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        	style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        	style.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        	style.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        	style.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        	style.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
        	style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
        	
        	if (i == 0) {
        		CellRangeAddress range = new CellRangeAddress(0, (short) 0, 0, (short) 3);
        		sheet.addMergedRegion(range);
        	}
        	// 生成一个字体
        	HSSFFont font = book.createFont();
        	font.setFontHeightInPoints((short) 10);
        	font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        	font.setFontName("宋体");
        	// 把字体 应用到当前样式
        	style.setFont(font);
        	
        	HSSFCell cell = row.createCell(0);
        	cell.setCellStyle(style);
        	cell.setCellValue("teSSSSSSSSSSSSSSSSSSSSst" + i);
        	
        	cell= row.createCell(1);
        	cell.setCellValue("test" + i);
        	cell.setCellStyle(style);
        	
        	cell= row.createCell(2);
        	cell.setCellStyle(style);
        	cell.setCellValue("tesSSSSSSSSSSSSSSSSSSSt" + i);
        	
        	cell= row.createCell(3);
        	cell.setCellStyle(style);
        	cell.setCellValue("test" + i);
        	
        	cell= row.createCell(3);
        	cell.setCellStyle(style);
        	cell.setCellValue("test" + i);
        	
        	CellRangeAddress range = new CellRangeAddress(2, 4, 2, 2);
    		sheet.addMergedRegion(range);
    		
    		range = new CellRangeAddress(5, 8, 2, 2);
    		sheet.addMergedRegion(range);
    		
    		range = new CellRangeAddress(5, 6, 2, 2);
    		sheet.addMergedRegion(range);
    		
    		range = new CellRangeAddress(5, 7, 2, 2);
    		sheet.addMergedRegion(range);
    		
    		range = new CellRangeAddress(5, 5, 2, 2);
    		sheet.addMergedRegion(range);
        	
        }
        setSizeColumn(sheet, 3);
        FileOutputStream fos = new FileOutputStream("e:/shoot.xls");
        book.write(fos);
        fos.close();
	}
	
	// 自适应宽度(中文支持)
    private void setSizeColumn(HSSFSheet sheet, int size) {
        for (int columnNum = 0; columnNum < size; columnNum++) {
            int columnWidth = sheet.getColumnWidth(columnNum) / 256;
            for (int rowNum = 0; rowNum < sheet.getLastRowNum(); rowNum++) {
                HSSFRow currentRow;
                //当前行未被使用过
                if (sheet.getRow(rowNum) == null) {
                    currentRow = sheet.createRow(rowNum);
                } else {
                    currentRow = sheet.getRow(rowNum);
                }
 
                if (currentRow.getCell(columnNum) != null) {
                    HSSFCell currentCell = currentRow.getCell(columnNum);
                    if (currentCell.getCellType() == XSSFCell.CELL_TYPE_STRING) {
                        int length = currentCell.getStringCellValue().getBytes().length;
                        if (columnWidth < length) {
                            columnWidth = length;
                        }
                    }
                }
            }
            sheet.setColumnWidth(columnNum, columnWidth * 256);
        }
    }
	
	public static void main(String [] args) throws IOException {
		new ExcelWork();
	}
	
}
