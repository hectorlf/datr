package com.hectorlopezfernandez.datr.service.impl;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.hectorlopezfernandez.datr.dao.UserDao;
import com.hectorlopezfernandez.datr.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	private UserDao userDao;

	@Inject
	public UserServiceImpl(UserDao userDao) {
		this.userDao = userDao;
	}

}
