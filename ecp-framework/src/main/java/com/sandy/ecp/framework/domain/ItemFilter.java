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
package com.sandy.ecp.framework.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

/**
 * 数据库字段过滤器
 * @author Sandy
 * @since 1.0.0 04th 12 2018
 */
public class ItemFilter {
	
	public enum Operator {
		EQ("="), LIKE("like"), GT(">"), LT("<"), GTE(">="), LTE("<=");
		
		private String symbol;		// 鎿嶄綔绗﹀彿
		
		Operator(String symbol) {
			this.symbol = symbol;
		}
		public String getSymbol() {
			return this.symbol;
		}
	}

	public String fieldName;
	public Object value;
	public Operator operator;

	public ItemFilter(String fieldName, Operator operator, Object value) {
		this.fieldName = fieldName;
		this.value = value;
		this.operator = operator;
	}

	public static Map<String, ItemFilter> parse(Map<String, Object> searchParams) {
		Map<String, ItemFilter> filters = new HashMap<String, ItemFilter>();

		for (Entry<String, Object> entry : searchParams.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			if (StringUtils.isBlank((String) value)) {
				continue;
			}
			String[] names = StringUtils.split(key, "_");
			if (names.length != 2) {
				throw new IllegalArgumentException(key + " is not a valid search filter name");
			}
			String filedName = names[1];
			Operator operator = Operator.valueOf(names[0]);
			ItemFilter filter = new ItemFilter(filedName, operator, value);
			filters.put(key, filter);
		}
		return filters;
	}
}

