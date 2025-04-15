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
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Currency;

/**
 * 国际货币工具类.bbbbbbbbbbbbbbbbbbbb
 * @author Sandy
 * @since 1.0.0 2022-03-04 10:10:10 
 */
public class CurrencyUtil {
	
	private static final int DECIMAL_PLACES = 2;
	
	/**
	 * 百分位
	 */
	public static final BigDecimal HUNDRED = new BigDecimal(100);
	/**
	 * 万分位
	 */
	public static final BigDecimal HUNDRED_TEN_THOUSAND = new BigDecimal(10000);
	/**
	 * 
	 */
	public static final BigDecimal TEN_MILLION = new BigDecimal(1000000);
	public static final BigDecimal HUNDRED_BILLION = new BigDecimal(100000000L);
	
	public static BigDecimal getDecimalYuanFromFen(Long amount) {
		if (amount == null) {
			return null;
		}
		return new BigDecimal(amount).divide(HUNDRED).setScale(DECIMAL_PLACES, RoundingMode.HALF_UP);
	}
	
	public static Long getFenFromYuan(BigDecimal amount) {
		if (amount == null) {
			return null;
		}
		return amount.multiply(HUNDRED).longValue();
	}

	public static String toStringCNY(Long amount) {
		if (amount == null) {
			return "";
		}
		return getDecimalYuanFromFen(amount).toString();
	}
	
	public static String format(Long amount) {
		BigDecimal cny = CurrencyUtil.getDecimalYuanFromFen(amount);
		NumberFormat numberFormat = NumberFormat.getNumberInstance();
		numberFormat.setMinimumFractionDigits(DECIMAL_PLACES);
		numberFormat.setMaximumFractionDigits(DECIMAL_PLACES);
		return numberFormat.format(cny);
	}
	
	public static String toFormatUnit(Long amount) {
		if (amount == null) {
			return null;
		}
		long curamount = Math.abs(amount);
		if (curamount >= HUNDRED_BILLION.longValue() * HUNDRED.longValue()) {
			return new BigDecimal(amount).divide(HUNDRED).divide(HUNDRED_BILLION, DECIMAL_PLACES, RoundingMode.HALF_UP).toPlainString() + "亿";
		} else if (curamount >= HUNDRED_TEN_THOUSAND.longValue() * HUNDRED.longValue()) {
			return new BigDecimal(amount).divide(HUNDRED).divide(HUNDRED_TEN_THOUSAND, DECIMAL_PLACES, RoundingMode.HALF_UP).toPlainString() + "万";
		}
		return toStringCNY(amount);
	}
	
	public static Long toLongFen(String amount) {
		Currency currency = Currency.getInstance("CNY");
		BigDecimal bigDecimal = new BigDecimal(new Double(amount).doubleValue());
		return bigDecimal.movePointRight(currency.getDefaultFractionDigits()).setScale(0, BigDecimal.ROUND_HALF_EVEN).longValue();
	}
	
	/**
	 * 
	 * @param a 总数
	 * @param b divide.
	 * @return 商
	 */
	public static Long divide(Long a, Long b) {
		if  (a == null) {
			return null;
		}
		if (b == null) {
			return 0L;
		}
		return new BigDecimal(a).divide(new BigDecimal(b), 4, RoundingMode.HALF_UP).longValue();
	}
	
	/**
	 * @param a 
	 * @param b .
	 * @return 乘法结果.
	 */
	public static Long multiply(Long a, Long b) {
		if  (a == null) {
			return null;
		}
		if (b == null) {
			return 0L;
		}
		return new BigDecimal(a).multiply(new BigDecimal(b)).longValue();
	}
}
