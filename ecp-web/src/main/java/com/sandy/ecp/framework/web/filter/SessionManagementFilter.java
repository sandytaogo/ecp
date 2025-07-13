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
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Properties;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.web.filter.GenericFilterBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sandy.ecp.framework.net.URLEncoder;
import com.sandy.ecp.framework.session.SessionVO;
import com.sandy.ecp.framework.vo.Result;
import com.sandy.ecp.framework.web.context.EcpWebSecurityContextHolder;

/**
 * session 过滤器.
 * @author Sandy
 * @date 2023-05-05 12:12:12
 */
public class SessionManagementFilter extends GenericFilterBean {

	/** Logger available to subclasses */
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired(required = false)
	private ObjectMapper objectMapper;
	
	@Autowired(required = false)
	@Qualifier("authProperties")
	private Properties authProperties;
	
	@Override
	@Autowired(required = false)
	public void setEnvironment(Environment environment) {
		super.setEnvironment(environment);
	}
	
//	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		if (request.getCharacterEncoding() == null) {
			request.setCharacterEncoding(Charset.defaultCharset().name());
		}
		if (response.getCharacterEncoding() == null) {
			response.setCharacterEncoding(Charset.defaultCharset().name());
		}
		StringBuffer url = request.getRequestURL();
		if (logger.isDebugEnabled()) {
			logger.debug("url {}", url);
		}
		boolean isAuth = true;
		if (authProperties != null) {
			Collection<Object> values = authProperties.keySet();
			for (Object v : values) {
				if (0 < url.indexOf(v.toString())) {
					isAuth = loginAuth(request, response);
					break;
				}
			}
		}
		if (isAuth) {
			chain.doFilter(request, response);
		}
		EcpWebSecurityContextHolder.clearContext();
	}
	
	public boolean loginAuth(HttpServletRequest request, HttpServletResponse response) throws IOException {
		SessionVO vo = EcpWebSecurityContextHolder.getSessionVO();
		if (vo != null) {
			return true;
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
			String authCenter = super.getEnvironment().getProperty("auth.center.domain");
			String appDomain = super.getEnvironment().getProperty("application.domain");
			if (isHttps) {
//				authCenter.replace("http", "https");
//				appDomain.replace("http", "https");
			}
			StringBuilder url = new StringBuilder(128);
			url.append(appDomain);
			url.append(path);
			response.sendRedirect(authCenter + "?oauth_callback=" + URLEncoder.encode(url.toString()));
		}
		return false;
	}
}
