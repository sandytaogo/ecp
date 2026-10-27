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
package com.sandy.ecp.recognition.test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.sourceforge.tess4j.ITessAPI.TessOcrEngineMode;
import net.sourceforge.tess4j.ITessAPI.TessPageSegMode;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

/**
 * https://digi.bib.uni-mannheim.de/tesseract/
 * Description: 
 * https://tess4j.sourceforge.net/
 * 4：如果是识别中文图片，则需要自行下载中文检测包chi_sim.traineddata，并将chi_sim.traineddata文件放到tessdata文件夹下
 * 下载地址：https://raw.githubusercontent.com/tesseract-ocr/tessdata/master/chi_sim.traineddata
 * 其它检测包下载地址：https://codeload.github.com/tesseract-ocr/tessdata/zip/master
 * @author Sandy
 * @Date 2024年4月2日 下午9:25:16
 * @since 1.0.0
 */
public class CarLicensePlateTest {

	public String toString() {
		ITesseract instance = new Tesseract();
	    //如果未将tessdata放在根目录下需要指定绝对路径
	    //设置训练库的位置
	    instance.setDatapath("h:\\tessdata");
	    //如果需要识别英文之外的语种，需要指定识别语种，并且需要将对应的语言包放进项目中
	    // chi_sim ：简体中文， eng 根据需求选择语言库
	    instance.setLanguage("chi_sim");
//	    instance.setVariable("tessedit_char_whitelist", "acekopxyABCEHKMOPTXY0123456789");
	    //使用OSD进行自动页面分割以进行图像处理.
//	   ‌0‌：仅定向和脚本检测（OSD Only）
//	    ‌1‌：全自动分页 + OSD（默认推荐复杂文档）
//	    ‌3‌：全自动分页（无 OSD，常规文档）
//	    ‌4‌：假设单列文本（多栏排版如报纸）
//	    ‌6‌：假设单一文本块（适合截图、简单文档）
//	    ‌7‌：视为单行文本（标题、短标语、验证码）
//	    ‌8‌：视为单个单词（孤立词、无空格验证码）
//	    ‌10‌：视为单个字符（车牌单字、符号）
//	    ‌11‌：稀疏文本（分散标签、水印）
	    //setPageSegMode(10)：启用单字符分割模式，提升复杂排版识别率
	  	instance.setPageSegMode(TessPageSegMode.PSM_AUTO_OSD);
	    //设置引擎模式是网络LSTM引擎
	    //instance.setOcrEngineMode(TessOcrEngineMode.OEM_LSTM_ONLY);
	    //setOcrEngineMode(3)：选择LSTM+字典模式，平衡速度与精度
	    instance.setOcrEngineMode(TessOcrEngineMode.OEM_DEFAULT);
	    // 指定识别图片
	    File imgDir = new File("C:\\Users\\Sandy\\Desktop\\car.jpg");
	    imgDir = new File("C:\\Users\\Sandy\\Desktop\\8jd5vfe42g.jpeg");
	    imgDir = new File("F:\\mnt\\recognition\\20260715\\6b462607dbf043d39b3dd3e48500dd43.jpg");
	    
	    imgDir = new File("C:\\Users\\Sandy\\Desktop\\cgd.png");
//	    imgDir = new File("C:\\Users\\Sandy\\Desktop\\aa.jpg");
//	    imgDir = new File("C:\\Users\\Sandy\\Desktop\\cc.jpg");
//	    imgDir = new File("C:\\Users\\Sandy\\Desktop\\ddd.jpg");
//	    imgDir = new File("C:\\Users\\Sandy\\Desktop\\fff.jpg");
//	    imgDir = new File("C:\\Users\\Sandy\\Desktop\\gg.jpg");
	    long startTime = System.currentTimeMillis();
	    String ocrResult = null;
		try {
			BufferedImage fi = ImageIO.read(imgDir);
			ocrResult = instance.doOCR(fi);
		} catch (TesseractException e) { 
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    // 输出识别结果
	    System.out.println("识别结果: \n" + ocrResult + "\n 耗时：" + (System.currentTimeMillis() - startTime) + "ms");
		return null;
	}
	
	public static void main(String[] args) {
		int counter = 0;
		for (;;) {
			if (0 < counter) {
				break;
			}
			new CarLicensePlateTest().toString();
			counter ++;
		}
	}
}
