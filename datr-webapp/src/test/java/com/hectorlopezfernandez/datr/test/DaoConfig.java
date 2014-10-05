package com.hectorlopezfernandez.datr.test;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hectorlopezfernandez.datr.dao.AuthDao;
import com.hectorlopezfernandez.datr.dao.MetadataDao;
import com.hectorlopezfernandez.datr.dao.UserDao;
import com.hectorlopezfernandez.datr.dao.impl.AuthDaoImpl;
import com.hectorlopezfernandez.datr.dao.impl.MetadataDaoImpl;
import com.hectorlopezfernandez.datr.dao.impl.UserDaoImpl;

@Configuration
public class DaoConfig {

	@Bean
	public MetadataDao metadataDao() {
		return new MetadataDaoImpl();
	}

	@Bean
	public AuthDao authDao() {
		return new AuthDaoImpl();
	}

	@Bean
	public UserDao userDao() {
		return new UserDaoImpl();
	}

}
