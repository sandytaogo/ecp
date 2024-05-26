/*
 * Copyright 2016-2018 the original author or authors.
 *
 * Licensed under the HUIFU  License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 *      http://huifuwang.com
 */
package com.sandy.ecp.framework.util;

import java.io.File;
import java.util.Calendar;

/**
 * File Utils
 * 
 * @author Sandy
 * @since 1.0.0 12th 12 2016
 */
public class FileUtil {

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
