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
package com.sandy.ecp.recognition;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.LoadLibs;

/**
 * 4：如果是识别中文图片，则需要自行下载中文检测包chi_sim.traineddata，并将chi_sim.traineddata文件放到tessdata文件夹下
 * 下载地址：https://raw.githubusercontent.com/tesseract-ocr/tessdata/master/chi_sim.traineddata
 * 其它检测包下载地址：https://codeload.github.com/tesseract-ocr/tessdata/zip/master
 * @author Sandy
 * @Date 2024年4月2日 下午9:25:16
 * @since 1.0.0
 * @see https://tess4j.sourceforge.net
 */
public class ImageConvertString {
	
	@Autowired(required = false)
	private Environment env;
	
	public String doOCR(String path) throws TesseractException {
		ITesseract instance = new Tesseract();
	    //如果未将tessdata放在根目录下需要指定绝对路径
	    //设置训练库的位置
	    instance.setDatapath("h:\\tessdata");
	    File tessDataFolder = LoadLibs.extractTessResources("tessdata");
	    //如果需要识别英文之外的语种，需要指定识别语种，并且需要将对应的语言包放进项目中
	    // chi_sim ：简体中文， eng 根据需求选择语言库
	    instance.setLanguage("chi_sim");
//	    instance.setVariable("tessedit_char_whitelist", "acekopxyABCEHKMOPTXY0123456789");
	    //使用OSD进行自动页面分割以进行图像处理.
//	    instance.setPageSegMode(1);
	    //设置引擎模式是网络LSTM引擎
//	    instance.setOcrEngineMode(1);
	    // 指定识别图片
	    File imgDir = new File("C:\\Users\\Sandy\\Desktop\\car.jpg");
	    imgDir = new File("C:\\Users\\Sandy\\Desktop\\8jd5vfe42g.jpeg");
	    imgDir = new File("C:\\Users\\Sandy\\Desktop\\gg.jpg");
	   String ocrResult = instance.doOCR(imgDir);
	   return null;
	}
}
