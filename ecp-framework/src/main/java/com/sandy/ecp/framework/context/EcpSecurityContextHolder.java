/*
 * Copyright 2018 the original author or authors.
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
package com.sandy.ecp.framework.context;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.sandy.ecp.framework.session.EcpAuthenticationToken;
import com.sandy.ecp.framework.session.SessionVO;

/**
 * Enterprise Cloud Platform Security Context Holder.
 * @author Sandy
 * @param <PK>
 * @version 1.0.0
 * @since 1.0.0 04th 12 2022
 */
public class EcpSecurityContextHolder {
	
	public static SecurityContext getContext() {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		return securityContext;
	}
	
	public static SessionVO getSessionVO() {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication authentication = securityContext.getAuthentication();
		if (authentication != null) {
			Object details = authentication.getDetails();
			if (details instanceof SessionVO) {
				return (SessionVO) authentication.getDetails();
			}
		}
		return null;
	}
	
	public static void setSessionVO(Object session) {
		SecurityContext sc = EcpSecurityContextHolder.getContext();
		EcpAuthenticationToken authentication = new EcpAuthenticationToken(null, null);
		authentication.setDetails(session);
		sc.setAuthentication(authentication);
	}
	
	public static void clearContext() {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		securityContext.setAuthentication(null);
		SecurityContextHolder.clearContext();
	}
}
