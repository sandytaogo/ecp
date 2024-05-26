package com.sandy.ecp.framework.session;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

public class AuthenticationToken extends AbstractAuthenticationToken {

	private static final long serialVersionUID = -118746433258145998L;
	
	public AuthenticationToken() {
		super(AuthorityUtils.NO_AUTHORITIES);
	}

	public AuthenticationToken(Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
	}

	@Override
	public Object getCredentials() {
		return null;
	}

	@Override
	public Object getPrincipal() {
		return null;
	}
}
