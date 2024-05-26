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

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * 企业云平台日期范围工具类.
 * @author Sandy
 * @since 1.0.0 07th 07 2017
 */
public class DateRange implements Serializable {

	private static final long serialVersionUID = 1131859733873632795L;

	private Date fromDate;

	private Date toDate;

	/**
	 * default between the current date.
	 * @param date
	 */
	public DateRange(Date date) {
		// setting fromDate
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		this.fromDate = calendar.getTime();

		// setting toDate
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		this.toDate = calendar.getTime();
	}

	/**
	 * Date Range
	 * 
	 * @param fromDate
	 * @param toDate
	 */
	public DateRange(Date fromDate, Date toDate) {
		// setting fromDate
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fromDate);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		this.fromDate = calendar.getTime();
		// setting toDate
		calendar.setTime(toDate);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		this.toDate = calendar.getTime();
	}

	public Date getFromDate() {
		return fromDate;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	public Date getToDate() {
		return toDate;
	}
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	
	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder(100);
		buffer.append('[');
		buffer.append("fromDate=").append(fromDate);
		buffer.append(",toDate=").append(toDate);
		buffer.append(']');
		return buffer.toString();
	}
}