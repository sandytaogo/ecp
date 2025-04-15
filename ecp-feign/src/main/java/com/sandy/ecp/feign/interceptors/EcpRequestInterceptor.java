/*
 * Copyright 2024-2030 the original author or authors.
 *
 * Licensed under the company License, Version 2.0 (the "License");
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
package com.sandy.ecp.feign.interceptors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * ECP feign 编码拦请求截器.
 * @author Sandy
 * @since 1.0.0 2024-09-12 12:12:12
 */
public class EcpRequestInterceptor implements RequestInterceptor {

	/**
	 * 拦截请求流式编程设计理念.
	 * @param template RequestTemplate.
	 */
	@Override
	public void apply(RequestTemplate template) {
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		if (requestAttributes != null) {
			 final HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
			 String cookie = request.getHeader("cookie");
			 template.header("Cookie", cookie);
		}
	}
}
