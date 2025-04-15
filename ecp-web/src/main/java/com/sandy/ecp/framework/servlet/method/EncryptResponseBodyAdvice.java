/*
 * Copyright 2023-2030 the original author or authors.
 *
 * Licensed under the company License, Version 2.0 (the "License");
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
package com.sandy.ecp.framework.servlet.method;

import java.nio.charset.StandardCharsets;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sandy.ecp.framework.annotation.EnableEncrypt;
import com.sandy.ecp.framework.exception.EcpRuntimeException;
import com.sandy.ecp.framework.session.SessionVO;
import com.sandy.ecp.framework.util.Sm4Util;
import com.sandy.ecp.framework.util.StringUtil;

/**
 * 企业云平台网络响应加密拦截处理器.
 * @author Sandy
 * @since 1.0.0 2023-04-23 12:12:12
 */
public class EncryptResponseBodyAdvice implements ResponseBodyAdvice<Object> {
	
	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired(required = false)
	protected ObjectMapper objectMapper;
	
	@Autowired
	private Environment environment;
	
	private String charset = StandardCharsets.UTF_8.name();
	
	/**
	 * 初始化.
	 */
	@PostConstruct
	public void initializing() {
		charset = environment.getProperty("spring.http.encoding.charset");
		if (StringUtil.isBlank(charset)) {
			charset = StandardCharsets.UTF_8.name();
		}
	}
	
	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		EnableEncrypt enableEncrypt = returnType.getMethod().getAnnotation(EnableEncrypt.class);
		return enableEncrypt != null;
	}

	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
			Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
			ServerHttpResponse response) {
		EnableEncrypt ee = returnType.getMethod().getAnnotation(EnableEncrypt.class);
		if (ee != null && ee.out()) {
			HttpServletRequest httpRequest = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
			HttpSession httpSession = httpRequest.getSession(false);
			SessionVO session = null;
			if (httpSession != null) {
				session = (SessionVO) httpSession.getAttribute(SessionVO.KEY);
			}
			if (session == null) {
				throw new EcpRuntimeException("Encrypt key null Response fail");
			}
			if (body instanceof MappingJacksonValue) {
				MappingJacksonValue mjv = (MappingJacksonValue) body;
				try {
					String v = objectMapper.writeValueAsString(mjv.getValue());
					v = Sm4Util.encryptEcb(session.getSm4(), v, charset);
					mjv.setValue(v);
				} catch (Exception e) {
					if (logger.isErrorEnabled()) {
						logger.error(e.getMessage(), e);
					}
				}
			} else if (body instanceof String) {
				try {
					String v = (String) body;
					v = Sm4Util.encryptEcb(session.getSm4(), v, charset);
					body = v;
				} catch (Exception e) {
					if (logger.isErrorEnabled()) {
						logger.error(e.getMessage(), e);
					}
				}
			} else {
				try {
					String v = objectMapper.writeValueAsString(body);
					v = Sm4Util.encryptEcb(session.getSm4(), v, charset);
					body = v;
				} catch (Exception e) {
					if (logger.isErrorEnabled()) {
						logger.error(e.getMessage(), e);
					}
				}
			}
		}
		return body;
	}
}
