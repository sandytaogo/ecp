/*
 * Copyright 2023-2035 the original author or authors.
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
package com.sandy.ecp.framework.servlet.method;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import com.sandy.ecp.framework.annotation.EnableEncrypt;
import com.sandy.ecp.framework.session.SessionVO;
import com.sandy.ecp.framework.util.IoUtil;
import com.sandy.ecp.framework.util.Sm2Util;
import com.sandy.ecp.framework.util.Sm4Util;
import com.sandy.ecp.framework.util.StringUtil;

/**
 * 企业云平台网络入口加密拦截处理器.
 * @author Sandy
 * @since 1.0.0 2023-04-23 12:12:12
 */
public class EncryptRequestBodyAdvice implements RequestBodyAdvice {
	
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
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
	public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
		EnableEncrypt enableEncrypt = methodParameter.getMethod().getAnnotation(EnableEncrypt.class);
		return enableEncrypt != null;
	}

	@Override
	public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
			Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
		EnableEncrypt ee = parameter.getMethod().getAnnotation(EnableEncrypt.class);
		if (ee == null) {
			return inputMessage;
		}
		HttpServletRequest httpRequest = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		HttpSession httpSession = httpRequest.getSession(false);
		if (httpSession == null) {
			return inputMessage;
		}
		SessionVO session = (SessionVO) httpSession.getAttribute(SessionVO.KEY);
		if (session == null) {
			return inputMessage;
		}
		try {
			String encryptedData = IoUtil.readyString(inputMessage.getBody(), charset);
//			inputMessage.getBody().reset(); // 重置流以便后续读取
			String encryptName = ee.inName();
			if ("sm2".equals(encryptName)) {
				encryptedData = Sm2Util.decrypt(encryptedData, session.getPrivateKey());
			} else if ("sm4".equals(encryptName)) {
				encryptedData = Sm4Util.decryptEcb(session.getSm4(), encryptedData, charset);
			}
			final byte[] bytes = encryptedData.getBytes();
	        return new HttpInputMessage() {
	            @Override
	            public InputStream getBody() throws IOException {
	                return new ByteArrayInputStream(bytes);
	            }
	            @Override
	            public HttpHeaders getHeaders() {
	                return inputMessage.getHeaders();
	            }
	        };
		} catch (Exception e) {
			// TODO: handle exception
		}
		return inputMessage;
	}

	@Override
	public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
			Class<? extends HttpMessageConverter<?>> converterType) {
		return body;
	}

	@Override
	public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter,
			Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
		return body;
	}

}
