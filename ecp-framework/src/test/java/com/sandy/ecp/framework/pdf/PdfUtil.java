package com.sandy.ecp.framework.pdf;

import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;

public final class PdfUtil {

	/**
	 * 构造方法.
	 */
	private PdfUtil() {
		
	}
	
	/**
	 * 合并列向左对齐.
	 * @param str 单元格属性值
	 * @param font 字体
	 * @param col 列
	 * @return PdfPCell
	 */
	public static PdfPCell mergeColForLeft(final String str, final Font font, final int col) {
		final PdfPCell cell = new PdfPCell(new Paragraph(str, font));
		cell.setMinimumHeight(25);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setColspan(col);
		return cell;
	}
	
	/**
	 * 合并列向右对齐.
	 * @param str 单元格属性值
	 * @param font 字体
	 * @param col 列
	 * @return PdfPCell
	 */
	public static PdfPCell mergeColForRight(final String str, final Font font, final int col) {
		final PdfPCell cell = new PdfPCell(new Paragraph(str, font));
		cell.setMinimumHeight(25);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setColspan(col);
		return cell;
	}
	
	/**
	 * 获取指定内容与字体的单元格.
	 * @param string 单元格属性值
	 * @param font 字体
	 * @return PdfPCell
	 */
	public static PdfPCell getPDFCell(final String string, final Font font) {
		final PdfPCell cell = new PdfPCell(new Paragraph(string, font));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setMinimumHeight(25);
		return cell;
	}
	
	/**
	 * 合并列向左对齐边框隐藏.
	 * @param str 单元格属性值
	 * @param font 字体
	 * @param col 列
	 * @return PdfPCell
	 */
	public static PdfPCell mergeColForHide(final String str, final Font font, final int col) {
		final PdfPCell cell = new PdfPCell(new Paragraph(str, font));
		//cell.setMinimumHeight(25);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setColspan(col);
		cell.disableBorderSide(15);
		return cell;
	}
	
	/**
	 * 合并列向左对齐边框隐藏.
	 * @param str 单元格属性值
	 * @param font 字体
	 * @param col 列
	 * @return PdfPCell
	 */
	public static PdfPCell mergeColForHidePadding(final String str, final Font font, final int col) {
		final PdfPCell cell = new PdfPCell(new Paragraph(str, font));
		cell.setMinimumHeight(25);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setColspan(col);
		cell.disableBorderSide(15);
		cell.setPaddingLeft(30);
		return cell;
	}
	
	/**
	 * 合并列向左对齐.
	 * @param str 单元格属性值
	 * @param font 字体
	 * @param col 列
	 * @return PdfPCell
	 */
	public static PdfPCell mergeColForMinHeight(final String str, final Font font, final int col) {
		final PdfPCell cell = new PdfPCell(new Paragraph(str, font));
		cell.setMinimumHeight(15);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setColspan(col);
		return cell;
	}
	
	/**
	 * 获取指定内容与字体的单元格.
	 * @param str
	 * @param font
	 * @param width
	 * @param height 
	 * @param ha Horizontal Alignment  PdfPCell
	 * @param va Vertical Alignment PdfPCell
	 * @param colspan
	 * @return PdfPCell
	 */
	public static PdfPCell getCell(String str, final Font font, int width, int height, int ha, int va, int colspan) {
		final PdfPCell cell = new PdfPCell(new Paragraph(str, font));
		cell.setHorizontalAlignment(ha);
		cell.setVerticalAlignment(va);
		if (0 <= height) {
			cell.setFixedHeight(height);
		}
		if (0 < colspan) {
			cell.setColspan(colspan);
		}
		return cell;
	}
}

