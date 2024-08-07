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
package com.sandy.ecp.framework.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.poi.util.IOUtils;

/**
 * zip 压缩文件支持.
 * @author Sandy
 * @see https://www.cnblogs.com/cliveleo/articles/9759019.html
 * @since 1.0.0 28th 12 2018
 */
public class ZipUtil {
	
	public static void main(String [] args) throws IOException {
		FileOutputStream fos = new FileOutputStream("e:test.zip");
		ZipOutputStream zipOutputStream = new  ZipOutputStream(fos);
		File file =new File("e:calc_quistion.PNG");
		FileInputStream fis = new FileInputStream(file);
		int read = 0;
		zipOutputStream.putNextEntry(new ZipEntry("test.png"));
		byte [] bytes = new byte[1024];
		int offset = 0;
		while ((read = fis.read(bytes)) != -1) {
			zipOutputStream.write(bytes, offset, read);
		}
		zipOutputStream.closeEntry();
		zipOutputStream.flush();
		zipOutputStream.close();
		IOUtils.closeQuietly(fis);
	}
}
