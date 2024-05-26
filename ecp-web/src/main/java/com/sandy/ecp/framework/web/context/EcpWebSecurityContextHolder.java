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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.sandy.ecp.framework.context.EcpSecurityContextHolder;
import com.sandy.ecp.framework.session.SessionVO;

/**
 * 框架web 安全上下文
 * @author Sandy
 * @since 1.0.0 2023-01-01 10:10:01
 */
public class EcpWebSecurityContextHolder {

	public static SessionVO getSessionVO() {
		final HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		if (request != null) {
			HttpSession session = request.getSession(false);
			if (session != null) {
				Object details = session.getAttribute(SessionVO.KEY);
				if (details instanceof SessionVO) {
					return (SessionVO) details;
				}
			}
		}
		return null;
	}
	
	public static void setSessionVO(Object session) {
		EcpSecurityContextHolder.setSessionVO(session);
	}
	
	public static void clearContext() {
		EcpSecurityContextHolder.clearContext();
	}
}
