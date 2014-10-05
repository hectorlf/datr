package com.hectorlopezfernandez.datr.dao;

import java.util.Collection;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.hectorlopezfernandez.datr.model.Language;
import com.hectorlopezfernandez.datr.model.auth.Authority;
import com.hectorlopezfernandez.datr.model.auth.Principal;

public interface AuthDao {

	Principal getByUsername(String username);
	
	/**
	 * Implementation of Spring Security's UserDetailsService
	 */
	UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
	
	Authority getAuthority(String username, String authority);

	Principal createPrincipal(String username, String password, boolean enabled, Language language, Collection<String> authorities);

	void deletePrincipal(Principal principal);

	Authority assignAuthority(Principal principal, String authority);

	void unassignAuthority(Principal principal, String authority);

}
