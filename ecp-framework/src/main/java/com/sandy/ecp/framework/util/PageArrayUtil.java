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

/**
 * 企业云平台分页工具类
 * @author Sandy
 * @since 1.0.0 12th 12 2024
 */
public class PageArrayUtil {

	private PageArrayUtil() {
		super();
	}
	
	/**
	 * 获取分页大小计算函数.
	 * @param pageSize
	 * @return 返回分页大小
	 */
	public static int getPageSize(Integer pageSize) {
		if (pageSize == null || pageSize < 1) {
			return PageArrayList.DEFAULT_PAGE_SIZE;
		}
		return pageSize;
	}
}
