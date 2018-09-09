package org.sachin.security;

import org.springframework.security.core.GrantedAuthority;

/**
 * 
 * @author sachin.shinde
 *
 */
public class AuthorizationGranted implements GrantedAuthority{

	private String authorizationRole;
	public AuthorizationGranted(String authorizationRole) {
		this.authorizationRole = authorizationRole;
	}
	@Override
	public String getAuthority() {
		return this.authorizationRole;
	}

}
