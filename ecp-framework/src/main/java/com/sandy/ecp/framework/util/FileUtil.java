/*
 * Copyright 2024-2030 the original author or authors.
 *
 * Licensed under the company, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.company.com/licenses/LICENSE-2.0
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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import org.springframework.util.DigestUtils;

/**
 * File Utils
 * 
 * @author Sandy
 * @since 1.0.0 12th 12 2016
 */
public final class FileUtil {
	
	private FileUtil() {
		super();
	}
	
	/**
	 * 创建目录.
	 * @param file 文件对象.
	 * @throws IOException
	 */
	public static boolean createDirectory(File file) throws IOException {
		if(!file.exists()) {
			String os = System.getProperty("os.name");
			if (os != null && 0 <= os.indexOf("Windows")) {
				return file.mkdirs();
			} else {
				Set<PosixFilePermission> perms = new HashSet<PosixFilePermission>();
				perms.add(PosixFilePermission.OWNER_READ);
				perms.add(PosixFilePermission.OWNER_WRITE);
				perms.add(PosixFilePermission.OWNER_EXECUTE);
				perms.add(PosixFilePermission.OTHERS_READ);
				Path path = Paths.get(file.getPath());
				Files.createDirectories(path, PosixFilePermissions.asFileAttribute(perms));
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 读取文件MD5
	 * @param path
	 * @return md5 digest as hex
	 * @throws IOException
	 */
	public static String md5AsHex(String path) throws IOException {
		return DigestUtils.md5DigestAsHex(new FileInputStream (new File(path)));
	}

	/**
	 * @param name
	 * @return
	 */
	public static String getSuffix(String name) {
		if (null == name) {
			return null;
		}
		int lastIndex = name.lastIndexOf(".");
		if (lastIndex < 0) {
			return name;
		}
		return name.substring(lastIndex, name.length());
	}

	/**
	 * @param root
	 * @param foler
	 * @param name
	 * @return
	 */
	public static String getFilePath(String root, String foler, String name) {
		return new StringBuilder(root).append(foler).append(name).toString();
	}

	/**
	 * @param root
	 * @param foler
	 * @param name
	 * @param suffix
	 * @return
	 */
	public static String getFilePath(String root, String foler, String name, String suffix) {
		return new StringBuilder(root).append(foler).append(name).append(suffix).toString();
	}

	/**
	 * @param oldName
	 * @param newNane
	 * @return
	 */
	public static String getNewFileName(String oldName, String newNane) {
		return new StringBuilder(newNane).append(getSuffix(oldName)).toString();
	}

	/**
	 * Created if the file does not exist
	 * 
	 * @param folder
	 * @return is Created folder
	 */
	public static boolean checkFolderMkdirs(String folder) {
		File file = new File(folder);
		if (!file.exists()) {
			return file.mkdirs();
		}
		return false;
	}

	/**
	 * get Date Folder
	 * 
	 * @return date folder
	 */
	public static String getDateFolder() {
		return getDateFolder(new StringBuilder());
	}

	/**
	 * get Date Folder File.separator
	 * 
	 * @param value
	 *            File.separator default linux
	 * @return
	 */
	public static String getDateFolder(StringBuilder value) {
		Calendar calendar = Calendar.getInstance();
		return value.append(calendar.get(Calendar.YEAR)).append("/").append((calendar.get(Calendar.MARCH) + 1))
				.append("/").append(calendar.get(Calendar.DATE)).append("/").toString();
	}
}
