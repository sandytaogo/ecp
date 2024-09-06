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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sourceforge.tess4j.TesseractException;

/**
 * $ tesseract --help-extra
 *	Usage:
 *	  tesseract --help | --help-extra | --version
 *	  tesseract --list-langs
 *	  tesseract imagename outputbase [options...] [configfile...]
 *	OCR options:
 *	  -l LANG[+LANG]        Specify language(s) used for OCR.
 *	NOTE: These options must occur before any configfile.
 *	
 *	Single options:
 *	  --help                Show this help message.
 *	  --help-extra          Show extra help for advanced users.
 *	  --version             Show version information.
 *	  --list-langs          List available languages for tesseract engine.
 *	[root@iZwz92u03a7v4p3ubwzt47Z lib64]# tesseract --help-extra
 *	Usage:
 *	  tesseract --help | --help-extra | --help-psm | --help-oem | --version
 *	  tesseract --list-langs [--tessdata-dir PATH]
 *	  tesseract --print-parameters [options...] [configfile...]
 *	  tesseract imagename|imagelist|stdin outputbase|stdout [options...] [configfile...]
 *	OCR options:
 *	  --tessdata-dir PATH   Specify the location of tessdata path.
 *	  --user-words PATH     Specify the location of user words file.
 *	  --user-patterns PATH  Specify the location of user patterns file.
 *	  --dpi VALUE           Specify DPI for input image.
 *	  -l LANG[+LANG]        Specify language(s) used for OCR.
 *	  -c VAR=VALUE          Set value for config variables.
 *	                        Multiple -c arguments are allowed.
 *	  --psm NUM             Specify page segmentation mode.
 *	  --oem NUM             Specify OCR Engine mode.
 *	NOTE: These options must occur before any configfile.
 *	...省略了psm和oem的详细解释，后面会介绍。
 *	比如使用psm，很多老的文档都是：
 *	tesseract test.png test -l chi_sim -psm 1
 * @author Sandy
 * @see http://fancyerii.github.io/2019/03/12/3_tesseract/
 */
public class CmdImageConvertString {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(CmdImageConvertString.class);
	
	private String root;
	
	public void setRoot(String root) {
		this.root = root;
	}

	/**
	 *	Page segmentation modes:
	 *	  0    Orientation and script detection (OSD) only.
	 *	  1    Automatic page segmentation with OSD.
	 *	  2    Automatic page segmentation, but no OSD, or OCR. (not implemented)
	 *	  3    Fully automatic page segmentation, but no OSD. (Default)
	 *	  4    Assume a single column of text of variable sizes.
	 *	  5    Assume a single uniform block of vertically aligned text.
	 *	  6    Assume a single uniform block of text.
	 *	  7    Treat the image as a single text line.
	 *	  8    Treat the image as a single word.
	 *    9    Treat the image as a single word in a circle.
	 *	  10    Treat the image as a single character.
	 *	  11    Sparse text. Find as much text as possible in no particular order.
	 *	  12    Sparse text with OSD.
	 *	  13    Raw line. Treat the image as a single text line,
	 *	        bypassing hacks that are Tesseract-specific.
	 *	  OCR Engine modes:
	 *	   0    Legacy engine only.
	 *     1    Neural nets LSTM engine only.
	 *     2    Legacy + LSTM engines.
	 *     3    Default, based on what is available.
	 *	  Single options:
	 *     -h, --help            Show minimal help message.
	 *	   --help-extra          Show extra help for advanced users.
	 *	   --help-psm            Show page segmentation modes.
	 *	   --help-oem            Show OCR Engine modes.
	 *	   -v, --version         Show version information.
	 *     --list-langs          List available languages for tesseract engine.
	 *	   --print-parameters    Print tesseract parameters.
	*/
	public String doOCR(String path) throws TesseractException {
		StringBuilder sb = new StringBuilder();
		String result = root + "/result";
		BufferedReader cmdReader = null;
		BufferedReader bufferedReader = null;
		try {
	        // 要执行的命令 tesseract /usr/local/app/aa.jpg test -l chi_sim --oem 1
	        String command = String.format("tesseract %s %s -l chi_sim --oem 1", path, result); // 例如列出当前目录下的文件
	        // 执行命令
	        Process process = Runtime.getRuntime().exec(command);
	        // 读取命令的输出
	        cmdReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
	        String line;
	        while ((line = cmdReader.readLine()) != null) {
	        	sb.append(line);
	        }
	        // 等待命令执行完成
	        process.waitFor();
	        // 获取退出值
	        int exitValue = process.exitValue();
	        if (LOGGER.isInfoEnabled()) {
	        	LOGGER.info("doOCR context={}, exitValue={}", sb.toString(), exitValue);
	        }
	        sb.delete(0, sb.length());
	        FileReader fis = new FileReader(result + ".txt");
	        bufferedReader = new BufferedReader(fis);
	        while ((line = bufferedReader.readLine()) != null) {
	        	sb.append(line);
	        }
	    } catch (Exception e) {
	        throw new TesseractException(e);
	    } finally {
	    	IOUtils.closeQuietly(cmdReader);
	    	IOUtils.closeQuietly(bufferedReader);
	    }
		return sb.toString();
	}
}
