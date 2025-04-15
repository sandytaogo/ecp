/*
 * Copyright 2018-2030 the original author or authors.
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
package com.sandy.ecp.framework.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.filter.GenericFilterBean;

import com.sandy.ecp.framework.servlet.XssHttpServletRequestWrapper;

/**
 * 企业云平台 Xss 内容替换过滤器.
 * （1）持久型跨站：最直接的危害类型，跨站代码存储在服务器（数据库）。
 * （2）非持久型跨站：反射型跨站脚本漏洞，最普遍的类型。用户访问服务器-跨站链接-返回跨站代码。
 * （3）DOM跨站（DOM XSS）：DOM（document object model文档对象模型），客户端脚本处理逻辑导致的安全问题。
 * @author Sandy
 * @date 2023-05-05 12:12:12
 */
public class EcpXssWebFilter extends GenericFilterBean {
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		XssHttpServletRequestWrapper requestWrapper = new XssHttpServletRequestWrapper(request);
		chain.doFilter(requestWrapper, res);
	}
	
	@Override
	public void destroy() {
		
	}
}
