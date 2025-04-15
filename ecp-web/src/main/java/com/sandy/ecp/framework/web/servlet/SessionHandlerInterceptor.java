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
package com.sandy.ecp.framework.web.servlet;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerInterceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sandy.ecp.framework.session.SessionVO;
import com.sandy.ecp.framework.vo.Result;
import com.sandy.ecp.framework.web.context.EcpWebSecurityContextHolder;

public class SessionHandlerInterceptor implements HandlerInterceptor {

	@Autowired(required = false)
	private ObjectMapper objectMapper;
	
	@Autowired(required = false)
	@Qualifier("authProperties")
	private Properties authProperties;
	
	@Autowired(required = false)
	private Environment environment;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		return this.loginAuth(request, response);
	}
	
	public boolean loginAuth(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession(false);
		if (session != null) {
			Object details = session.getAttribute(SessionVO.KEY);
			if (details != null) {
				EcpWebSecurityContextHolder.setSessionVO(details);
				return true;
			}
		}
		return unauthorize(request, response);
	}
	
	public boolean unauthorize(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String requestedWith = request.getHeader("X-Requested-With");
		if ("XMLHttpRequest".equals(requestedWith)) {
			Result result = new Result();
			result.setCode(401);
			result.setMsg("尚未登录系统");
			response.setHeader("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
			objectMapper.writeValue(response.getOutputStream(), result);
		} else {
			String path = request.getServletPath();
			boolean isHttps = path.indexOf("https") == 0;
			String authCenter = environment.getProperty("auth.center.domain");
			String appDomain = environment.getProperty("application.domain");
			if (isHttps) {
//				authCenter.replace("http", "https");
//				appDomain.replace("http", "https");
			}
			StringBuilder url = new StringBuilder(128);
			url.append(appDomain);
			url.append(path);
			response.sendRedirect(authCenter + "?oauth_callback=" + url.toString());
		}
		return false;
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		EcpWebSecurityContextHolder.clearContext();
	}
}
