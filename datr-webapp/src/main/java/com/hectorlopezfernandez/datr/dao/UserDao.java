package com.hectorlopezfernandez.datr.dao;

import java.util.List;
import java.util.Locale;

import com.hectorlopezfernandez.datr.model.User;

public interface UserDao {

	List<User> findAllUsers();

	User getByUsername(String username);
	
	void setLocaleFromLocaleResolver(String username, Locale locale);

}
