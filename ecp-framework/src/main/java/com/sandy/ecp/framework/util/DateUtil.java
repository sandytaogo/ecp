/*
 * Copyright 2018 the original author or authors.
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

public class DateUtil {
	
	protected static final String DATE_REGEX = "^([1-9]\\d{3}-)(([0]{0,1}[1-9]-)|([1][0-2]-))(([0-3]{0,1}[0-9]))$";
	
	public static final String DATE_PATTERN = "yyyy-MM-dd";

	public static String format(Date date) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
		return sdf.format(date);
	}
	
	public static Date date() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
	
	public static String format(Date date, String pattern) {
		if (date == null) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		String text = sdf.format(date);
		return text;
	}
	
	public static Date toDate(String date) {
		if (StringUtil.isBlank(date) || "undefined".equals(date)) {
			return null;
		}
		boolean matches = Pattern.matches("\\d{4}-\\d{1,2}-\\d{1,2}", date);
		if (!matches) {
			return null;
		}
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
			return sdf.parse(date);
		} catch (ParseException | NumberFormatException e) {
			System.err.println(String.format("日期:%s", date));
			e.printStackTrace();
		}
		return null;
	}
	
	public static Date toDate(int year, int month, int day) {
		return Date.from(LocalDate.of(year, month, day).atStartOfDay(ZoneOffset.ofHours(8)).toInstant());
	}
	
	public static Date toDate(String date, String pattern) {
		if (StringUtil.isBlank(date) || "undefined".equals(date)) {
			return null;
		}
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			return sdf.parse(date);
		} catch (ParseException | NumberFormatException e) {
			System.err.println(String.format("日期:%s", date));
			e.printStackTrace();
		}
		return null;
	}
	
	public static int betweenDay(Date source, Date target) {
		int timeMillisDay = 1000 * 3600 * 24;
		int days = (int) ((source.getTime() - target.getTime()) / timeMillisDay);
		return days;
	}
	
	public static boolean equalsDate(Date source,Date target) {   
        Calendar c1 = Calendar.getInstance();   
        Calendar c2 = Calendar.getInstance();   
        c1.setTime(source);  
        c2.setTime(target);
        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)   
        		&& c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH)   
                && c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH);   
    }  
	
	public static int toMinutes(Date fromDate, Date toDate) {
		return (int) (( toDate.getTime() - fromDate.getTime() ) / (1000 * 60));
	}
	
	/**
	 * 获取月份总天数
	 * @param year
	 * @param month
	 * @return days.
	 */
	public static int lengthOfMonth(int year, int month) {
		YearMonth yearMonth = YearMonth.of(year, month);
        int daysInMonth = yearMonth.lengthOfMonth();
        return daysInMonth;
	}
}
