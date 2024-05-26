package com.sandy.ecp.framework.pdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;

public class GenarePdf {
	
	private static final String fontPath = "/simfang.ttf";
	@SuppressWarnings("unused")
	private static String fontName = "宋体";
	protected static String encoding = "gb2312";
//  BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
	@SuppressWarnings("unused")
	public static void main(String[] args) throws DocumentException, IOException {
		File resFile = File.createTempFile("simfang", ".ttf");
		System.out.println(resFile.getPath());
		// 第一步，创建document对象
        Rectangle rectPageSize = new Rectangle(PageSize.A4);
        //下面代码设置页面横置
        //rectPageSize = rectPageSize.rotate();
        //创建document对象并指定边距
        Document doc = new Document(rectPageSize, 8, 8, 8, 8);
        //创建基础字体
        String path = GenarePdf.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		
    	BaseFont bf = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED);
		XMLWorkerFontProvider provider = new XMLWorkerFontProvider();
    	Font titleFont = new Font(bf, 20, Font.BOLD, BaseColor.BLACK);
    	
    	//titleFont = provider.getFont("楷书", Font.BOLD, BaseColor.BLACK);
        Font tableFont = new Font(bf, 15, Font.BOLD, BaseColor.BLACK);
        Font textFont = new Font(bf, 12, Font.NORMAL, BaseColor.BLACK);
        
        
        //创建输出流
        PdfWriter.getInstance(doc, new FileOutputStream(new File("f://test.pdf")));
        //打开文档
        doc.open();
        
        Paragraph paragraph = new Paragraph("资金结算系统CA证书（变更）申请表", titleFont);
        paragraph.setLeading(40);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        paragraph.setSpacingBefore(20);
        paragraph.setSpacingAfter(10);
        doc.add(paragraph);
        
        // 创建一个有4列的表格
        PdfPTable table = new PdfPTable(7);
        table.setTotalWidth(new float[]{60,80,80,80,80,80,80}); //设置列宽
        table.setLockedWidth(true); //锁定列宽
        
        PdfPCell cell = PdfUtil.getCell("申请单位基本信息", tableFont, -1, 40, PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_MIDDLE, 7);
        table.addCell(cell);
        
        cell = PdfUtil.getCell("全    称", textFont, -1, 40, PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_MIDDLE, 2);
        table.addCell(cell);
        cell = PdfUtil.getCell("远光软件股份有限公司", textFont, -1, 40, PdfPCell.ALIGN_LEFT, PdfPCell.ALIGN_MIDDLE, 5);
        table.addCell(cell);
        
        cell = PdfUtil.getCell("统一社会信用代码", textFont, -1, 40, PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_MIDDLE, 2);
        table.addCell(cell);
        cell = PdfUtil.getCell("AAAAAAAAAA", textFont, -1, 40, PdfPCell.ALIGN_LEFT, PdfPCell.ALIGN_MIDDLE, 2);
        table.addCell(cell);
        cell = PdfUtil.getCell("组织结构代码", textFont, -1, 40, PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_MIDDLE, 1);
        table.addCell(cell);
        cell = PdfUtil.getCell("9999", textFont, -1, 40, PdfPCell.ALIGN_LEFT, PdfPCell.ALIGN_MIDDLE, 2);
        table.addCell(cell);

        cell = PdfUtil.getCell("申请事项", tableFont, -1, 40, PdfPCell.ALIGN_CENTER, PdfPCell.ALIGN_MIDDLE, 7);
        table.addCell(cell);
        
        table = createCell(table, new String[]{"序号", "名称", "ID", "证件编号", "证书类型", "业务类型", "备注"}, 1, 7, -1);
        for (int i = 1; i < 5; i++) {
            table = createCell(table, new String[]{"深圳", "东莞工厂", "001002003004", "00" + i, "a" + i, "f://Wind.jpg", "时间"}, 1, 7, -1);
        }
        doc.add(table);
        doc.close();
	}
	
	/**
     * 创建单元格
     */
    private static PdfPTable createCell(PdfPTable table, String[] title, int row, int cols, int img_col_num) throws DocumentException, IOException {
        //添加中文字体
    	BaseFont bf = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED);
    	Font font = new Font(bf, 12, Font.NORMAL, BaseColor.BLACK);
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < cols; j++) {
                PdfPCell cell = new PdfPCell();
                if (i == 0 && title != null) {//设置表头
                    if (img_col_num == j) {//添加图片列
                        Image img = Image.getInstance(title[img_col_num]);
                        img.setBorderWidth(0);
                        img.scaleToFit(50, 70);// 大小
                        cell = new PdfPCell(img);
                        cell.setPadding(1);
                    } else {
                        cell = new PdfPCell(new Phrase(title[j], font)); //这样表头才能居中
                    }
                    if (table.getRows().size() == 0) {
                        cell.setBorderWidthTop(1);
                    }
                }
                if (row == 1 && cols == 1) { //只有一行一列
                    cell.setBorderWidthTop(1);
                }
                if (j == 0) {//设置左边的边框宽度
                    cell.setBorderWidthLeft(1);
                }
                if (j == (cols - 1)) {//设置右边的边框宽度
                    cell.setBorderWidthRight(1);
                }
                if (i == (row - 1)) {//设置底部的边框宽度
                    cell.setBorderWidthBottom(1);
                }
                cell.setFixedHeight(40);
                cell.setUseAscender(true); //设置可以居中
                cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER); //设置水平居中
                cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE); //设置垂直居中
                table.addCell(cell);
            }
        }
        return table;
    }
}
