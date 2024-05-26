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
package com.sandy.ecp.framework.web.context;

import javax.servlet.ServletContext;

import org.springframework.web.context.ServletContextAware;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
/**
 * web上下文.
 * @author Sandy
 * @date 2023-05-05 12:12:12
 */
public class EcpWebApplicationContext implements ServletContextAware {
	
	private static ServletContext servletContext;

	private static WebApplicationContext webContext;
	
	@Override
	public void setServletContext(ServletContext servletContext) {
		EcpWebApplicationContext.servletContext = servletContext;
		EcpWebApplicationContext.webContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
	}
	
	public static WebApplicationContext getWebContext() {
		return webContext;
	}

	public static ServletContext getServletContext() {
		return servletContext;
	}
	
}
