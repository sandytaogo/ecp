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

import java.math.BigDecimal;

public class NumberFormat {

	public static final BigDecimal HUNDRED_TEN_THOUSAND = new BigDecimal(10000);
	public static final BigDecimal HUNDRED_BILLION = new BigDecimal(100000000);
	
	private static final int DECIMAL_PLACES = 2;
	
	public static String toFormatUnit(Integer amount) {
		if (amount == null) {
			return "";
		}
		return toFormatUnit(amount.longValue());
	}
	
	public static String toFormatUnit(Long amount) {
		if (amount == null) {
			return "";
		}
		long curamount = Math.abs(amount);
		if (curamount >= HUNDRED_BILLION.longValue()) {
			return new BigDecimal(amount).divide(HUNDRED_BILLION, DECIMAL_PLACES, BigDecimal.ROUND_HALF_UP).toPlainString() + "亿";
		} else if (curamount >= HUNDRED_TEN_THOUSAND.longValue()) {
			return new BigDecimal(amount).divide(HUNDRED_TEN_THOUSAND, DECIMAL_PLACES, BigDecimal.ROUND_HALF_UP).toPlainString() + "万";
		}
		return amount.toString();
	}
}
