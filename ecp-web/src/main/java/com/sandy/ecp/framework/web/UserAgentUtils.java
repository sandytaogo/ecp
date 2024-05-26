/*
 * Copyright 2016-2018 the original author or authors.
 *
 * Licensed under the HUIFU  License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 *      http://sandy.com
 */
package com.sandy.ecp.framework.web;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Container class for user-agent information with operating system and browser
 * details. Can decode user-agent strings.
 *
 * @author Sandy
 * @Since 1.0.0 14th 05 2018
 */
public class UserAgentUtils {

	protected final static Pattern LINUX = Pattern.compile(".*Linux.*");
	protected final static Pattern MAC_68K = Pattern.compile(".*(68k|68000).*");
	protected final static Pattern MAC = Pattern.compile(".*(Mac|apple|MacOS8).*");
	protected final static Pattern WIN_XP = Pattern.compile(".*(Windows NT 5\\.1|Windows XP).*");
	protected final static Pattern WIN_NT = Pattern.compile(".*(WinNT|Windows NT).*");
	protected final static Pattern WIN_2000 = Pattern.compile(".*(Win2000|Windows 2000|Windows NT 5\\.0).*");
	protected final static Pattern WIN_2003 = Pattern.compile(".*(Windows NT 5\\.2).*");
	protected final static Pattern WIN_7 = Pattern.compile(".*(Windows NT 6\\.1).*");
	protected final static Pattern WIN_8 = Pattern.compile(".*(Windows NT 6\\.2).*");
	protected final static Pattern WIN_9X = Pattern.compile(".*(9x 4.90|Win9(5|8)|Windows 9(5|8)|95/NT|Win32|32bit).*");
	protected final static Pattern WIN_10X = Pattern.compile(".*(9x 5.0|Win9(5|8)|Windows NT 10|95/NT|Win32|32bit).*");

	protected final static Pattern CLIENT = Pattern.compile(";\\s?(\\S*?\\s?\\S*?)\\s?(Build)?/");
	
	/**
	 * get user agent information
	 * 
	 * @return User-Agent String
	 */
	public String getuserAgent() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		return getuserAgent(request);
	}

	/**
	 * get user agent information
	 * 
	 * @param request
	 * @return User-Agent String
	 */
	public static String getuserAgent(HttpServletRequest request) {
		return request.getHeader("User-Agent");
	}

	/**
	 * get client operating system
	 * 
	 * @param request
	 * @return client operating system
	 */
	public static String getClientOs(HttpServletRequest request) {
		String userAgent = getuserAgent(request);
		return getClientOs(userAgent);
	}
	
	public static String getClientOs(String userAgent) {
		String cos = "unknow os";
		if (null == userAgent) {
			return cos;
		}
		Matcher m = WIN_8.matcher(userAgent);
		if (m.find()) {
			cos = "Win 8";
			return cos;
		}
		m = WIN_10X.matcher(userAgent);
		if (m.find()) {
			cos = "Win 10";
			return cos;
		}
		m = WIN_7.matcher(userAgent);
		if (m.find()) {
			cos = "Win 7";
			return cos;
		}
		m = WIN_XP.matcher(userAgent);
		if (m.find()) {
			cos = "WinXP";
			return cos;
		}
		m = WIN_2003.matcher(userAgent);
		if (m.find()) {
			cos = "Win2003";
			return cos;
		}
		m = WIN_2000.matcher(userAgent);
		if (m.find()) {
			cos = "Win2000";
			return cos;
		}
		m = MAC.matcher(userAgent);
		if (m.find()) {
			cos = "MAC";
			return cos;
		}
		m = WIN_NT.matcher(userAgent);
		if (m.find()) {
			cos = "WinNT";
			return cos;
		}
		m = LINUX.matcher(userAgent);
		if (m.find()) {
			cos = "Linux";
			return cos;
		}
		m = MAC_68K.matcher(userAgent);
		if (m.find()) {
			cos = "Mac68k";
			return cos;
		}
		m = WIN_9X.matcher(userAgent);
		if (m.find()) {
			cos = "Win9x";
			return cos;
		}
		return cos;
	}
}

