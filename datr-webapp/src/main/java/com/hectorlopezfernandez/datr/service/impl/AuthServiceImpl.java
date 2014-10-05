package com.hectorlopezfernandez.datr.service.impl;

import javax.inject.Inject;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.hectorlopezfernandez.datr.dao.AuthDao;
import com.hectorlopezfernandez.datr.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {
	
	private AuthDao authDao;

	@Inject
	public AuthServiceImpl(AuthDao authDao) {
		this.authDao = authDao;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserDetails u = authDao.loadUserByUsername(username);
		return u;
	}

}
