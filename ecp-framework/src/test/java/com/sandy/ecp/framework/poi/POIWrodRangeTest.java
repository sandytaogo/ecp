package com.sandy.ecp.framework.poi;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;



public class POIWrodRangeTest {
	
	public void test() throws IOException {
		
		String templatePath = "E:\\test.doc";  
		   InputStream is = new FileInputStream(templatePath);  
		   HWPFDocument doc = new HWPFDocument(is);  
		   Range range = doc.getRange();  
		   //在表格内使用“\r”是不能换行的  
		   range.replaceText("${param1}", "参数1的内容\n换行");  
		   //(char)11就代表一个换行符可以用在表格中  
		   range.replaceText("${param2}", "参数2的内容"+(char)0X0B+"换行");  
		   //非表格内使用“\r”是可以换行的  
		   range.replaceText("${param3}", "参数3的内容sdsdsdsddd\rdd\r\r\r\r\r\r\rssdsdsdsdsdsdsdsd\r换行");  
		   //非表格内使用“(char)11”也是可以换行的  
		   range.replaceText("${param4}", "参数4的内容"+(char)0X0B+"换行");  
		   OutputStream os = new FileOutputStream("e:\\newLine2.doc");  
		   doc.write(os);
	}

	public static void main(String [] args) throws IOException {
		
		new POIWrodRangeTest().test();
	}
	
}
