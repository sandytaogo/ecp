/*
 * Copyright 2023-2035 the original author or authors.
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
package com.sandy.ecp.framework.web.security;

import javax.servlet.http.HttpServletRequest;

import com.sandy.ecp.framework.util.StringUtil;

public class SecurityUtils {

	/**
	 * 令牌前缀
	 */
	public static final String PREFIX = "Bearer ";

	/**
	 * 根据request获取请求token
	 */
	public static String getToken(HttpServletRequest request) {
		// 从header获取token标识
		String token = request.getHeader(SecurityConstants.AUTHORIZATION_HEADER);
		return replaceTokenPrefix(token);
	}

	/**
	 * 裁剪token前缀
	 */
	public static String replaceTokenPrefix(String token) {
		// 如果前端设置了令牌前缀，则裁剪掉前缀
		if (StringUtil.isNotEmpty(token) && token.startsWith(PREFIX)) {
			token = token.replaceFirst(PREFIX, "");
		}
		return token;
	}

}
