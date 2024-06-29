/*
 * Copyright 2024-2030 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sandy.ecp.framework.poi;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;
import org.junit.Test;

/**
 * POI Wrod Range Test
 * @author Sandy
 * @date 2024-02-02 12:12:12
 */
public class POIWrodRangeTest {
	
	@Test
	public void test() throws IOException {
		InputStream is = POIWrodRangeTest.class.getResourceAsStream("/replace-test.doc");
		if (is != null) {
			return ;
		}
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
}
