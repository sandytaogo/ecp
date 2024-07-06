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
package com.sandy.ecp.framework.session;

import java.util.Collection;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.util.Assert;

/**
 * Enterprise Cloud Platform Authentication Token.
 * @author Sandy
 * @param <PK>
 * @version 1.0.0
 * @since 1.0.0 04th 12 2022
 */
public class EcpAuthenticationToken extends AbstractAuthenticationToken implements InitializingBean {


	private static final long serialVersionUID = -118746433258145998L;
	
	private Object principal;
	private Object credentials;
	
	private AuthenticationManager authenticationManager;
	
	public EcpAuthenticationToken(Object principal, Object credentials) {
		super(AuthorityUtils.NO_AUTHORITIES);
		this.principal = principal;
		this.credentials = credentials;
	}
	
	public EcpAuthenticationToken(Collection<? extends GrantedAuthority> authorities, Object principal, Object credentials) {
		super(authorities);
		this.principal = principal;
		this.credentials = credentials;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(this.authenticationManager, "authenticationManager is required");
	}

	@Override
	public Object getCredentials() {
		return credentials;
	}

	@Override
	public Object getPrincipal() {
		return principal;
	}

	public AuthenticationManager getAuthenticationManager() {
		return authenticationManager;
	}

	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}
}
